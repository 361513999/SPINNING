package com.hhkj.spinning.www.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hhkj.spinning.www.bean.DrillItem1Left;

import java.util.ArrayList;

/**
 * Created by cloor on 2017/12/30.
 */

public class DrillItem1LeftAdapter extends PagerAdapter {
    private ArrayList<View> lefts;
    public DrillItem1LeftAdapter(ArrayList<View> lefts){
            this.lefts = lefts;
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
        container.addView(lefts.get(position));
        return lefts.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
