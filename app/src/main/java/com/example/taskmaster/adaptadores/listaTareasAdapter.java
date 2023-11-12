package com.example.taskmaster.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.R;
import com.example.taskmaster.entidades.Tareas;

import java.util.ArrayList;

public class listaTareasAdapter extends RecyclerView.Adapter<listaTareasAdapter.TareasViewHolder> {

    ArrayList<Tareas> listaTareas;

    public listaTareasAdapter(ArrayList<Tareas> listaTareas){
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public TareasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_tarea, null, false);
        return new TareasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareasViewHolder holder, int position) {
        holder.viewNombre.setText(listaTareas.get(position).getNombre());
        holder.viewFecha.setText(listaTareas.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }
    public class TareasViewHolder extends RecyclerView.ViewHolder {
        TextView viewNombre, viewFecha;
        public TareasViewHolder(@NonNull View itemView) {
            super(itemView);

            viewNombre = itemView.findViewById(R.id.viewNombre);
            viewFecha = itemView.findViewById(R.id.viewFecha);
        }
    }
}
