package com.example.controldeflotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//Clase que maneja la pantalla de Login
public class LoginActivity extends AppCompatActivity implements Serializable{

    Button btnLogin;
    EditText etNombre, etPass;

    CheckBox tvCheck;

    String nombre, password, cookieMaestra;
    boolean correcto = false;

    static List<Flota> myObjects = null;

    ProgressBar pbConecta;

    public static String urlFinalLocal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnLogin = findViewById(R.id.btnEntrada);
        etNombre = findViewById(R.id.etCodigoEmpresa);
        etPass = findViewById(R.id.etPass);

        tvCheck = findViewById(R.id.tvCheck);
        pbConecta = findViewById(R.id.pbConecta);

        pbConecta.setVisibility(View.INVISIBLE);

        Intent intento = this.getIntent();
        Bundle extra = intento.getExtras();

        String flota = extra.getString("url");

        urlFinalLocal = "flotas.arcoelectronica.net:8083/" + flota;
        //PARA PRUEBAS CON EL TELÉFONO PEQUEÑO
        //urlFinalLocal = "arco06server:8083/" + flota ;//Servidor de Arco

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isConnected()){
            Toast.makeText(getApplicationContext(), "Por favor, tiene que tener algún tipo de conexión a la red. Conéctese a red wifi, o con Datos móviles", Toast.LENGTH_LONG).show();
            return;
        }

        int tipoConexion = networkInfo.getType();

        //Si está en Arco. Comprueba si la conexión es por wifi o datos
//        if(flota.equals("visorarco")) {
//            if (tipoConexion == 1) {
//                urlFinalLocal = "arco06server:8083/visorarco";
//            } else {
//                urlFinalLocal = "flotas.arcoelectronica.net:8083/visorarco";
//            }
//        }

        etPass.setTypeface(Typeface.DEFAULT);
        etPass.setTransformationMethod(new PasswordTransformationMethod());

        //Si hay guardadas credenciales, las muestro
        credenciales();


        //Al pulsar el botón de Login, comprueba si hay datos guardados, los compruebo y si es correcto, realizo la conexión y recojo los datos de la Flota
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etNombre.setTextColor(Color.BLACK);
                correcto = false;

                pbConecta.setVisibility(View.VISIBLE);//ProgressBar al intentar realizar la conexión

                nombre = etNombre.getText().toString().trim();
                password = etPass.getText().toString().trim();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(proceso(nombre, password)){
                            if(tvCheck.isChecked()){
                                MenuActivity.borraDatos = false;
                                SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user", nombre);
                                editor.putString("pass", password);
                                editor.commit();
                            }
                            List<Flota> flotas = recogerDatos();//NO QUITAR esta línea. flotas, no se usa en esta Activity, pero sí en MenuActivity
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                        } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(nombre.isEmpty() || password.isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Los datos introducidos son erróneos", Toast.LENGTH_SHORT).show();
                                        }
                                        pbConecta.setVisibility(View.INVISIBLE);
                                    }
                                });
                        }
                    }
                });
            }
        });
    }

    //Comprueba que los datos introducidos, sean correctos
    public boolean proceso(String nom, String pass) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        StringBuffer response = new StringBuffer();
        BufferedReader reader = null;
        String linea, devuelve = "";
        URL url = null;
        try {
            url = new URL("http://" + urlFinalLocal + "/validarLogin.action?username=" + nom + "&password=" + pass);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Object respuesta = connection.getContent();
            if (respuesta instanceof InputStream) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((linea = reader.readLine()) != null) {
                    response.append(linea);
                }
                devuelve = response.toString();
                Log.e("Connection:", devuelve);
            }
            if (devuelve.contains("<input type=\"password\"")) {
                correcto = false;
            } else {
                correcto = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
        for (HttpCookie cookie : cookies) {
            cookieMaestra = cookie.getValue();
            Log.e("Cookies:", cookie.getDomain() + "; " + cookie.getValue());
        }
        return correcto;
    }

    //Recojo los datos de la flota
    public List recogerDatos(){

        String url = "http://" + urlFinalLocal + "/vehiculosFlota.action";

        Response response = null;

        OkHttpClient client = new OkHttpClient();
        Uri uri = Uri.parse(url);
        Request request = new Request.Builder()
                .url(uri.toString())
                .addHeader("Cookie","JSESSIONID="+cookieMaestra)
                .get()
                .build();

        try {
            response = client.newCall(request).execute();
            pbConecta.setVisibility(View.INVISIBLE);//Escondo la ProgressBar al realizar la conexión
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "No ha sido posible realizar la conexión. Por favor, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
            pbConecta.setVisibility(View.INVISIBLE);//Escondo la ProgressBar si algo ha ido mal, para que el usuario, vuelva a intentar realizar la conexión
            e.printStackTrace();
        }

        try {
            String prueba =  response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            myObjects = mapper.readValue(prueba.trim(), new TypeReference<List<Flota>>(){});


        } catch (Exception e) {
            e.printStackTrace();
        }

        return myObjects;
    }

    //Si el usuario guardó las credenciales, se mostrarán aquí
    public void cargarPreferencias(){

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("user","");
        String pass = preferences.getString("pass","");

        etNombre.setText(user);
        etPass.setText(pass);
    }

    //Compruebo si el usuario ha guardado las credenciales o no
    public void credenciales(){
        SharedPreferences prefBorrado = getSharedPreferences("borrar", Context.MODE_PRIVATE);
        boolean borramos = prefBorrado.getBoolean("borrar", false);
        if(!borramos){
            cargarPreferencias();
        }else{
            etNombre.setText("");
            etPass.setText("");
        }
    }
}