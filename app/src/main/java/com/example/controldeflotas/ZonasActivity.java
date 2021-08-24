package com.example.controldeflotas;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;

import java.util.List;


public class ZonasActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ListView listViewZonas;
    List<Zona> listaZonas = new ArrayList<>();
    ArrayAdapter<Zona> adaptador;

    List<Marker> listaMarcas = new ArrayList<>();
    LatLng[] latLngs;
    Marker marked;

    Polygon poligono;

    ImageButton btnVer;
    EditText etBuscar;

    Button btnInsert, btnUpdate, btnDelete, btnFinalizar;

    boolean seleccionado;

    Zona zonaElegida;


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
        btnVer = findViewById(R.id.btnVer);
        etBuscar = findViewById(R.id.etBuscar);
        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        seleccionado = false;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                cargaDatos();
            }
        });

        btnFinalizar.setVisibility(View.INVISIBLE);

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_HYBRID){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Zona zonaVacia = new Zona();
                btnFinalizar.setVisibility(View.VISIBLE);

                btnFinalizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DialogoZonas(ZonasActivity.this, zonaVacia);
                    }
                });

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprueba que haya una zona seleccionada
                if(!seleccionado) {
                    Toast.makeText(getApplicationContext(), "Por favor, seleccione una zona primero", Toast.LENGTH_SHORT).show();
                }else{
                    btnFinalizar.setVisibility(View.VISIBLE);
                    //PolygonOptions poligonOptions = new PolygonOptions();
                    for(LatLng coordenadas : latLngs){
                        marked = mMap.addMarker(new MarkerOptions().position(coordenadas).draggable(true));
                        listaMarcas.add(marked);
                    }

                    poligono.setPoints(markersToLatLng(listaMarcas));

                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(@NonNull Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(@NonNull Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(@NonNull Marker marker) {
                            for(int g=0;g<listaMarcas.size();g++){
                                if(listaMarcas.get(g).getId() == marker.getId()){
                                    listaMarcas.get(g).setPosition(marker.getPosition());
                                }
                            }
                            poligono.remove();
                            PolygonOptions poligonOptions = new PolygonOptions();
                            latLngs = new LatLng[listaMarcas.size()];
                            for(int i=0;i<listaMarcas.size();i++){
                                latLngs[i] = listaMarcas.get(i).getPosition();
                            }
                            for(LatLng coordenadas : latLngs){
                                poligonOptions.add(coordenadas)
                                        .fillColor(Color.argb(128, 255, 0, 0));
                            }

                            //Dibujo el polígono
                            poligono = mMap.addPolygon(poligonOptions);
                            zonaElegida.setDireccion(Datos.obtenerDireccion(marker.getPosition().latitude, marker.getPosition().longitude));
                            Toast.makeText(getApplicationContext(), "Ha dejado la marca en " + Datos.obtenerDireccion(marker.getPosition().latitude, marker.getPosition().longitude) ,Toast.LENGTH_SHORT).show();
                        }
                    });

                    btnFinalizar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DialogoZonas(ZonasActivity.this, zonaElegida);
                        }
                    });
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprueba que haya una zona seleccionada
                if(!seleccionado){
                    Toast.makeText(getApplicationContext(), "Por favor, seleccione una zona primero", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ZonasActivity.this);
                    alerta.setMessage("¿Está seguro de querer eliminar la zona " + zonaElegida.getCodigo() + "?")
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "Ha seleccionado eliminar la zona " + zonaElegida.getCodigo(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Eliminar Zona");
                    titulo.show();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng spain = new LatLng(40,-3.5);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(spain)
                .zoom(5)
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
                                if(listaMarcas != null){
                                    for(int x = 0;x<listaMarcas.size();x++){
                                        listaMarcas.get(x).remove();
                                    }
                                    listaMarcas.clear();
                                }
                            }


                            seleccionado = true;

                            zonaElegida = adaptador.getItem(position);

                            String[] coordArray = zonaElegida.getCoordenadas();
                            latLngs = new LatLng[coordArray.length];
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
                                        .fillColor(Color.argb(128, 255, 0, 0));
                            }

                            //Dibujo el polígono
                            poligono = mMap.addPolygon(poligonOptions);

                            //Centro la cámara, para que la zona, salga centrada en el mapa
                            LatLngBounds.Builder centraCamara = new LatLngBounds.Builder();
                            for(LatLng centros:latLngs) {
                                centraCamara.include(centros);
                            }
                            LatLngBounds limites = centraCamara.build();
                            int ancho = getResources().getDisplayMetrics().widthPixels;
                            int alto = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int)(ancho/4);

                            CameraUpdate centrarZonas = CameraUpdateFactory.newLatLngBounds(limites, ancho, alto, padding);
                            mMap.animateCamera(centrarZonas);
                        }
                   });
                }
            });


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> markersToLatLng(List<Marker> markers){
        List<LatLng> latLngs = new ArrayList<>();
        if(markers == null){
            return latLngs;
        }
        for(Marker m : markers){
            latLngs.add(m.getPosition());
        }
        return latLngs;
    }



    public static String trataTipo(String tipo){

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