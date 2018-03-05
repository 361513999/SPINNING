package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.SportMenu;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.utils.ClientManager;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;


/**
 * Created by cloor on 2017/8/9.
 */

public class BtListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<SearchResult> sportMenus;
    private SharedUtils sharedUtils;
    private Handler handler;
    public BtListAdapter(Context context, ArrayList<SearchResult> sportMenus,Handler handler){
        this.context = context;
        this.sportMenus =sportMenus;
        inflater = LayoutInflater.from(context);
        this.handler = handler;
        sharedUtils = new SharedUtils(Common.config);
    }
    public void updata(ArrayList<SearchResult> sportMenus){
        this.sportMenus = sportMenus;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return sportMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return sportMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView name,mac,status;

    }
    private int index = -1;
    public void selected(int position){
        this.index = position;
        notifyDataSetChanged();
    }
    public int getSelectId(){
        return  index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_bt_list, null);
            viewHolder.name =  convertView.findViewById(R.id.name);
            viewHolder.mac = convertView.findViewById(R.id.mac);
            viewHolder.status = convertView.findViewById(R.id.status);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        SearchResult it = sportMenus.get(position);
        if(sharedUtils.getStringValue("bt_mac").equals(it.getAddress())){

            if( ClientManager.getClient().getConnectStatus(it.getAddress())==2){
                //已连接
                viewHolder.status.setVisibility(View.VISIBLE);
                viewHolder.status.setText("断开");
            }else{
                //已断开
              //  viewHolder.status.setText("连接");
            }
        }else {
            viewHolder.status.setVisibility(View.GONE);
        }
        viewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolder.status.getText().toString().equals("断开")){
                    handler.sendEmptyMessage(0);
                }
            }
        });
        viewHolder.name.setText(it.getName());
        viewHolder.mac.setText(it.getAddress());
        return  convertView;
    }
}
