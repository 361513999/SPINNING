package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.InScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by cloor on 2017/8/9.
 */

public class DrillItem0Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int height;
    public DrillItem0Adapter(Context context,int height){
        this.context = context;
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

        LinearLayout child,content0;
        TextView slo;
        InScrollListView content;
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
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }

        viewHolder.content0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
        DrillItem0ItemAdapter adapter = new DrillItem0ItemAdapter(context);
        viewHolder.content.setAdapter(adapter);
         viewHolder.slo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewHolder.content.setVisibility(View.VISIBLE);
        }
    });


        return  convertView;
    }
}
