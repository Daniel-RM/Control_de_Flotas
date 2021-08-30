package com.example.controldeflotas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogoDatosCan {

    @SuppressLint("ResourceAsColor")
    public DialogoDatosCan(Context context, Vehiculo vehiculo){

        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_datos_can);

        TextView tvMatricula, tvDireccion, tvFechaHora, tvVelocidad, tvDistancia, tvAltitud, tvRpm, tvTemperatura, tvPresion;
        TextView tvOdometro, tvCombustibleUsado, tvHorasMotor, tvCombustibleNivel, tvTemperaturaMotor, tvDistanciaServicio;
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

        tvOdometro = dialogo.findViewById(R.id.tvOdometro);
        tvCombustibleUsado = dialogo.findViewById(R.id.tvCombustibleUsado);
        tvHorasMotor = dialogo.findViewById(R.id.tvHorasMotor);
        tvCombustibleNivel = dialogo.findViewById(R.id.tvCombustibleNivel);
        tvTemperaturaMotor = dialogo.findViewById(R.id.tvTemperaturaMotor);
        tvDistanciaServicio = dialogo.findViewById(R.id.tvDistanciaServicio);

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

        //tvMatricula.setText(vehiculo.getIdentificador());
        tvMatricula.setText(vehiculo.getMatricula().trim());
        tvDireccion.setText(Datos.obtenerDireccion(vehiculo.getLatitud(), vehiculo.getLongitud()));
        tvFechaHora.setText(vehiculo.getFecha());
        //tvFechaHora.setText(fecha);
        tvVelocidad.setText(vehiculo.getVelocidad() + " km/h");
        tvDistancia.setText(vehiculo.getDistancia() + " kms");
        tvAltitud.setText(vehiculo.getAltitud() + " mts");
        tvRpm.setText(vehiculo.getRpm() + " rpm");
        tvTemperatura.setText(vehiculo.getTemperatura() + " °C");
        tvPresion.setText(vehiculo.getPresion().substring(0,3) + " mbar");
        tvOdometro.setText(vehiculo.getOdometro() + " kms");
        tvCombustibleUsado.setText(vehiculo.getCombustibleTotalUsado() + " lts");
        tvHorasMotor.setText(vehiculo.getHorasMotor() + " hrs");
        tvCombustibleNivel.setText(vehiculo.getCombustibleNivel() + " %");
        tvTemperaturaMotor.setText(vehiculo.getTmpMotor() + " °");
        tvDistanciaServicio.setText(vehiculo.getDistanciaServicio() + " kms");


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });


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