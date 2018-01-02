package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.DrillItem1LeftAdapter;
import com.hhkj.spinning.www.adapter.DrillItem1RightAdapter;
import com.hhkj.spinning.www.adapter.SportListAdapter;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;

import java.util.ArrayList;


/**
 * Created by cloor on 2017/12/30.
 */

@SuppressLint("ValidFragment")
public class DrillItem1 extends BaseFragment {
    private DrillItem1RightAdapter drillItem1RightAdapter ;
    private Activity activity;
    private Handler handler;
    private ViewPager item0;
    private GridView drill_list;
    private PullToRefreshView pull_to_refresh_list;
    private DrillItem1LeftAdapter drillItem1LeftAdapter;
    private ArrayList<View> lefts = new ArrayList<>();
    public DrillItem1(Activity activity, Handler handler){
        this.activity = activity;
        this.handler = handler;
    }
    private final int runTime = 400;
    private PullToRefreshView.OnHeaderRefreshListener listHeadListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onHeaderRefreshComplete();


                }
            }, runTime);
        }
    };
    private boolean isMore = true;
    private PullToRefreshView.OnFooterRefreshListener listFootListener = new PullToRefreshView.OnFooterRefreshListener() {
        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onFooterRefreshComplete();
                    if (isMore) {

                    } else {
                        NewToast.makeText(activity, "没有数据可加载", Common.TTIME).show();

                    }

                }
            }, runTime);
        }
    };
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drillItem1LeftAdapter = new DrillItem1LeftAdapter(lefts);
        item0 = view.findViewById(R.id.item0);
        item0.setAdapter(drillItem1LeftAdapter);
        item0.post(new Runnable() {
            @Override
            public void run() {
                addView();
            }
        });
        //276   80
        drill_list = view.findViewById(R.id.drill_list);
        pull_to_refresh_list = view.findViewById(R.id.pull_to_refresh_list);
        pull_to_refresh_list.setOnHeaderRefreshListener(listHeadListener);
        pull_to_refresh_list.setOnFooterRefreshListener(listFootListener);
        drill_list.post(new Runnable() {
            @Override
            public void run() {
                int div  = FileUtils.dip2px(activity,4 );
                int width = (drill_list.getMeasuredWidth()-div)/2;
                double height = (80.0/136.0)*width;
                P.c(width+"==="+height);
                drillItem1RightAdapter = new DrillItem1RightAdapter(activity,width, (int) height);
                drill_list.setAdapter(drillItem1RightAdapter);
            }
        });


    }
    private void addView(){
        lefts.clear();
        for(int i=0;i<10;i++){
            View  view = getLayoutInflater().inflate(R.layout.item_drill_item1_left,null);
            lefts.add(view);
        }
        drillItem1LeftAdapter.update(lefts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drill_item1, container, false);
        return view;

    }
}
