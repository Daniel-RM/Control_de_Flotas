
package com.example.controldeflotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


public class DetallesActivity extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    Bitmap bmp;
    InputStream in;

    private GoogleMap mMap;
    static Vehiculo vehiculo;

    EditText editText;
    TextView tvCoche;

    Button btnEnviar, btnDescargar;
    ImageButton btnVuelve, btnVisual;

    ProgressBar pbDetalles;

    ListView listView;
    ArrayList<Datos> listaDatos = new ArrayList<>();
    List<Datos> listaPuntos = new ArrayList<>();

    Marker punto, marcaRuta, puntoFinal;
    Polyline line = null;

    String fecha, fechaRuta;
    String NOMBRE_DIRECTORIO = "/storage/self/primary/Download";
    String NOMBRE_DOCUMENTO  = "MiPDF.pdf";
    String[] informacionArray;
    String[] partes;
    int lineas, tipoMapa;

    private ArrayList<Datos> datosList;
    ListViewAdapter adapter;

    Drawable imagen;

    @SuppressLint({"WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        tvCoche = findViewById(R.id.tvCoche);
        listView = findViewById(R.id.listView);

        datosList = new ArrayList<Datos>();

        //Mantengo la aplicaci??n fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MenuActivity.context = this;

        editText = findViewById(R.id.editTextDate);
        btnDescargar = findViewById(R.id.btnDescargar);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnVuelve = findViewById(R.id.btnVuelve);
        pbDetalles = findViewById(R.id.pbDetalles);
        btnVisual = findViewById(R.id.btnVisuali);


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

        tvCoche.setText(vehiculo.getMatricula().trim());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                recogeDatos(fechaRuta);
            }
        });

        //Bot??n para volver a la pantalla inicial
        btnVuelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intento);
            }
        });

        //Bot??n para descargar el PDF con los datos de la ruta
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaDatos.size()==0){
                    Toast.makeText(getApplicationContext(), "No existen datos de esta fecha", Toast.LENGTH_SHORT).show();
                }else {
                    lineas = listaDatos.size();
                    crearPDF(false, editText.getText().toString());
                }
            }
        });

        //Bot??n para enviar el PDF con los datos de la ruta
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineas = listaDatos.size();
                crearPDF(true, editText.getText().toString());
            }
        });

        //Bot??n para cambiar el modo de vista del mapa
        btnVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    //Evito que puedan pulsar la tecla "hacia atr??s"
    @Override
    public void onBackPressed() {}


    //Creo el PDF con su formato
    public void crearPDF(boolean envio, String cuando) {

        File file = null;

        //Establezco el n??mero de p??ginas
        int paginas;
        int vueltas = 0;

        if ((listaDatos.size() % 30) == 0) {
            paginas = listaDatos.size() / 30;
        } else {
            paginas = (listaDatos.size() / 30) + 1;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        }

        PdfDocument miDocumento = new PdfDocument();
        Paint myPaint = new Paint();

        do {
            for (int x = 0; x < paginas; x++) {
                //Preparo la primera p??gina
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(500, 792, x + 1).create();
                PdfDocument.Page myPage = miDocumento.startPage(myPageInfo);
                Canvas canvas = myPage.getCanvas();

                myPaint.setTypeface(Typeface.MONOSPACE);

                myPaint.setColor(Color.rgb(81, 164, 157));
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(12.0f);
                canvas.drawText("Informe de " + vehiculo.getMatricula(),  myPageInfo.getPageWidth() / 2, 60, myPaint);

                //////////////////////////////////Insertar logo Arco en el PDF
                imagen = getApplicationContext().getResources().getDrawable(R.drawable.logotipo_arco);
                int ancho = 480;
                int alto = 40;
                imagen.setBounds(410,10, ancho, alto);
                imagen.draw(canvas);
                ///////////////////////////////////////////////////////////////
                ////////////////////////////////////////Insertar mapa en el PDF

                        String latEiffelTower = "48.858235";
                        String lngEiffelTower = "2.294571";
                        String url = "http://maps.google.com/maps/api/staticmap?center=" + latEiffelTower + "," + lngEiffelTower + "&zoom=15&size=200x200&sensor=false&key=AIzaSyC0xfrl2TKnA33pGZNhE1Njn2a2r1EiPmU";
                        bmp = null;
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet request = new HttpGet(url);
                        in = null;
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    in = httpClient.execute(request).getEntity().getContent();
                                    bmp = BitmapFactory.decodeStream(in);
                                    in.close();
                                }catch(IllegalStateException e){
                                    e.printStackTrace();
                                }catch(ClientProtocolException e){
                                    e.printStackTrace();
                                }catch(IOException e){
                                    e.printStackTrace();
                                }
                                Drawable mapa = new BitmapDrawable(getResources(), bmp);
                                //mapa.setBounds(10,10,480,60);
                                mapa.draw(canvas);

                            }
                        });
                ///////////////////////////////////////////////////////////////

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(10.0f);
                myPaint.setColor(Color.rgb(122, 119, 119));
                canvas.drawText("Fecha: " + cuando, 10, 85, myPaint);


                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(8.0f);
                myPaint.setColor(Color.rgb(81, 164, 157));


                String formatTitulo = String.format("%1$-10s %2$-65s %3$-20s", "Hora ","Direcci??n ","Status");

                canvas.drawText(formatTitulo, 10, 120, myPaint);
                canvas.drawLine(10, 123, myPageInfo.getPageWidth() - 10, 123, myPaint);


                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(8.0f);
                myPaint.setColor(Color.BLACK);

                int startXPosition = 10;
                int endXPosition = myPageInfo.getPageWidth() - 10;
                int startYPosition = 140;


                if (lineas > 30) {
                    if (vueltas != 0) {
                        int loop = (vueltas * 30) + 30;
                        for (int i = (vueltas * 30); i <= loop; i++) {
                            if (informacionArray[i].contains("Reinicio")) {
                                myPaint.setColor(Color.RED);
                            } else {
                                myPaint.setColor(Color.BLACK);
                            }

                            String formatDato = String.format("%1$-10s %2$-65s %3$-20s", informacionArray[i].split("/")[0].trim(),informacionArray[i].split("/")[1].trim(),informacionArray[i].split("/")[2].trim());
                            canvas.drawText(formatDato, startXPosition, startYPosition, myPaint);
                            myPaint.setAlpha(50);
                            canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, myPaint);
                            myPaint.setAlpha(255);
                            startYPosition += 20;
                        }
                        canvas.drawText("P??gina " + (vueltas+1), 440,780, myPaint);
                    } else {

                        for (int i = 0; i <= 30; i++) {
                            if (informacionArray[i].contains("Reinicio")) {
                                myPaint.setColor(Color.RED);
                            } else {
                                myPaint.setColor(Color.BLACK);
                            }
                            String formatDato = String.format("%1$-10s %2$-65s %3$-20s", informacionArray[i].split("/")[0].trim(),informacionArray[i].split("/")[1].trim(),informacionArray[i].split("/")[2].trim());
                            canvas.drawText(formatDato, startXPosition, startYPosition, myPaint);
                            myPaint.setAlpha(50);
                            canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, myPaint);
                            myPaint.setAlpha(255);
                            startYPosition += 20;
                        }
                        canvas.drawText("P??gina " + (vueltas+1), 440,780, myPaint);
                    }
                    vueltas++;
                    lineas = lineas - 30;
                    miDocumento.finishPage(myPage);
                } else {
                    for (int i = (vueltas * 30); i < listaDatos.size(); i++) {
                        if (informacionArray[i].contains("Reinicio")) {
                            myPaint.setColor(Color.RED);
                        } else {
                            myPaint.setColor(Color.BLACK);
                        }

                        String formatDato = String.format("%1$-10s %2$-65s %3$-20s", informacionArray[i].split("/")[0].trim(),informacionArray[i].split("/")[1].trim(),informacionArray[i].split("/")[2].trim());
                        canvas.drawText(formatDato, startXPosition, startYPosition, myPaint);
                        myPaint.setAlpha(50);
                        canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, myPaint);
                        myPaint.setAlpha(255);
                        startYPosition += 20;
                    }
                    canvas.drawText("P??gina " + (vueltas+1), 440,780, myPaint);
                    lineas = lineas - 30;
                    miDocumento.finishPage(myPage);
                }
            }
        } while (lineas > 0);

        //Creo el fichero. Si existe uno previo, lo borro
        NOMBRE_DOCUMENTO = vehiculo.getMatricula().trim() + ".pdf";
        file = new File(NOMBRE_DIRECTORIO, NOMBRE_DOCUMENTO);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
        }

        //Escribo en el fichero
        try {
            miDocumento.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Directorio: ", file.getAbsolutePath());

        Toast.makeText(getApplicationContext(), "PDF creado correctamente en " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        miDocumento.close();

        if(envio == true){

            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), "com.example.controldeflotas" + ".provider", file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.putExtra(Intent.EXTRA_EMAIL,"");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Informe de " + vehiculo.getIdentificador());//Asunto - t??tulo mensaje
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola.\nLe adjunto PDF con el informe de " + vehiculo.getIdentificador() + "\nUn saludo.");//Mensaje
            emailIntent.setType("application/pdf");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "Enviar email usando:"));
        }else{
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), DetallesActivity.this.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try{
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(getApplicationContext(), "No existe una aplicaci??n para abrir el PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //M??todo que recoge los datos de un veh??culo en una fecha concreta, los muestra en la listView y muestra la ruta en el mapa
    public void recogeDatos(String fecha) {

        int posic = 0;

        if(!listaDatos.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listaDatos.clear();
                    listaPuntos.clear();
                    line.remove();
                    informacionArray = new String[0];
                    partes = new String[0];
                    datosList.clear();
                }
            });
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

                if(devuelve.equals("")){
                    pbDetalles.setVisibility(View.INVISIBLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "No hay datos de este veh??culo en esta fecha", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                String separa = Pattern.quote("||");
                partes = devuelve.split(separa);
                informacionArray = new String[partes.length];

                int contador = 0;

                for (String trama : partes) {
                    contador++;
                    Datos dato = new Datos();
                    String separa2 = Pattern.quote("|");
                    String[] datos = trama.split(separa2);
                    if(dato==null){
                        continue;
                    }
//                    if(partes.length == 1){
//                        continue;
//                    }
                    String hora = datos[2].substring(0,2) + ":" + datos[2].substring(2,4) + ":" + datos[2].substring(4,6);
                    dato.setLatitud(Double.parseDouble(datos[0]));
                    dato.setLongitud(Double.parseDouble(datos[1]));
                    dato.setHora(hora);
                    dato.setEstado(datos[4]);
                    dato.setVelocidad(datos[12]);
                    dato.setComportamiento(datos[7]);
                    if(!datos[16].equals("null")){
                        dato.setZona(datos[16]);
                    }else{
                        dato.setZona(dato.getLatitud() + "/" + dato.getLongitud());
                    }

                    if(datos[7].equals("Arranque motor") || datos[7].equals("Paro motor") || datos[7].equals("Reinicio m??dulo") || datos[7].equals("Inicio Descarga") || datos[7].equals("Final Descarga") || datos[7].equals("Sin codificar") || contador == partes.length){
                        listaDatos.add(dato);
                        informacionArray[posic] = dato.toString();
                        posic++;
                    }

                    listaPuntos.add(dato);
                    
                }
            }

            //Muestro la listview de la ruta y la dibujo en el mapa
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    datosList = listaDatos;
                    adapter = new ListViewAdapter(datosList, DetallesActivity.this);
                    listView.setAdapter(adapter);
                    pbDetalles.setVisibility(View.INVISIBLE);
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

                    //Dibujo la l??nea de la ruta
                    if (!adapter.isEmpty()) {
                        LatLng puntoA, puntoB;
                        for (int x = 0; x < (listaPuntos.size() - 1); x++) {
                            LatLng posicion = new LatLng(listaPuntos.get(x).getLatitud(), listaPuntos.get(x).getLongitud());
                            puntoA = new LatLng(listaPuntos.get(x).getLatitud(), listaPuntos.get(x).getLongitud());
                            puntoB = new LatLng(listaPuntos.get(x + 1).getLatitud(), listaPuntos.get(x + 1).getLongitud());

                            line = mMap.addPolyline(new PolylineOptions()
                                    .add(puntoA, puntoB)
                                    .width(5)
                                    .color(Color.RED)
                                    .geodesic(true));
                        }
                        //Coloco el icono en la ??ltima posici??n
                        Double ultimaLatitud = (listaPuntos.get(listaPuntos.size()-1).getLatitud());
                        Double ultimaLongitud = (listaPuntos.get(listaPuntos.size()-1).getLongitud());
                        LatLng ultimaPosicion = new LatLng (ultimaLatitud, ultimaLongitud);
                        puntoFinal = mMap.addMarker(new MarkerOptions().position(ultimaPosicion).title(vehiculo.getIdentificador()));

                        dibujaIcono(vehiculo, puntoFinal);

                        //Centro la c??mara en la ruta
                        LatLngBounds.Builder centraCamara = new LatLngBounds.Builder();
                        LatLng[] puntos = new LatLng[listaPuntos.size()];
                        for(int i=0;i<listaPuntos.size();i++){
                            puntos[i] = new LatLng(listaPuntos.get(i).getLatitud(), listaPuntos.get(i).getLongitud());
                        }
                        for(LatLng centros : puntos){
                            centraCamara.include(centros);
                        }

                        LatLngBounds limites = centraCamara.build();

                        int padding = 150;

                        CameraUpdate centrarmarcadores = CameraUpdateFactory.newLatLngBounds(limites, padding);
                        mMap.animateCamera(centrarmarcadores);
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

    //Muestro el calendario para que el usuario elija una fecha
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog =new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    //M??todo que recoge la fecha elegida por el usuario y manda llama al m??todo que muestra la ruta, pero con la fecha elegidoa
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String date, dia;

        pbDetalles.setVisibility(View.VISIBLE);

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

    //M??todo que dibuja el icono con el color correspondiente al tipo de veh??culo y la acci??n que est?? llevando a cabo en el momento
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