package com.example.taskmaster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class DbSesion extends DbHelper {

    Context context;
    public DbSesion(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    
    public long insertarSesion(String usuario, String email, String contrasena) {

        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("usuario", usuario);
            values.put("email", email);
            values.put("contrasena", contrasena);
            id = db.insert(TABLE_SESION, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }
}
