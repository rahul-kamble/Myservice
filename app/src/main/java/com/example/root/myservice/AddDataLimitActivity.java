package com.example.root.myservice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.modelClass.DataUsage;

import java.util.ArrayList;
import java.util.List;

public class AddDataLimitActivity extends Activity {

    EditText datausage;
    Button btnSubmitdata;
    private MydatabaseHelper mDb;
    private String packageName;
    private SharedPreferences sharedPreferences;
    private Spinner spinner;
    private String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_limit);
        sharedPreferences = getSharedPreferences("pckName", MODE_PRIVATE);
        packageName = sharedPreferences.getString("packageName", null);
        Log.e("p", "" + packageName);
        getActionBar().setTitle(packageName);
        mDb = new MydatabaseHelper(this);
        mDb.open();
        btnSubmitdata = (Button) findViewById(R.id.btnDataLimit);
        datausage = (EditText) findViewById(R.id.edtdatalimit);
        List<String> list = new ArrayList<String>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo packageInfo : packages) {
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        spinner = (Spinner) findViewById(R.id.planets_spinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appName = spinner.getSelectedItem().toString();
                Toast.makeText(AddDataLimitActivity.this, "" + appName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSubmitdata.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                int data = Integer.parseInt(datausage.getText().toString());

                if (data != 0 && appName != null) {
                    DataUsage dataUsage = new DataUsage();
                    dataUsage.setPackageName(appName);
                    dataUsage.setDatalimit(data);
                    long id = mDb.addDataLimit(dataUsage);
                    Log.e("d", "" + id);
                } else {
                    Toast.makeText(AddDataLimitActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
