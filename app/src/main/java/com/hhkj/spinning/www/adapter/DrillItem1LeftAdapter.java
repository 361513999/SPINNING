package com.hhkj.spinning.www.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.DrillItem1Left;
import com.hhkj.spinning.www.bean.HomeOnlineList;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.NewToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/12/30.
 */

public class DrillItem1LeftAdapter extends PagerAdapter {
    private ArrayList<View> lefts;
    private ArrayList<HomeOnlineList> onlineLists;
    private Handler handler;
    public DrillItem1LeftAdapter(ArrayList<View> lefts,ArrayList<HomeOnlineList> onlineLists,Handler handler){
            this.lefts = lefts;
            this.onlineLists = onlineLists;
            this.handler = handler;
    }
    public void update(ArrayList<View> lefts){
            this.lefts = lefts;
            notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return lefts.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = lefts.get(position);
        final HomeOnlineList online = onlineLists.get(position);
        ImageView item_left0 = view.findViewById(R.id.item_left0);
        CircleImageView item_left1 = view.findViewById(R.id.item_left1);
        TextView item_left2 = view.findViewById(R.id.item_left2);
        TextView item_left3 = view.findViewById(R.id.item_left3);
        TextView item_left4 = view.findViewById(R.id.item_left4);
        TextView item_left6 = view.findViewById(R.id.item_left6);
        ImageLoader.getInstance().displayImage(FileUtils.addImage(online.getImage()), item_left0);
        ImageLoader.getInstance().displayImage(FileUtils.addImage(online.getIco()), item_left1);
        item_left2.setText(online.getName());
        item_left3.setText(online.getPlayTime() + " 分钟");
        item_left4.setText(online.getTitle());
        if(online.getStatus()==0){
            item_left6.setText("未开始");
        }else if(online.getStatus()==1){
            item_left6.setText("正在直播");

        }else if(online.getStatus()==2||online.getStatus()==3||online.getStatus()==4){
            item_left6.setText("已结束");
        }

        container.addView(view);
        item_left6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 4;
                msg.obj = online;
                handler.sendMessage(msg);
            }
        });
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
