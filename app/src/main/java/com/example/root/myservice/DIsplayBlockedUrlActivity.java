package com.example.root.myservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.root.myservice.adapter.BlockedUrlAdapter;
import com.example.root.myservice.databse.MydatabaseHelper;
import com.example.root.myservice.listner.TimeListner;
import com.example.root.myservice.modelClass.BlockUrl;

import java.util.ArrayList;


public class DIsplayBlockedUrlActivity extends Activity implements TimeListner {
    ListView listView;
    int deletePosition;
    ArrayList<BlockUrl> blockedurlList = new ArrayList<>();
    BlockedUrlAdapter urlAdapter;
    MydatabaseHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb = new MydatabaseHelper(this);
        mDb.open();
        listView = (ListView) findViewById(R.id.listview1);
        registerForContextMenu(listView);
        populateList();
    }

    private void populateList() {
        blockedurlList.clear();
        blockedurlList = mDb.readBlockedUrl();
        urlAdapter = new BlockedUrlAdapter(this, blockedurlList);
        listView.setAdapter(urlAdapter);
        urlAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_blocked_url, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent intent = new Intent(DIsplayBlockedUrlActivity.this, BlockUrlActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int rowId = Integer.parseInt(blockedurlList.get(deletePosition).getUrlid());
        mDb.deleteUrl(rowId);
        populateList();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onLongClick(int postion) {
        deletePosition = postion;
    }
}
