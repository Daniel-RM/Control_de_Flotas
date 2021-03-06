package com.example.controldeflotas;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class DialogoAjustes {

    Dialog dialogo;
    Context context;

    CheckBox chkOdometro, chkCombustible, chkTemp, chkRalenti, chkCombustibleRalenti, chkFrenadas, chkSobrefrenadas, chkVelocidad, chkAceleracion;

    Button btnGuardaDatos, btnSeleccion, btnCancelaSel;

    public DialogoAjustes(Context context){
        dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_ajustes);

        this.context = context;

        MenuActivity.datos = new ArrayList<>();

        chkOdometro = dialogo.findViewById(R.id.chkOdometro);
        chkCombustible = dialogo.findViewById(R.id.chkCombustible);
        chkTemp = dialogo.findViewById(R.id.chkTemp);
        chkRalenti = dialogo.findViewById(R.id.chkRalenti);
        chkCombustibleRalenti = dialogo.findViewById(R.id.chkCombustibleRalenti);
        chkFrenadas = dialogo.findViewById(R.id.chkFrenadas);
        chkSobrefrenadas = dialogo.findViewById(R.id.chkSobrefrenadas);
        chkVelocidad = dialogo.findViewById(R.id.chkVelocidad);
        chkAceleracion = dialogo.findViewById(R.id.chkAceleracion);
        btnGuardaDatos = dialogo.findViewById(R.id.btnGuardaDatos);
        btnSeleccion = dialogo.findViewById(R.id.btnSeleccion);
        btnCancelaSel = dialogo.findViewById(R.id.btnCancelaSel);


        btnCancelaSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });


        btnSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chkOdometro.isChecked()) {
                    chkOdometro.setChecked(true);
                    chkCombustible.setChecked(true);
                    chkTemp.setChecked(true);
                    chkRalenti.setChecked(true);
                    chkCombustibleRalenti.setChecked(true);
                    chkFrenadas.setChecked(true);
                    chkSobrefrenadas.setChecked(true);
                    chkVelocidad.setChecked(true);
                    chkAceleracion.setChecked(true);
                    btnSeleccion.setText("Quitar selecci??n");
                }else{
                    chkOdometro.setChecked(false);
                    chkCombustible.setChecked(false);
                    chkTemp.setChecked(false);
                    chkRalenti.setChecked(false);
                    chkCombustibleRalenti.setChecked(false);
                    chkFrenadas.setChecked(false);
                    chkSobrefrenadas.setChecked(false);
                    chkVelocidad.setChecked(false);
                    chkAceleracion.setChecked(false);
                    btnSeleccion.setText("Seleccionar todo");
                }
            }
        });

        //Recojo los datos CAN seleccionados por el usuario para mostrar
        btnGuardaDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chkOdometro.isChecked()){
                    MenuActivity.datos.add("odometro");
                }
                if(chkCombustible.isChecked()){
                    MenuActivity.datos.add("combustibleTot");
                }
                if(chkTemp.isChecked()){
                    MenuActivity.datos.add("tmp_motor");
                }
                if(chkRalenti.isChecked()){
                    MenuActivity.datos.add("tiempo_ralenti");
                }
                if(chkCombustibleRalenti.isChecked()){
                    MenuActivity.datos.add("combustible_ralenti");
                }
                if(chkFrenadas.isChecked()){
                    MenuActivity.datos.add("aplicaciones_freno");
                }
                if(chkSobrefrenadas.isChecked()){
                    MenuActivity.datos.add("aplicaciones_sobrefrenado");
                }
                if(chkVelocidad.isChecked()){
                    MenuActivity.datos.add("duracion_sobrevelocidad");
                }
                if(chkAceleracion.isChecked()){
                    MenuActivity.datos.add("duracion_sobreaceleracion");
                }

                dialogo.dismiss();
            }
        });

        dialogo.show();
    }



}
