package com.example.root.myservice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.root.myservice.adapter.TimeListAdapter;
import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.listner.TimeListner;
import com.example.root.myservice.modelClass.MasterTime;

import java.util.ArrayList;


public class ShowTimeActivity extends Activity implements TimeListner {
    int position;
    MydatabaseHelper mDb;
    ListView listView;
    ArrayList<MasterTime> timeArrayList = new ArrayList<>();
    TimeListAdapter timeListAdapter;
    SharedPreferences sharedPreferences;
    private int deletePosition;
    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("pckName", MODE_PRIVATE);
        packageName = sharedPreferences.getString("packageName", null);
        listView = (ListView) findViewById(R.id.listview1);
        getActionBar().setTitle(packageName);
        Log.e("title", "" + getActionBar().getTitle());
        position = sharedPreferences.getInt("position", -1);
        mDb = new MydatabaseHelper(this);
        mDb.open();
        registerForContextMenu(listView);
        populateList();
    }

    private void populateList() {
        timeArrayList.clear();
        timeArrayList.addAll(mDb.readTimingOfPackage(packageName));
        timeListAdapter = new TimeListAdapter(this, timeArrayList);
        listView.setAdapter(timeListAdapter);
        timeListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("delete");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_time, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        Toast.makeText(ShowTimeActivity.this,"hi"+deletePosition,Toast.LENGTH_SHORT).show();
        int rowid = Integer.parseInt(timeArrayList.get(deletePosition).getTime_Id());
        mDb.deleteTime(rowid);
        populateList();
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addtime) {
            Intent intent = new Intent(ShowTimeActivity.this, AddTimeActivity.class);
            intent.putExtra("packageName", packageName);
            startActivity(intent);
//            return true;
        }
        if(id==R.id.home)
        {
            Intent intent = new Intent(ShowTimeActivity.this, AppListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLongClick(int postion) {
        deletePosition = postion;

    }
}
