package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//Clase que muestra un mapa con la flota entera y maneja el men?? de opciones principal
public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    static Context context;
    static boolean modo_normal;

    static boolean primerEvento, primeraAlarma;

    Map<String, Vehiculo> mapaVehiculos = new HashMap<>();
    Map<String, Marker> mapaMarcas = new HashMap<>();
    List<Flota> flotas = null;
    static ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
    ArrayList<Evento> listaEventos = new ArrayList<>();
    ArrayList<Alarma> listaAlarmas = new ArrayList<Alarma>();

    Toolbar toolbar;

    private boolean showDownloadMenu = false;

    private boolean primeraEjecucion = false;

    final String ESTADO_MARCHA = "En Marcha";
    final String ESTADO_PARADO = "Parado";
    final String ESTADO_RALENTI = "Ralent??";
    final String ESTADO_DESCARGANDO = "Descargando";
    final String TODOS = "Todos";
    final String TITULO = "ESTADOS:";

    SharedPreferences.Editor editorBorrado;
    static boolean borraDatos;

    Button btnEvento, btnAlarma;
    ImageButton btnEsconde;
    LinearLayout linearTitulo;

    RecyclerView recyclerEventos;
    RecyclerEventosAdapter eventosAdapter;
    RecyclerAlarmasAdapter alarmasAdapter;
    int cuentas, cuentasAlarmas, tipoMapa;

    public static List<String> datos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        flotas = LoginActivity.myObjects;
        primerEvento = true;
        primeraAlarma = true;
        cuentas = 0;
        cuentasAlarmas = 0;

        //Mantengo la aplicaci??n fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;

        modo_normal = true;

        listaVehiculos = new ArrayList<>();

        btnAlarma = findViewById(R.id.btnAlarma);
        btnEvento = findViewById(R.id.btnEvento);
        btnEsconde = findViewById(R.id.btnEsconde);
        recyclerEventos = findViewById(R.id.rcEvento);
        linearTitulo = findViewById(R.id.linearTitulo);

        recyclerEventos.setVisibility(View.INVISIBLE);
        btnEsconde.setVisibility(View.INVISIBLE);
        linearTitulo.setVisibility(View.INVISIBLE);

        eventosAdapter = new RecyclerEventosAdapter(listaEventos, context);
        alarmasAdapter = new RecyclerAlarmasAdapter(listaAlarmas, context);

        //Cargo toda la informaci??n necesaria para la pantalla central de la aplicaci??n
        trataDatos();
        eventos();
        alarmas();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                asignaPlanta();
            }
        });


        borraDatos = false;

        SharedPreferences prefBorrado = getSharedPreferences("borrar", Context.MODE_PRIVATE);
        editorBorrado = prefBorrado.edit();
        editorBorrado.putBoolean("borrar", borraDatos);
        editorBorrado.commit();

        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        sacaListView();
                        recyclerEventos.setLayoutManager(new LinearLayoutManager(context));
                        recyclerEventos.setAdapter(eventosAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,new LinearLayoutManager(context).getOrientation());
                        dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.line_divider_evento));
                        recyclerEventos.addItemDecoration(dividerItemDecoration);
                        btnEsconde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                escondeListView();
                            }
                        });
                    };
                });
            }
        });

        btnAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        sacaListView();
                        recyclerEventos.setLayoutManager(new LinearLayoutManager(context));
                        recyclerEventos.setAdapter(alarmasAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,new LinearLayoutManager(context).getOrientation());
                        dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.line_divider));
                        recyclerEventos.addItemDecoration(dividerItemDecoration);
                        btnEsconde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                escondeListView();
                            }
                        });
                    }
                });
            }
        });
    }


    //Evito que puedan pulsar la tecla "hacia atr??s"
    @Override
    public void onBackPressed() {}

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

            WebSocketClient webSocket = new WebSocketClient(new URI("ws://" + LoginActivity.urlFinalLocal + "/ws/comunicaciones/1")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    this.send("latidoVehiculos");
                }

                @Override
                public void onMessage(String s) {
                    //Tratar la informaci??n aqu??. Rellenar el map para despu??s mostrarlo en el mapa
                    try {

                        JSONObject objeto = new JSONObject(s);
                        JSONObject mensaje = new JSONObject(objeto.getString("mensaje"));
                        int j = 0;

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
                            vehiculo.setZona(jVehiculo.getString("zona"));
                            if(jVehiculo.has("descripcion")) {
                                vehiculo.setDescripcion(jVehiculo.getString("descripcion"));
                            }
                            if(jVehiculo.has("tipoVehiculo")){
                                vehiculo.setTipoVehiculo(jVehiculo.getString("tipoVehiculo"));
                            }

                            ////////////////////   DATOS VIAJE   ////////////////////////////////////
                            if(jVehiculo.has("viaje")){
                                //Log.e("Viaje:", "Tenemos opci??n de viaje: " + vehiculo.getMatricula());
                                JSONObject viaje = new JSONObject(jVehiculo.getString("viaje"));

                                    vehiculo.setCarga_descarga_viaje(String.valueOf(viaje.getInt("carga_descarga")));
                                    vehiculo.setAgua_total_viaje(String.valueOf(viaje.getInt("agua_total")));
                                    vehiculo.setHora_viaje(viaje.getString("hora"));
                                    vehiculo.setM3_viaje(viaje.getString("m3"));
                                    vehiculo.setCliente_promsa_viaje(viaje.getString("cliente_promsa"));
                                    vehiculo.setIdviaje(String.valueOf(viaje.getInt("idviaje")));
                                    vehiculo.setCaducado_viaje(String.valueOf(viaje.getBoolean("caducado")));
                                    vehiculo.setAlbaran_viaje(viaje.getString("albaran"));
                                    vehiculo.setObra_promsa_viaje(viaje.getString("obra_promsa"));
                                    vehiculo.setCliente_viaje(viaje.getString("cliente"));
                                    vehiculo.setObra_viaje(viaje.getString("obra"));
                                    vehiculo.setZona_viaje(viaje.getString("zona"));
                                    vehiculo.setAgua_viaje(String.valueOf(viaje.getInt("agua")));

                            }
                            /////////////////////////////////////////////////////////////////////////

                            ////////////////////   DATOS CAN   //////////////////////////////////////
                            if(jVehiculo.has("odometro")){
                                vehiculo.setOdometro(jVehiculo.getString("odometro"));
                            }
                            if(jVehiculo.has("combustibleTot")){
                                vehiculo.setCombustibleTotalUsado(jVehiculo.getString("combustibleTot"));
                            }
                            if(jVehiculo.has("hrs_motor")){
                                vehiculo.setHorasMotor(jVehiculo.getString("hrs_motor"));
                            }
                            if(jVehiculo.has("combustible")){
                                vehiculo.setCombustibleNivel(jVehiculo.getString("combustible"));
                            }
                            if(jVehiculo.has("tmp_motor")){
                                vehiculo.setTmpMotor(jVehiculo.getString("tmp_motor"));
                            }
                            if(jVehiculo.has("distancia_servicio")){
                                vehiculo.setDistanciaServicio(jVehiculo.getString("distancia_servicio"));
                            }
                            /////////////////////////////////////////////////////////////////////////

                            mapaVehiculos.put(identificador,vehiculo);

                            Double lati = vehiculo.getLatitud();
                            Double longi = vehiculo.getLongitud();
                            String ident = vehiculo.getIdentificador();

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
                                        if (vehiculo.getLatitud() != 0.0 && vehiculo.getLongitud() != 0.0 ) {
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
                                    //Centro la c??mara en los Marker
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
                public void onClose(int i, String s, boolean b) { }

                @Override
                public void onError(Exception e) { }
            };

            webSocket.connect();

        }catch(Exception e){
            Log.e("Error", e.toString());
            e.printStackTrace();
        }

        Log.e("ArrayList:", mapaVehiculos.toString());
    }

    //////////////////////////////////////////////////////////
    public void asignaPlanta(){
        URL url = null;
        URLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        String linea, devuelve = "";

        try {
            url = new URL("http://" + LoginActivity.urlFinalLocal + "/listaVehiculo.action");
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
                while ((linea = reader.readLine())!=null){
                    response.append(linea);
                }
                devuelve = response.toString();

                JSONArray mensaje = new JSONArray(devuelve);

                for(int i=0;i<mensaje.length();i++){
                    JSONObject objeto = mensaje.getJSONObject(i);
                    for(int j=0;j<listaVehiculos.size();j++){
                        if(listaVehiculos.get(j).getMatricula().trim().equals(objeto.getString("matricula").trim())){
                            listaVehiculos.get(j).setPlanta(objeto.getString("desc_flota").trim());
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //////////////////////////////////////////////////////////
    public void eventos()  {
        try {
            WebSocketClient webSocketEvento = new WebSocketClient(new URI("ws://" + LoginActivity.urlFinalLocal + "/ws/comunicaciones/1")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    this.send("latidoEventos");
                }

                @Override
                public void onMessage(String s) {
                    try{

                        JSONObject objeto = new JSONObject(s);
                        JSONArray mensaje = new JSONArray(objeto.getString("mensaje"));

                        listaEventos.clear();

                        for(int x = 0;x<mensaje.length();x++){
                            Evento evento = new Evento();
                            JSONObject trama = mensaje.getJSONObject(x);
                            evento.setFecha((String) trama.get("fecha"));
                            evento.setTipo(trama.getInt("tipo"));
                            evento.setOpcion((String) trama.get("opcion"));
                            evento.setIdmodulo((String) trama.get("idmodulo"));
                            evento.setCantidad(trama.getInt("cantidad"));
                            evento.setAlbaran((String) trama.get("albaran"));

                            listaEventos.add(evento);

                            if(!primerEvento) {
                                recyclerEventos.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(listaEventos.size()!=cuentas) {
                                            eventosAdapter.notifyItemInserted(0);
                                            eventosAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                        cuentas = listaEventos.size();
                        primerEvento = false;

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {}

                @Override
                public void onError(Exception e) {}
            };
            webSocketEvento.connect();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////
    public void alarmas(){
        try {
            WebSocketClient webSocketAlarma = new WebSocketClient(new URI("ws://" + LoginActivity.urlFinalLocal + "/ws/comunicaciones/1")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    this.send("latidoEventosAlertas");
                }

                @Override
                public void onMessage(String s) {
                    try {
                        JSONObject objeto = new JSONObject(s);
                        JSONArray mensajeAlerta = new JSONArray(objeto.getString("mensaje"));

                        listaAlarmas.clear();

                        for(int y = 0;y<mensajeAlerta.length();y++){
                            Alarma alarma = new Alarma();
                            JSONObject trama = mensajeAlerta.getJSONObject(y);
                            alarma.setFecha((String)trama.get("fecha"));
                            alarma.setTipo(trama.getInt("tipo"));
                            alarma.setOpcion((String)trama.get("opcion"));
                            alarma.setIdmodulo((String) trama.get("idmodulo"));
                            alarma.setCantidad(trama.getInt("cantidad"));
                            alarma.setAlbaran((String) trama.get("albaran"));

                            listaAlarmas.add(alarma);

                            if(!primeraAlarma){
                                recyclerEventos.post(new Runnable() {
                                    @SuppressLint("ResourceAsColor")
                                    @Override
                                    public void run() {
                                        if(listaAlarmas.size()!=cuentasAlarmas){
                                            alarmasAdapter.notifyItemInserted(0);
                                            alarmasAdapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(), alarma.toString(), Toast.LENGTH_LONG);
                                        }
                                    }
                                });
                            }
                        }

                        cuentasAlarmas = listaAlarmas.size();
                        primeraAlarma = false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onClose(int i, String s, boolean b) {}

                @Override
                public void onError(Exception e) {}

            };

            webSocketAlarma.connect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////

    //M??todo que a??ade opciones al men??
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

    //M??todo que muestra los veh??culos, seg??n su estado (parado, en marcha, etc...)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(!item.getTitle().toString().equals(TITULO)) {
            for (Vehiculo coche : mapaVehiculos.values()) {
                if (coche.getEstado() == null) {
                    continue;
                }
                Marker marker = mapaMarcas.get(coche.getIdentificador());
                if (marker != null){
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
        }
        return true;
    }

    //M??todo que maneja la salida de la aplicaci??n: decide si se guardan los datos del Login al salir o vuelve a esa pantalla de Login
    public void cerrar(View view){

        AlertDialog.Builder alerta = new AlertDialog.Builder(MenuActivity.this);
        alerta.setMessage("Desea salir de la aplicaci??n o salir y cerrar la sesi??n?")
                .setCancelable(true)
                .setNegativeButton("Salir y cerrar sesi??n", new DialogInterface.OnClickListener() {
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        titulo.setTitle("Salir y cerrar sesi??n");
        titulo.show();

    }

    //Pulsan icono zonas - Vamos a ZonasActivity
    public void zonas(View view){
        Intent intent = new Intent(getApplicationContext(), ZonasActivity.class);
        startActivity(intent);
    }

    //Cambio el modo del mapa al pusar el bot??n
    public void visualizar(View view){

        switch (tipoMapa){
            case 0:
                mMap.setTrafficEnabled(true);
                tipoMapa=1;
                break;
            case 1:
                mMap.setTrafficEnabled(false);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                tipoMapa = 2;
                break;
            case 2:
                mMap.setTrafficEnabled(true);
                tipoMapa=3;
                break;
            case 3:
                mMap.setTrafficEnabled(false);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                tipoMapa = 0;
                break;

        }
    }

    //Icono informes - abro cuadro de di??logo con las distintas opciones: informes, albaranes o cancelar.
    public void informes(View view){
        new DialogoInforme(MenuActivity.this, listaVehiculos, listaEventos, listaAlarmas, MenuActivity.this);
    }

    //El usuario podr?? seleccionar los datos a mostrar en el cuadro de di??logo
    public void ajustes(View view){
        new DialogoAjustes(context);
    }

    public void toggleMenu(View view){
        showDownloadMenu=!showDownloadMenu;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){
        mMap = googleMap;

        //Cargo los datos de los veh??culos y los muestro al cargar el mapa
        iniciarlizarWS();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                marker.showInfoWindow();
                Vehiculo vehi = mapaVehiculos.get(marker.getTitle());
                new DialogoDatos(MenuActivity.this, vehi, listaEventos, listaAlarmas);
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


    public void sacaListView(){
        btnEvento.setVisibility(View.INVISIBLE);
        btnAlarma.setVisibility(View.INVISIBLE);
        recyclerEventos.setVisibility(View.VISIBLE);
        btnEsconde.setVisibility(View.VISIBLE);
        linearTitulo.setVisibility(View.VISIBLE);
    }

    public void escondeListView(){
        recyclerEventos.setVisibility(View.INVISIBLE);
        btnEsconde.setVisibility(View.INVISIBLE);
        btnEvento.setVisibility(View.VISIBLE);
        btnAlarma.setVisibility(View.VISIBLE);
        linearTitulo.setVisibility(View.INVISIBLE);
    }

}



