package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentActivity;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.lowagie.text.pdf.AcroFields;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.AsynchronousByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ZonasActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String[] partes;

    ListView listViewZonas;
    List<Zona> listaZonas = new ArrayList<>();
    ArrayAdapter<Zona> adaptador;

    Map<String, Zona> mapaZonas = new HashMap<>();

    Polygon poligono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listViewZonas = findViewById(R.id.listViewZonas);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                cargaDatos();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng spain = new LatLng(40,-3.5);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(spain)
                .zoom(6)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void cargaDatos(){
        URL url = null;
        URLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        String linea, devuelve = "";

        try {
            url = new URL("http://" + LoginActivity.urlFinalLocal + "/ajaxZonas.action");

            //url = new URL("http://" + LoginActivity.urlFinalLocal + "/gestionZonas.action");
            connection = url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Object respuesta = connection.getContent();

            if(respuesta instanceof InputStream) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((linea = reader.readLine()) != null) {
                    response.append(linea);
                }

                devuelve = response.toString();
                Log.e("Respuesta: ", devuelve);

                JSONArray objeto = new JSONArray(devuelve);

                for (int x = 0; x < objeto.length(); x++) {
                    Zona zona = new Zona();
                    JSONArray trama = objeto.getJSONArray(x);
                    zona.setCodigo((String) trama.get(0));
                    zona.setDescripcion((String) trama.get(1));
                    zona.setTipo(trataTipo((String) trama.get(2)));
                    zona.setCoordenadas((String) trama.get(3));
                    zona.setDireccion(String.valueOf(trama.get(4)));

                    listaZonas.add(zona);
                    mapaZonas.put(zona.getCodigo(), zona);
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adaptador = new ArrayAdapter<Zona>(getApplicationContext(), R.layout.lista_item, listaZonas);
                    listViewZonas.setAdapter(adaptador);

                    listViewZonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if(poligono != null){
                                poligono.remove();
                            }

                            Zona zonaElegida = adaptador.getItem(position);

                            String[] coordArray = zonaElegida.getCoordenadas();
                            LatLng[] latLngs = new LatLng[coordArray.length];
                            for(int i=0;i<coordArray.length;i++){
                                String latLngString = coordArray[i];
                                String[] latlong = latLngString.split(" ");
                                double lati = Double.parseDouble(latlong[0]);
                                double longi = Double.parseDouble(latlong[1]);
                                latLngs[i] = new LatLng(lati,longi);

                            }

                            PolygonOptions poligonOptions = new PolygonOptions();
                            for(LatLng coordenadas : latLngs){
                                poligonOptions.add(coordenadas)
                                        .fillColor(Color.RED);
                            }

                            poligono = mMap.addPolygon(poligonOptions);

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLngs[3])
                                    .zoom(13)
                                    .bearing(0)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                   });
                }
            });


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    private String trataTipo(String tipo){

        String tipoCorregido = "";

        if(tipo.equals("1")){
            tipoCorregido = "Planta hormigón";
        }else if(tipo.equals("2")){
            tipoCorregido = "Obra Cliente";
        }else if(tipo.equals("3")){
            tipoCorregido = "Oficina Central";
        }else if(tipo.equals("4")){
            tipoCorregido = "Zona 4";
        }else if(tipo.equals("99")){
            tipoCorregido = "Talleres";
        }
        return tipoCorregido;
    }
}