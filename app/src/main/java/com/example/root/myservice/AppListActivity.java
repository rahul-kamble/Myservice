package com.example.root.myservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.myservice.adapter.Listadapter;
import com.example.root.myservice.service.ScreenOnOffService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppListActivity extends Activity {

    private static final ComponentName LAUNCHER_COMPONENT_NAME = new ComponentName(
            "com.example.root.myservice", "com.example.root.myservice.Launcher");
    SharedPreferences sharedPreferences;
    private ListView apps;
    private PackageManager packageManager;
    private String title;
    BroadcastReceiver mReceiver;

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent setIntent = new Intent(Intent.ACTION_MAIN);
////        setIntent.addCategory(Intent.CATEGORY_HOME);
////        setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        startActivity(setIntent);
//
//    }

    private boolean isLauncherIconVisible() {
        int enabledSetting = getPackageManager()
                .getComponentEnabledSetting(LAUNCHER_COMPONENT_NAME);
        return enabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

    private void hideLauncherIcon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Important!");
        builder.setMessage("To launch the app again, dial phone number 12345.");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getPackageManager().setComponentEnabledSetting(LAUNCHER_COMPONENT_NAME,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("pckName", MODE_PRIVATE);
        Log.e("oncreate", "onCreate");

        Intent intent=new Intent(AppListActivity.this, ScreenOnOffService.class);
        startService(intent);

        apps = (ListView) findViewById(R.id.listview1);
        apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                LinearLayout linearLayout = (LinearLayout) view;

                TextView packageName = (TextView) linearLayout.findViewById(R.id.txtPackageName);

                Intent intent = new Intent(AppListActivity.this, ShowTimeActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                PackageManager packageManager = getPackageManager();
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName.getText().toString(), 0);
                    title = (String) ((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                editor.putString("packageName", title);
                editor.putInt("position", position);
                editor.commit();
                startActivity(intent);

            }
        });
        packageManager = getPackageManager();
        final List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_META_DATA); // all apps in the phone

        final List<PackageInfo> packageList1 = packageManager
                .getInstalledPackages(0);
//        packageList1.

        try {
            packageList1.clear();
            for (int n = 0; n <    packageList.size(); n++) {

                PackageInfo PackInfo = packageList.get(n);
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(PackInfo.packageName, PackageManager.GET_PERMISSIONS);
                    if (info.requestedPermissions != null) {
                        for (String p : info.requestedPermissions) {
                            if (p.equals("android.permission.INTERNET")) {
                                packageList1.add(packageList.get(n)); // add in 2nd list if it is user installed app
                                Collections.sort(packageList1, new Comparator<PackageInfo>()
                                        // this will sort App list on the basis of app name
                                {
                                    public int compare(PackageInfo o1, PackageInfo o2) {
                                        return o1.applicationInfo.loadLabel(getPackageManager()).toString()
                                                .compareToIgnoreCase(o2.applicationInfo.loadLabel(getPackageManager())
                                                        .toString());// compare and return sorted packagelist.
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                }  if(permissionInfo.)
////                }
//                if (((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
////                //check weather it is system app or user installed app
////                {
//                    try {
//
//                        packageList1.add(packageList.get(n)); // add in 2nd list if it is user installed app
//                        Collections.sort(packageList1, new Comparator<PackageInfo>()
//                                // this will sort App list on the basis of app name
//                        {
//                            public int compare(PackageInfo o1, PackageInfo o2) {
//                                return o1.applicationInfo.loadLabel(getPackageManager()).toString()
//                                        .compareToIgnoreCase(o2.applicationInfo.loadLabel(getPackageManager())
//                                                .toString());// compare and return sorted packagelist.
//                            }
//                        });
//
//
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
                }
            }       }catch (Exception e)
        {

        }
        Listadapter Adapter = new Listadapter(this, packageList1, packageManager);
        apps.setAdapter(Adapter);
        ComponentName devAdminReceiver = new ComponentName(AppListActivity.this, SettingActivity.class);
        DevicePolicyManager dpm = (DevicePolicyManager) AppListActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.removeActiveAdmin(devAdminReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_url:
                Intent intent = new Intent(AppListActivity.this, ShowBlockedUrlActivity.class);
                startActivity(intent);
                break;
            case R.id.hide_app:
                hideLauncherIcon();
                break;
            case R.id.unHide_app:
                unHideLauncherIcon();
                break;
            case R.id.data_limit:
                Intent intent1 = new Intent(AppListActivity.this, AddDataLimitActivity.class);
                startActivity(intent1);
                break;
            case R.id.show_data_limit:
                Intent intent2 = new Intent(AppListActivity.this, ShowDataUsageActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void unHideLauncherIcon() {
        getPackageManager().setComponentEnabledSetting(LAUNCHER_COMPONENT_NAME, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
