package com.example.root.myservice.broadcastReciever;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by root on 16/9/15.
 */
public class DeviceAdmin extends DeviceAdminReceiver
{
    //	implement onEnabled(), onDisabled(),
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
//        Intent intent1=new Intent(context, LoginActivity.class)'
//        startActivity(intent1);

        return "you cant deactivate";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
    }

}