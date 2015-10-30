package com.example.root.myservice.broadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.modelClass.DataUsage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30/10/15.
 */
public class ShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> list = new ArrayList<String>();
        Log.e("ShutdownReciever", "ShutdownReciever");
        MydatabaseHelper mydatabaseHelper = new MydatabaseHelper(context);
        mydatabaseHelper.open();
        ArrayList<DataUsage> dataUsageArrayList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS);
                if (info.requestedPermissions != null) {
                    for (String p : info.requestedPermissions) {
                        if (p.equals("android.permission.INTERNET")) {
                            pm.getApplicationLabel(packageInfo).toString();
                            list.add(pm.getApplicationLabel(packageInfo).toString()); // add in 2nd list if it is user installed app
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            int UID = 0;
            DataUsage dataUsage = new DataUsage();
            String packageName = list.get(i).toString();
            for (ApplicationInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {
                    //get the UID for the selected app
                    UID = packageInfo.uid;
                    break; //found a match, don't need to search anymore
                }

            }
            long data = TrafficStats.getUidRxBytes(UID) + TrafficStats.getUidTxBytes(UID);
            dataUsage.setDatalimit((int) data);
            dataUsage.setPackageName(packageName);
            dataUsageArrayList.add(dataUsage);

        }
        mydatabaseHelper.addAppDataUsage(dataUsageArrayList);

    }
}
