package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhkj.spinning.www.R;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class SportMenuAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> rbs = new ArrayList<>();
    public SportMenuAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        init();
    }
    private void init(){
        rbs.add("骑行方法");
        rbs.add("注意事项");
        rbs.add("饮食搭配");
        rbs.add("特别推荐");
    }
    @Override
    public int getCount() {
        return rbs.size();
    }

    @Override
    public Object getItem(int position) {
        return rbs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView item0;
        View item1;
    }
    private int index = -1;
    public void selected(int position){
        this.index = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sport_menu, null);
            viewHolder.item0 =  convertView.findViewById(R.id.item0);
            viewHolder.item1 = convertView.findViewById(R.id.item1);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        String it = rbs.get(position);
        viewHolder.item0.setText(it);
        if(index==position){
            viewHolder.item0.setTextColor(Color.WHITE);
            viewHolder.item1.setBackgroundResource(R.drawable.shape_sport_item_pressed);
        }else{
            viewHolder.item0.setTextColor(Color.parseColor("#888888"));
            viewHolder.item1.setBackgroundResource(R.drawable.shape_sport_item_normal);
        }
        return  convertView;
    }
}
