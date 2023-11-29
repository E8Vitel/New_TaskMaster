package com.example.taskmaster.Alarma;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.taskmaster.R;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


public class AlarmReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        // Resto del código de tu onReceive...
        mostrarNotificacion(intent);
    }

    private void mostrarNotificacion(Intent intent) {
        Log.d("AlarmReceiver", "Alarm received!");

        String taskName = intent.getStringExtra("TASK_NAME");
        long taskTime = intent.getLongExtra("TASK_TIME", -1);

        long currentTime = System.currentTimeMillis();

        if (taskTime != -1) {
            long timeDiff = taskTime - currentTime;
            long hoursDiff = timeDiff / (60 * 60 * 1000);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notisFecha")
                    .setSmallIcon(R.drawable.notifications_active)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setDefaults(Notification.DEFAULT_ALL);

            if (hoursDiff > 0) {
                // Tarea en el futuro
                builder.setContentTitle("A la tarea: " + taskName + " le faltan " + hoursDiff + " horas");
                builder.setContentText("Recuerda realizar tu tarea a tiempo!");
            } else {
                // Tarea ya comenzó
                builder.setContentTitle("¡Es hora de realizar tu tarea!");
                builder.setContentText("No olvides completar la tarea: " + taskName);
            }

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
    }


    private void solicitarPermisoVibrar() {
        if (context instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.VIBRATE}, 123);
        }
    }

    // Resto del código de tu BroadcastReceiver...
}

