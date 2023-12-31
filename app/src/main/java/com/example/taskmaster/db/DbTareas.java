package com.example.taskmaster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.taskmaster.TareaFragment;
import com.example.taskmaster.entidades.Tareas;

import java.util.ArrayList;

public class DbTareas extends DbHelper {

    Context context;

    public DbTareas(@Nullable Context context) {
        super(context);
        this.context = context;
    }


    public long insertarTarea(String nombre, String descripcion, String fecha) {

        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            values.put("fecha", fecha);
            id = db.insert(TABLE_NOTAS, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }

    public ArrayList<Tareas> mostrarTareas() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Tareas> listaTareas = new ArrayList<>();
        Tareas tarea = null;
        Cursor cursorTareas = null;

        cursorTareas = db.rawQuery("SELECT * FROM " + TABLE_NOTAS, null);

        if (cursorTareas.moveToFirst()) {
            do {
                tarea = new Tareas();
                tarea.setId(cursorTareas.getInt(0));
                tarea.setNombre(cursorTareas.getString(1));
                tarea.setDescripcion(cursorTareas.getString(2));
                tarea.setFecha(cursorTareas.getString(3));
                listaTareas.add(tarea);
            } while (cursorTareas.moveToNext());
        }
        cursorTareas.close();
        return listaTareas;
    }

    public Tareas verTareas(int id) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Tareas tarea = null;
        Cursor cursorTareas = null;

        cursorTareas = db.rawQuery("SELECT * FROM " + TABLE_NOTAS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorTareas.moveToFirst()) {
            tarea = new Tareas();
            tarea.setId(cursorTareas.getInt(0));
            tarea.setNombre(cursorTareas.getString(1));
            tarea.setDescripcion(cursorTareas.getString(2));
            tarea.setFecha(cursorTareas.getString(3));

        }
        cursorTareas.close();
        return tarea;
    }

    public boolean editarTarea(int id, String nombre, String descripcion, String fecha) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_NOTAS + " SET nombre = '"+nombre+"', descripcion = '"+descripcion+"',fecha = '"+fecha+"' WHERE id = '"+id+"' ");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }
        return correcto;
    }

    public boolean eliminarTarea(int id) {

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_NOTAS + " WHERE id = '" + id +"'");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }
        return correcto;
    }
}
