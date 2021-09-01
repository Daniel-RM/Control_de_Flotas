package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    static Context context;
    static boolean modo_normal;

    Map<String, Vehiculo> mapaVehiculos = new HashMap<>();
    Map<String, Marker> mapaMarcas = new HashMap<>();
    List<Flota> flotas = null;
    List<Vehiculo> listaVehiculos = new ArrayList<>();

    Toolbar toolbar;

    private boolean showDownloadMenu = false;

    private boolean primeraEjecucion = false;

    public static boolean datosCAN = false;


    final String ESTADO_MARCHA = "En Marcha";
    final String ESTADO_PARADO = "Parado";
    final String ESTADO_RALENTI = "Ralentí";
    final String ESTADO_DESCARGANDO = "Descargando";
    final String TODOS = "Todos";
    final String TITULO = "ESTADOS:";

    final String SALIR = "Salir";
    final String SINCERRAR = "Salir sin cerrar sesión";
    final String CERRAR = "Salir y cerrar sesión";

    final String ZONAS = "Zonas";

    final String VISUALIZAR = "Visualizar";
    final String MODOVISUAL = "Cambiar vista";

    final String INFORMES = "Informes";
    final String ALBARANES = "Albaranes";

    final String CONFIGURACIÓN = "Configuración";

    SharedPreferences.Editor editorBorrado;
    static boolean borraDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        flotas = LoginActivity.myObjects;

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

        modo_normal = true;

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String fecha = format.format(currentDate);

        trataDatos();

        borraDatos = false;

        SharedPreferences prefBorrado = getSharedPreferences("borrar", Context.MODE_PRIVATE);
        editorBorrado = prefBorrado.edit();
        editorBorrado.putBoolean("borrar", borraDatos);
        editorBorrado.commit();

    }

    @Override
    public void onBackPressed() {
        //Evito que puedan pulsar la tecla "hacia atrás"
    }

    private void trataDatos(){

        for (Flota flota : flotas){
           for(Vehiculo vehiculo : flota.vehiculos){
               mapaVehiculos.put(vehiculo.getIdentificador(),vehiculo);
               listaVehiculos.add(vehiculo);
           }
        }
    }

    private void iniciarlizarWS() {
        try {
            //WebSocketClient webSocket = new WebSocketClient(new URI("ws://arco06server:8083/ControlFlotasUnifor/ws/comunicaciones/1")) {
            WebSocketClient webSocket = new WebSocketClient(new URI("ws://" + LoginActivity.urlFinalLocal + "/ws/comunicaciones/1")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    this.send("latidoVehiculos");
                }

                @Override
                public void onMessage(String s) {

                    //Tratar la información aquí. Rellenar el map para después mostrarlo en el mapa
                    try {

                        JSONObject objeto = new JSONObject(s);
                        JSONObject mensaje = new JSONObject(objeto.getString("mensaje"));


                        for (Iterator<String> it = mensaje.keys(); it.hasNext(); ) {
                            Vehiculo vehiculo;
                            String identificador = it.next();
                            JSONObject jVehiculo = (JSONObject) mensaje.get(identificador);
                            vehiculo = mapaVehiculos.get(identificador);

                            if (vehiculo == null){
                                continue;
                            }

                            vehiculo.setIdentificador(identificador);
                            vehiculo.setAltitud(String.valueOf(jVehiculo.getInt("altitud")));
                            vehiculo.setDistancia(String.valueOf(jVehiculo.getInt("distancia_dia")));
                            vehiculo.setEstado(jVehiculo.getString("estado"));
                            vehiculo.setLatitud(jVehiculo.getDouble("latitud"));
                            vehiculo.setLongitud(jVehiculo.getDouble("longitud"));
                            vehiculo.setVelocidad(String.valueOf(jVehiculo.getInt("velocidad")));
                            vehiculo.setRpm(String.valueOf(jVehiculo.getInt("rpm_cuba")));
                            vehiculo.setTemperatura(String.valueOf(jVehiculo.getDouble("temperatura")));
                            vehiculo.setPresion(String.valueOf(jVehiculo.getDouble("presion")));
                            vehiculo.setFecha(jVehiculo.getString("ultima_trama"));
                            if(jVehiculo.has("descripcion")) {
                                vehiculo.setDescripcion(jVehiculo.getString("descripcion"));
                            }
                            if(jVehiculo.has("tipoVehiculo")){
                                vehiculo.setTipoVehiculo(jVehiculo.getString("tipoVehiculo"));
                            }

                            vehiculo.setOdometro(jVehiculo.getString("odometro"));
                            vehiculo.setCombustibleTotalUsado(jVehiculo.getString("combustibleTot"));
                            vehiculo.setHorasMotor(jVehiculo.getString("hrs_motor"));
                            vehiculo.setCombustibleNivel(jVehiculo.getString("combustible"));
                            vehiculo.setTmpMotor(jVehiculo.getString("tmp_motor"));
                            vehiculo.setDistanciaServicio(jVehiculo.getString("distancia_servicio"));

                            mapaVehiculos.put(identificador,vehiculo);

                            Double lati = vehiculo.getLatitud();
                            Double longi = vehiculo.getLongitud();
                            String ident = vehiculo.getIdentificador();
                            String matricula = vehiculo.getMatricula();

                            if (primeraEjecucion){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Marker punto;
                                        LatLng marca = new LatLng(lati, longi);
                                        if(lati!=0 && longi!=0) {
                                            if (mapaMarcas.get(ident) == null) {
                                                punto = mMap.addMarker(new MarkerOptions().position(marca).title(ident));
                                                punto.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_marcha));
                                                mapaMarcas.put(ident, punto);
                                            } else {
                                                punto = mapaMarcas.get(ident);
                                                punto.setPosition(marca);
                                            }
                                        }
                                        Log.e("ArrayList:", mapaVehiculos.toString());
                                    }
                                });
                            }
                        }

                        //Muestro los iconos en el mapa
                        if(!primeraEjecucion) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LatLngBounds.Builder centraCamara = new LatLngBounds.Builder();
                                    for(Vehiculo vehiculo:mapaVehiculos.values()) {
                                        Marker marca = null;
                                        if (vehiculo.getLatitud() != 0.0 && vehiculo.getLongitud() != 0.0) {
                                            LatLng posicion = new LatLng(vehiculo.getLatitud(), vehiculo.getLongitud());
                                            marca = mMap.addMarker(new MarkerOptions().position(posicion).title(vehiculo.getIdentificador()));

                                            if (vehiculo.getEstado() != null) {

                                                DetallesActivity.dibujaIcono(vehiculo, marca);

                                            } else {
                                                marca.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coche_paro));
                                            }

                                            mapaMarcas.put(vehiculo.getIdentificador(), marca);
                                            centraCamara.include(posicion);
                                        }
                                    }
                                    //Centro la cámara en los Marker
                                    LatLngBounds limites = centraCamara.build();

                                    int ancho = getResources().getDisplayMetrics().widthPixels;
                                    int alto = getResources().getDisplayMetrics().heightPixels;
                                    int padding = (int) (alto * 0.05); // 5% de espacio (padding) superior e inferior

                                    CameraUpdate centrarmarcadores = CameraUpdateFactory.newLatLngBounds(limites, ancho, alto, padding);
                                    mMap.animateCamera(centrarmarcadores);
                                }
                            });
                            primeraEjecucion = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                }

                @Override
                public void onError(Exception e) {
                }
            };

            webSocket.connect();

        }catch(Exception e){
            Log.e("Error", e.toString());
            e.printStackTrace();
        }

        Log.e("ArrayList:", mapaVehiculos.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        menu.add(TITULO);
        menu.add(TODOS);
        menu.add(ESTADO_MARCHA);
        menu.add(ESTADO_PARADO);
        menu.add(ESTADO_RALENTI);
        menu.add(ESTADO_DESCARGANDO);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(!item.getTitle().toString().equals(TITULO)) {
            for (Vehiculo coche : mapaVehiculos.values()) {
                if(coche.getEstado()==null){
                    continue;
                }
                Marker marker = mapaMarcas.get(coche.getIdentificador());
                switch (item.getTitle().toString()) {

                    case ESTADO_MARCHA:
                        marker.setVisible(coche.getEstado().equals("0"));
                        break;
                    case ESTADO_PARADO:
                        marker.setVisible(coche.getEstado().equals("1"));
                        break;
                    case ESTADO_RALENTI:
                        marker.setVisible(coche.getEstado().equals("2"));
                        break;
                    case ESTADO_DESCARGANDO:
                        marker.setVisible(coche.getEstado().equals("3"));
                        break;
                    case TODOS:
                        marker.setVisible(true);
                        break;
                }

            }
        }

            return true;

    }

    public void cerrar(View view){

        AlertDialog.Builder alerta = new AlertDialog.Builder(MenuActivity.this);
        alerta.setMessage("Desea salir de la aplicación o salir y cerrar la sesión?")
                .setCancelable(true)
                .setNegativeButton("Salir y cerrar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borraDatos = true;
                        editorBorrado.putBoolean("borrar", borraDatos);
                        editorBorrado.commit();
                        finishAffinity();
                    }
                })
                .setPositiveButton("Volver a la pantalla de Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNeutralButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });


        AlertDialog titulo = alerta.create();
        titulo.setTitle("Salir y cerrar sesión");
        titulo.show();

    }

    //Pulsan icono zonas - Vamos a ZonasActivity
    public void zonas(View view){
        Intent intent = new Intent(getApplicationContext(), ZonasActivity.class);
        startActivity(intent);
    }

    //Cambio el modo del mapa al pusar el botón
    public void visualizar(View view){
        if(mMap.getMapType()==GoogleMap.MAP_TYPE_HYBRID){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            modo_normal = true;
        }else{
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            modo_normal = false;
        }
    }

    //Icono informes - abro cuadro de diálogo con las distintas opciones: informes, albaranes o cancelar.
    public void informes(View view){
        AlertDialog.Builder alerta = new AlertDialog.Builder(MenuActivity.this);
        alerta.setMessage("Qué tipo de informe quiere?")
                .setCancelable(false)
                .setPositiveButton("Informes y detalles", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DialogoInforme(MenuActivity.this, listaVehiculos);
                    }
                })
                .setNegativeButton("Albaranes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Ver albaranes", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("Informes y albaranes");
        titulo.show();
    }

    //El usuario podrá seleccionar los datos a mostrar en el cuadro de diálogo
    public void ajustes(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
        alert.setMessage("Elija los datos a mostrar:")
                .setCancelable(false)
                .setPositiveButton("Datos del vehículo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datosCAN = false;
                    }
                })
                .setNegativeButton("Datos del vehículo y su estado", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datosCAN = true;
                    }
                });
        AlertDialog titulo = alert.create();
        titulo.setTitle("Datos a mostrar");
        titulo.show();
    }

    public void toggleMenu(View view){
        showDownloadMenu=!showDownloadMenu;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        iniciarlizarWS();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                marker.showInfoWindow();
                Vehiculo vehi = mapaVehiculos.get(marker.getTitle());
                if(datosCAN){
                    new DialogoDatosCan(MenuActivity.this, vehi);
                }else {
                    new DialogoDatos(MenuActivity.this, vehi);
                }
                return true;
            }
        });

        LatLng spain = new LatLng(40,-3.5);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(spain)
                .zoom(6)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

}

