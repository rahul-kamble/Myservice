package com.example.root.myservice.broadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.root.myservice.AppListActivity;

/**
 * Created by root on 17/9/15.
 */
public class LaunchViaDialReceiver extends BroadcastReceiver {
    private static final String LAUNCHER_NUMBER = "12345";

    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNubmer = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        if (LAUNCHER_NUMBER.equals(phoneNubmer)) {
            setResultData(null);
            Intent appIntent = new Intent(context, AppListActivity.class);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
    }
}

