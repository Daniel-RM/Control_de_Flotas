package com.example.controldeflotas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DialogoDatosCan {

    public DialogoDatosCan(Context context, Vehiculo vehiculo, ArrayList<Evento> listaEventos, ArrayList<Alarma> listaAlarmas){

        final Dialog dialogoCAN = new Dialog(context);
        dialogoCAN.setCancelable(false);
        dialogoCAN.setContentView(R.layout.dialogo_datos_can);

        LinearLayout llFuera1, llFuera2, llFuera3, llFuera4;

        LinearLayout lltvTitle3, lltvTitle6, lltvTitle9, lltvDato3, lltvDato6, lltvDato9;
        TextView tvMatricula, tvDireccion, tvFechaHora, tvVelocidad, tvDistancia, tvAltitud, tvRpm, tvTemperatura, tvPresion;
        TextView tvTitle1, tvTitle2, tvTitle3, tvTitle4, tvTitle5, tvTitle6, tvTitle7, tvTitle8, tvTitle9;
        TextView tvDato1, tvDato2, tvDato3, tvDato4, tvDato5, tvDato6, tvDato7, tvDato8, tvDato9;
        Button btnDetalle, btnCancel;
        int numDatos = 0;

        llFuera1 = dialogoCAN.findViewById(R.id.llFuera1);
        llFuera2 = dialogoCAN.findViewById(R.id.llFuera2);
        llFuera3 = dialogoCAN.findViewById(R.id.llFuera3);
        llFuera4 = dialogoCAN.findViewById(R.id.llFuera4);

        lltvTitle3 = dialogoCAN.findViewById(R.id.lltvTitle3);
        lltvTitle6 = dialogoCAN.findViewById(R.id.lltvTitle6);
        lltvTitle9 = dialogoCAN.findViewById(R.id.lltvTitle9);
        lltvDato3 = dialogoCAN.findViewById(R.id.lltvDato3);
        lltvDato6 = dialogoCAN.findViewById(R.id.lltvDato6);
        lltvDato9 = dialogoCAN.findViewById(R.id.lltvDato9);

        tvMatricula = dialogoCAN.findViewById(R.id.tvMatricula);
        tvDireccion = dialogoCAN.findViewById(R.id.tvDireccion);
        tvFechaHora = dialogoCAN.findViewById(R.id.tvFechaHora);
        tvVelocidad = dialogoCAN.findViewById(R.id.tvVelocidad);
        tvDistancia = dialogoCAN.findViewById(R.id.tvDistancia);
        tvAltitud = dialogoCAN.findViewById(R.id.tvAltitud);
        tvRpm = dialogoCAN.findViewById(R.id.tvRpm);
        tvTemperatura = dialogoCAN.findViewById(R.id.tvTemperatura);
        tvPresion = dialogoCAN.findViewById(R.id.tvPresion);

        tvTitle1 = dialogoCAN.findViewById(R.id.tvTitle1);
        tvTitle2 = dialogoCAN.findViewById(R.id.tvTitle2);
        tvTitle3 = dialogoCAN.findViewById(R.id.tvTitle3);
        tvTitle4 = dialogoCAN.findViewById(R.id.tvTitle4);
        tvTitle5 = dialogoCAN.findViewById(R.id.tvTitle5);
        tvTitle6 = dialogoCAN.findViewById(R.id.tvTitle6);
        tvTitle7 = dialogoCAN.findViewById(R.id.tvTitle7);
        tvTitle8 = dialogoCAN.findViewById(R.id.tvTitle8);
        tvTitle9 = dialogoCAN.findViewById(R.id.tvTitle9);

        tvDato1 = dialogoCAN.findViewById(R.id.tvDato1);
        tvDato2 = dialogoCAN.findViewById(R.id.tvDato2);
        tvDato3 = dialogoCAN.findViewById(R.id.tvDato3);
        tvDato4 = dialogoCAN.findViewById(R.id.tvDato4);
        tvDato5 = dialogoCAN.findViewById(R.id.tvDato5);
        tvDato6 = dialogoCAN.findViewById(R.id.tvDato6);
        tvDato7 = dialogoCAN.findViewById(R.id.tvDato7);
        tvDato8 = dialogoCAN.findViewById(R.id.tvDato8);
        tvDato9 = dialogoCAN.findViewById(R.id.tvDato9);

        btnDetalle = dialogoCAN.findViewById(R.id.btnDetalle);
        btnCancel = dialogoCAN.findViewById(R.id.btnCancel);

        llFuera1.setVisibility(View.GONE);
        llFuera2.setVisibility(View.GONE);
        llFuera3.setVisibility(View.GONE);
        llFuera4.setVisibility(View.GONE);

        switch (vehiculo.getEstado()){
            case "0":
                tvMatricula.setBackgroundColor(Color.GREEN);
                break;
            case "1":
                tvMatricula.setBackgroundColor(Color.RED);
                break;
            case "2":
                tvMatricula.setBackgroundColor(context.getResources().getColor(R.color.purple_700));
                break;
            case "3":
                tvMatricula.setBackgroundColor(Color.MAGENTA);
                break;
        }

        tvMatricula.setText(vehiculo.getMatricula().trim());

        numDatos = MenuActivity.datos.size();

        ////////Datos CAN a mostrar////////////////

        if(numDatos == 0){
            lltvDato3.setVisibility(View.GONE);
            lltvDato6.setVisibility(View.GONE);
            lltvDato9.setVisibility(View.GONE);
            lltvTitle3.setVisibility(View.GONE);
            lltvTitle6.setVisibility(View.GONE);
            lltvTitle9.setVisibility(View.GONE);
            Toast.makeText(context, "Seleccione los datos que quiere mostrar en la pestaña de Ajustes", Toast.LENGTH_LONG).show();
        }

        if(numDatos>0 && numDatos<=3){

            lltvDato6.setVisibility(View.GONE);
            lltvDato9.setVisibility(View.GONE);
            lltvTitle6.setVisibility(View.GONE);
            lltvTitle9.setVisibility(View.GONE);

            if(numDatos == 1){
                tvTitle1.setVisibility(View.GONE);
                tvTitle3.setVisibility(View.GONE);
                tvDato1.setVisibility(View.GONE);
                tvDato3.setVisibility(View.GONE);

                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato2.setText(trataDato(vehiculo, MenuActivity.datos.get(0)));

            }else if(numDatos == 2){
                tvTitle2.setVisibility(View.GONE);
                tvDato2.setVisibility(View.GONE);

                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));

            }else{
                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato2.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
            }

        }

        if(numDatos>3 && numDatos<=6){
            lltvDato9.setVisibility(View.GONE);
            lltvTitle9.setVisibility(View.GONE);

            if(numDatos == 4){
                tvTitle2.setVisibility(View.GONE);
                tvTitle5.setVisibility(View.GONE);
                tvDato2.setVisibility(View.GONE);
                tvDato5.setVisibility(View.GONE);

                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle4.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato4.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
                tvTitle6.setText(ponerTitulo(MenuActivity.datos.get(3)));
                tvDato6.setText(trataDato(vehiculo,MenuActivity.datos.get(3)));

            }else if(numDatos == 5){
                tvTitle5.setVisibility(View.GONE);
                tvDato5.setVisibility(View.GONE);

                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato2.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
                tvTitle4.setText(ponerTitulo(MenuActivity.datos.get(3)));
                tvDato4.setText(trataDato(vehiculo,MenuActivity.datos.get(3)));
                tvTitle6.setText(ponerTitulo(MenuActivity.datos.get(4)));
                tvDato6.setText(trataDato(vehiculo,MenuActivity.datos.get(4)));

            }else{
                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato2.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
                tvTitle4.setText(ponerTitulo(MenuActivity.datos.get(3)));
                tvDato4.setText(trataDato(vehiculo,MenuActivity.datos.get(3)));
                tvTitle5.setText(ponerTitulo(MenuActivity.datos.get(4)));
                tvDato5.setText(trataDato(vehiculo,MenuActivity.datos.get(4)));
                tvTitle6.setText(ponerTitulo(MenuActivity.datos.get(5)));
                tvDato6.setText(trataDato(vehiculo,MenuActivity.datos.get(5)));
            }
        }

        if(numDatos>6 && numDatos<=9){

            if(numDatos == 7){
                tvTitle7.setVisibility(View.GONE);
                tvDato7.setVisibility(View.GONE);
                tvTitle9.setVisibility(View.GONE);
                tvDato9.setVisibility(View.GONE);

                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato2.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
                tvTitle4.setText(ponerTitulo(MenuActivity.datos.get(3)));
                tvDato4.setText(trataDato(vehiculo,MenuActivity.datos.get(3)));
                tvTitle5.setText(ponerTitulo(MenuActivity.datos.get(4)));
                tvDato5.setText(trataDato(vehiculo,MenuActivity.datos.get(4)));
                tvTitle6.setText(ponerTitulo(MenuActivity.datos.get(5)));
                tvDato6.setText(trataDato(vehiculo,MenuActivity.datos.get(5)));
                tvTitle8.setText(ponerTitulo(MenuActivity.datos.get(6)));
                tvDato8.setText(trataDato(vehiculo,MenuActivity.datos.get(6)));

            }else if(numDatos == 8){
                tvTitle8.setVisibility(View.GONE);
                tvDato8.setVisibility(View.GONE);

                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato2.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
                tvTitle4.setText(ponerTitulo(MenuActivity.datos.get(3)));
                tvDato4.setText(trataDato(vehiculo,MenuActivity.datos.get(3)));
                tvTitle5.setText(ponerTitulo(MenuActivity.datos.get(4)));
                tvDato5.setText(trataDato(vehiculo,MenuActivity.datos.get(4)));
                tvTitle6.setText(ponerTitulo(MenuActivity.datos.get(5)));
                tvDato6.setText(trataDato(vehiculo,MenuActivity.datos.get(5)));
                tvTitle7.setText(ponerTitulo(MenuActivity.datos.get(6)));
                tvDato7.setText(trataDato(vehiculo,MenuActivity.datos.get(6)));
                tvTitle9.setText(ponerTitulo(MenuActivity.datos.get(7)));
                tvDato9.setText(trataDato(vehiculo,MenuActivity.datos.get(7)));
            }else{
                tvTitle1.setText(ponerTitulo(MenuActivity.datos.get(0)));
                tvDato1.setText(trataDato(vehiculo,MenuActivity.datos.get(0)));
                tvTitle2.setText(ponerTitulo(MenuActivity.datos.get(1)));
                tvDato2.setText(trataDato(vehiculo,MenuActivity.datos.get(1)));
                tvTitle3.setText(ponerTitulo(MenuActivity.datos.get(2)));
                tvDato3.setText(trataDato(vehiculo,MenuActivity.datos.get(2)));
                tvTitle4.setText(ponerTitulo(MenuActivity.datos.get(3)));
                tvDato4.setText(trataDato(vehiculo,MenuActivity.datos.get(3)));
                tvTitle5.setText(ponerTitulo(MenuActivity.datos.get(4)));
                tvDato5.setText(trataDato(vehiculo,MenuActivity.datos.get(4)));
                tvTitle6.setText(ponerTitulo(MenuActivity.datos.get(5)));
                tvDato6.setText(trataDato(vehiculo,MenuActivity.datos.get(5)));
                tvTitle7.setText(ponerTitulo(MenuActivity.datos.get(6)));
                tvDato7.setText(trataDato(vehiculo,MenuActivity.datos.get(6)));
                tvTitle8.setText(ponerTitulo(MenuActivity.datos.get(7)));
                tvDato8.setText(trataDato(vehiculo,MenuActivity.datos.get(7)));
                tvTitle9.setText(ponerTitulo(MenuActivity.datos.get(8)));
                tvDato9.setText(trataDato(vehiculo,MenuActivity.datos.get(8)));
            }
        }

        //////////////////////////////////////////////

        if(!Datos.obtenerDireccion(vehiculo.getLatitud(), vehiculo.getLongitud()).contains("null")){
            tvDireccion.setText(Datos.obtenerDireccion(vehiculo.getLatitud(), vehiculo.getLongitud()));
        }else{
            tvDireccion.setText("No existen datos");
        }

        tvFechaHora.setText(vehiculo.getFecha());

        if(!vehiculo.getVelocidad().equals("null")){
            tvVelocidad.setText(vehiculo.getVelocidad() + " km/h");
        }else{
            tvVelocidad.setText("Sin datos");
        }

        if(!vehiculo.getDistancia().equals("null")){
            tvDistancia.setText(vehiculo.getDistancia() + " kms");
        }else{
            tvDistancia.setText("Sin datos");
        }

        if(!vehiculo.getAltitud().equals("null")){
            tvAltitud.setText(vehiculo.getAltitud() + " mts");
        }else{
            tvAltitud.setText("Sin datos");
        }

        if(!vehiculo.getRpm().equals("null")){
            tvRpm.setText(vehiculo.getRpm() + " rpm");
        }else{
            tvRpm.setText("Sin datos");
        }

        if(!vehiculo.getTemperatura().equals("null")){
            tvTemperatura.setText(vehiculo.getTemperatura() + " °C");
        }else{
            tvTemperatura.setText("Sin datos");
        }

        if(!vehiculo.getPresion().equals("null")){
            tvPresion.setText(vehiculo.getPresion().substring(0,3) + " mbar");
        }else{
            tvPresion.setText("Sin datos");
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

    public String ponerTitulo(String dato){

        if(dato.equals("odometro")){
            dato = "Odómetro:";
        }else if(dato.equals("combustibleTot")){
            dato = "Combustible Total:";
        }else if (dato.equals("tmp_motor")){
            dato = "Temperatura del motor:";
        }else if(dato.equals("tiempo_ralenti")){
            dato = "Tiempo en ralentí:";
        }else if(dato.equals("combustible_ralenti")){
            dato = "Cobustible en ralentí";
        }else if(dato.equals("aplicaciones_freno")){
            dato = "Aplicaciones de freno:";
        }else if(dato.equals("aplicaciones_sobrefrenado")){
            dato = "Aplicaciones de sobrefrenado:";
        }else if(dato.equals("duracion_sobrevelocidad")){
            dato = "Tiempo en sobrevelocidad:";
        }else if(dato.equals("duracion_sobreaceleracion")){
            dato = "Tiempo en sobreaceleración:";
        }
        return dato;
    }


    public String trataDato(Vehiculo vehiculo, String dato){

        if(dato.equals("odometro")){
            if(!vehiculo.getOdometro().equals("null")) {
                dato = vehiculo.getOdometro() + " kms.";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("combustibleTot")){
            if(!vehiculo.getCombustibleTotalUsado().equals("null")) {
                dato = vehiculo.getCombustibleTotalUsado() + " lts.";
            }else{
                dato = "Sin datos";
            }
        }else if (dato.equals("tmp_motor")){
            if(!vehiculo.getTmpMotor().equals("null")) {
                dato = vehiculo.getTmpMotor() + " °C";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("tiempo_ralenti")){
            if(vehiculo.getRalenti()!=null) {
                dato = vehiculo.getRalenti() + " seg.";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("combustible_ralenti")){
            if(vehiculo.getCombustibleRalenti()!=null) {
                dato = vehiculo.getCombustibleRalenti() + " lts.";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("aplicaciones_freno")){
            if(vehiculo.getFreno()!=null) {
                dato = vehiculo.getFreno() + " veces";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("aplicaciones_sobrefrenado")){
            if(vehiculo.getSobrefreno()!=null) {
                dato = vehiculo.getSobrefreno() + " veces";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("duracion_sobrevelocidad")){
            if(vehiculo.getSobrevelocidad()!=null) {
                dato = vehiculo.getSobrevelocidad() + " seg.";
            }else{
                dato = "Sin datos";
            }
        }else if(dato.equals("duracion_sobreaceleracion")){
            if(vehiculo.getSobreaceleracion()!=null) {
                dato = vehiculo.getSobreaceleracion() + " seg.";
            }else{
                dato = "Sin datos";
            }
        }

        return dato;
    }

}
