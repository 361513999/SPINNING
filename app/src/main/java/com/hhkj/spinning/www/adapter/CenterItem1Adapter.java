package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.CenterItem1Edit;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/8/9.
 */

public class CenterItem1Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
   private ArrayList<CenterItem1Edit> centerItem1Edits;
    public CenterItem1Adapter(Context context,ArrayList<CenterItem1Edit> centerItem1Edits){
        this.context = context;
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
        TextView txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_center1_list, null);

            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }

        return  convertView;
    }
}
