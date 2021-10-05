package com.example.controldeflotas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

//Adaptador personalizado para mostrar el código, la dirección postal y el tipo de las zonas
public class ListViewZonasAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Zona> zonasList;
    Activity activity;

    private ItemFilter mFilter = new ItemFilter();
    private ArrayList<Zona>filteredData = null;
    private ArrayList<Zona>originalData = null;

    public ListViewZonasAdapter(ArrayList<Zona> zonasList, Activity activity) {
        this.originalData = zonasList;
        this.filteredData = zonasList;
        this.zonasList = zonasList;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return  filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
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

        Zona item = filteredData.get(position);
        holder.mtvCodigoZonas.setText(item.getCodigo());
        holder.mtvDireccionZonas.setText(item.getDescripcion());
        holder.mtvTipoZonas.setText(item.getTipo());

        return convertView;
    }

    //Filtro para buscar una zona en concreto
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Zona> list = originalData;

            int count = list.size();
            final ArrayList<Zona> nlist = new ArrayList<>(count);

            Zona filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toString().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Zona>) results.values;
            notifyDataSetChanged();
        }

    }
}
