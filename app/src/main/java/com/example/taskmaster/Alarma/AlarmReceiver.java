package com.example.taskmaster.Alarma;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.taskmaster.R;

public class AlarmReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Bundle taskName = intent.getExtras();
        if (taskName != null) {
            String nombre = taskName.getString("nombreTarea");
            Log.d("AlarmReceiver", "Bundle: " + nombre);
            mostrarNotificacion(nombre);
        }
    }

    private void mostrarNotificacion(String name) {
        Log.d("AlarmReceiver", "Alarm received!");


        Log.d("AlarmReceiver", "Nombre de la tarea recibido: " + name);
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationService.NotificationChannels.TAREAS_CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_notifications_active_24)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("¡Es hora de realizar tu tarea!")
                    .setContentText("No olvides completar la tarea: " + name);


            if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                // Tienes el permiso, mostrar la notificación
                Log.d("AlarmReceiver", "Mostrando notificación desde AlarmReceiver...");
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                Log.d("NotificationService", "Mostrando notificación...");
                notificationManager.notify(1, builder.build());
            } else {
                // Si no tienes el permiso, solicitarlo al usuario
                Log.d("AlarmReceiver", "No tienes permiso para vibrar. Solicitando permiso...");
                solicitarPermisoVibrar();
            }
        }


    private void solicitarPermisoVibrar() {
        if (context instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.VIBRATE}, 123);
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificacionesTareas";
            String description = "Notis fecha";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(NotificationService.NotificationChannels.TAREAS_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Log.d("AlarmReceiver", "Canal de notificación creado en AlarmReceiver");
        }
    }
    // Resto del código de tu BroadcastReceiver...
}
