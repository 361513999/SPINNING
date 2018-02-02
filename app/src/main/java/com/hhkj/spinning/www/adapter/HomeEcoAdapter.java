package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.VideoBean;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class HomeEcoAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int width = 0;
    private int height = 0;
    private ArrayList<VideoBean> videoBeans;
    private Handler handler;
    public HomeEcoAdapter(Context context,int width,int height,ArrayList<VideoBean> videoBeans,Handler handler){
        this.context = context;
        this.width = width;
        this.height = height;
        this.videoBeans = videoBeans;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }
    public void updata(ArrayList<VideoBean> videoBeans){
        this.videoBeans = videoBeans;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return videoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return videoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView item1,item2;
        LinearLayout child;
        ImageView item0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_home_eco, null);
            viewHolder.item1 = (TextView) convertView.findViewById(R.id.item1);
            viewHolder.item2 = (TextView) convertView.findViewById(R.id.item2);
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
        final VideoBean bean = videoBeans.get(position);
        ImageLoader.getInstance().displayImage(FileUtils.addImage(bean.getImage()),viewHolder.item0);
        viewHolder.item1.setText(bean.getTitle());
        viewHolder.item2.setText("时长: "+bean.getTime());
        viewHolder.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return  convertView;
    }
}
