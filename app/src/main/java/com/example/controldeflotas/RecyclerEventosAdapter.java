package com.example.controldeflotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerEventosAdapter extends RecyclerView.Adapter<RecyclerEventosAdapter.ViewHolder>{

    private List<Evento> eventosList;
    private Context context;

    public RecyclerEventosAdapter(List<Evento> eventosList, Context context) {
        this.eventosList = eventosList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_eventos, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_eventos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHora.setText(eventosList.get(position).getFecha());
        holder.tvMatricula.setText(eventosList.get(position).getIdmodulo());
        holder.tvEvento.setText(eventosList.get(position).getTipo());

    }

    @Override
    public int getItemCount() {
        return eventosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvHora, tvMatricula, tvEvento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            tvHora = itemView.findViewById(R.id.tvHoraEventoTitle);
//            tvMatricula = itemView.findViewById(R.id.tvMatriculaEventoTitle);
//            tvEvento = itemView.findViewById(R.id.tvEventoTitle);
            tvHora = itemView.findViewById(R.id.tvHoraEvento);
            tvMatricula = itemView.findViewById(R.id.tvMatriculaEvento);
            tvEvento = itemView.findViewById(R.id.tvComportamientoEvento);

        }
    }
}
