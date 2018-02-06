package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.InScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    private int index;
    private int playIndex;
    private ImageView play_ico;
    public DrillItem0ItemAdapter(Context context,ArrayList<Map<String,String>> maps,Handler handler,int index,int playIndex,ImageView play_ico){
        this.context = context;
        this.maps = maps;
        this.handler = handler;
        this.index = index;
        this.playIndex = playIndex;
        this.play_ico = play_ico;
        inflater = LayoutInflater.from(context);

    }
    public void play(int playIndex){
        this.playIndex = playIndex;
        notifyDataSetChanged();
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
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



        if(playIndex==position){
            viewHolder.item0.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            viewHolder.item0.setTextColor(Color.parseColor("#888888"));
        }
        final Map<String,String> map = maps.get(position);
        viewHolder.item0.setText(map.get("title"));
        viewHolder.item0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.item0.setTextColor(context.getResources().getColor(R.color.white));
                ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.music_pause,play_ico);
                Message msg = new Message();
                msg.what = 2;
                msg.arg1 = index;
                msg.arg2 = position;
                handler.sendMessage(msg);
            }
        });
        return  convertView;
    }
}
