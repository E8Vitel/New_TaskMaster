package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskmaster.Entidades.Tareas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CrearActivity extends AppCompatActivity {

    EditText taskName, descripcion, txtFechaLimite;
    private FirebaseAuth auth;

    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        auth = FirebaseAuth.getInstance();
        taskName = findViewById(R.id.createTarea);
        descripcion = findViewById(R.id.createDesc);
        txtFechaLimite = findViewById(R.id.createFecha);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirDatos();
            }
        });
        txtFechaLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });
    }

    public void subirDatos() {

        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        String tarea = taskName.getText().toString();
        String description = descripcion.getText().toString();
        String fecha = txtFechaLimite.getText().toString();

        Tareas tareas = new Tareas(tarea, description, fecha);

        FirebaseDatabase.getInstance().getReference("Tareas").child(uid).child(tarea)
                .setValue(tareas).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CrearActivity.this, "Tarea creada", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CrearActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void showDateTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                TimePickerDialog timePickerDialog = new TimePickerDialog(CrearActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);

                        String selectedDateTime = selectedDate + " " + selectedTime;

                        txtFechaLimite.setText(selectedDateTime);
                    }
                }, hourOfDay, minute, false);

                timePickerDialog.show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}