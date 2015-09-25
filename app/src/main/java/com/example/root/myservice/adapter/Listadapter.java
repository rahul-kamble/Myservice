package com.example.root.myservice.adapter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.myservice.R;

import java.util.List;

/**
 * Created by root on 3/6/15.
 */
public class Listadapter extends BaseAdapter {

    List<PackageInfo> packageList;
    Activity context;
    PackageManager packageManager;


    public Listadapter(Activity context, List<PackageInfo> packageList,
                       PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;

    }

    public int getCount() {
        return packageList.size();
    }

    public Object getItem(int position) {
        return packageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_app, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView
                    .findViewById(R.id.txtPName);
            holder.packageName = (TextView) convertView.findViewById(R.id.txtPackageName);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        PackageInfo packageInfo = (PackageInfo) getItem(position);

        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);
        String appName = packageManager.getApplicationLabel(
                packageInfo.applicationInfo).toString();
        String packageName = packageInfo.applicationInfo.packageName;
        appIcon.setBounds(0, 0, 70, 70);
        holder.apkName.setCompoundDrawables(appIcon, null, null, null);
        holder.apkName.setCompoundDrawablePadding(20);
        holder.apkName.setText(appName);
//        holder.apkName.setText(packageName);
        holder.packageName.setText(packageName);
        return convertView;

    }

    private class ViewHolder {
        TextView apkName, packageName;

    }


}
