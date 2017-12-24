package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.SportListAdapter;
import com.hhkj.spinning.www.adapter.SportMenuAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;

/**
 * Created by Administrator on 2017/12/21/021.
 */

public class SportActivity extends BaseActivity {
    private ListView sport_menu,sport_list;
    private LinearLayout list_content;
    private PullToRefreshView pull_to_refresh_list;
    private SportListAdapter sportListAdapter;
    private SportMenuAdapter sportMenuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_layout);
    }

    @Override
    public void process(Message msg) {

    }


    @Override
    public void init() {
        list_content = findViewById(R.id.list_content);
        sport_list = findViewById(R.id.sport_list);
        pull_to_refresh_list = findViewById(R.id.pull_to_refresh_list);
        pull_to_refresh_list.setOnHeaderRefreshListener(listHeadListener);
        pull_to_refresh_list.setOnFooterRefreshListener(listFootListener);
        sport_menu = findViewById(R.id.sport_menu);
        sportMenuAdapter = new SportMenuAdapter(SportActivity.this);
        sport_menu.setAdapter(sportMenuAdapter);
        list_content.post(new Runnable() {
            @Override
            public void run() {
                int width = list_content.getMeasuredWidth();
                double fix = 90.0/505.0;
                int height = (int) (width*fix);
                sportListAdapter = new SportListAdapter(SportActivity.this,width,height);
                sport_list.setAdapter(sportListAdapter);
            }
        });
        sportMenuAdapter.selected(0);
        sport_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sportMenuAdapter.selected(i);
            }
        });
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
                        NewToast.makeText(SportActivity.this, "没有数据可加载", Common.TTIME).show();

                    }

                }
            }, runTime);
        }
    };

}
