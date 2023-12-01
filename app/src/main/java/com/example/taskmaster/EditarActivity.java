
package com.example.taskmaster;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class EditarActivity extends AppCompatActivity {

    EditText txtFecha, txtDesc, txtTarea;
    Button btnModificar, btnVolver;
    FloatingActionButton btnEditar, btnEliminar;
    Boolean correcto = false;
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

                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

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
}