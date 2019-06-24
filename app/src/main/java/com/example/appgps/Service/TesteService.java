package com.example.appgps.Service;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.appgps.BroadcastReceiver.BroadcastReceiverTeste;

public class TesteService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Worke w = new Worke(startId);
        w.start();
        return super.onStartCommand(intent, flags, startId);
    }
    class Worke extends Thread{
        public int count =0;
        public int startId = 0;
        public boolean ativo = true;
        public  Worke(int startId){
                this.startId = startId;
            }
        @SuppressLint("MissingPermission")
        public void run (){

            while (ativo){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                count++;
                Log.e("conteador","contador: " + count);
                Intent intent = new Intent(getApplicationContext(), BroadcastReceiverTeste.class);
                intent.setAction("LOCALIZACAO_ATUAL");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}