package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Context context;

    ArrayList<Vehiculo> listaVehiculos;

    private boolean showDownloadMenu = false;
    private final int MENU_DOWNLOAD = 1;
    private final int MENU_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

        listaVehiculos = new ArrayList<>();

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String fecha = format.format(currentDate);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("3709LNS");
        vehiculo.setLatitud(41.493891);
        vehiculo.setLongitud(-1.365427);
        vehiculo.setEstado("Parado");
        vehiculo.setFecha(fecha);
        vehiculo.setVelocidad("0");
        vehiculo.setDistancia("0");
        vehiculo.setAltitud("400");
        vehiculo.setRpm("0");
        vehiculo.setTemperatura("150");
        vehiculo.setPresion("0");

        listaVehiculos.add(vehiculo);

        vehiculo = new Vehiculo();
        vehiculo.setMatricula("1234ABC");
        vehiculo.setLatitud(43.384968);
        vehiculo.setLongitud(-3.872779);
        vehiculo.setEstado("En Marcha");
        vehiculo.setFecha(fecha);
        vehiculo.setVelocidad("120");
        vehiculo.setDistancia("210");
        vehiculo.setAltitud("687");
        vehiculo.setRpm("15");
        vehiculo.setTemperatura("175");
        vehiculo.setPresion("0");

        listaVehiculos.add(vehiculo);

        vehiculo = new Vehiculo();
        vehiculo.setMatricula("9999CCC");
        vehiculo.setLatitud(39.940690);
        vehiculo.setLongitud(-3.436161);
        vehiculo.setEstado("Ralentí");
        vehiculo.setFecha(fecha);
        vehiculo.setVelocidad("0");
        vehiculo.setDistancia("21");
        vehiculo.setAltitud("300");
        vehiculo.setRpm("5");
        vehiculo.setTemperatura("152");
        vehiculo.setPresion("0");

        listaVehiculos.add(vehiculo);

        Log.e("ArrayList:", listaVehiculos.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("FLOTA ARCO");
        for (int x=0;x<listaVehiculos.size();x++){
            menu.add(listaVehiculos.get(x).getMatricula());
        }
        return true;
    }


   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(!item.getTitle().equals("FLOTA ARCO")) {
            Vehiculo coche = null;
            for (int i = 0; i < listaVehiculos.size(); i++) {
                if (listaVehiculos.get(i).getMatricula() == item.getTitle()) {
                    coche = listaVehiculos.get(i);
                }
            }
            Toast.makeText(getApplicationContext(), "Ha seleccionado " + item, Toast.LENGTH_SHORT).show();

            new DialogoDatos(context, coche);
            return true;
        }else{
            return false;
        }
    }

    public void toggleMenu(View view){
        showDownloadMenu=!showDownloadMenu;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Double lati, longi;

        //Recorro la lista de vehículos y los muestro en el mapa
        for (int i=0;i<listaVehiculos.size();i++){
            lati = listaVehiculos.get(i).getLatitud();
            longi = listaVehiculos.get(i).getLongitud();

            LatLng marca = new LatLng(lati,longi);
            mMap.addMarker(new MarkerOptions().position(marca).title(listaVehiculos.get(i).getMatricula()));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40,-3.5),5.7f));

    }

}

