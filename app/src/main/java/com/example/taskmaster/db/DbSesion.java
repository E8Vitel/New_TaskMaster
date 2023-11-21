package com.example.taskmaster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public boolean autenticarUsuario(String email, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "email=? AND contrasena=?";
        String[] selectionArgs = {email, contrasena};

        Cursor cursor = db.query(TABLE_SESION, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
    public boolean revisarEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_SESION +" WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean revisarUsuario(String usuario) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_SESION +" WHERE usuario=?", new String[]{usuario});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}
