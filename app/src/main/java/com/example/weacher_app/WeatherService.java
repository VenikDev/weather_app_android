package com.example.weacher_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class WeatherService extends Service implements  WeatherDelegate {

    private boolean STOP = false;
    private boolean UPDATE = false;
    MyApplication mApp;

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                "channel_name")
                .setContentTitle("Сервис погоды")
                .setContentText("")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        startForeground(Integer.MAX_VALUE, notificationBuilder.build());

        mApp = (MyApplication) getApplicationContext();
        mApp.getWeather().setDelegate(this);

        Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    void work(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                   if(UPDATE){
                       mApp.notification(
                               mApp.getWeather().getCity() + ": " +
                                       mApp.getWeather().currentTemperature + " °C");
                       UPDATE = false;
                   }
                }
                while (!STOP);
                stopSelf();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба запущена", Toast.LENGTH_SHORT).show();
        STOP = false;
        work();
        return Service.START_STICKY;
    }

    @Override
    public void onUpdate() {
        UPDATE = true;
    }

    @Override
    public void onError() {

    }
}