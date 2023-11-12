package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmaster.db.DbHelper;

public class SesionActivity extends AppCompatActivity {

    Button btnInicio;
    TextView registro;
    TextView iforgor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        btnInicio = findViewById(R.id.btnInicio);
        registro = findViewById(R.id.txtRegistroIs);
        iforgor = findViewById(R.id.Iforgor);

        DbHelper dbHelper = new DbHelper(SesionActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Algo a salido mal...", Toast.LENGTH_LONG).show();
        }

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SesionActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SesionActivity.this,RegistroActivity.class);
                startActivity(intent);
            }
        });
    }
}