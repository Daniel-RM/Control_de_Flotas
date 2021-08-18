package com.example.controldeflotas;

import androidx.fragment.app.FragmentActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.AsynchronousByteChannel;
import java.util.regex.Pattern;

public class ZonasActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String[] partes;

    ListView listViewZonas;

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
        listViewZonas.setBackgroundColor(Color.WHITE);

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

            if(respuesta instanceof InputStream){
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((linea = reader.readLine()) != null){
                    response.append(linea);
                }
                devuelve = response.toString();
                Log.e("Respuesta: ", devuelve);

                String separador = Pattern.quote("]");
                partes = devuelve.split(separador);

                for(String trama : partes){
                    Zona zona = new Zona();
                    String separa = Pattern.quote(",");
                    String[] zonas = trama.split(separa);

                    if(zona == null){
                        continue;
                    }

//                    zona.setCodigo(zonas[0]);
//                    zona.setDescripcion(zonas[1]);
//                    zona.setTipo(zonas[2]);
//                    zona.setCoordenadas(zonas[3] + "/" + zonas[4] + "/" + zonas[5] + "/" + zonas[6] + "/" + zonas[7]);
//                    zona.setDireccion(zonas[8] + ", " + zonas[9] + ", " + zonas[10]);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}