package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.SportList;
import com.hhkj.spinning.www.common.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class SportListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int width ,height= 0;
    private ArrayList<SportList> sportLists;
    public SportListAdapter(Context context,int width ,int height, ArrayList<SportList> sportLists){
        this.context = context;
        this.width = width;
        this.height = height;
        this.sportLists = sportLists;
        inflater = LayoutInflater.from(context);

    }
    public void updata( ArrayList<SportList> sportLists){
        this.sportLists = sportLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sportLists.size();
    }

    @Override
    public Object getItem(int position) {
        return sportLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        ImageView item_tag;
        LinearLayout item_content;
        TextView item0,item1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sport_list, null);
            viewHolder.item_tag = convertView.findViewById(R.id.item_tag);
            viewHolder.item_content = convertView.findViewById(R.id.item_content);
            viewHolder.item0 = convertView.findViewById(R.id.item0);
            viewHolder.item1 = convertView.findViewById(R.id.item1);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }

        SportList list = sportLists.get(position);
        ImageLoader.getInstance().displayImage(FileUtils.addImage(list.getImage()),  viewHolder.item_tag);
        viewHolder.item_content.setLayoutParams(new AbsListView.LayoutParams(width,height));
        viewHolder.item0.setText(list.getName());
        viewHolder.item1.setText(list.getTip());
        return  convertView;
    }
}
