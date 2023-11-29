package com.example.taskmaster;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.Alarma.AlarmReceiver;
import com.example.taskmaster.Alarma.NotificationService;
import com.example.taskmaster.db.DbTareas;
import com.example.taskmaster.entidades.Tareas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditarActivity extends AppCompatActivity {

    EditText txtFecha, txtDesc, txtTarea;
    Button btnModificar, btnVolver;
    FloatingActionButton btnEditar, btnEliminar;
    Boolean correcto = false;
    Tareas tareas;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtTarea = findViewById(R.id.txtTarea);
        txtDesc = findViewById(R.id.txtDesc);
        txtFecha = findViewById(R.id.txtFecha);
        btnModificar = findViewById(R.id.btnUpdate);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        btnEditar = findViewById(R.id.editar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        DbTareas dbTareas = new DbTareas(EditarActivity.this);
        tareas = dbTareas.verTareas(id);

        if (tareas != null) {
            txtTarea.setText(tareas.getNombre());
            txtDesc.setText(tareas.getDescripcion());
            txtFecha.setText(tareas.getFecha());
            btnEditar.setVisibility(View.INVISIBLE);
            btnEliminar.setVisibility(View.INVISIBLE);
        }

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtTarea.getText().toString().equals("") || txtFecha.getText().toString().equals("")) {
                    correcto = dbTareas.editarTarea(id, txtTarea.getText().toString(), txtDesc.getText().toString(), txtFecha.getText().toString());
                    if (correcto) {
                        Toast.makeText(EditarActivity.this, "Registro Actualizado!", Toast.LENGTH_LONG).show();
                        verRegistro();
                        setAlarm();
                    } else {
                        Toast.makeText(EditarActivity.this, "Error al modificar el registro...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditarActivity.this, "Debe rellenar los campos de tarea o fecha!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void verRegistro() {
        Intent intent = new Intent(EditarActivity.this, VerActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
    private void showDateTimePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Aquí puedes manejar la fecha seleccionada
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                // Crear el TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditarActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Aquí puedes manejar la hora y los minutos seleccionados
                        String selectedTime = hourOfDay + ":" + minute;

                        // Concatenar la fecha y la hora seleccionadas
                        String selectedDateTime = selectedDate + " " + selectedTime;

                        // Mostrar la fecha y la hora en tu TextView o donde sea necesario
                        txtFecha.setText(selectedDateTime);
                    }
                }, hourOfDay, minute, false); // El último parámetro indica si se muestra el formato de 24 horas o no

                timePickerDialog.show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
    private void setAlarm() {
        String taskDateTime = txtFecha.getText().toString();
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
            intent.putExtra("TASK_NAME", txtTarea.getText().toString());
            intent.putExtra("TASK_TIME", timeInMillis);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

            // Configura la alarma para que se active en la fecha y hora especificadas
            alarmManager.set(AlarmManager.RTC, timeInMillis, pendingIntent);

            Intent serviceIntent = new Intent(this, NotificationService.class);
            startService(serviceIntent);
        }
    }
}