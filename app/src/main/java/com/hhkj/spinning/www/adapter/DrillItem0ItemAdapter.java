package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.widget.InScrollListView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cloor on 2017/8/9.
 */

public class DrillItem0ItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Map<String,String>> maps;
    private Handler handler;
    public DrillItem0ItemAdapter(Context context,ArrayList<Map<String,String>> maps,Handler handler){
        this.context = context;
        this.maps = maps;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return maps.size();
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
            TextView item0;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_drill_item0_item, null);
            viewHolder.item0 = (TextView) convertView.findViewById(R.id.item0);

            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        final Map<String,String> map = maps.get(position);
        viewHolder.item0.setText(map.get("title"));
        viewHolder.item0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = map;
                handler.sendMessage(msg);
            }
        });
        return  convertView;
    }
}
