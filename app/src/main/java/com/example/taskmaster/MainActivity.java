package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.databinding.ActivityMainBinding;
import com.example.taskmaster.db.DbTareas;
import com.example.taskmaster.entidades.Tareas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FloatingActionButton btnCrear;
    EditText txtFechaLimite,descripcion,taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnCrear = findViewById(R.id.btnCrear);

        replaceFragment(new TareaFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.inicio) {
                replaceFragment(new TareaFragment());
            } else if (itemId == R.id.config) {
                replaceFragment(new ConfigFragment());
            } else if (itemId == R.id.about) {
                replaceFragment(new AboutFragment());
            } else if (itemId == R.id.cuenta) {
                replaceFragment(new CuentaFragment());
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

                        if (id > 0) {
                            Toast.makeText(MainActivity.this, "Registro Existoso", Toast.LENGTH_SHORT).show();
                            limpiar();
                        } else {
                            Toast.makeText(MainActivity.this, "Registro Fallido", Toast.LENGTH_SHORT).show();
                        }
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
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Aquí puedes manejar la fecha seleccionada
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                txtFechaLimite.setText(selectedDate);
            }
        }, 2023, 11, 3); // Ajusta el año, mes y día según sea necesario

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