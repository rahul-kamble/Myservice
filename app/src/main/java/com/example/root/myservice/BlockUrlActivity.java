package com.example.root.myservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.myservice.databse.MydatabaseHelper;


public class BlockUrlActivity extends Activity implements View.OnClickListener {
    MydatabaseHelper mDb;
    private EditText geturl;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_url);
        geturl = (EditText) findViewById(R.id.edtUrl);
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);
        mDb = new MydatabaseHelper(this);
        mDb.open();
    }


    @Override
    public void onClick(View v) {
        String url = geturl.getText().toString();
        if (!url.equals("")) {
            mDb.open();
            mDb.addBlockedUrl(url);
            Toast.makeText(BlockUrlActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
            geturl.setText("");
            finish();
        } else {
            Toast.makeText(BlockUrlActivity.this, "Enter Url!", Toast.LENGTH_SHORT).show();

        }


    }
}
