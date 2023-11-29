package com.example.taskmaster;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import com.example.taskmaster.Alarma.AlarmReceiver;
import com.example.taskmaster.Alarma.NotificationService;
import com.example.taskmaster.databinding.ActivityMainBinding;
import com.example.taskmaster.db.DbTareas;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_NOTIFICATION_PERMISSION_CODE = 1;

    private NotificationManagerCompat notificationManager;
    private long taskId;
    private NotificationCompat.Builder builder;
    ActivityMainBinding binding;
    FloatingActionButton btnCrear;
    EditText txtFechaLimite, descripcion, taskName;
    TextView txtFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TaskMaster);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnCrear = findViewById(R.id.btnCrear);
        txtFragment = findViewById(R.id.txtFragment);

        replaceFragment(new TareaFragment());
        binding.bottomNavigationView.setBackground(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notisFecha", "notisFecha2", NotificationManager.IMPORTANCE_DEFAULT);

            Log.d("NotificationService", "Creando canal de notificación...");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        // Solicitar permisos de vibración
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, REQUEST_NOTIFICATION_PERMISSION_CODE);
        }


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.inicio) {
                replaceFragment(new TareaFragment());
                txtFragment.setText("Inicio");
            } else if (itemId == R.id.cuenta) {
                replaceFragment(new CuentaFragment());
                txtFragment.setText("Perfil");
            }
            return true;
        });
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        taskName = view.findViewById(R.id.taskName);
        descripcion = view.findViewById(R.id.descripcion);
        txtFechaLimite = view.findViewById(R.id.txtFechaLimite);

        builder.setTitle("Agregar Tarea")
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbTareas dbTareas = new DbTareas(MainActivity.this);
                        long id = dbTareas.insertarTarea(taskName.getText().toString(), descripcion.getText().toString(), txtFechaLimite.getText().toString());
                        setAlarm();
                        replaceFragment(new TareaFragment());

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


        txtFechaLimite = view.findViewById(R.id.txtFechaLimite);
        txtFechaLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;

                        String selectedDateTime = selectedDate + " " + selectedTime;

                        txtFechaLimite.setText(selectedDateTime);
                    }
                }, hourOfDay, minute, false);

                timePickerDialog.show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }


    private void limpiar() {
        taskName.setText("");
        descripcion.setText("");
        txtFechaLimite.setText("");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void setAlarm() {
        String taskDateTime = txtFechaLimite.getText().toString();
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
            intent.putExtra("TASK_NAME", taskName.getText().toString());
            intent.putExtra("TASK_TIME", timeInMillis);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

            // Configura la alarma para que se active en la fecha y hora especificadas
            alarmManager.set(AlarmManager.RTC, timeInMillis, pendingIntent);

            Intent serviceIntent = new Intent(this, NotificationService.class);
            startService(serviceIntent);
        }
    }

}