package com.example.root.myservice.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Browser;
import android.util.Log;
import android.widget.Toast;

import com.example.root.myservice.LockScreenActivity;
import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.modelClass.BlockUrl;
import com.example.root.myservice.modelClass.MasterTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AppControlService extends Service {


    MydatabaseHelper mDb;
    private Timer timer;
    boolean urlVisited=false;
    int  counter=0;
    private Calendar fromTime, toTime, currentTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //String activityOnTop;
        mDb = new MydatabaseHelper(getApplicationContext());
        mDb.open();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                getActiveApp();
//                getChromeLastURL();
//                boolean t=checkTime("10:12 pm","11:20 pm","11:50 pm");

//              boolean t=  checkTime("11:00-12:00");
                Log.e("hi","Service Started");

            }
        }, 0, 3000);

//        handler.postDelayed(checkData, 1000);


    }


    private String getCurrentTime() {
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String currentTime = null;
        if (locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:m");
            currentTime = sdf.format(location.getTime());
            Log.v("time", currentTime);
        }

        return currentTime;


    }

    public boolean checkTime(String startTime, String endTime, String currenttime) {
        try {
            String[] starttime = startTime.split(" ");
            String[] starthr = starttime[0].split(":");

            String[] enttime = endTime.split(" ");
            String[] endhr = enttime[0].split(":");
            String[] currenthr = currenttime.split(":");


            fromTime = Calendar.getInstance();
            fromTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starthr[0]));
            fromTime.set(Calendar.MINUTE, Integer.valueOf(starthr[1]));

            toTime = Calendar.getInstance();
            toTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhr[0]));
            toTime.set(Calendar.MINUTE, Integer.valueOf(endhr[1]));

            currentTime = Calendar.getInstance();
            currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(currenthr[0]));
            Log.e("h", "" + Calendar.HOUR_OF_DAY);
            currentTime.set(Calendar.MINUTE, Integer.parseInt(currenthr[1]));
            Log.e("r", "" + Calendar.MINUTE);
            if (currentTime.after(fromTime) && currentTime.before(toTime)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

    private void blokURL() {
        ArrayList<BlockUrl> blockUrls = mDb.readBlockedUrl();
        if (blockUrls.size() != 0) {
            for (int i = 0; i < blockUrls.size(); i++) {
                String url = blockUrls.get(i).getUrl();
                String chromeUrl = getChromeLastURL();
                String lasturl=chromeUrl;
                Log.e("u", url);
                Log.e("url", chromeUrl);
                boolean b = chromeUrl.contains(url);

                if (b == true) {
                    int c=counter;
                    counter++;
                    int id=counter++;
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://www.appcontrol.com/"));
                    viewIntent.setPackage("com.android.browser");
                    viewIntent.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.browser");
                    viewIntent.setFlags(Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(viewIntent);

                }
            }
        }
    }

    private String getChromeLastURL() {
        String[] proj = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL};
        //  Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
        Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj, sel, null, Browser.BookmarkColumns.DATE + " ASC");
        mCur.moveToFirst();
        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")

        String url = "";

        mCur.moveToLast();

        //title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
        url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
        Log.v("focus" + " url", "Chrome URL = " + url);
        return url;
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    private void getActiveApp() {
        List<String> apps = new ArrayList<>();

        Log.d("d", "r");
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }

            apps.add(p.packageName);
        }

        //  List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        //   ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        //activityOnTop = ar.topActivity.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> RunningTask = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : RunningTask) {
            if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {


                if (process.processName.contains("com.android.browser")) {
                    if (isNetworkAvailable(getApplicationContext())) {
                        blokURL();
                    }
                }
                if (process.processName.contains("com.android.chrome")) {
                    if (isNetworkAvailable(getApplicationContext())) {
                        blokURL();
                    }
                }
                ArrayList<MasterTime> timeArrayList = mDb.readTimingOfPackage(process.processName);
                if (timeArrayList.size() != 0) {
                    for (int i = 0; i < timeArrayList.size(); i++) {
                        String currentTimt = getCurrentTime();
                        String startTime = timeArrayList.get(i).getStart_time();
                        String endTime = timeArrayList.get(i).getEnd_time();
                        if (checkTime(startTime, endTime, currentTimt)) {
                            Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
                            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(lockIntent);
                        }
                    }
                }

//                if(process.processName.contains("com.android.browser"))
//                {
//                    Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
//                            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(lockIntent);
//                }
            }

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //code to check installed browser in phone

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://www.google.com"));
//        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo info : list) {
//            appinfo=info;
//
//        }
        return START_STICKY;
    }


}

