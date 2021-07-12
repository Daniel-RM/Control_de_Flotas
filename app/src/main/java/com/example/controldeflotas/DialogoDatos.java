package com.example.controldeflotas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DialogoDatos {

    Context context;

    public DialogoDatos(Context context, Vehiculo vehiculo){

        this.context = context;

        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_datos);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        //String fecha = currentDate.toString();
        String fecha = format.format(currentDate);

        TextView tvVelocidadTitle, tvDistanciaTitle, tvAltitudTitle, tvRpmTitle, tvTemperaturaTitle, tvPresionTitle, tvMatricula, tvDireccion, tvFechaHora, tvVelocidad, tvDistancia, tvAltitud, tvRpm, tvTemperatura, tvPresion;
        Button btnDetalle, btnCancel;

        tvMatricula = dialogo.findViewById(R.id.tvMatricula);
        tvDireccion = dialogo.findViewById(R.id.tvDireccion);
        tvFechaHora = dialogo.findViewById(R.id.tvFechaHora);
        tvVelocidadTitle = dialogo.findViewById(R.id.tvVelocidadTitle);
        tvDistanciaTitle = dialogo.findViewById(R.id.tvDistanciaTitle);
        tvAltitudTitle = dialogo.findViewById(R.id.tvAltitudTitle);
        tvRpmTitle = dialogo.findViewById(R.id.tvRpmTitle);
        tvTemperaturaTitle = dialogo.findViewById(R.id.tvTemperaturaTitle);
        tvPresionTitle = dialogo.findViewById(R.id.tvPresionTitle);

        tvVelocidad = dialogo.findViewById(R.id.tvVelocidad);
        tvDistancia = dialogo.findViewById(R.id.tvDistancia);
        tvAltitud = dialogo.findViewById(R.id.tvAltitud);
        tvRpm = dialogo.findViewById(R.id.tvRpm);
        tvTemperatura = dialogo.findViewById(R.id.tvTemperatura);
        tvPresion = dialogo.findViewById(R.id.tvPresion);

        btnDetalle = dialogo.findViewById(R.id.btnDetalle);
        btnCancel = dialogo.findViewById(R.id.btnCancel);


        /*tvMatricula.setText("3709LNS");
        tvDireccion.setText("Autopista Vasco-Aragonesa");
        tvFechaHora.setText(fecha);
        tvVelocidad.setText("120 Km/h");
        tvDistancia.setText("112 kms.");
        tvAltitud.setText("504m");
        tvRpm.setText("15 rpm");
        tvTemperatura.setText("149°");
        tvPresion.setText("0 mbar");*/

        switch (vehiculo.getEstado()){
            case "Parado":
                tvMatricula.setBackgroundColor(Color.RED);
                break;
            case "En Marcha":
                tvMatricula.setBackgroundColor(Color.GREEN);
                break;
            case "Ralentí":
                tvMatricula.setBackgroundColor(Color.MAGENTA);
                break;
        }

        tvMatricula.setText(vehiculo.getMatricula());
        tvDireccion.setText(obtenerDireccion(vehiculo.getLatitud(), vehiculo.getLongitud()));
        tvFechaHora.setText(fecha);
        tvVelocidad.setText(vehiculo.getVelocidad());
        tvDistancia.setText(vehiculo.getDistancia());
        tvAltitud.setText(vehiculo.getAltitud());
        tvRpm.setText(vehiculo.getRpm());
        tvTemperatura.setText(vehiculo.getTemperatura());
        tvPresion.setText(vehiculo.getPresion());



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
            }
        });

        dialogo.show();

    }

    public String obtenerDireccion(double latitud, double longitud){

            String direccion = "";

            if(latitud != 0.0 && longitud != 0.0){
                try{
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
                    if(!list.isEmpty()){
                        Address DirCalle = list.get(0);
                        direccion = DirCalle.getAddressLine(0);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            return direccion;
    }

}
