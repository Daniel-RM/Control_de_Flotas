package com.example.controldeflotas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

//Clase que maneja el cuadro de diálogo de los datos del coche con los datos del CAN
public class DialogoDatosCan {

    @SuppressLint("ResourceAsColor")
    public DialogoDatosCan(Context context, Vehiculo vehiculo, ArrayList<Evento> listaEventos, ArrayList<Alarma> listaAlarmas){

        final Dialog dialogoCAN = new Dialog(context);
        dialogoCAN.setCancelable(false);
        dialogoCAN.setContentView(R.layout.dialogo_datos_can);

        TextView tvMatricula, tvDireccion, tvFechaHora, tvVelocidad, tvDistancia, tvAltitud, tvRpm, tvTemperatura, tvPresion;
        TextView tvOdometro, tvCombustibleUsado, tvHorasMotor, tvCombustibleNivel, tvTemperaturaMotor, tvDistanciaServicio;
        Button btnDetalle, btnCancel;

        tvMatricula = dialogoCAN.findViewById(R.id.tvMatricula);
        tvDireccion = dialogoCAN.findViewById(R.id.tvDireccion);
        tvFechaHora = dialogoCAN.findViewById(R.id.tvFechaHora);

        tvVelocidad = dialogoCAN.findViewById(R.id.tvVelocidad);
        tvDistancia = dialogoCAN.findViewById(R.id.tvDistancia);
        tvAltitud = dialogoCAN.findViewById(R.id.tvAltitud);
        tvRpm = dialogoCAN.findViewById(R.id.tvRpm);
        tvTemperatura = dialogoCAN.findViewById(R.id.tvTemperatura);
        tvPresion = dialogoCAN.findViewById(R.id.tvPresion);

        tvOdometro = dialogoCAN.findViewById(R.id.tvDato1);
        tvCombustibleUsado = dialogoCAN.findViewById(R.id.tvDato2);
        tvHorasMotor = dialogoCAN.findViewById(R.id.tvDato3);
        tvCombustibleNivel = dialogoCAN.findViewById(R.id.tvDato4);
        tvTemperaturaMotor = dialogoCAN.findViewById(R.id.tvDato5);
        tvDistanciaServicio = dialogoCAN.findViewById(R.id.tvDato6);

        btnDetalle = dialogoCAN.findViewById(R.id.btnDetalle);
        btnCancel = dialogoCAN.findViewById(R.id.btnCancel);

        switch (vehiculo.getEstado()){
            case "0":
                tvMatricula.setBackgroundColor(Color.GREEN);
                break;
            case "1":
                tvMatricula.setBackgroundColor(Color.RED);
                break;
            case "2":
                tvMatricula.setBackgroundColor(R.color.purple_700);
                break;
            case "3":
                tvMatricula.setBackgroundColor(Color.MAGENTA);
                break;
        }

        tvMatricula.setText(vehiculo.getMatricula().trim());

        if(!Datos.obtenerDireccion(vehiculo.getLatitud(), vehiculo.getLongitud()).contains("null")){
            tvDireccion.setText(Datos.obtenerDireccion(vehiculo.getLatitud(), vehiculo.getLongitud()));
        }else{
            tvDireccion.setText("No existen datos");
        }

        tvFechaHora.setText(vehiculo.getFecha());

        if(!vehiculo.getVelocidad().equals("null")){
            tvVelocidad.setText(vehiculo.getVelocidad() + " km/h");
        }else{
            tvVelocidad.setText("No hay datos");
        }

        if(!vehiculo.getDistancia().equals("null")){
            tvDistancia.setText(vehiculo.getDistancia() + " kms");
        }else{
            tvDistancia.setText("No hay datos");
        }

        if(!vehiculo.getAltitud().equals("null")){
            tvAltitud.setText(vehiculo.getAltitud() + " mts");
        }else{
            tvAltitud.setText("No hay datos");
        }

        if(!vehiculo.getRpm().equals("null")){
            tvRpm.setText(vehiculo.getRpm() + " rpm");
        }else{
            tvRpm.setText("No hay datos");
        }

        if(!vehiculo.getTemperatura().equals("null")){
            tvTemperatura.setText(vehiculo.getTemperatura() + " °C");
        }else{
            tvTemperatura.setText("No hay datos");
        }

        if(!vehiculo.getPresion().equals("null")){
            tvPresion.setText(vehiculo.getPresion().substring(0,3) + " mbar");
        }else{
            tvPresion.setText("No hay datos");
        }

        if(!vehiculo.getOdometro().equals("null")){
            tvOdometro.setText(vehiculo.getOdometro() + " kms");
        }else{
            tvOdometro.setText("No hay datos");
        }

        if(!vehiculo.getCombustibleTotalUsado().equals("null")){
            tvCombustibleUsado.setText(vehiculo.getCombustibleTotalUsado() + " lts");
        }else{
            tvCombustibleUsado.setText("No hay datos");
        }

        if(!vehiculo.getHorasMotor().equals("null")){
            tvHorasMotor.setText(vehiculo.getHorasMotor() + " hrs");
        }else{
            tvHorasMotor.setText("No hay datos");
        }

        if(!vehiculo.getCombustibleNivel().equals("null")){
            tvCombustibleNivel.setText(vehiculo.getCombustibleNivel() + " %");
        }else{
            tvCombustibleNivel.setText("No hay datos");
        }

        if(!vehiculo.getTmpMotor().equals("null")){
            tvTemperaturaMotor.setText(vehiculo.getTmpMotor() + " °");
        }else{
            tvTemperaturaMotor.setText("No hay datos");
        }

        if(!vehiculo.getDistanciaServicio().equals("null")){
            tvDistanciaServicio.setText(vehiculo.getDistanciaServicio() + " kms");
        }else{
            tvDistanciaServicio.setText("No hay datos");
        }

        //Botón que esconde el cuadro de diálogo
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogoDatos(context,vehiculo, listaEventos, listaAlarmas);
                dialogoCAN.dismiss();
            }
        });

        //Botón que llama a Detalles Activity, para mostrar el informe del vehículo seleccionado
        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(context, DetallesActivity.class);
                intento.putExtra("Vehiculo", vehiculo);
                context.startActivity(intento);
                dialogoCAN.dismiss();
            }
        });

        dialogoCAN.show();
    }
}
