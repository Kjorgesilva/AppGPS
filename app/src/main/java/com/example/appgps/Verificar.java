package com.example.appgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.example.appgps.ForeGround.MyForeGroundService;

public class Verificar extends Activity {



    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(Verificar.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Verificar.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //se não tiver a permissão e ela ja foi negada anteriormente, exibe uma explicação para o usuario
            if (ActivityCompat.shouldShowRequestPermissionRationale(Verificar.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(Verificar.this);
                alerta.setTitle("Atenção");
                alerta.setMessage("É necessário que aceite a permissão de acesso a localização " +
                        "para que as funções do aplicativo possam funcionar corretamente.");
                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //abre o dialog padrão do android que não pode ser alterado, perguntando se o usuário aceita as permissões ou não
                        ActivityCompat.requestPermissions(Verificar.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                2);
                    }
                });
                alerta.setCancelable(false);
                alerta.show();

            } else {
                //se não tiver a permissão e ela nunca foi negada ou exibida para o usuário, exibe um dialog
                //padrão do android que não pode ser alterado, perguntando se o usuário aceita as permissões ou não
                ActivityCompat.requestPermissions(Verificar.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);

            }


        }
    }
}
