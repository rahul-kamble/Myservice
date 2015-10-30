package com.example.root.myservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.myservice.R;
import com.example.root.myservice.ShowDataUsageActivity;
import com.example.root.myservice.modelClass.DataUsage;

import java.util.ArrayList;

/**
 * Created by root on 17/10/15.
 */
public class DataUsageAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    ArrayList<DataUsage> dataUsages;
    private Context context;

    public DataUsageAdapter(Context context, ArrayList<DataUsage> dataUsages) {
        this.context = context;
        this.dataUsages = dataUsages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataUsages.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_datausage, null);
            holder = new ViewHolder();

            holder.packagename = (TextView) convertView
                    .findViewById(R.id.pAckage);
            holder.dataLimit = (TextView) convertView.findViewById(R.id.dataLimit);


            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        DataUsage dataUsage = dataUsages.get(position);
        String pacakage = dataUsage.getPackageName();
        int dataLimit = dataUsage.getDatalimit();

        holder.packagename.setText(pacakage);
        holder.dataLimit.setText(dataLimit+" MB" );

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowDataUsageActivity showDataUsageActivity = (ShowDataUsageActivity) context;
                showDataUsageActivity.onLongClick(position);
                return false;
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView packagename, dataLimit;
    }
}
