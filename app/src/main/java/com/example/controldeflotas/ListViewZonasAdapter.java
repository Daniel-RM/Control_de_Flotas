package com.example.controldeflotas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewZonasAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Zona> zonasList;
    Activity activity;

    private ItemFilter mFilter = new ItemFilter();
    private LayoutInflater mInflater;
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
       // return zonasList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
        //return zonasList.get(position);
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

        //Zona item = zonasList.get(position);
        Zona item = filteredData.get(position);
        holder.mtvCodigoZonas.setText(item.getCodigo());
        //holder.mtvDireccionZonas.setText(item.getDireccion());
        holder.mtvDireccionZonas.setText(item.getDescripcion());
        holder.mtvTipoZonas.setText(item.getTipo());

        return convertView;
    }


//    private class ArrayFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence prefix) {
//            final FilterResults results = new FilterResults();
//
//            if (mOriginalValues == null) {
//                synchronized (mLock) {
//                    mOriginalValues = new ArrayList<>(mObjects);
//                }
//            }
//
//            if (prefix == null || prefix.length() == 0) {
//                final ArrayList<T> list;
//                synchronized (mLock) {
//                    list = new ArrayList<>(mOriginalValues);
//                }
//                results.values = list;
//                results.count = list.size();
//            } else {
//                final String prefixString = prefix.toString().toLowerCase();
//
//                final ArrayList<T> values;
//                synchronized (mLock) {
//                    values = new ArrayList<>(mOriginalValues);
//                }
//
//                final int count = values.size();
//                final ArrayList<T> newValues = new ArrayList<>();
//
//                for (int i = 0; i < count; i++) {
//                    final T value = values.get(i);
//                    final String valueText = value.toString().toLowerCase();
//
//                    // First match against the whole, non-splitted value
//                    if (valueText.startsWith(prefixString)) {
//                        newValues.add(value);
//                    } else {
//                        final String[] words = valueText.split(" ");
//                        for (String word : words) {
//                            if (word.startsWith(prefixString)) {
//                                newValues.add(value);
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                results.values = newValues;
//                results.count = newValues.size();
//            }
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            //noinspection unchecked
//            mObjects = (List<T>) results.values;
//            if (results.count > 0) {
//                notifyDataSetChanged();
//            } else {
//                notifyDataSetInvalidated();
//            }
//        }
//    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            //final List<String> list = originalData;
            final ArrayList<Zona> list = originalData;
            //final ArrayList<Zona> list = zonasList;
//            final List<String> list = null;
//            for(int x=0;x<zonasList.size();x++){
//                list.add(zonasList.get(x).toString());
//            }

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
