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

public class RegistroActivity extends AppCompatActivity {

    TextView inicioSesion;
    Button btnRegistro;

    EditText txtUsuario, txtEmail, txtContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasena = findViewById(R.id.txtContrasena);
        inicioSesion = findViewById(R.id.txtIniciarSesion);
        btnRegistro = findViewById(R.id.btnRegistro);

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this,SesionActivity.class);
                startActivity(intent);
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbSesion dbSesion = new DbSesion(RegistroActivity.this);
                long id = dbSesion.insertarSesion(txtUsuario.getText().toString(), txtEmail.getText().toString(), txtContrasena.getText().toString());

                if (id > 0) {
                    Toast.makeText(RegistroActivity.this, "Registro Existoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroActivity.this,SesionActivity.class);
                    startActivity(intent);
                    limpiar();
                } else {
                    Toast.makeText(RegistroActivity.this, "Registro Fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void limpiar() {
        txtUsuario.setText("");
        txtEmail.setText("");
        txtContrasena.setText("");
    }
}