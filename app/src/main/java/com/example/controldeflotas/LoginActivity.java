package com.example.controldeflotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements Serializable{

    Button btnLogin;
    EditText etNombre, etPass;

    CheckBox tvCheck;
    int x = 0;

    String nombre, password;
    boolean correcto = false;

    String cookieMaestra;

    static List<Flota> myObjects = null;

    public static String urlFinalPruebas = "arco06server:8083/ControlFlotas"; // admin
    public static String urlFinalLocal = "arco06server:8083/visorarco"; // arco0


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        btnLogin = findViewById(R.id.btnLogin);
        etNombre = findViewById(R.id.etNombre);
        etPass = findViewById(R.id.etPass);

        tvCheck = findViewById(R.id.tvCheck);

        credenciales();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etNombre.setTextColor(Color.BLACK);
                correcto = false;

//                etNombre.setText("admin");
//                etPass.setText("arco0*asi4");

//                etNombre.setText("arco0");
//                etPass.setText("arco0");

                nombre = etNombre.getText().toString();
                password = etPass.getText().toString();

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
                            List<Flota> flotas = recogerDatos();
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
                                    }
                                });
                        }
                    }
                });
            }
        });
    }


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
            // connection.getContent();
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
        } catch (IOException e) {
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

    public void cargarPreferencias(){

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();

        String user = preferences.getString("user","");
        String pass = preferences.getString("pass","");


        etNombre.setText(user);
        etPass.setText(pass);
    }

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