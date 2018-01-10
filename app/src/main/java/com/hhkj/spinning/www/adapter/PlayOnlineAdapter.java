package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.PlayOnlinePerson;
import com.hhkj.spinning.www.common.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * Created by cloor on 2017/8/9.
 */

public class PlayOnlineAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private int height = 0;
    private Handler handler;
    private ArrayList<PlayOnlinePerson> onlinePeople;
    public PlayOnlineAdapter(Context context,int height, Handler handler,ArrayList<PlayOnlinePerson> onlinePeople){
        this.context = context;
        this.onlinePeople = onlinePeople;
        this.height = height;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }
    public void updata(ArrayList<PlayOnlinePerson> onlinePeople){
        this.onlinePeople = onlinePeople;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return onlinePeople.size();
    }

    @Override
    public Object getItem(int position) {
        return onlinePeople.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView item0,item2,item3;
        LinearLayout child;
        ImageView item1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_play_online, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.item1 = convertView.findViewById(R.id.item1);
            viewHolder.item0 = convertView.findViewById(R.id.item0);
            viewHolder.item2 = convertView.findViewById(R.id.item2);
            viewHolder.item3 = convertView.findViewById(R.id.item3);
            viewHolder.child = convertView.findViewById(R.id.child);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
//        layoutParams.setMargins(0,0,8,0);

        double fix = 25.0/43.0;
        int cWidth = (int) (height*fix);
        RelativeLayout.LayoutParams item1Params = new RelativeLayout.LayoutParams(cWidth,cWidth);
        item1Params.addRule(RelativeLayout.RIGHT_OF,viewHolder.item0.getId());
        item1Params.addRule(RelativeLayout.CENTER_VERTICAL);
        item1Params.setMargins(5,0,5,0);
        viewHolder.item1.setLayoutParams(item1Params);
        viewHolder.child.setLayoutParams(layoutParams);
        viewHolder.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(0);
            }
        });

        PlayOnlinePerson person = onlinePeople.get(position);

        ImageLoader.getInstance().displayImage(FileUtils.addImage(person.getIco()),viewHolder.item1);
        viewHolder.item2.setText(person.getName());
        viewHolder.item0.setText(String.valueOf(position+1));
        viewHolder.item3.setText(person.getKcal()+" Kcal");
        return  convertView;
    }
}
