package com.example.controldeflotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etNombre, etPass;

    String nombre, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Mantengo la aplicación fija en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnLogin = findViewById(R.id.btnLogin);
        etNombre = findViewById(R.id.etNombre);
        etPass = findViewById(R.id.etPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean correcto;

                nombre = etNombre.getText().toString();
                password = etPass.getText().toString();

                if(nombre.equals("arco0") && password.equals("arco0")){
                    correcto = true;
                }else{
                    if(nombre.isEmpty() || password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Por favor, tiene que rellenar los dos campos", Toast.LENGTH_SHORT).show();
                        correcto = false;
                    }else {
                        Toast.makeText(getApplicationContext(), "Revise los datos. Alguno es erróneo", Toast.LENGTH_SHORT).show();
                        correcto = false;
                    }
                }

                if(correcto) {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}