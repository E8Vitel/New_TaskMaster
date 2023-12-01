package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SesionActivity extends AppCompatActivity {

    Button btnInicio;
    TextView registro;
    SignInButton btnSignIn;
    EditText txtEmailIs, txtContraIs;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        auth = FirebaseAuth.getInstance();
        btnInicio = findViewById(R.id.btnInicio);
        registro = findViewById(R.id.txtRegistroIs);
        txtEmailIs = findViewById(R.id.txtEmailIs);
        txtContraIs = findViewById(R.id.txtContraIs);
        btnSignIn = findViewById(R.id.btnSignIn);


        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String email = txtEmailIs.getText().toString().trim();
                    String contrasena = txtContraIs.getText().toString().trim();

                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (!contrasena.isEmpty()) {
                            auth.signInWithEmailAndPassword(email, contrasena).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(SesionActivity.this, "Bienvenido!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SesionActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SesionActivity.this, "Algo ha salido mal...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SesionActivity.this, "El campo contraseña no puede estar vacio", Toast.LENGTH_SHORT).show();
                        }
                    } else if (email.isEmpty()){
                        Toast.makeText(SesionActivity.this, "El campo email no puede estar vacio", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SesionActivity.this, "Introduzca un email valido", Toast.LENGTH_SHORT).show();
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