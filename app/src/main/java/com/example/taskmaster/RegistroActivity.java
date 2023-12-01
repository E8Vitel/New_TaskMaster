
package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmaster.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView inicioSesion;
    Button btnRegistro;
    ActivityRegistroBinding binding;
    EditText txtUsuario, txtEmail, txtContrasena, txtConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtUsuario = findViewById(R.id.txtUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasena = findViewById(R.id.txtContrasena);
        txtConfirmar = findViewById(R.id.txtConfirmar);
        inicioSesion = findViewById(R.id.txtIniciarSesion);
        btnRegistro = findViewById(R.id.btnRegistro);
        auth = FirebaseAuth.getInstance();

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, SesionActivity.class);
                startActivity(intent);
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = binding.txtUsuario.getText().toString();
                String email = binding.txtEmail.getText().toString();
                String contrasena = binding.txtContrasena.getText().toString();
                String confirmar = binding.txtConfirmar.getText().toString();

                if (usuario.equals("") || email.equals("") || contrasena.equals("") || confirmar.equals("")) {
                    Toast.makeText(RegistroActivity.this, "Todos los campos deben ser rellenados", Toast.LENGTH_SHORT).show();
                } else if (contrasena.length() > 6) {
                    if (contrasena.equals(confirmar)) {
                        auth.createUserWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistroActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistroActivity.this, SesionActivity.class);
                                    intent.putExtra("user", usuario);
                                    startActivity(intent);
                                    limpiar();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "La contraseña no puede tener menos de 6 caracteres", Toast.LENGTH_SHORT).show();
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
