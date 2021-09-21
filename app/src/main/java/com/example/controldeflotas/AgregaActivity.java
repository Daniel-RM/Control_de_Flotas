package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class AgregaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btnAgrega, btnFine, btnCancelo;
    ImageButton imgVer, imgVolver;
    Zona zonaNueva;
    List<LatLng> puntosTocados = new ArrayList<>();
    List<Marker> listaMarcas = new ArrayList<>();
    Polygon polygon;
    Marker marked;
    LatLng[] latLngs;
    PolygonOptions poligonOptions = new PolygonOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toast.makeText(getApplicationContext(), "Pulse el botón \"Nueva\" para comenzar", Toast.LENGTH_SHORT).show();

        zonaNueva = new Zona();

        btnAgrega = findViewById(R.id.btnAgrega);
        btnFine = findViewById(R.id.btnFine);
        btnCancelo = findViewById(R.id.btnCancelo);
        imgVer = findViewById(R.id.imgVer);
        imgVolver = findViewById(R.id.imgVolver);

        //Botones de finalizar y cancelar, estarán deshabilitados, hasta que no se añada una zona
        btnFine.setEnabled(false);
        btnCancelo.setEnabled(false);

        //Método para cambiar el tipo de vista del mapa
        imgVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_HYBRID){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });

        //Método para volver a la actividad anterior
        imgVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ZonasActivity.class);
                startActivity(intent);
            }
        });

        //Método para cancelar la zona creada, dejar el mapa en blanco y volver a empezar
        btnCancelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(polygon != null){
                    polygon.remove();
                    for(int x=0;x<listaMarcas.size();x++){
                        listaMarcas.get(x).remove();
                    }
                    latLngs = new LatLng[0];

                    poligonOptions = new PolygonOptions();
                    marked.remove();
                    listaMarcas.clear();
                    puntosTocados.clear();
                    puntosTocados = new ArrayList<>();
                    polygon = null;
                    zonaNueva = new Zona();
                    mMap.clear();
                    btnFine.setEnabled(false);
                    btnCancelo.setEnabled(false);
                }
            }
        });

        //Método para añadir una zona nueva
        btnAgrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Marque 4 puntos en orden(sin diagonales) en el mapa para dibujar la zona, por favor", Toast.LENGTH_SHORT).show();
                //Cuando pulsan en el mapa, añado las marcas. Espero a que haya 4 marcas para crearla
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        if(polygon == null) {
                            if (puntosTocados.size() < 4) {
                                puntosTocados.add(latLng);
                                marked = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                                listaMarcas.add(marked);
                            }

                            if (puntosTocados.size() == 4) {
                                for (LatLng coordenadas : puntosTocados) {
                                    poligonOptions.add(coordenadas)
                                            .fillColor(Color.argb(128, 255, 0, 0));
                                }
                                polygon = mMap.addPolygon(poligonOptions);
                                btnCancelo.setEnabled(true);
                                btnFine.setEnabled(true);
                                zonaNueva.setDireccion(Datos.obtenerDireccion(marked.getPosition().latitude, marked.getPosition().longitude));
                                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(@NonNull Marker marker) {}

                                    @Override
                                    public void onMarkerDrag(@NonNull Marker marker) {}

                                    @Override
                                    public void onMarkerDragEnd(@NonNull Marker marker) {

                                        for (int g = 0; g < listaMarcas.size(); g++) {
                                            if (listaMarcas.get(g).getId() == marker.getId()) {
                                                listaMarcas.get(g).setPosition(marker.getPosition());
                                            }
                                        }
                                        polygon.remove();
                                        poligonOptions = new PolygonOptions();
                                        latLngs = new LatLng[listaMarcas.size()];
                                        for (int i = 0; i < listaMarcas.size(); i++) {
                                            latLngs[i] = listaMarcas.get(i).getPosition();
                                        }
                                        for (LatLng coordenadas : latLngs) {
                                            poligonOptions.add(coordenadas)
                                                    .fillColor(Color.argb(128, 255, 0, 0));
                                        }

                                        //Dibujo el polígono
                                        polygon = mMap.addPolygon(poligonOptions);
                                        zonaNueva.setDireccion(Datos.obtenerDireccion(marker.getPosition().latitude, marker.getPosition().longitude));
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        //Método que llamará al cuadro de diálogo, para recoger los datos que faltan, para crear la zona nueva
        btnFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coordeno = "";
                for(int x = 0;x<puntosTocados.size();x++) {
                    String punto = puntosTocados.get(x).toString();
                    punto = punto.trim();
                    punto = punto.replace("lat/lng:", "");
                    coordeno = coordeno + punto + "|";
                }
                zonaNueva.setCoordenadas(coordeno);
                new DialogoZonas(AgregaActivity.this, zonaNueva, puntosTocados, false);
            }
        });


    }

    //Evito que puedan pulsar la tecla "hacia atrás"
    @Override
    public void onBackPressed() { }

    //Se carga el mapa
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng spain = new LatLng(40,-3.5);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(spain)
                .zoom(5)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}