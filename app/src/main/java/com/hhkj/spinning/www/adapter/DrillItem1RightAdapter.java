package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by cloor on 2017/8/9.
 */

public class DrillItem1RightAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int width = 0;
    private int height = 0;
    private Handler handler;
    public DrillItem1RightAdapter(Context context, int width, int height,Handler handler){
        this.context = context;
        this.width = width;
        this.height = height;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return 20;
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
        TextView txt;
        LinearLayout child;
        ImageView item0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_drill_item1_right, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.item0 = convertView.findViewById(R.id.item0);
            viewHolder.child = convertView.findViewById(R.id.child);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(0,0,8,0);
        viewHolder.child.setLayoutParams(layoutParams);
        viewHolder.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(0);
            }
        });
        ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.ic_launcher,viewHolder.item0);

        return  convertView;
    }
}