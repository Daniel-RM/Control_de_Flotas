package com.example.controldeflotas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//Clase que maneja el cuadro de diálogo de los datos del coche sin los datos del CAN
public class DialogoDatos  {

    @SuppressLint("ResourceAsColor")
    public DialogoDatos(Context context, Vehiculo vehiculo){

        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_datos);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String fecha = format.format(currentDate);

        TextView  tvMatricula, tvDireccion, tvFechaHora, tvVelocidad, tvDistancia, tvAltitud, tvRpm, tvTemperatura, tvPresion;
        Button btnDetalle, btnCancel;

        tvMatricula = dialogo.findViewById(R.id.tvMatricula);
        tvDireccion = dialogo.findViewById(R.id.tvDireccion);
        tvFechaHora = dialogo.findViewById(R.id.tvFechaHora);

        tvVelocidad = dialogo.findViewById(R.id.tvVelocidad);
        tvDistancia = dialogo.findViewById(R.id.tvDistancia);
        tvAltitud = dialogo.findViewById(R.id.tvAltitud);
        tvRpm = dialogo.findViewById(R.id.tvRpm);
        tvTemperatura = dialogo.findViewById(R.id.tvTemperatura);
        tvPresion = dialogo.findViewById(R.id.tvPresion);

        btnDetalle = dialogo.findViewById(R.id.btnDetalle);
        btnCancel = dialogo.findViewById(R.id.btnCancel);

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
            tvVelocidad.setText( "No hay datos");
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

        //Botón que esconde el cuadro de diálogo
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        //Botón que llama a Detalles Activity, para mostrar el informe del vehículo seleccionado
        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(context, DetallesActivity.class);
                intento.putExtra("Vehiculo", vehiculo);
                context.startActivity(intento);
                dialogo.dismiss();
            }
        });

        dialogo.show();

    }

}
