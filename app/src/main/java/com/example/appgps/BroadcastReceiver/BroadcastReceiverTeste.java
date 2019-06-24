package com.example.appgps.BroadcastReceiver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.appgps.Service.TesteService;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;


public class BroadcastReceiverTeste extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {



            final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            //checa se as permissões de localização não foram aceitas
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //enquanto as permissões não forem aceitas, vai repetindo o alarm, pois caso o usuário aceite as permissões depois,
                //o receiver já estará agendando novamente para poder obter a localização, quando as permissões são aceitas,
                //não retorna mas dentro desse if, vai direto para o else
                AlarmManager alm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                Intent intnt = new Intent(context, BroadcastReceiverTeste.class);
                intnt.setAction("LOCALIZACAO_ATUAL");
                PendingIntent pdi = PendingIntent.getBroadcast(context, 14, intnt, 0);
                if (alm != null) {
                    alm.cancel(pdi);
                    alm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 300000, pdi);
                }

                Log.e("receiver", "entrou location, permissão não concedida");

            } else {

                Log.e("receiver", "entrou location");


                //GPS_PROVIDER = Este provedor determina a localização usando satélites. Dependendo das condições, esse provedor pode demorar
                // um pouco para retornar uma posição do local, porém é o mais preciso.

                //NETWORK_PROVIDER = Este provedor determina a localização com base na disponibilidade de torres de celular e pontos de acesso WiFi.
                // os resultados são recuperados por meio de uma pesquisa de rede


                //minTime = quantidade de tempo que irá tentar trazer a localização novamente (milissegundos)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {


                    @Override
                    public void onLocationChanged(Location location) {

                        //quando recebe a posição do técnico, já agenda um novo Receiver para mandar a posição novamente em 4 minutos
                        AlarmManager alm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        Intent intnt = new Intent(context, BroadcastReceiverTeste.class);
                        intnt.setAction("LOCALIZACAO_ATUAL");
                        PendingIntent pdi = PendingIntent.getBroadcast(context, 14, intnt, 0);

                        if (alm != null) {

                            //Remove todas as atualizações de localização para este LocationListener, para não ficar
                            //repetindo o método. Após esta chamada, as atualizações não ocorrerão mais para este ouvinte,
                            //ou seja, sairá desse método e só retorna quando o Receiver chamar novamente
                            locationManager.removeUpdates(this);

                            alm.cancel(pdi);
                            alm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, 300000, pdi);
                        }

                        Log.e("receiver", "latitude " + String.valueOf(location.getLatitude()) +
                                " longitude: " + String.valueOf(location.getLongitude()));

                        Log.e("receiver", "agendado");

                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                        Log.e("receiver", "onStatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        //quando usuário liga a localização
                        Log.e("receiver", "ligado");

                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        //quando usuário desliga a localização
                        Log.e("receiver", "desligado");
                        //quando entra nesse método, quando o usuário ligar a localização novamente,
                        //o método onProviderEnabled é chamado e o sistema volta a tentar obter a localização
                        //novamente sem a necessidade de agendar um novo receiver
                    }
                });
            }
        }

}
