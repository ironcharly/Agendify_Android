package com.example.agendify_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendify_android.R;
import com.example.agendify_android.model.Turno;

import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {

    public interface OnTurnoClickListener {
        void onTurnoClick(Turno turno);
    }

    private final List<Turno> turnos;
    private final OnTurnoClickListener listener;

    public TurnosAdapter(List<Turno> turnos, OnTurnoClickListener listener) {
        this.turnos = turnos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Turno turno = turnos.get(position);
        holder.tvCliente.setText(turno.getNombreClienteFinal());
        String servicio = turno.getNombreServicio() != null ? turno.getNombreServicio() : "-";
        holder.tvServicio.setText(servicio);
        holder.tvFechaHora.setText(turno.getFechaHoraInicio());
        holder.tvEstado.setText(turno.getEstado());
        holder.itemView.setOnClickListener(v -> listener.onTurnoClick(turno));
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCliente;
        final TextView tvServicio;
        final TextView tvFechaHora;
        final TextView tvEstado;

        TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvServicio = itemView.findViewById(R.id.tvServicio);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}