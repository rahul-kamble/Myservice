package com.example.root.myservice.modelClass;

import android.database.Cursor;

/**
 * Created by root on 3/6/15.
 */
public class MasterTime {
    private String packege_name;
    private String start_time;
    private String end_time;
    private String time_Id;

    public String getPackege_name() {
        return packege_name;
    }

    public void setPackege_name(String packege_name) {
        this.packege_name = packege_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTime_Id() {
        return time_Id;
    }

    public void setTime_Id(String time_Id) {
        this.time_Id = time_Id;
    }
}
