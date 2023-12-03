package com.example.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.Entidades.Tareas;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorTareas extends RecyclerView.Adapter<VerTareas> {

    private Context context;
    private List<Tareas> listaTareas;

    public AdaptadorTareas(Context context, List<Tareas> listaTareas) {
        this.context = context;
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public VerTareas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_tarea, parent, false);
        return new VerTareas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerTareas holder, int position) {
            holder.viewNombre.setText(listaTareas.get(position).getNombre());
            holder.viewDescripcion.setText(listaTareas.get(position).getDescripcion());
            holder.viewFecha.setText(listaTareas.get(position).getFecha());

            holder.recCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VerActivity.class);
                    intent.putExtra("nombre", listaTareas.get(holder.getAdapterPosition()).getNombre());
                    intent.putExtra("descripcion", listaTareas.get(holder.getAdapterPosition()).getDescripcion());
                    intent.putExtra("fecha", listaTareas.get(holder.getAdapterPosition()).getFecha());
                    intent.putExtra("key", listaTareas.get(holder.getAdapterPosition()).getKey());
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void buscarTarea(ArrayList<Tareas> buscarLista) {
        listaTareas = buscarLista;
        notifyDataSetChanged();
    }
}

class VerTareas extends RecyclerView.ViewHolder {

    TextView viewNombre, viewDescripcion, viewFecha;
    RelativeLayout recCard;
    public VerTareas(@NonNull View itemView) {
        super(itemView);

        viewNombre = itemView.findViewById(R.id.viewNombre);
        viewDescripcion = itemView.findViewById(R.id.viewDescription);
        viewFecha = itemView.findViewById(R.id.viewFecha);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
