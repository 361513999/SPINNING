package com.hhkj.spinning.www.adapter;

import android.content.Context;
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

public class PersonCenterItem1_EditLeftAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> rbs = new ArrayList<>();
    public PersonCenterItem1_EditLeftAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        init();
    }
    private void init(){
        rbs.add("提醒时间");
        rbs.add("目标");
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
        TextView txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_person_left, null);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        String it = rbs.get(position);
        viewHolder.txt.setText(it);
        return  convertView;
    }
}
