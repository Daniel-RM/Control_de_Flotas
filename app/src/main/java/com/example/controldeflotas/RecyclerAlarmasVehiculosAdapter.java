package com.example.controldeflotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAlarmasVehiculosAdapter extends RecyclerView.Adapter<RecyclerAlarmasVehiculosAdapter.ViewHolder>{

    private List<Alarma> alarmaList;
    private Context context;

    public RecyclerAlarmasVehiculosAdapter(List<Alarma> alarmaList, Context context){
        this.alarmaList = alarmaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_alertas_vehiculo, parent, false);
        parent.setBackgroundColor(context.getColor(R.color.bluefondo));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHora.setText(alarmaList.get(position).getFecha());
        holder.tvEvento.setText(alarmaList.get(position).getTipo());
    }

    @Override
    public int getItemCount() {
        return alarmaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvHora, tvEvento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHora = itemView.findViewById(R.id.tvHoraVehiculo);
            tvEvento = itemView.findViewById(R.id.tvEventoVehiculo);
        }
    }

}
