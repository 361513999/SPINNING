package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.common.TimeUtil;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class CenterItem1Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Handler handler;
    private Context context;
   private ArrayList<CenterItem1Edit> centerItem1Edits;
    public CenterItem1Adapter(Context context,ArrayList<CenterItem1Edit> centerItem1Edits,Handler handler){
        this.context = context;
        this.handler  = handler;
        this.centerItem1Edits = centerItem1Edits;
        inflater = LayoutInflater.from(context);

    }
    public void updata(ArrayList<CenterItem1Edit> centerItem1Edits){
        this.centerItem1Edits = centerItem1Edits;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return centerItem1Edits.size();
    }

    @Override
    public Object getItem(int position) {
        return centerItem1Edits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView tog,time;
          ImageView edit,del;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_center1_list, null);
            viewHolder.tog = convertView.findViewById(R.id.tog);
            viewHolder.time = convertView.findViewById(R.id.time);
            viewHolder.del = convertView.findViewById(R.id.del);
            viewHolder.edit = convertView.findViewById(R.id.edit);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        final CenterItem1Edit edit = centerItem1Edits.get(position);
        viewHolder.tog.setText(edit.getTog());
        viewHolder.time.setText(TimeUtil.getTimeH(edit.getTime()));
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = 0;
                msg.obj = edit;
                handler.sendMessage(msg);

            }
        });
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = 1;
                msg.obj = edit;
                handler.sendMessage(msg);
            }
        });
        return  convertView;
    }
}
