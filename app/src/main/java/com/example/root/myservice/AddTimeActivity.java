package com.example.root.myservice;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.modelClass.MasterTime;

import java.util.Calendar;


public class AddTimeActivity extends Activity implements View.OnClickListener {

    String string_Start_am, string_Start_pm, string_StartPM;
    private Button submit;
    private EditText starttime, endtime;
    private MydatabaseHelper mDb;
    private TimePicker start, end;
    private String packageName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
        sharedPreferences = getSharedPreferences("pckName", MODE_PRIVATE);
        packageName = sharedPreferences.getString("packageName", null);
        Log.e("p", packageName);
        getActionBar().setTitle(packageName);
        mDb = new MydatabaseHelper(this);
        mDb.open();
        submit = (Button) findViewById(R.id.btnSubmit);
        starttime = (EditText) findViewById(R.id.edtStart);
        endtime = (EditText) findViewById(R.id.edtEnd);
        starttime.setOnClickListener(this);
        endtime.setOnClickListener(this);
        submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.edtStart:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int hr = selectedHour / 12;
                        int hr1 = selectedHour % 12;
                        if (hr == 0) {
                            string_Start_am = hr1 + ":" + selectedMinute + " am";
                            if (string_Start_am.equals("0:" + selectedMinute + " am")) {
                                string_Start_am = "12:" + selectedMinute + " am";
                                starttime.setText(string_Start_am);
                            } else {
                                starttime.setText(string_Start_am);
                            }
                        } else {
                            string_StartPM = hr1 + ":" + selectedMinute + " pm";
                            if (string_StartPM.equals("0:" + selectedMinute + " pm")) {
                                string_Start_pm = "12:" + selectedMinute + " pm";
                                starttime.setText(string_Start_pm);
                            } else {
                                starttime.setText(string_StartPM);
                            }
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;

            case R.id.edtEnd:
                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker1;
                mTimePicker = new TimePickerDialog(AddTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int hr = selectedHour / 12;
                        int hr1 = selectedHour % 12;
                        if (hr == 0) {
                            String endtime1 = hr1 + ":" + selectedMinute + " am";
                            if (endtime1.equals("0:" + selectedMinute + " am")) {
                                endtime.setText("12:" + selectedMinute + " am");
                            } else {
                                endtime.setText(endtime1);
                            }
                        } else {
                            String endtime1 = hr1 + ":" + selectedMinute + " pm";
                            if (endtime1.equals("0:" + selectedMinute + " pm")) {
                                endtime.setText("12:" + selectedMinute + " pm");
                            } else {
                                endtime.setText(endtime1);
                            }
                        }
                    }
                }, hour1, minute1, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.btnSubmit:
                String startTime = starttime.getText().toString();
                String string;
                String string1;
                String endTime = endtime.getText().toString();
                if (!startTime.equals("") && !endTime.equals("")) {
                    String[] sub = startTime.split(" ");
                    String[] s = sub[0].split(":");
                    if (sub[1].equals(" pm")) {
                        int hr = Integer.parseInt(s[0]);
                        hr = hr + 12;
                        string = hr + ":" + s[1] + " " + sub[1];
                    } else {
                        string = s[0] + ":" + s[1] + " " + sub[1];
                    }
                    String[] sub1 = endTime.split(" ");
                    String[] s1 = sub1[0].split(":");
                    if (sub1[1].equals("pm")) {
                        int hr = Integer.parseInt(s1[0]);
                        hr = hr + 12;
                        string1 = hr + ":" + s1[1] + " " + sub1[1];
                    } else {
                        string1 = s1[0] + ":" + s1[1] + " " + sub1[1];
                    }
                    MasterTime masterTime = new MasterTime();
                    masterTime.setPackege_name(packageName);
                    masterTime.setStart_time(string);
                    masterTime.setEnd_time(string1);
                    mDb.addTime(masterTime);
                    finish();
                } else {
                    Toast.makeText(AddTimeActivity.this, "please select time !", Toast.LENGTH_SHORT).show();
                }

                break;
        }


    }
}
