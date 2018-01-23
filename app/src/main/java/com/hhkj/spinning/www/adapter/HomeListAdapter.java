package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.HomeOnlineList;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class HomeListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int width = 0;
    private int height = 0;
    private ArrayList<HomeOnlineList> lists;
    private Handler handler;
    public HomeListAdapter(Context context, int width, int height,ArrayList<HomeOnlineList> lists,Handler handler){
        this.context = context;
        this.width = width;
        this.height = height;
        this.lists = lists;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }
    public void updata(ArrayList<HomeOnlineList> lists){
        this.lists  = lists;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {

        LinearLayout child;
        TextView item0,item4,item5,item2;

        CircleImageView item1;//116  70
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_home_list, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.item0 = convertView.findViewById(R.id.item0);
            viewHolder.item2 = convertView.findViewById(R.id.item2);
            viewHolder.item4 = convertView.findViewById(R.id.item4);
            viewHolder.item5 = convertView.findViewById(R.id.item5);
            viewHolder.item1 = convertView.findViewById(R.id.item1);

            viewHolder.child = convertView.findViewById(R.id.child);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        final HomeOnlineList obj = lists.get(position);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
      //  viewHolder.child.setPadding(0,4,0,4);
        viewHolder.child.setLayoutParams(layoutParams);
        double fix = 70.0/100.0;
        int cWidth = (int) (height*fix);
        RelativeLayout.LayoutParams item1Params = new RelativeLayout.LayoutParams(cWidth,cWidth);
        item1Params.addRule(RelativeLayout.RIGHT_OF,viewHolder.item0.getId());
        item1Params.addRule(RelativeLayout.CENTER_VERTICAL);
        item1Params.setMargins(5,0,5,0);
        viewHolder.item1.setLayoutParams(item1Params);
        ImageLoader.getInstance().displayImage( obj.getIco(),viewHolder.item1);
        viewHolder.item0.setText(obj.getStartTime());
        viewHolder.item2.setText(obj.getTitle());
        viewHolder.item4.setText(obj.getTime()+"Min");
        viewHolder.item5.setText(obj.getName());
        viewHolder.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = -3;
                msg.arg1 = position;
                msg.obj = obj;
                handler.sendMessage(msg);
            }
        });
        return  convertView;
    }
}
