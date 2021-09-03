package com.example.controldeflotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

//Clase que maneja la pantalla que seleccionará una flota u otra
public class MainActivity extends AppCompatActivity {

    Button btnEntrada;
    EditText etCodigoEmpresa;

    String codigo = "";

    //Mapeo de las posibles flotas, con su código para entrar en la aplcación
    public static Map<String, String> conexiones = new HashMap<>();
    {
        conexiones.put("ARCO","visorarco");
        conexiones.put("CANDESA","ControlFlotasCandesa");
        conexiones.put("HORAESA","ControlFlotasHoraesa");
        conexiones.put("TXIKI","ControlFlotasAtxa");
        conexiones.put("VASCOS","ControlFlotasVascos");
        conexiones.put("LLOREDA","ControlFlotasLloreda");
        conexiones.put("PROMSA","ControlFlotas");
        conexiones.put("UNIFOR","ControlFlotasUnifor");
        conexiones.put("PREMON","ControlFlotasPremon");
        conexiones.put("CANARY","ControlFlotasCanary");
        conexiones.put("LLUCBETON","ControlFlotasLlucbeton");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEntrada = findViewById(R.id.btnEntrada);
        etCodigoEmpresa = findViewById(R.id.etCodigoEmpresa);

        //Comprueba que el código no sea null ni esté vacío y que esté entre los códigos de las empresas. Dependiendo del código introducido, se cargará una flota u otra
        btnEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigo = etCodigoEmpresa.getText().toString();
                if(codigo.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Por favor, introduzca un código para poder arrancar la aplicación", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (conexiones.get(codigo)==null){
                        Toast.makeText(getApplicationContext(), "Por favor, introduzca un código válido", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("url", conexiones.get(codigo));
                        startActivity(intent);
                    }
                }
            }
        });
    }
}