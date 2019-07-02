package com.example.appgps.ForeGround;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appgps.R;
import com.example.appgps.WebService.LocalizacaoAtualWs;

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class MyForeGroundService extends Service {
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";


    public boolean ativo = true;
    public int count = 0;
    LocationManager locationManager;
    String lattitude, longitude;
    public Context contexto = this;
    public Handler mHandler;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mHandler = new Handler();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    MyForeGroundService.Worke w = new Worke(startId);
                    w.start();
                    break;


            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public Notification getNotification() {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }

        Intent intent = new Intent();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_simbolo_logo_sighra_color);
        builder.setSubText("Localização");

        //aparecer um card antes
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setFullScreenIntent(pendingIntent, true);


        // Texto do card
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("SIGhRA");
        builder.setStyle(bigTextStyle);

        Notification notification = builder.setPriority(PRIORITY_MAX).setCategory(Notification.CATEGORY_SERVICE).build();
        // Start foreground service.
        startForeground(1, notification);

        return notification;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }

    private void startForegroundService() {
        getNotification();
    }


    class Worke extends Thread {
        public int startId = 0;

        public Worke(int startId) {
            this.startId = startId;
        }


        public void run() {


            while (ativo) {

                try {
                    Thread.sleep(5000);


                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Log.e("gps", "GPS Desligado");
                        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!enabled) {

                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(contexto, "Ative a localição do seu aparelho", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

//                                    android.support.v7.app.AlertDialog.Builder alerta = new android.support.v7.app.AlertDialog.Builder(contexto);
//
//                                    LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                    View n = inflater.inflate(R.layout.dialog, null);
//                                    alerta.setView(n);
//
//                                    final AlertDialog fechar = alerta.create();
//                                    fechar.setCanceledOnTouchOutside(false);
//                                    Button btn = n.findViewById(R.id.btnFechar);
//                                    btn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Log.e("testeste", "Deu certo o alrta");
//                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(intent);
//                                        }
//                                    });
//
//
//                                    alerta.setCancelable(false);
//                                    alerta.show();
//                                    Log.e("passou", "passou");


//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);


                        }


                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        getLocation();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {

            //GPS
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //REDE
            Location locationREDE = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (locationGPS != null) {
                double latti = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.e("localização", "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);
                enviaValor(latti, longi);

            } else if (locationREDE != null) {
                double latti = locationREDE.getLatitude();
                double longi = locationREDE.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.e("localização", "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);
                enviaValor(latti, longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.e("localização", "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);

            } else {

                Log.e("log", "Não deu para rastrear");

            }
        }
    }


    private void enviaValor(Double latitude, Double longitude) {
        Map<String, Double> map = new HashMap<>();
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        LocalizacaoAtualWs.enviarLocalizacao(contexto, "localizacao", map);

    }


}



