package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.DrillItem0Adapter;
import com.hhkj.spinning.www.adapter.DrillItem1RightAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;
import com.hhkj.spinning.www.widget.xlist.XListView;

import java.lang.ref.WeakReference;


/**
 * Created by cloor on 2017/12/30.
 */

@SuppressLint("ValidFragment")
public class DrillItem0 extends BaseFragment {
        private Activity activity;
    private DrillItem0Adapter drillItem0Adapter ;
        private Handler handler;
        private SwipeRefreshLayout drill_item0_refr;
    private XListView xListView;

        public DrillItem0(Activity activity, Handler handler){
            this.activity = activity;
            this.handler = handler;
        }
private XListView.IXListViewListener ixListViewListener =new XListView.IXListViewListener(){

    @Override
    public void onRefresh() {
          P.c(" 刷新");
        xListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        P.c(" 更多");
        xListView.stopLoadMore();
    }
};
    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            drill_item0_refr.setRefreshing(true);

        }
    };
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        xListView = view.findViewById(R.id.grids);
        drill_item0_refr = view.findViewById(R.id.drill_item0_refr);
        drill_item0_refr.setOnRefreshListener(listener);

        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(false);
        xListView.setXListViewListener(ixListViewListener);
        //85/335
        xListView.post(new Runnable() {
            @Override
            public void run() {
                int height = (int) ((85/335.0)*(xListView.getMeasuredWidth()/2));
                drillItem0Adapter = new DrillItem0Adapter(activity,height);
                xListView.setAdapter(drillItem0Adapter);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drill_item0, container, false);
        drill_handler = new Drill_Handler(DrillItem0.this);
        return view;

    }
    private Drill_Handler drill_handler;
    private class Drill_Handler extends Handler {
        WeakReference<DrillItem0> mLeakActivityRef;
        public Drill_Handler(DrillItem0 leakActivity) {
            mLeakActivityRef = new WeakReference<DrillItem0>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){

               switch (msg.what){
                   case 0:
                       Intent intent = new Intent(activity,PlayerActivity.class);
                       startActivity(intent);
                       break;
               }


            }
        }
    }
}
