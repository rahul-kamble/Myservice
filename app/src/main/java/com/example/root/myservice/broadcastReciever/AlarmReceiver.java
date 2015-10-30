package com.example.root.myservice.broadcastReciever;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.root.myservice.databse.MydatabaseHelper;

/**
 * Created by root on 27/10/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    MydatabaseHelper mydatabaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("alarm", "in Alarm");
        mydatabaseHelper = new MydatabaseHelper(context);
        mydatabaseHelper.deleteAllData();

    }
}
