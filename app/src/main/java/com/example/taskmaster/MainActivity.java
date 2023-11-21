package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taskmaster.databinding.ActivityMainBinding;
import com.example.taskmaster.db.DbTareas;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                        String taskDateTime = txtFechaLimite.getText().toString();
                        DbTareas dbTareas = new DbTareas(MainActivity.this);
                        long id = dbTareas.insertarTarea(taskName.getText().toString(), descripcion.getText().toString(), txtFechaLimite.getText().toString());

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

    private void configureAlarmForTask(long taskId, String taskDateTime) {
        // Configurar la alarma aquí
        setAlarm(taskId, taskDateTime);
    }

    private void setAlarm(long taskId, String taskDateTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = dateFormat.parse(taskDateTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("taskId", taskId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) taskId, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Alarma configurada para la tarea", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al configurar la alarma: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void showNotificationIfCloseToDeadline(String taskDateTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = dateFormat.parse(taskDateTime);

            Calendar taskCalendar = Calendar.getInstance();
            taskCalendar.setTime(date);

            // Verificar si la fecha está cerca de 3 días
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH, 3);

            if (taskCalendar.before(now)) {
                // Si la fecha está cerca, mostrar la notificación
                showNotification("Tarea cercana", "La tarea está cerca de su fecha límite");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no tiene el permiso, solicitarlo
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.VIBRATE}, 1);
        } else {
            // Si ya tiene el permiso, continuar con la lógica para mostrar la notificación
            showNotification("Título", "Contenido de la notificación");
        }

        notificationManager.notify(1, builder.build());
    }
}