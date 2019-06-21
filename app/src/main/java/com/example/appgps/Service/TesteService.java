package com.example.appgps.Service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
        public void run (){
            while (ativo == true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                Log.e("conteador","contador" + count);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}