package com.example.root.myservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.myservice.R;
import com.example.root.myservice.ShowTimeActivity;
import com.example.root.myservice.modelClass.MasterTime;

import java.util.ArrayList;

/**
 * Created by root on 6/9/15.
 */
public class TimeListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    private ArrayList<MasterTime> timeArrayList;

    public TimeListAdapter(Context context, ArrayList<MasterTime> timeArrayList) {
        this.context = context;
        this.timeArrayList = timeArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return timeArrayList.size();
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
            convertView = inflater.inflate(R.layout.list_time, null);
            holder = new ViewHolder();

            holder.startTime = (TextView) convertView
                    .findViewById(R.id.edtStartTime);
            holder.endTime = (TextView) convertView.findViewById(R.id.edtEndStime);


            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        MasterTime masterTime=timeArrayList.get(position);
        String startTime=masterTime.getStart_time();
        String[] sub = startTime.split(" ");
        String[] s = sub[0].split(":");
        String string;
        if(sub[1].equals(" pm"))
        {
            int hr= Integer.parseInt(s[0]);
            hr=hr-12;
            string=hr+":"+s[1]+" "+sub[1];
        }
        else
        {
            string=s[0]+":"+s[1]+" "+sub[1];
        }
        String endTime=masterTime.getEnd_time();
        String[] sub1 = endTime.split(" ");
        String[] s1 = sub1[0].split(":");
        String string1;
        if(sub1[1].equals("pm"))
        {
            int hr= Integer.parseInt(s1[0]);
            hr=hr-12;
            string1=hr+":"+s1[1]+" "+sub1[1];
        }
        else
        {
            string1=s1[0]+":"+s1[1]+" "+sub1[1];
        }
        holder.startTime.setText(string);
        holder.endTime.setText(string1);
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowTimeActivity showTimeActivity = (ShowTimeActivity) context;
               showTimeActivity.onLongClick(position);
                return false;
            }
        });

        return convertView;
    }


    private class ViewHolder {
        TextView startTime,endTime;
    }


}
