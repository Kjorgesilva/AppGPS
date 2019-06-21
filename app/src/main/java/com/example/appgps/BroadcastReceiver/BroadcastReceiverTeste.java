package com.example.appgps.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.appgps.Service.TesteService;

public class BroadcastReceiverTeste extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, TesteService.class);
            context.startService(pushIntent);
        }
    }
}
