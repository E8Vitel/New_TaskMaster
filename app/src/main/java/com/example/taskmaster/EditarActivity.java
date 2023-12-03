
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.Alarma.AlarmReceiver;
import com.example.taskmaster.Alarma.NotificationService;
import com.example.taskmaster.Entidades.Tareas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditarActivity extends AppCompatActivity {

    EditText txtFecha, txtDesc, txtTarea;
    Button btnModificar, btnVolver;
    FloatingActionButton btnEditar, btnEliminar;
    DatabaseReference databaseReference;
    String nombre;
    TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtTarea = findViewById(R.id.txtTarea);
        txtDesc = findViewById(R.id.txtDesc);
        txtFecha = findViewById(R.id.txtFecha);
        btnModificar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        btnEditar = findViewById(R.id.editar);

        btnModificar.setVisibility(View.VISIBLE);
        btnEliminar.setVisibility(View.INVISIBLE);

        txtTitulo.setText("Editar Tarea");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();



        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            txtTarea.setText(bundle.getString("nombre"));
            txtDesc.setText(bundle.getString("desc"));
            txtFecha.setText(bundle.getString("fecha"));
            String nombre1 = bundle.getString("nombre");
            databaseReference = FirebaseDatabase.getInstance().getReference("Tareas").child(uid).child(nombre1);
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
                String nuevaTarea = txtTarea.getText().toString();
                String nuevaDesc = txtDesc.getText().toString();
                String nuevaFecha = txtFecha.getText().toString();

                actualizarTarea(nuevaTarea, nuevaDesc, nuevaFecha);

                setAlarm();

                finish();
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditarActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);

                        String selectedDateTime = selectedDate + " " + selectedTime;

                        txtFecha.setText(selectedDateTime);
                    }
                }, hourOfDay, minute, false);

                timePickerDialog.show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
    private void actualizarTarea(String nuevaTarea, String nuevaDesc, String nuevaFecha) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("nombre");

        Tareas tareaActualizada = new Tareas(nuevaTarea, nuevaDesc, nuevaFecha);
        tareaActualizada.setKey(key);

        databaseReference.setValue(tareaActualizada);

        Toast.makeText(EditarActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EditarActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // En tu método setAlarm() de EditarActivity
    private void setAlarm() {
        String taskDateTime = txtFecha.getText().toString();
        String taskNameValue = txtTarea.getText().toString();
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