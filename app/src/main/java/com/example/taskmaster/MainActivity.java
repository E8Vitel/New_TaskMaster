package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.databinding.ActivityMainBinding;
import com.example.taskmaster.db.DbTareas;
import com.example.taskmaster.entidades.Tareas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

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
            } else if (itemId == R.id.config) {
                replaceFragment(new ConfigFragment());
                txtFragment.setText("Configuración");
            } else if (itemId == R.id.about) {
                replaceFragment(new AboutFragment());
                txtFragment.setText("Acerca de Task Master");
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

        // Configura el AlertDialog
        builder.setTitle("Agregar Tarea")
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbTareas dbTareas = new DbTareas(MainActivity.this);
                        long id = dbTareas.insertarTarea(taskName.getText().toString(), descripcion.getText().toString(), txtFechaLimite.getText().toString());

                        Toast.makeText(MainActivity.this, "Registro Existoso", Toast.LENGTH_SHORT).show();
                        limpiar();
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

        // Agregar lógica para mostrar el DatePickerDialog al tocar el EditText de fecha
        txtFechaLimite = view.findViewById(R.id.txtFechaLimite);
        txtFechaLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Aquí puedes manejar la hora y los minutos seleccionados
                        String selectedTime = hourOfDay + ":" + minute;

                        // Concatenar la fecha y la hora seleccionadas
                        String selectedDateTime = selectedDate + " " + selectedTime;

                        // Mostrar la fecha y la hora en tu TextView o donde sea necesario
                        txtFechaLimite.setText(selectedDateTime);
                    }
                }, hourOfDay, minute, false); // El último parámetro indica si se muestra el formato de 24 horas o no

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
}