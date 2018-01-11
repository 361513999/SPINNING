package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.AudioBean;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.InScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class DrillItem0Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int height;
    private ArrayList<AudioBean> audioBeans;
    private Handler handler;
    public DrillItem0Adapter(Context context,int height,ArrayList<AudioBean> audioBeans, Handler handler){
        this.context = context;
        this.height = height;
        this.audioBeans = audioBeans;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }
    public void updata(ArrayList<AudioBean> audioBeans){
        this.audioBeans = audioBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return audioBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return audioBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {

        LinearLayout child,content0;
        TextView item0,item1;
        InScrollListView content;
        ImageView item2;
        Button slo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_drill_item0, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.child = convertView.findViewById(R.id.child);
            viewHolder.slo = convertView.findViewById(R.id.slo);
            viewHolder.content0 = convertView.findViewById(R.id.content0);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.item0 = convertView.findViewById(R.id.item0);
            viewHolder.item1 = convertView.findViewById(R.id.item1);
            viewHolder.item2 = convertView.findViewById(R.id.item2);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        AudioBean bean = audioBeans.get(position);
        viewHolder.item0.setText(bean.getTip());
        viewHolder.item1.setText(bean.getTitle());
        ImageLoader.getInstance().displayImage(FileUtils.addImage(bean.getImage()),viewHolder.item2);

        viewHolder.content0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
        DrillItem0ItemAdapter adapter = new DrillItem0ItemAdapter(context,bean.getMaps(),handler);
        viewHolder.content.setAdapter(adapter);
         viewHolder.slo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(viewHolder.content.getVisibility()==View.VISIBLE){
                viewHolder.content.setVisibility(View.GONE);
            }else{
                viewHolder.content.setVisibility(View.VISIBLE);
            }

        }
    });


        return  convertView;
    }
}
