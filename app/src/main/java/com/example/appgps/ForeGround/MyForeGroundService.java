package com.example.appgps.ForeGround;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.appgps.R;


public class MyForeGroundService extends Service {
    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_PLAY = "ACTION_PLAY";

    public boolean ativo = true;
    public int count = 0;



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
        if(intent != null)
        {
            String action = intent.getAction();

            switch (action){
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "Ligado", Toast.LENGTH_LONG).show();
                    MyForeGroundService.Worke w = new Worke(startId);
                    w.start();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    ativo = false;
                    count = 0;
                    Toast.makeText(getApplicationContext(), "Desligado", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /* Used to build and start foreground service. */
    private void startForegroundService()
    {
        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
//        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
//        builder.setLargeIcon(largeIconBitmap);
        builder.setSubText("Localização");
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("SIGhRA");
        bigTextStyle.bigText("Mensagem");
        // Set big text style.
        builder.setStyle(bigTextStyle);


        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

//         Add Play button intent in notification.
//        Intent playIntent = new Intent(this, MyForeGroundService.class);
//        playIntent.setAction(ACTION_PLAY);
//        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
//        NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "", pendingPlayIntent);
//        builder.addAction(playAction);

//        // Add Pause button intent in notification.
//        Intent pauseIntent = new Intent(this, MyForeGroundService.class);
//        pauseIntent.setAction(ACTION_PAUSE);
//        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
//        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
//        builder.addAction(prevAction);

        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);
    }

    private void stopForegroundService()
    {
        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

  class Worke extends Thread {
        public int startId = 0;

        public Worke(int startId) {
            this.startId = startId;
        }

        @SuppressLint("MissingPermission")
        public void run() {

            while (ativo) {
                //Log.e("contador", "antes try ");
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                Log.e("contador", "contador: " + count);
//                Looper.prepare();
//                Toast.makeText(getApplicationContext(), "contador" + count, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "ativo: " + ativo, Toast.LENGTH_SHORT).show();
//
//                Log.e("contador", "antes do loop ");
//                Looper.loop();
//                Looper.getMainLooper().quit();
//                Log.e("contador", "depois loop ");


            }
        }

    }
}
