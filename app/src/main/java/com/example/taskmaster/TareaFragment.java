
package com.example.taskmaster;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.Entidades.Tareas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TareaFragment extends Fragment {
    RecyclerView tareas;
    List<Tareas> listaTareas;
    AdaptadorTareas adaptadorTareas;
    SearchView buscar;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tarea, container, false);

        tareas = view.findViewById(R.id.tareas);
        listaTareas = new ArrayList<>();
        buscar = view.findViewById(R.id.buscar);
        buscar.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        tareas.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        adaptadorTareas = new AdaptadorTareas(getActivity(), listaTareas);
        tareas.setAdapter(adaptadorTareas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Tareas").child(uid);
            dialog.show();

            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaTareas.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        Tareas tareas1 = itemSnapshot.getValue(Tareas.class);
                        listaTareas.add(tareas1);
                    }
                    adaptadorTareas.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                }
            });

            buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    buscarList(newText);
                    return true;
                }
            });
        }

        return view;
    }
    public void buscarList(String text) {
        ArrayList<Tareas> buscarList = new ArrayList<>();
        for (Tareas tareas1: listaTareas) {
            if (tareas1.getNombre().toLowerCase().contains(text.toLowerCase())) {
                buscarList.add(tareas1);
            }
        }
        adaptadorTareas.buscarTarea(buscarList);
    }
}
