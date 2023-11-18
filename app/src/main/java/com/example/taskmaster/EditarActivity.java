package com.example.taskmaster;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.db.DbTareas;
import com.example.taskmaster.entidades.Tareas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                showDatePickerDialog();
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
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Aquí puedes manejar la fecha seleccionada
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                txtFecha.setText(selectedDate);
            }
        }, 2023, 11, 3); // Ajusta el año, mes y día según sea necesario

        datePickerDialog.show();
    }
}