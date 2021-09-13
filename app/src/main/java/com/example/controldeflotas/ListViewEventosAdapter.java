package com.example.controldeflotas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListViewEventosAdapter extends BaseAdapter {

    public ArrayList<Evento> eventosList;
    Activity activity;

    public ListViewEventosAdapter(ArrayList<Evento> eventosList, Activity activity) {
        this.eventosList = eventosList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return eventosList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView mtvHoraEvento;
        TextView mtvMatriculaEvento;
        TextView mtvComportamientoEvento;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.lista_eventos, null);
            holder = new ViewHolder();
            holder.mtvHoraEvento = (TextView)convertView.findViewById(R.id.tvHoraEvento);
            holder.mtvMatriculaEvento = (TextView)convertView.findViewById(R.id.tvMatriculaEvento);
            holder.mtvComportamientoEvento = (TextView)convertView.findViewById(R.id.tvComportamientoEvento);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Evento item = eventosList.get(position);
        holder.mtvHoraEvento.setText(item.getFecha());
        holder.mtvMatriculaEvento.setText(dameMatricula(item.getIdmodulo()));
        holder.mtvComportamientoEvento.setText(String.valueOf(item.getTipo()));

        return convertView;
    }

    public String dameMatricula(String id){
        String matricula = "";
        for(int x=0;x<MenuActivity.listaVehiculos.size();x++){
            if(id.equals(MenuActivity.listaVehiculos.get(x).getIdentificador())){
                matricula = MenuActivity.listaVehiculos.get(x).getMatricula();
            }
        }
        return matricula;
    }
}
