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

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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

    String fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        getSupportActionBar().hide();

        listView = findViewById(R.id.listView);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editText = findViewById(R.id.editTextDate);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        fecha = format.format(currentDate);
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

        recogeDatos();

        /*adaptador = new ArrayAdapter<Datos>(this, android.R.layout.simple_list_item_1, listaDatos);
        listView.setAdapter(adaptador);
        listView.setVisibility(View.VISIBLE);*/
    }

    public void recogeDatos() {
        URL url = null;

        try {
            url = new URL("http://" + LoginActivity.urlFinalLocal + "/tramasReport.action?id=" + vehiculo.getIdentificador() + "&sid=" + fecha);
            int x = 0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Marker punto;
        String marcha = vehiculo.getEstado();

        // Add a marker and move the camera
        LatLng marca = new LatLng(vehiculo.getLatitud(), vehiculo.getLongitud());
        punto = mMap.addMarker(new MarkerOptions().position(marca).title(vehiculo.getIdentificador()));

        switch (marcha){
            case "0":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_marcha));
                break;
            case "1":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_paro));
                break;
            case "2":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_ralenti));
                break;
            case "3":
                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_descarga));
                break;
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(marca)
                .zoom(12)
                .bearing(0)
                .build();
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

            mMap.clear();
            onMapReady(mMap);

            listaDatosHoy = new ArrayList<>();
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            editText.setText(date);

            for (int i = 0; i < listaDatos.size(); i++) {
                if (listaDatos.get(i).getFecha().equals(date) && listaDatos.get(i).getMatricula().equals(vehiculo.getMatricula())) {
                    listaDatosHoy.add(listaDatos.get(i));
                }
            }

            adaptador = new ArrayAdapter<Datos>(this, R.layout.lista_item, listaDatosHoy);
            listView.setAdapter(adaptador);


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