package com.example.appgps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import com.example.appgps.ForeGround.MyForeGroundService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private TextView localizacao;
    private LocationManager locationManager;
    private LocationListener listener;
    private Context contexto = this;
    private Button botao;

    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(contexto, MyForeGroundService.class);
        intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);

        Verificar verificar = new Verificar();

        //verifica se a permissão de localização foi aceita ou recusada
        if (verificaPermissaoLocalizacao()) {
            //se a permissão de localização foi aceita, chama o método que verifica o status (ligada ou desligada) de localização do sistema
            verificaStatusLocalizacao();
        } else {
            //se a permissão de localização não foi aceita, chama o método que requisita permissão novamente
            verificar.requestPermission();
        }


    }
    public boolean verificaPermissaoLocalizacao() {
        if (ContextCompat.checkSelfPermission(contexto,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            return true;
        } else {


            return false;
        }
    }


    public void verificaStatusLocalizacao() {

        //verifica se a localização do dispositivo está ligada ou desligada

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();

        SettingsClient mSettingsClient = LocationServices.getSettingsClient(MainActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //quando a localização estiver ligada, entra neste método
                        Log.e("GPS", "success.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // quando a localização ainda não estiver ligada, entra neste método
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {

                                    ResolvableApiException rae = (ResolvableApiException) e;

                                    // para chamar o onActivityResult em uma activity
                                    // rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                                    // para chamar o onActivityResult em um fragment
                                    // rae.getResolution().getIntentSender() = exibe um dialog solicitando para que o usuário ative a localização
                                    // startIntentSenderForResult chama o onActivityResult para capturar a resposta da interação do usuário com o
                                    // dialog exibido pelo rae.getResolution().getIntentSender()
                                    startIntentSenderForResult(rae.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null,
                                            0, 0, 0, null);

                                    Log.e("GPS", "solicitou que usuário ative a localização");

                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS", "Não foi possível executar a ação do dialog.");
                                }
                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                                break;
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS", "checkLocationSettings -> onCanceled");
                    }
                });
    }



}
