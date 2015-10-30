package com.example.root.myservice.broadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.root.myservice.service.ScreenOnOffService;

/**
 * Created by root on 16/9/15.
 */
public class MyReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, ScreenOnOffService.class);
        context.startService(myIntent);

    }
}