package com.example.controldeflotas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewZonasAdapter extends BaseAdapter {

    public ArrayList<Zona> zonasList;
    Activity activity;

    public ListViewZonasAdapter(ArrayList<Zona> zonasList, Activity activity) {
        this.zonasList = zonasList;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return zonasList.size();
    }

    @Override
    public Object getItem(int position) {
        return zonasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView mtvCodigoZonas;
        TextView mtvDireccionZonas;
        TextView mtvTipoZonas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.lista_zonas, null);
            holder = new ViewHolder();
            holder.mtvCodigoZonas = (TextView)convertView.findViewById(R.id.tvCodigoZonas);
            holder.mtvDireccionZonas = (TextView)convertView.findViewById(R.id.tvDireccionZonas);
            holder.mtvTipoZonas = (TextView)convertView.findViewById(R.id.tvTipoZonas);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Zona item = zonasList.get(position);
        holder.mtvCodigoZonas.setText(item.getCodigo());
        holder.mtvDireccionZonas.setText(item.getDireccion());
        holder.mtvTipoZonas.setText(item.getTipo());

        return convertView;
    }
}
