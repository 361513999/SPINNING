package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.widget.InScrollListView;

/**
 * Created by cloor on 2017/8/9.
 */

public class DrillItem0ItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    public DrillItem0ItemAdapter(Context context){
        this.context = context;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_drill_item0_item, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }



        return  convertView;
    }
}
