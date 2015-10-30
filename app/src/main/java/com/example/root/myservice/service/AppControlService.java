package com.example.root.myservice.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
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
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Browser;
import android.provider.Settings;
import android.util.Log;

import com.example.root.myservice.LockScreenActivity;
import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.modelClass.BlockUrl;
import com.example.root.myservice.modelClass.DataUsage;
import com.example.root.myservice.modelClass.MasterTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class AppControlService extends Service {


    MydatabaseHelper mDb;
    boolean urlVisited = false;
    int counter = 0;
    ArrayList<MasterTime> timeArrayList;
    private Timer timer;
    private Calendar fromTime, toTime, currentTime;
    private String packag;
    private ArrayList<DataUsage> dataUsageArrayList;
    private ArrayList<String> packageName = new ArrayList<>();
    private ArrayList<String> timingPackage = new ArrayList<>();
    private int data;
    private String startTime = null, endTime = null;


    public String getForegroundProcess(Context context) {
        String topPackageName = null;
        String title = null;
        UsageStatsManager usage = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        }
        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                }
            }
            if (runningTask.isEmpty()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return null;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                topPackageName = runningTask.get(runningTask.lastKey()).getPackageName();
            }
        }
        if (topPackageName == null) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(topPackageName, 0);
            title = (String) ((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("appname", "" + title);
        return title;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (dataUsageArrayList != null) {
//            for (int i = 0; i < dataUsageArrayList.size(); i++) {
//                packageName.add(dataUsageArrayList.get(i).getPackageName());
//            }
//        }

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.e("onstart", "onstart");
        mDb = new MydatabaseHelper(getApplicationContext());
        mDb.open();
        timeArrayList = mDb.readTiming();
        dataUsageArrayList = mDb.readDataUsage();


        if (dataUsageArrayList != null) {
            for (int i = 0; i < dataUsageArrayList.size(); i++) {
                packageName.add(dataUsageArrayList.get(i).getPackageName());
            }
        }
        if (timeArrayList != null) {
            for (int i = 0; i < timeArrayList.size(); i++) {
                timingPackage.add(timeArrayList.get(i).getPackege_name());
            }
        }



        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if (packageName != null) {
                        for (int i = 0; i < packageName.size(); i++) {
                            if (getForegroundProcess(getApplicationContext()) != null) {
                                if (packageName.get(i).contains(getForegroundProcess(getApplicationContext())))

                                {
                                    dataUsageArrayList.get(i).getDatalimit();
                                    packag = dataUsageArrayList.get(i).getPackageName();
                                    data = dataUsageArrayList.get(i).getDatalimit();
                                    getDataLimit();
                                    break;
//                            getTimeLimit();
                                }
                            }
                        }
                        if (timingPackage != null) {
                            for (int i = 0; i < timingPackage.size(); i++) {
                                if (timingPackage.get(i).contains(getForegroundProcess(getApplicationContext())))

                                {
                                    timeArrayList.get(i).getPackege_name();
                                    packag = timeArrayList.get(i).getPackege_name();
                                    startTime = timeArrayList.get(i).getStart_time();
                                    endTime = timeArrayList.get(i).getEnd_time();
//                            getDataLimit();
                                    getTimeLimit();
                                }
                            }
                        }

//                getChromeLastURL();
//                boolean t=checkTime("10:12 pm","11:20 pm","11:50 pm");

//              boolean t=  checkTime("11:00-12:00");

//                Log.e("current", "" + getForegroundProcess(getApplicationContext()));
                    }
                } else {
                    ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


                    //  List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
                    //   ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
                    //activityOnTop = ar.topActivity.getPackageName();
                    String title = null;
                    List<ActivityManager.RunningAppProcessInfo> RunningTask = mActivityManager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo process : RunningTask) {
                        if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            PackageManager packageManager = getPackageManager();
                            try {
                                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(process.processName, 0);
                                title = (String) ((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (packageName != null && packageName.size()>0) {
                                for (int i = 0; i < packageName.size(); i++) {
                                    if (packageName.get(i).contains(title))
                                    {
                                        dataUsageArrayList.get(i).getDatalimit();
                                        packag = dataUsageArrayList.get(i).getPackageName();
                                        data = dataUsageArrayList.get(i).getDatalimit();
                                        getDataLimit();
                                    }
                                }
                            }
                            if (timingPackage != null && timingPackage.size()>0) {
                                for (int i = 0; i < timingPackage.size(); i++) {
                                    if (timingPackage.get(i).contains(title))
                                    {
                                        timeArrayList.get(i).getPackege_name();
                                        packag = timeArrayList.get(i).getPackege_name();
                                        startTime = timeArrayList.get(i).getStart_time();
                                        endTime = timeArrayList.get(i).getEnd_time();
                                        getTimeLimit();
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }, 0, 3000);
        return super.onStartCommand(intent, flags, startId);

    }

    private void getTimeLimit() {
        if (timeArrayList.size() != 0) {
//                    for (int i = 0; i < timeArrayList.size(); i++) {
            String currentTimt = getCurrentTime();
            if (checkTime(startTime, endTime, currentTimt)) {
                Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lockIntent);
            }
        }
//                }

    }

    private String getCurrentTime() {
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String currentTime = null;
        if (locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && location != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:m");
            currentTime = sdf.format(location.getTime());
            Log.e("time", currentTime);
        }

        return currentTime;


    }

    public boolean checkTime(String startTime, String endTime, String currenttime) {
        try {
            String[] starttime = startTime.split(" ");
            String[] starthr = starttime[0].split(":");

            String[] enttime = endTime.split(" ");
            String[] endhr = enttime[0].split(":");

            fromTime = Calendar.getInstance();
            fromTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starthr[0]));
            fromTime.set(Calendar.MINUTE, Integer.valueOf(starthr[1]));

            toTime = Calendar.getInstance();
            toTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endhr[0]));
            toTime.set(Calendar.MINUTE, Integer.valueOf(endhr[1]));
            currentTime = Calendar.getInstance();

            if (currenttime != null) {

                String[] currenthr = currenttime.split(":");
                currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(currenthr[0]));
//                Log.e("h", "" + Calendar.HOUR_OF_DAY);
                currentTime.set(Calendar.MINUTE, Integer.parseInt(currenthr[1]));
//                Log.e("r", "" + Calendar.MINUTE);
            } else {
                int hr = currentTime.get(Calendar.HOUR_OF_DAY);
                if (hr == 0)
                    currentTime.set(Calendar.HOUR_OF_DAY, hr);
                Log.e("h1", "" + hr);
                int min = currentTime.get(Calendar.MINUTE);
                currentTime.set(Calendar.MINUTE, min);
                Log.e("r1", "" + min);
//                SimpleDateFormat sdf = new SimpleDateFormat("H:m");
//                time = sdf.format( Calendar.getInstance()  );
//                String[] currenthr = time.split(":");
//                currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(currenthr[0]));
//                Log.e("h1", "" + Calendar.HOUR_OF_DAY);
//                currentTime.set(Calendar.MINUTE, Integer.parseInt(currenthr[1]));
//                Log.e("r1", "" + Calendar.MINUTE);
            }

        } catch (Exception e) {
            return false;
        }
        if (currentTime.after(fromTime) && currentTime.before(toTime)) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("stop", "stopped");
        timer.cancel();

      stopSelf();

//        sendBroadcast(new Intent("YouWillNeverKillMe"));

    }

    private void blokURL() {
        ArrayList<BlockUrl> blockUrls = mDb.readBlockedUrl();
        if (blockUrls.size() != 0) {
            for (int i = 0; i < blockUrls.size(); i++) {
                String url = blockUrls.get(i).getUrl();
                String chromeUrl = getChromeLastURL();
                String lasturl = chromeUrl;
                Log.e("u", url);
                Log.e("url", chromeUrl);
                boolean b = chromeUrl.contains(url);

                if (b == true) {
                    int c = counter;
                    counter++;
                    int id = counter++;
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://www.google.com/"));
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

    private void getDataLimit() {
        List<String> apps = new ArrayList<>();
        Log.e("hi", "Service Started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getForegroundProcess(getApplicationContext()) != null)


            {
//
//                for (int i = 0; i < dataUsageArrayList.size(); i++) {
                String packageName = packag;
                int dataMb = data;
                if (getForegroundProcess(getApplicationContext()).contains(packageName)) {
                    final PackageManager pm = getPackageManager();
                    //get a list of installed apps.
                    List<ApplicationInfo> packages = pm.getInstalledApplications(
                            PackageManager.GET_META_DATA);
                    int UID = 0;
                    //loop through the list of installed packages and see if the selected
                    //app is in the list
                    for (ApplicationInfo packageInfo : packages) {
                        if (packageInfo.packageName.equals(packageName)) {
                            //get the UID for the selected app
                            UID = packageInfo.uid;
                            break; //found a match, don't need to search anymore
                        }

                    }
                    long data = TrafficStats.getUidRxBytes(UID) + TrafficStats.getUidTxBytes(UID);
                    data=data+mDb.readDataUsage(packageName);

                    float datainMB = ((float) Math.round((data / (1024 * 1024)) * 10) / 10);
                    Log.e("data", "" + datainMB);

                    if (datainMB > dataMb) {
                        Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
                        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(lockIntent);
                    }
                }
            }
            if (getForegroundProcess(getApplicationContext()).contains("Browser")) {
                if (isNetworkAvailable(getApplicationContext())) {
                    blokURL();
                }
            }
            if (getForegroundProcess(getApplicationContext()).contains("Chrome")) {
                if (isNetworkAvailable(getApplicationContext())) {
                    blokURL1();
                }
            }
//                if (timeArrayList.size() != 0) {
//                    for (int i = 0; i < timeArrayList.size(); i++) {
//                        String currentTimt = getCurrentTime();
//                        String startTime = timeArrayList.get(i).getStart_time();
//                        String endTime = timeArrayList.get(i).getEnd_time();
//                        if (checkTime(startTime, endTime, currentTimt)) {
//                            Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
//                            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(lockIntent);
//                        }
//                    }
//                }
        } else {
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
                            blokURL1();
                        }
                    }

//                    ArrayList<MasterTime> timeArrayList = mDb.readTimingOfPackage(process.processName);
//                    if (timeArrayList.size() != 0) {
//                        for (int i = 0; i < timeArrayList.size(); i++) {
//                            String currentTimt = getCurrentTime();
//                            String startTime = timeArrayList.get(i).getStart_time();
//                            String endTime = timeArrayList.get(i).getEnd_time();
//                            if (checkTime(startTime, endTime, currentTimt)) {
//                                Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
//                                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(lockIntent);
//                            }
//                        }
//                    }
//                if(process.processName.contains("com.android.browser"))
//                {
//                    Intent lockIntent = new Intent(AppControlService.this, LockScreenActivity.class);
//                            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(lockIntent);
//                }
                }

            }
        }
    }

    private void blokURL1() {
        ArrayList<BlockUrl> blockUrls = mDb.readBlockedUrl();
        if (blockUrls.size() != 0) {
            for (int i = 0; i < blockUrls.size(); i++) {
                String url = blockUrls.get(i).getUrl();
                String chromeUrl = getChromeLastURL();
                String lasturl = chromeUrl;
                Log.e("u", url);
                Log.e("url", chromeUrl);
                boolean b = chromeUrl.contains(url);

                if (b == true) {
                    int c = counter;
                    counter++;
                    int id = counter++;
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://www.google.com/"));
                    viewIntent.setPackage("com.android.chrome");
                    viewIntent.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome");
                    viewIntent.setFlags(Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(viewIntent);

                }
            }
        }
    }


}

