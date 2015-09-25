package com.example.root.myservice.modelClass;

/**
 * Created by root on 3/6/15.
 */
public class MasterPackage {
    private long package_ID;
    private String package_name;
    private String data_usage;
    public long getPackage_ID()
    {
        return package_ID;
    }
    public void setPackage_ID(long package_ID) {
        this.package_ID=package_ID;
    }
    public  String getPackage_name()
    {
        return package_name;
    }
    public void setPackage_name(String package_name)
    {
        this.package_name=package_name;
    }
    public String getData_usage()
    {
        return data_usage;
    }
    public void setData_usage(String  data_usage)
    {
        this.data_usage=data_usage;
    }
}
