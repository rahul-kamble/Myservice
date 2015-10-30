package com.example.root.myservice.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.IBinder;
import android.util.Log;

import com.example.root.myservice.broadcastReciever.AlarmReceiver;
import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.modelClass.DataUsage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScreenOnOffService extends Service {
    ArrayList<String> packageName=new ArrayList<>();
    MydatabaseHelper mDb;
    BroadcastReceiver mybroadcast = new BroadcastReceiver() {

        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.e("BroadcastReceiver", "MyReceiver");
            Intent intent1 = new Intent(getApplicationContext(), AppControlService.class);

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.e("BroadcastReceiver", "Screen ON");
                startService(intent1);
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e("BroadcastReceiver]", "Screen OFF");
                context.stopService(intent1);
            }

        }
    };
    private AlarmManager alarmMgr;

    public ScreenOnOffService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mDb=new MydatabaseHelper(getApplicationContext());
//        ArrayList<DataUsage> dataUsageArrayList=mDb.readDataUsage();
//       for(int i=0;i<dataUsageArrayList.size();i++)
//       {
//           packageName.add(dataUsageArrayList.get(i).getPackageName());
//       }

        Intent intent = new Intent(getApplicationContext(), AppControlService.class);
        startService(intent);
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);

        // Set the alarm to start at approximately 01:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);

// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.

        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }


}
