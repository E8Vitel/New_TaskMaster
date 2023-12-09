package com.example.taskmaster.Alarma;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.taskmaster.MainActivity;
import com.example.taskmaster.R;

public class NotificationService extends JobService {
    public class NotificationChannels {
        public static final String TAREAS_CHANNEL_ID = "notificacionesTareas";
    }

    private static final String CHANNEL_ID = NotificationChannels.TAREAS_CHANNEL_ID;
    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();
        showNotification();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void showNotification() {
        // Create the notification channel first

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Recordatorio de tarea")
                .setContentText("¡Tu tarea está llegando!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }


    private void createNotificationChannel() {
        CharSequence name = "notificacionesTareas";
        String description = "Notis fecha";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
