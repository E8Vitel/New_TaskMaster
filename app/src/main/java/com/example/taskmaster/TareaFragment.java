package com.example.taskmaster;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.taskmaster.db.DbTareas;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmaster.adaptadores.listaTareasAdapter;
import com.example.taskmaster.entidades.Tareas;

import java.util.ArrayList;

public class TareaFragment extends Fragment {
    RecyclerView listaTareas;
    ArrayList<Tareas> listaArrayTareas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tarea, container, false);

        listaTareas = view.findViewById(R.id.listaTareas);
        listaTareas.setLayoutManager(new LinearLayoutManager(getActivity()));

        DbTareas dbTareas = new DbTareas(getActivity());

        listaArrayTareas = new ArrayList<>();

        listaTareasAdapter adapter = new listaTareasAdapter(dbTareas.mostrarTareas());
        listaTareas.setAdapter(adapter);

        return view;
    }
}