package com.example.root.myservice.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.root.myservice.modelClass.BlockUrl;
import com.example.root.myservice.modelClass.MasterTime;

import java.util.ArrayList;

/**
 * Created by root on 2/6/15.
 */
public class MydatabaseHelper {


    private static final String DATABASE_NAME = "Parental_Control";
    private static final int VERSION_NAME =1;
    private static final String MASTER_TABLE = "Master_Package";
    private static final String KEY_UID = "_id";
    private static final String KEY_PNAME = "Package_Name";
    private static final String KEY_DATAUSAGE = "Data_Usage ";
    private static final String CREATE_MASTER_TABLE = "CREATE TABLE " + MASTER_TABLE + "(" + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PNAME + " VARCHAR(255)," + KEY_DATAUSAGE + " INTEGER );";

    private static final String TIMING_TABLE = "Master_Time";
    private static final String KEY_TIMEUID = "time_id";
    private static final String KEY_PACKAGE_NAME = "package_name";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String CREATE_TIME_TABLE = "CREATE TABLE " + TIMING_TABLE + "(" + KEY_TIMEUID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PACKAGE_NAME + " VARCHAR(255)," + KEY_START_TIME + " VARCHAR(255)," + KEY_END_TIME + " VARCHAR(255) );";

    private static final String BLOCKURL_TABLE="Master_BlockUrl";
    private static final String KEY_URLID="url_id";
    private static final String KEY_URL="url";
    private static final String CREATE_BLOCKURL_TABL="CREATE TABLE " + BLOCKURL_TABLE + "(" +KEY_URLID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_URL + " VARCHAR(255) );";

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
                BlockUrl blockUrl=new BlockUrl();
                blockUrl.setUrl(cursor.getString(cursor.getColumnIndex(KEY_URL)));
                blockUrl.setUrlid(cursor.getString(cursor.getColumnIndex(KEY_URLID)));
                blockedurlList.add(blockUrl);
            } while (cursor.moveToNext());
            cursor.close();
            close();
        }
        return blockedurlList;

    }
//    public boolean isPresent(String startTime,String endTime)
//    {
//        boolean present=false;
//        String query=""
//        mDb.rawQuery()
//    }

    public ArrayList<MasterTime> readTimingOfPackage(String packagename)

    {
        ArrayList<MasterTime> timeArrayList = new ArrayList<>();

        String read ="SELECT * FROM Master_Time WHERE package_name= '"+packagename+"'";
        open();
          Cursor cursor = mDb.rawQuery(read, null);

        if (cursor.moveToFirst())
        do {

            String startTime = cursor.getString(cursor.getColumnIndex(KEY_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));
            String packageName = cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME));
            String timeuid = cursor.getString(cursor.getColumnIndex(KEY_TIMEUID));
            MasterTime masterTime = new MasterTime();
            masterTime.setStart_time(startTime);
            masterTime.setEnd_time(endTime);
            masterTime.setPackege_name(packageName);
            masterTime.setTime_Id(timeuid);
            timeArrayList.add(masterTime);

        }
        while (cursor.moveToNext()) ;
        cursor.close();
        close();

        return timeArrayList;

    }

    public boolean deleteUrl(int rowId)
    {
        boolean deleteSuccessfull;
        open();
        deleteSuccessfull=mDb.delete(BLOCKURL_TABLE,KEY_URLID + "=" +rowId,null)>0;
        close();
        return deleteSuccessfull;
    }
    public boolean deleteTime(int rowId)
    {
        boolean deleteSuccessfull=false;
        open();
        deleteSuccessfull=mDb.delete(TIMING_TABLE,KEY_TIMEUID + "=" +rowId,null)>0;
        close();
        return deleteSuccessfull;
    }

    public long addTime(MasterTime masterTime) {
        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_NAME, masterTime.getPackege_name());
        values.put(KEY_START_TIME, masterTime.getStart_time());
        values.put(KEY_END_TIME, masterTime.getEnd_time());
        return mDb.insert(TIMING_TABLE, null, values);
    }

    public long addBlockedUrl(String url)
    {
        ContentValues values=new ContentValues();
        values.put(KEY_URL,url);
        return mDb.insert(BLOCKURL_TABLE,null,values);
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
            Log.e("database","created");


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_MASTER_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_TIME_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + CREATE_BLOCKURL_TABL);
            onCreate(sqLiteDatabase);

        }
    }
}
