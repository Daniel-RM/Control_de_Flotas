package com.example.controldeflotas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//Adaptador personalizado, para mostrar la hora, dirección postal y el comportamiento de un vehículo
public class ListViewAdapter extends BaseAdapter {

    public ArrayList<Datos> datosList;
    Activity activity;

    public ListViewAdapter(ArrayList<Datos> datosList, Activity activity) {
        this.datosList = datosList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return datosList.size();
    }

    @Override
    public Object getItem(int position) {
        return datosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView mtvHoraDatos;
        TextView mtvDireccionDatos;
        TextView mtvAccionDatos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.lista_datos, null);
            holder = new ViewHolder();
            holder.mtvHoraDatos = (TextView)convertView.findViewById(R.id.tvHoraDatos);
            holder.mtvDireccionDatos = (TextView)convertView.findViewById(R.id.tvDireccionDatos);
            holder.mtvAccionDatos = (TextView)convertView.findViewById(R.id.tvAccionDatos);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Datos item = datosList.get(position);
        holder.mtvHoraDatos.setText(item.getHora());
        holder.mtvDireccionDatos.setText(Datos.obtenerDireccion(item.getLatitud(), item.getLongitud()));
        holder.mtvAccionDatos.setText(item.getComportamiento());

        return convertView;
    }

}
