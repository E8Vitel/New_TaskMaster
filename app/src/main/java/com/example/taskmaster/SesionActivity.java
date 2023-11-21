package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmaster.db.DbSesion;


public class SesionActivity extends AppCompatActivity {

    Button btnInicio;
    TextView registro;

    EditText txtEmailIs, txtContraIs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        btnInicio = findViewById(R.id.btnInicio);
        registro = findViewById(R.id.txtRegistroIs);
        txtEmailIs = findViewById(R.id.txtEmailIs);
        txtContraIs = findViewById(R.id.txtContraIs);



        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmailIs.getText().toString().trim();
                String contrasena = txtContraIs.getText().toString().trim();
                DbSesion dbSesion = new DbSesion(SesionActivity.this);
                boolean autenticado = dbSesion.autenticarUsuario(email, contrasena);
                if (email.equals("") || contrasena.equals("")) {
                    Toast.makeText(SesionActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
                } else {
                    if (autenticado) {
                        Intent intent = new Intent(SesionActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SesionActivity.this, "Email y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SesionActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }

}