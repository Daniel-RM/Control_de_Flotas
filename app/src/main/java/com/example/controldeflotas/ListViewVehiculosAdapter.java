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

public class ListViewVehiculosAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Vehiculo> vehiculosList;
    Activity activity;

    private ItemFilter mFilter = new ItemFilter();
    private LayoutInflater mInflater;
    private ArrayList<Vehiculo> filteredData = null;
    private ArrayList<Vehiculo> originalData = null;

    public ListViewVehiculosAdapter(ArrayList<Vehiculo> vehiculosList, Activity activity){
        this.originalData = vehiculosList;
        this.filteredData = vehiculosList;
        this.vehiculosList = vehiculosList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return filteredData.size();
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
        if(mFilter == null){
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    private class ViewHolder{
        TextView mtvCoche;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.lista_coche, null);
            holder = new ViewHolder();
            holder.mtvCoche = (TextView)convertView.findViewById(R.id.textPersonalizado);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Vehiculo item = filteredData.get(position);
        holder.mtvCoche.setText(item.getMatricula());

        return convertView;
    }



    private class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<Vehiculo> list = originalData;
            int count = list.size();
            final ArrayList<Vehiculo> nlist = new ArrayList<>(count);
            Vehiculo filterableString;

            for(int i=0;i<count;i++){
                filterableString = list.get(i);
                if(filterableString.toString().toLowerCase().contains(filterString)){
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Vehiculo>) results.values;
            notifyDataSetChanged();
        }
    }

}
