package com.example.root.myservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.myservice.DIsplayBlockedUrlActivity;
import com.example.root.myservice.R;
import com.example.root.myservice.modelClass.BlockUrl;

import java.util.ArrayList;

/**
 * Created by root on 12/9/15.
 */

public class BlockedUrlAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<BlockUrl> blockedurlList;

    public BlockedUrlAdapter(Context context, ArrayList<BlockUrl> blockedurlList) {
        this.context = context;
        this.blockedurlList = blockedurlList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return blockedurlList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_url, null);
            holder = new ViewHolder();
            holder.urlName = (TextView) convertView.findViewById(R.id.txtBlockedUrl);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        BlockUrl  blockUrl = blockedurlList.get(position);
        holder.urlName.setText(blockUrl.getUrl());
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DIsplayBlockedUrlActivity blockedUrlActivity = (DIsplayBlockedUrlActivity) context;
                blockedUrlActivity.onLongClick(position);
                return false;
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView urlName;
    }
}
