
package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class DetallesActivity extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    //static Context context;

    private GoogleMap mMap;
    static Vehiculo vehiculo;

    EditText editText;

    ListView listView;
    List<Datos> listaDatos = new ArrayList<>();
    ArrayAdapter<Datos> adaptador;

    Marker punto, marcaRuta, puntoFinal;
    Polyline line = null;

    String fecha, fechaRuta;

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

        MenuActivity.context = this;

        editText = findViewById(R.id.editTextDate);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatRuta = new SimpleDateFormat("yyyy-MM-dd");
        fecha = format.format(currentDate);
        fechaRuta = formatRuta.format(currentDate);
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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                recogeDatos(fechaRuta);
            }
        });
    }

    public void recogeDatos(String fecha) {

        if(!listaDatos.isEmpty()) {
            listaDatos.clear();
        }

        URL url = null;
        URLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        String linea, devuelve = "";


        try {
            url = new URL("http://" + LoginActivity.urlFinalLocal + "/tramasReport.action?id=" + vehiculo.getIdentificador() + "&sid=" + fecha);
            connection = url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            Object respuesta = connection.getContent();

            if(respuesta instanceof InputStream){
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((linea = reader.readLine()) != null){
                    response.append(linea);
                }
                devuelve = response.toString();
                Log.e("Respuesta: ", devuelve);

                String separa = Pattern.quote("||");
                String[] partes = devuelve.split(separa);

                for (String trama : partes) {
                    Datos dato = new Datos();
                    String separa2 = Pattern.quote("|");
                    String[] datos = trama.split(separa2);
                    if(dato==null){
                        continue;
                    }
                    String hora = datos[2].substring(0,2) + ":" + datos[2].substring(2,4) + ":" + datos[2].substring(4,6);
                    dato.setLatitud(Double.parseDouble(datos[0]));
                    dato.setLongitud(Double.parseDouble(datos[1]));
                    dato.setHora(hora);
                    dato.setEstado(datos[4]);
                    dato.setVelocidad(datos[12]);
                    dato.setComportamiento(datos[7]);
                    listaDatos.add(dato);
                }
            }

            //Muestro la listview de la ruta y la dibujo en el mapa
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adaptador = new ArrayAdapter<Datos>(getApplicationContext(), R.layout.lista_item, listaDatos){
                        private List<Datos> items;
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);

                            items = listaDatos;
                            double veloc = Double.parseDouble(items.get(position).getVelocidad().substring(0,3));
                            int x = 0;
                            TextView text  = (TextView)view.findViewById(R.id.textPersonalizado);

                            if(items.get(position)!=null){
                                if(items.get(position).getEstado().equals("1")){
                                    text.setTextColor(Color.RED);
                                }
                                if(items.get(position).getEstado().equals("0") && veloc > 5){
                                    text.setTextColor(Color.BLACK);
                                }
                                if(!items.get(position).getEstado().equals("1") && veloc < 5){
                                    text.setTextColor(Color.MAGENTA);
                                }
                            }
                            return view;
                        }
                    };

                    listView.setAdapter(adaptador);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if(marcaRuta != null){
                                marcaRuta.remove();
                            }
                            if(puntoFinal != null){
                                puntoFinal.remove();
                            }

                            //Dibujo el icono en el punto de la ruta que se ha pulsado
                            punto.remove();
                            LatLng puntoRuta = new LatLng((listaDatos.get(position).getLatitud()),(listaDatos.get(position).getLongitud()));
                            marcaRuta = mMap.addMarker(new MarkerOptions().position(puntoRuta).title(vehiculo.getIdentificador()));
                            dibujaIcono(vehiculo, marcaRuta);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(puntoRuta)
                                    .zoom(12)
                                    .bearing(0)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    });

                    //Dibujo la línea de la ruta
                    if (!adaptador.isEmpty()) {
                        LatLng puntoA, puntoB;
                        for (int x = 0; x < (listaDatos.size() - 1); x++) {

                            puntoA = new LatLng(listaDatos.get(x).getLatitud(), listaDatos.get(x).getLongitud());
                            puntoB = new LatLng(listaDatos.get(x + 1).getLatitud(), listaDatos.get(x + 1).getLongitud());

                            line = mMap.addPolyline(new PolylineOptions()
                                    .add(puntoA, puntoB)
                                    .width(5)
                                    .color(Color.RED)
                                    .geodesic(true));
                        }
                        /////////////////////////////////////////////////Coloco el icono en la última posición
                        Double ultimaLatitud = (listaDatos.get(listaDatos.size()-1).getLatitud());
                        Double ultimaLongitud = (listaDatos.get(listaDatos.size()-1).getLongitud());
                        LatLng ultimaPosicion = new LatLng (ultimaLatitud, ultimaLongitud);
                        puntoFinal = mMap.addMarker(new MarkerOptions().position(ultimaPosicion).title(vehiculo.getIdentificador()));

                        dibujaIcono(vehiculo, puntoFinal);

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(ultimaPosicion)
                                .zoom(12)
                                .bearing(0)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        /////////////////////////////////////////////////
                    }
                }
            });

        }catch(Exception e){
            Log.e("Error getContent:" , e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        String marcha = vehiculo.getEstado();

        if(MenuActivity.modo_normal == false){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        // Add a marker and move the camera
        LatLng marca = new LatLng(vehiculo.getLatitud(), vehiculo.getLongitud());
        punto = mMap.addMarker(new MarkerOptions().position(marca).title(vehiculo.getIdentificador()));

        dibujaIcono(vehiculo, punto);

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

        String date, dia;

        mMap.clear();
        onMapReady(mMap);
        punto.remove();

        if(month<10){
            date = dayOfMonth + "/" + 0 + (month + 1) + "/" + year;
            dia = year + "-" + 0 + (month + 1) + "-" + dayOfMonth;
        }else {
            date = dayOfMonth + "/" + (month + 1) + "/" + year;
            dia = year + "-" + (month + 1) + "-" + dayOfMonth;
        }
        editText.setText(date);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                recogeDatos(dia);
            }
        });

    }

    public static void dibujaIcono(Vehiculo vehiculo, Marker marker){
        String marcha = vehiculo.getEstado();
        String tipo = vehiculo.getTipoVehiculo().trim();

        switch(tipo){
            case "COCHE":
            case "FURGONETA":
                switch (marcha){
                    case "0":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_marcha));
                        break;
                    case "1":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_paro));
                        break;
                    case "2":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_ralenti));
                        break;
                    case "3":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_descarga));
                        break;
                }
                break;

            case "CAMION":
                switch (marcha){
                    case "0":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.camion_marcha));
                        break;
                    case "1":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.camion_paro));
                        break;
                    case "2":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.camion_ralenti));
                        break;
                    case "3":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.camion_descarga));
                        break;
                }
                break;

            case "HORMIGONERA":
                switch (marcha){
                    case "0":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hormigonera_marcha));
                        break;
                    case "1":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hormigonera_paro));
                        break;
                    case "2":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hormigonera_ralenti));
                        break;
                    case "3":
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hormigonera_descarga));
                        break;
                }
                break;
        }
    }

}