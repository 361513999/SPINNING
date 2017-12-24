package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by cloor on 2017/8/9.
 */

public class HomeListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int width = 0;
    private int height = 0;
    public HomeListAdapter(Context context, int width, int height){
        this.context = context;
        this.width = width;
        this.height = height;
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

        LinearLayout child;
        TextView item0;

        CircleImageView item1;//116  70
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_home_list, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.item0 = convertView.findViewById(R.id.item0);

            viewHolder.item1 = convertView.findViewById(R.id.item1);
            viewHolder.child = convertView.findViewById(R.id.child);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        viewHolder.child.setLayoutParams(layoutParams);
        double fix = 70.0/116.0;
        int cWidth = (int) (height*fix);
        RelativeLayout.LayoutParams item1Params = new RelativeLayout.LayoutParams(cWidth,cWidth);
        item1Params.addRule(RelativeLayout.RIGHT_OF,viewHolder.item0.getId());
        item1Params.addRule(RelativeLayout.CENTER_VERTICAL);
        item1Params.setMargins(5,0,5,0);
        viewHolder.item1.setLayoutParams(item1Params);
        ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.ic_launcher,viewHolder.item1);
        return  convertView;
    }
}
