package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;

public class EdicionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Polygon poligono;

    Zona zonaElegida;

    List<LatLng> listaCoordenadas;
    List<LatLng> listaCoordenadasEditadas = new ArrayList<>();
    List<Marker> listaMarcas = new ArrayList<>();
    LatLng[] latLngs;

    Button btnFinaliza, btnCancela;

    ImageButton btnVisualiza;

    TextView tvCode, tvDesc, tvZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnFinaliza = findViewById(R.id.btnFinaliza);
        btnCancela = findViewById(R.id.btnCancela);
        btnVisualiza = findViewById(R.id.btnVisualiza);
        tvCode = findViewById(R.id.tvCode);
        tvDesc = findViewById(R.id.tvDesc);
        tvZone = findViewById(R.id.tvZone);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            zonaElegida = (Zona) bundle.get("Zona");
            listaCoordenadas = (List<LatLng>) bundle.get("latlong");
            listaMarcas = (List<Marker>) bundle.get("lista");
        }

        tvCode.setText(zonaElegida.getCodigo());
        tvDesc.setText(zonaElegida.getDescripcion());
        tvZone.setText(zonaElegida.getTipo());

        latLngs = new LatLng[listaCoordenadas.size()];

        //Método para cancelar la edición. Volverá a ZonasActivity
        btnCancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(getIntent());
            }
        });

        //Método para finalizar la edición. Actualizará los datos
        btnFinaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogoZonas(EdicionActivity.this, zonaElegida, listaCoordenadasEditadas, true);
            }
        });

        //Método para cambiar el modo de visualización del mapa
        btnVisualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_HYBRID){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });
    }

    //Evito que puedan pulsar la tecla "hacia atrás"
    @Override
    public void onBackPressed() {}

    //Método que carga el mapa
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

        cargarZona();
    }

    //Método que muestra la zona elegida para editarla
    private void cargarZona(){

            if(poligono != null){
                poligono.remove();
                if(listaMarcas != null){
                    for(int x = 0;x<listaMarcas.size();x++){
                        listaMarcas.get(x).remove();
                    }
                    listaMarcas.clear();
                }
            }

            if(zonaElegida!=null) {

                String[] coordArray = zonaElegida.getCoordenadas();
                latLngs = new LatLng[coordArray.length];
                for (int i = 0; i < coordArray.length; i++) {
                    String latLngString = coordArray[i];
                    String[] latlong = latLngString.split(" ");
                    double lati = Double.parseDouble(latlong[0]);
                    double longi = Double.parseDouble(latlong[1]);
                    latLngs[i] = new LatLng(lati, longi);
                }

                PolygonOptions poligonOptions = new PolygonOptions();
                for (LatLng coordenadas : latLngs) {
                    poligonOptions.add(coordenadas)
                            .fillColor(Color.argb(128, 255, 0, 0));
                }

                //Dibujo el polígono
                poligono = mMap.addPolygon(poligonOptions);
            }

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


        for(int i=0;i<listaCoordenadas.size();i++){
            latLngs[i] = listaCoordenadas.get(i);
            int x = 0;
        }
        //Añado marcas, para poder realizar la edición de la zona
        for (LatLng coordenadas : latLngs) {
            Marker marked = mMap.addMarker(new MarkerOptions().position(coordenadas).draggable(true));
            listaMarcas.add(marked);
        }

        poligono.setPoints(ZonasActivity.markersToLatLng(listaMarcas));

        //Método que maneja el arrastre de las marcas, para acotar la zona
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
                poligono.remove();
                PolygonOptions poligonOptions = new PolygonOptions();
                latLngs = new LatLng[listaMarcas.size()];
                for (int i = 0; i < listaMarcas.size(); i++) {
                    latLngs[i] = listaMarcas.get(i).getPosition();
                }

                for (LatLng coordenadas : latLngs) {
                    poligonOptions.add(coordenadas)
                            .fillColor(Color.argb(128, 255, 0, 0));
                }

                //Dibujo el polígono
                poligono = mMap.addPolygon(poligonOptions);
                listaCoordenadasEditadas = poligono.getPoints();
                zonaElegida.setDireccion(Datos.obtenerDireccion(marker.getPosition().latitude, marker.getPosition().longitude));
                zonaElegida.setCoordenadas(listaCoordenadasEditadas.toString());
                Toast.makeText(getApplicationContext(), "Ha dejado la marca en " + Datos.obtenerDireccion(marker.getPosition().latitude, marker.getPosition().longitude), Toast.LENGTH_SHORT).show();
            }
        });
    }
}