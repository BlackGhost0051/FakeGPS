package com.blackghost.fakegps.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.blackghost.fakegps.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FakeGpsService extends Service {
    public static final String CHANNEL_ID = "fakegps_channel";
    private static final int NOTIFICATION_ID = 1001;

    private final IBinder binder = new LocalBinder();

    private ScheduledExecutorService executorService;
    private boolean running = false;
    private double currentLat;
    private double currentLon;

    public class LocalBinder extends Binder {
        public FakeGpsService getService() {
            return FakeGpsService.this;
        }
    }

    private void startForeground(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        Notification notification = buildNotification(
                "FakeGPS active",
                "Waiting for coordinates..."
        );

        startForeground(NOTIFICATION_ID, notification);

        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void onDestroy() {
        running = false;

        if(executorService != null){
            executorService.shutdownNow();
        }
        super.onDestroy();
    }


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "FakeGPS",
                            NotificationManager.IMPORTANCE_LOW
                    );

            channel.setDescription("Fake GPS foreground service");

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification(String title, String text){
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .build();
    }

    private void updateNotification(double lat, double lon){
        String text = "Lat: " + lat + " Lon: " + lon;

        Notification notification = buildNotification(
                "FakeGPS active",
                text
        );

        NotificationManager manager = getSystemService(NotificationManager.class);

        manager.notify(NOTIFICATION_ID, notification);
    }
}
