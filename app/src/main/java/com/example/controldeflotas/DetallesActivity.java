package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetallesActivity extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;
    Vehiculo vehiculo;

    EditText editText;

    ListView listView;
    ArrayList<Datos> listaDatos = new ArrayList<>();
    ArrayAdapter<Datos> adaptador;

    ArrayList<Datos> listaDatosHoy = new ArrayList<>();
    Polyline line = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        listView = findViewById(R.id.listView);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editText = findViewById(R.id.editTextDate);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(currentDate);
        editText.setText(fecha);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();

        vehiculo = (Vehiculo) extra.getSerializable("Vehiculo");

        Datos dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("12/7/2021");
        dato.setHora("12:00:00");
        dato.setLatitud(42.4305968);
        dato.setLongitud(-2.53488895);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("12/7/2021");
        dato.setHora("12:30:00");
        dato.setLatitud(42.22408775);
        dato.setLongitud(-1.8972332);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("12/7/2021");
        dato.setHora("13:00:00");
        dato.setLatitud(41.889091);
        dato.setLongitud(-1.42195578);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("12/7/2021");
        dato.setHora("13:30:00");
        dato.setLatitud(41.67831923);
        dato.setLongitud(-0.8782818);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Cambio dirección");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("12/7/2021");
        dato.setHora("14:00:00");
        dato.setLatitud(41.49433943);
        dato.setLongitud(-1.36805455);
        dato.setEstado("Parado");
        dato.setComportamiento("Paro motor");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        ////////////////////////////////////////////////////////////////////////////////////////////

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("11/7/2021");
        dato.setHora("12:30:00");
        dato.setLatitud(42.22408775);
        dato.setLongitud(-1.8972332);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("11/7/2021");
        dato.setHora("13:00:00");
        dato.setLatitud(41.889091);
        dato.setLongitud(-1.42195578);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("11/7/2021");
        dato.setHora("13:30:00");
        dato.setLatitud(41.67831923);
        dato.setLongitud(-0.8782818);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Cambio dirección");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("3709LNS");
        dato.setFecha("11/7/2021");
        dato.setHora("14:00:00");
        dato.setLatitud(41.49433943);
        dato.setLongitud(-1.36805455);
        dato.setEstado("Parado");
        dato.setComportamiento("Paro motor");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        ////////////////////////////////////////////////////////////////////////////////////////////

        dato = new Datos();
        dato.setMatricula("1234ABC");
        dato.setFecha("11/7/2021");
        dato.setHora("12:30:00");
        dato.setLatitud(42.22408775);
        dato.setLongitud(-1.8972332);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("1234ABC");
        dato.setFecha("11/7/2021");
        dato.setHora("13:00:00");
        dato.setLatitud(41.889091);
        dato.setLongitud(-1.42195578);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Envío periódico");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("1234ABC");
        dato.setFecha("11/7/2021");
        dato.setHora("13:30:00");
        dato.setLatitud(41.67831923);
        dato.setLongitud(-0.8782818);
        dato.setEstado("En Marcha");
        dato.setComportamiento("Cambio dirección");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        dato = new Datos();
        dato.setMatricula("1234ABC");
        dato.setFecha("11/7/2021");
        dato.setHora("14:00:00");
        dato.setLatitud(41.49433943);
        dato.setLongitud(-1.36805455);
        dato.setEstado("Parado");
        dato.setComportamiento("Paro motor");
        dato.setVelocidad("120");
        dato.setDistancia("110");
        listaDatos.add(dato);

        Log.e("ArrayList:", listaDatos.toString());

        /*adaptador = new ArrayAdapter<Datos>(this, android.R.layout.simple_list_item_1, listaDatos);
        listView.setAdapter(adaptador);
        listView.setVisibility(View.VISIBLE);*/

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Marker punto;
        String marcha = vehiculo.getEstado();

        // Add a marker and move the camera
        LatLng marca = new LatLng(vehiculo.getLatitud(), vehiculo.getLongitud());
        punto = mMap.addMarker(new MarkerOptions().position(marca).title(vehiculo.getMatricula()));

        switch (marcha){
            case "Parado":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_paro));
                break;
            case "En Marcha":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_marcha));
                break;
            case "Ralentí":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_ralenti));
                break;
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(marca)
                .zoom(12)
                .bearing(0)
                .build();
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marca,7f));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog =new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (line != null) {
            line.remove();
            mMap.clear();
            onMapReady(mMap);
        } else {
            listaDatosHoy = new ArrayList<>();
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            editText.setText(date);

            for (int i = 0; i < listaDatos.size(); i++) {

                if (listaDatos.get(i).getFecha().equals(date) && listaDatos.get(i).getMatricula().equals(vehiculo.getMatricula())) {
                    listaDatosHoy.add(listaDatos.get(i));
                }
            }

            adaptador = new ArrayAdapter<Datos>(this, android.R.layout.simple_list_item_1, listaDatosHoy);
            listView.setAdapter(adaptador);
            listView.setVisibility(View.VISIBLE);

            if (!adaptador.isEmpty()) {
                LatLng puntoA, puntoB;
                for (int x = 0; x < (listaDatosHoy.size() - 1); x++) {
                    puntoA = new LatLng(listaDatosHoy.get(x).getLatitud(), listaDatosHoy.get(x).getLongitud());
                    puntoB = new LatLng(listaDatosHoy.get(x + 1).getLatitud(), listaDatosHoy.get(x + 1).getLongitud());

                    line = mMap.addPolyline(new PolylineOptions()
                            .add(puntoA, puntoB)
                            .width(5)
                            .color(Color.RED)
                            .geodesic(true));
                }
            }
        }
    }
}