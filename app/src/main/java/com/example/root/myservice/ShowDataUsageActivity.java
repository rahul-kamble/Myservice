package com.example.root.myservice;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.root.myservice.R;
import com.example.root.myservice.adapter.DataUsageAdapter;
import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.listner.TimeListner;
import com.example.root.myservice.modelClass.DataUsage;

import java.util.ArrayList;

public class ShowDataUsageActivity extends Activity implements TimeListner {

    private ListView apps;
    private PackageManager packageManager;
    private ArrayList<DataUsage> dataUsages = new ArrayList<>();
    private DataUsageAdapter adapter;
    private MydatabaseHelper mDb;
    private int deletePosition = -1;
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview1);
        registerForContextMenu(listView);
        mDb = new MydatabaseHelper(this);
        mDb.open();
        populateList();



    }

    private void populateList() {
        dataUsages.clear();
        dataUsages.addAll(mDb.readDataUsage());
        adapter = new DataUsageAdapter(this, dataUsages);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    public boolean onContextItemSelected(MenuItem item) {
//        Toast.makeText(ShowTimeActivity.this,"hi"+deletePosition,Toast.LENGTH_SHORT).show();
        int rowid = dataUsages.get(deletePosition).getRowId();
        mDb.deleteDataUsage(rowid);
        populateList();
        return super.onContextItemSelected(item);
    }


    @Override
    public void onLongClick(int postion) {
        deletePosition = postion;
    }
}
