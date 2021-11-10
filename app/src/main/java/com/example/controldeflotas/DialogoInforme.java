package com.example.controldeflotas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;


//Clase que maneja el cuadro de diálogo que muestra los vehículos de la flota, para seleccionar uno de ellos. Saldrá un cuadro de diálogo con los datos del vehículo seleccionado
public class DialogoInforme extends Activity{

    Context contexto;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListViewVehiculosAdapter adapter;
    ListView listViewCoches;
    TextView texto;
    final Dialog dialogo;
    ArrayList<Evento> listaEventos;
    ArrayList<Alarma> listaAlarmas;
    EditText etBuscarV;


    public DialogoInforme(Context context, ArrayList<Vehiculo> lista, ArrayList<Evento> listaEv, ArrayList<Alarma> listaAl, Activity activity){

        dialogo = new Dialog(context);
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.dialogo_informe);

        texto = dialogo.findViewById(R.id.textView);
        listViewCoches = dialogo.findViewById(R.id.listViewCoches);
        etBuscarV = dialogo.findViewById(R.id.etBuscarV);

        this.listaEventos = listaEv;
        this.listaAlarmas = listaAl;
        this.contexto = context;
        this.activity = activity;


        //Muestro la lista de vehículos
        muestraCoches(lista);

        //Filtro que buscará vehículos filtrando por el campo de búsqueda
        etBuscarV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialogo.show();
    }

    private void muestraCoches(ArrayList<Vehiculo>lista){

        adapter = new ListViewVehiculosAdapter(lista, activity);
        listViewCoches.setAdapter(adapter);

        listViewCoches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new DialogoDatos(MenuActivity.context, (Vehiculo)adapter.getItem(position), listaEventos, listaAlarmas);
                dialogo.dismiss();
            }
        });
    }

    }

