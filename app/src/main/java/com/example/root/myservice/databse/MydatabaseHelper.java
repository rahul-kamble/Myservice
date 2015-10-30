package com.example.root.myservice.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.root.myservice.modelClass.BlockUrl;
import com.example.root.myservice.modelClass.DataUsage;
import com.example.root.myservice.modelClass.MasterTime;

import java.util.ArrayList;

/**
 * Created by root on 2/6/15.
 */
public class MydatabaseHelper {


    private static final String DATABASE_NAME = "Parental_Control";
    private static final int VERSION_NAME = 1;

    //common fields
    private static final String KEY_UID = "_id";
    private static final String KEY_PACKAGE_NAME = "package_name";

    private static final String MASTER_TABLE = "Master_Package";
    private static final String CREATE_MASTER_TABLE = "CREATE TABLE " + MASTER_TABLE + "(" + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PACKAGE_NAME + " VARCHAR(255)  );";

    private static final String TIMING_TABLE = "Master_Time";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String CREATE_TIME_TABLE = "CREATE TABLE " + TIMING_TABLE + "(" + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PACKAGE_NAME + " VARCHAR(255)," + KEY_START_TIME + " VARCHAR(255)," + KEY_END_TIME + " VARCHAR(255) );";

    private static final String BLOCKURL_TABLE = "Master_BlockUrl";
    private static final String KEY_URL = "url";
    private static final String CREATE_BLOCKURL_TABL = "CREATE TABLE " + BLOCKURL_TABLE + "(" + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_URL + " VARCHAR(255) );";

    private static final String DATAUSAGELIMIT_TABLE = "Data_Usage";
    private static final String KEY_DATAUSAGE = "datausage";
    private static final String CREATE_DATAUSAGELIMIT_TABLE = "CREATE TABLE " + DATAUSAGELIMIT_TABLE + "(" + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PACKAGE_NAME + " VARCHAR(255)," + KEY_DATAUSAGE + " VARCHAR(255) );";

    private static final String DATAUSAGE_TABLE = "DataUsage";
    private static final String CREATE_DATAUSAGE_TABLE = "CREATE TABLE " + DATAUSAGE_TABLE + "(" + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PACKAGE_NAME + " VARCHAR(255)," + KEY_DATAUSAGE + " VARCHAR(255) );";


    public SQLiteDatabase mDb;
    private Masterdatabase mDbHelper;
    private Context context;

    public MydatabaseHelper(Context context) {
        this.context = context;
    }

    public MydatabaseHelper open() {
        mDbHelper = new Masterdatabase(context);

        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public ArrayList<BlockUrl> readBlockedUrl() {
        ArrayList<BlockUrl> blockedurlList = new ArrayList<>();
        String readurls = "";
        readurls += "SELECT * FROM " + BLOCKURL_TABLE;
        Cursor cursor;
        open();
        cursor = mDb.rawQuery(readurls, null);
        if (cursor.moveToFirst()) {
            do {
                BlockUrl blockUrl = new BlockUrl();
                blockUrl.setUrl(cursor.getString(cursor.getColumnIndex(KEY_URL)));
                blockUrl.setUrlid(cursor.getString(cursor.getColumnIndex(KEY_UID)));
                blockedurlList.add(blockUrl);
            } while (cursor.moveToNext());
            cursor.close();
            close();
        }
        return blockedurlList;

    }

    public ArrayList<DataUsage> readDataUsage() {
        ArrayList<DataUsage> dataUsageArrayList = new ArrayList<>();
        String readDataUsage = "";
        readDataUsage += "SELECT * FROM " + DATAUSAGELIMIT_TABLE;
        Cursor cursor;
        open();
        cursor = mDb.rawQuery(readDataUsage, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DataUsage dataUsage = new DataUsage();
                    dataUsage.setPackageName(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME)));
                    dataUsage.setDatalimit(cursor.getInt(cursor.getColumnIndex(KEY_DATAUSAGE)));
                    dataUsage.setRowId(cursor.getInt(cursor.getColumnIndex(KEY_UID)));
                    dataUsageArrayList.add(dataUsage);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return dataUsageArrayList;
    }


    public ArrayList<MasterTime> readTimingOfPackage(String packagename)

    {
        ArrayList<MasterTime> timeArrayList = new ArrayList<>();

        String read = "SELECT * FROM Master_Time WHERE package_name= '" + packagename + "'";
        open();
        Cursor cursor = mDb.rawQuery(read, null);

        if (cursor.moveToFirst())
            do {

                String startTime = cursor.getString(cursor.getColumnIndex(KEY_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));
                String packageName = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME));
                String timeuid = cursor.getString(cursor.getColumnIndex(KEY_UID));
                MasterTime masterTime = new MasterTime();
                masterTime.setStart_time(startTime);
                masterTime.setEnd_time(endTime);
                masterTime.setPackege_name(packageName);
                masterTime.setTime_Id(timeuid);
                timeArrayList.add(masterTime);

            }
            while (cursor.moveToNext());
        cursor.close();
        close();

        return timeArrayList;

    }

    public int readDataUsage(String packageName)
    {
        int data = 0;
        String read = "SELECT datausage FROM "+ DATAUSAGE_TABLE +" WHERE package_name= '" + packageName + "'";
        open();
        Cursor cursor = mDb.rawQuery(read, null);
        if (cursor.moveToFirst()) {

            data = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DATAUSAGE)));
        }
        cursor.close();
//        close();

        return data;

    }

    public ArrayList<MasterTime> readTiming()

    {
        ArrayList<MasterTime> timeArrayList = new ArrayList<>();

        String read = "SELECT * FROM Master_Time";
        open();
        Cursor cursor = mDb.rawQuery(read, null);

        if (cursor.moveToFirst())
            do {

                String startTime = cursor.getString(cursor.getColumnIndex(KEY_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));
                String packageName = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME));
                String timeuid = cursor.getString(cursor.getColumnIndex(KEY_UID));
                MasterTime masterTime = new MasterTime();
                masterTime.setStart_time(startTime);
                masterTime.setEnd_time(endTime);
                masterTime.setPackege_name(packageName);
                masterTime.setTime_Id(timeuid);
                timeArrayList.add(masterTime);

            }
            while (cursor.moveToNext());
        cursor.close();
        close();

        return timeArrayList;

    }

    public void deleteAllData() {
        open();
        mDb.execSQL("delete from " + DATAUSAGE_TABLE);
        close();
    }

    public boolean deleteUrl(int rowId) {
        boolean deleteSuccessfull;
        open();
        deleteSuccessfull = mDb.delete(BLOCKURL_TABLE, KEY_UID + "=" + rowId, null) > 0;
        close();
        return deleteSuccessfull;
    }

    public boolean deleteTime(int rowId) {
        boolean deleteSuccessfull;
        open();
        deleteSuccessfull = mDb.delete(TIMING_TABLE, KEY_UID + "=" + rowId, null) > 0;
        close();
        return deleteSuccessfull;
    }

    public boolean deleteDataUsage(int rowId) {
        boolean deleteSuccessfull;
        open();
        deleteSuccessfull = mDb.delete(DATAUSAGELIMIT_TABLE, KEY_UID + "=" + rowId, null) > 0;
        close();
        return deleteSuccessfull;
    }

    public long addTime(MasterTime masterTime) {
        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_NAME, masterTime.getPackege_name());
        values.put(KEY_START_TIME, masterTime.getStart_time());
        values.put(KEY_END_TIME, masterTime.getEnd_time());
        open();
        return mDb.insert(TIMING_TABLE, null, values);

    }

    public long addBlockedUrl(String url) {
        ContentValues values = new ContentValues();
        values.put(KEY_URL, url);
        return mDb.insert(BLOCKURL_TABLE, null, values);
    }


    public long addDataLimit(DataUsage dataUsage) {
        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_NAME, dataUsage.getPackageName());
        values.put(KEY_DATAUSAGE, dataUsage.getDatalimit());
        return mDb.insert(DATAUSAGELIMIT_TABLE, null, values);
    }


    public void addAppDataUsage(ArrayList<DataUsage> dataUsageArrayList) {

        for (int i = 0; i < dataUsageArrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_PACKAGE_NAME, dataUsageArrayList.get(i).getPackageName());
            values.put(KEY_DATAUSAGE, dataUsageArrayList.get(i).getDatalimit());
            Log.e("adddata", "adddata");
            mDb.insert(DATAUSAGE_TABLE, null, values);
        }

    }

    private class Masterdatabase extends SQLiteOpenHelper {
        public Masterdatabase(Context context) {
            super(context, DATABASE_NAME, null, VERSION_NAME);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(CREATE_MASTER_TABLE);
            sqLiteDatabase.execSQL(CREATE_TIME_TABLE);
            sqLiteDatabase.execSQL(CREATE_BLOCKURL_TABL);
            sqLiteDatabase.execSQL(CREATE_DATAUSAGELIMIT_TABLE);
            sqLiteDatabase.execSQL(CREATE_DATAUSAGE_TABLE);
            Log.e("database", "created");


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_MASTER_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_TIME_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_BLOCKURL_TABL);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_DATAUSAGELIMIT_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_DATAUSAGE_TABLE);
            onCreate(sqLiteDatabase);

        }
    }
}
