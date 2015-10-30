package com.example.root.myservice.modelClass;

/**
 * Created by root on 17/10/15.
 */
public class DataUsage {
    private int datalimit;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    private int rowId;
    private String packageName;

    public int getDatalimit() {
        return datalimit;
    }

    public void setDatalimit(int datalimit) {
        this.datalimit = datalimit;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
