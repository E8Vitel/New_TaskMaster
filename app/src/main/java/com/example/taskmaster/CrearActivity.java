package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.taskmaster.Alarma.AlarmReceiver;
import com.example.taskmaster.Alarma.NotificationService;
import com.example.taskmaster.Entidades.Tareas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearActivity extends AppCompatActivity {

    EditText taskName, descripcion, txtFechaLimite;
    private FirebaseAuth auth;

    Button btnGuardar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        auth = FirebaseAuth.getInstance();
        taskName = findViewById(R.id.createTarea);
        descripcion = findViewById(R.id.createDesc);
        txtFechaLimite = findViewById(R.id.createFecha);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                subirDatos();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrearActivity.this, MainActivity.class));
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
                        setAlarm(tarea);
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

    private void setAlarm(String taskNameValue) {
        String taskDateTime = txtFechaLimite.getText().toString();
        Log.d("setAlarm", "Nombre de la tarea: " + taskNameValue);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(taskDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            long timeInMillis = date.getTime();
            Log.d("AlarmTime", "Time in millis: " + timeInMillis);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("TASK_TIME", timeInMillis);
            intent.putExtra("TASK_NAME", taskNameValue);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

            // Configura la alarma para que se active en la fecha y hora especificadas
            alarmManager.set(AlarmManager.RTC, timeInMillis, pendingIntent);

            Intent serviceIntent = new Intent(this, NotificationService.class);
            startService(serviceIntent);
        }
    }
}