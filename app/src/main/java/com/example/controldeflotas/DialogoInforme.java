package com.example.controldeflotas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DialogoInforme {

    ArrayAdapter<Vehiculo> adaptador;
    ListView listViewCoches;
    TextView texto;
    final Dialog dialogo;

    public DialogoInforme(Context context, List<Vehiculo> lista){

        dialogo = new Dialog(context);
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.dialogo_informe);

        texto = dialogo.findViewById(R.id.textView);
        listViewCoches = dialogo.findViewById(R.id.listViewCoches);

        muestraCoches(lista);

        dialogo.show();
    }

    private void muestraCoches(List<Vehiculo>lista){
        adaptador = new ArrayAdapter<Vehiculo>(MenuActivity.context, R.layout.lista_coche,lista ){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        listViewCoches.setAdapter(adaptador);

        listViewCoches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MenuActivity.context, DetallesActivity.class);
//                intent.putExtra("Vehiculo", lista.get(position));
//                MenuActivity.context.startActivity(intent);
                if(MenuActivity.datosCAN){
                    new DialogoDatosCan(MenuActivity.context, lista.get(position));
                }else{
                    new DialogoDatos(MenuActivity.context, lista.get(position));
                }
                dialogo.dismiss();
            }
        });
    }
}
