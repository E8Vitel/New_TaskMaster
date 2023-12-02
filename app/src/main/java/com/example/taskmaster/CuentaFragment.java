
package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CuentaFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    Button btnLogout;
    TextView txtNombre, txtUsuario, txtEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        btnLogout = view.findViewById(R.id.btnSignOut);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtUsuario = view.findViewById(R.id.txtUsuario);
        txtEmail = view.findViewById(R.id.txtEmail);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        obtenerDatosUsuario();
        return view;
    }
    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(getActivity(), SesionActivity.class));
        getActivity().finish();
    }
    private void obtenerDatosUsuario() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Obtener los datos del usuario
                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String usuario = snapshot.child("usuario").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        // Mostrar los datos en los TextView
                        txtNombre.setText(nombre);
                        txtUsuario.setText(usuario);
                        txtEmail.setText(email);
                    } else {
                        Toast.makeText(getActivity(), "No se encontraron datos de usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CuentaFragment", "Error al obtener datos del usuario: " + error.getMessage());
                }
            });
        }
    }
}