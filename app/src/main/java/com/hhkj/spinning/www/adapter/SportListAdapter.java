package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class SportListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> rbs = new ArrayList<>();
    private int width ,height= 0;
    public SportListAdapter(Context context,int width ,int height){
        this.context = context;
        this.width = width;
        this.height = height;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return 10;
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
        ImageView item_tag;
        LinearLayout item_content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sport_list, null);
            viewHolder.item_tag = convertView.findViewById(R.id.item_tag);
            viewHolder.item_content = convertView.findViewById(R.id.item_content);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.ic_launcher,  viewHolder.item_tag);
        viewHolder.item_content.setLayoutParams(new AbsListView.LayoutParams(width,height));
        return  convertView;
    }
}
