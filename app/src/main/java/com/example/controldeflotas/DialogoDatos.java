package com.example.controldeflotas;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//Clase que maneja el cuadro de diálogo de los datos del coche sin los datos del CAN
public class DialogoDatos {

    boolean sacado = false;

    @SuppressLint("ResourceAsColor")
    public DialogoDatos(Context context, Vehiculo vehiculo, ArrayList<Evento> listaEv, ArrayList<Alarma> listaAl){

        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.dialogo_datos);


        TextView  tvMatricula, tvDireccion, tvFechaHora, tvVelocidad, tvDistancia, tvAltitud, tvRpm, tvTemperatura, tvPresion;
        TextView tvViaje, tvPlanta, tvCliente, tvObra, tvAlbaran, tvM3, tvAgua;
        View llViaje;
        Button btnDetalle, btnCancel;
        Button btnInfo, btnEventos, btnAlertas;
        RecyclerView rvEventos;
        RecyclerEventosVehiculosAdapter adapter;
        RecyclerAlarmasVehiculosAdapter adapterAl;
        ArrayList<Evento> listaEventos = listaEv;
        ArrayList<Alarma> listaAlarmas = listaAl;
        ArrayList<Evento> listaEventoVehiculo = new ArrayList<>();
        ArrayList<Alarma> listaAlarmaVehiculo = new ArrayList<>();
        int tipoViaje = 0;
        int cuentas, cuentasAl;


        tvMatricula = dialogo.findViewById(R.id.tvMatricula);
        tvDireccion = dialogo.findViewById(R.id.tvDireccion);
        tvFechaHora = dialogo.findViewById(R.id.tvFechaHora);
        tvVelocidad = dialogo.findViewById(R.id.tvVelocidad);
        tvDistancia = dialogo.findViewById(R.id.tvDistancia);
        tvAltitud = dialogo.findViewById(R.id.tvAltitud);
        tvRpm = dialogo.findViewById(R.id.tvRpm);
        tvTemperatura = dialogo.findViewById(R.id.tvTemperatura);
        tvPresion = dialogo.findViewById(R.id.tvPresion);

        llViaje =  dialogo.findViewById(R.id.llViaje);
        tvViaje = dialogo.findViewById(R.id.tvViaje);
        tvPlanta = dialogo.findViewById(R.id.tvPlanta);
        tvCliente = dialogo.findViewById(R.id.tvCliente);
        tvObra = dialogo.findViewById(R.id.tvObra);
        tvAlbaran = dialogo.findViewById(R.id.tvAlbaran);
        tvM3 = dialogo.findViewById(R.id.tvM3);
        tvAgua = dialogo.findViewById(R.id.tvAgua);
        btnInfo = dialogo.findViewById(R.id.btnInfo);
        btnEventos = dialogo.findViewById(R.id.btnEventos);
        btnAlertas = dialogo.findViewById(R.id.btnAlertas);
        rvEventos = dialogo.findViewById(R.id.rvEventos);
        adapter = new RecyclerEventosVehiculosAdapter(listaEventoVehiculo, context);
        adapterAl = new RecyclerAlarmasVehiculosAdapter(listaAlarmaVehiculo, context);


        btnDetalle = dialogo.findViewById(R.id.btnDetalle);
        btnCancel = dialogo.findViewById(R.id.btnCancel);

        for(int x = 0;x<listaEventos.size();x++){
            if(listaEventos.get(x).getIdmodulo().equals(vehiculo.getIdentificador())){
                listaEventoVehiculo.add(listaEventos.get(x));
            }
        }

        for(int z =0;z<listaAlarmas.size();z++){
            if(listaAlarmas.get(z).getIdmodulo().equals(vehiculo.getIdentificador())){
                listaAlarmaVehiculo.add(listaAlarmas.get(z));
            }
        }

        cuentas = listaEventoVehiculo.size();
        cuentasAl = listaAlarmaVehiculo.size();

        //Si hay datos de "viaje", los muestro en el Cuadro de Diálogo
        if(vehiculo.getIdviaje() == null){
            llViaje.setVisibility(View.GONE);
        }else{
            tvPlanta.setText(vehiculo.getZona_viaje().trim());
            tvCliente.setText(vehiculo.getCliente_promsa_viaje().trim());
            tvObra.setText(vehiculo.getObra_promsa_viaje().trim());
            tvAlbaran.setText(vehiculo.getAlbaran_viaje());
            tvM3.setText(vehiculo.getM3_viaje());
            tvAgua.setText(vehiculo.getAgua_total_viaje() + " Lts.");
        }

        rvEventos.setVisibility(View.GONE);

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

        if(listaEventoVehiculo.size()!=0) {
            tipoViaje = listaEventoVehiculo.get(0).getTipoNum();
        }

        tvViaje.setText(trataViaje(tipoViaje));

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

        //Pulsan el botón de la info del CAN. Escondo este cuadro y muestro el de los datos CAN
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
                //new DialogoDatosCan(context, vehiculo, listaEventos, listaAlarmas);
                new DialogoDatosCanPrueba(context, vehiculo, listaEventos, listaAlarmas);
            }
        });

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sacado){
                    if(vehiculo.getIdviaje()!=null) {
                        llViaje.setVisibility(View.VISIBLE);
                    }
                    rvEventos.setVisibility(View.GONE);
                    sacado = false;
                }else {
                    llViaje.setVisibility(View.GONE);
                    rvEventos.setVisibility(View.VISIBLE);
                    rvEventos.setLayoutManager(new LinearLayoutManager(context));
                    rvEventos.setAdapter(adapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,new LinearLayoutManager(context).getOrientation());
                    dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.line_divider));
                    rvEventos.addItemDecoration(dividerItemDecoration);
                    if (listaEventoVehiculo.size() != cuentas) {
                        adapter.notifyItemInserted(0);
                        adapter.notifyDataSetChanged();
                    }
                    sacado = true;
                }
            }
        });

        btnAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sacado){
                    if(vehiculo.getIdviaje()!=null) {
                        llViaje.setVisibility(View.VISIBLE);
                    }
                    rvEventos.setVisibility(View.GONE);
                    sacado = false;
                }else {
                    llViaje.setVisibility(View.GONE);
                    rvEventos.setVisibility(View.VISIBLE);
                    rvEventos.setLayoutManager(new LinearLayoutManager(context));
                    rvEventos.setAdapter(adapterAl);
                    if (listaAlarmaVehiculo.size() != cuentasAl) {
                        adapterAl.notifyItemInserted(0);
                        adapterAl.notifyDataSetChanged();
                    }
                    sacado = true;
                }
            }
        });

        dialogo.show();

    }

    public String trataViaje(int tipo){
        String tipoV = "";
        switch (tipo){

            case 6:
                tipoV = "Descargando en obra";
                break;
            case 13:
                tipoV = "Volviendo de obra";
                break;
            case 8:
            case 14:
            case 15:
            case 18:
                tipoV = "En dirección a obra";
                break;

        }
        return tipoV;
    }

}
