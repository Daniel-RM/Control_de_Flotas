package com.example.controldeflotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAlarmasAdapter extends RecyclerView.Adapter<RecyclerAlarmasAdapter.ViewHolder>{

    private List<Alarma> alarmaList;
    private Context context;

    public RecyclerAlarmasAdapter(List<Alarma> alarmaList, Context context) {
        this.alarmaList = alarmaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_eventos, parent, false);
        parent.setBackgroundColor(context.getColor(android.R.color.holo_orange_light));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHora.setText(alarmaList.get(position).getFecha());
        holder.tvMatricula.setText(Alarma.dameMatricula(alarmaList.get(position).getIdmodulo()));
        holder.tvEvento.setText(alarmaList.get(position).getTipo());
    }

    @Override
    public int getItemCount() {
        return alarmaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvHora, tvMatricula, tvEvento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHora = itemView.findViewById(R.id.tvHoraEvento);
            tvMatricula = itemView.findViewById(R.id.tvMatriculaEvento);
            tvEvento = itemView.findViewById(R.id.tvComportamientoEvento);
        }
    }

}
