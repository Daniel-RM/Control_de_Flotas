package com.example.controldeflotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerEventosVehiculosAdapter extends RecyclerView.Adapter<RecyclerEventosVehiculosAdapter.ViewHolder>{

    private List<Evento> eventosList;
    private Context context;

    public RecyclerEventosVehiculosAdapter(List<Evento> eventosList, Context context){
        this.eventosList = eventosList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_eventos_vehiculo, parent, false);
        parent.setBackgroundColor(context.getColor(R.color.bluefondo));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHora.setText(eventosList.get(position).getFecha());
        holder.tvEvento.setText(eventosList.get(position).getTipo());
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return eventosList.size();
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
