package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.HomeEcoAdapter;
import com.hhkj.spinning.www.adapter.HomeListAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.HorizontalListView;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;
import com.hhkj.spinning.www.widget.us.feras.ecogallery.EcoGallery;

/**
 * Created by cloor on 2017/12/19.
 */

public class HomeActivity extends BaseActivity {
    private CircleImageView home_icon_tag;
    private TextView home_icon_txt;
    private HorizontalListView gallery_bottom;
    private HomeEcoAdapter homeEcoAdapter;
    private HomeListAdapter homeListAdapter;
    private LinearLayout home_icon_btn0,home_icon_btn1,home_icon_btn2,gallery_content;
    private PullToRefreshView pull_to_refresh_list;
    private ListView home_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
    }

    @Override
    public void process(Message msg) {

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
                        NewToast.makeText(HomeActivity.this, "没有数据可加载", Common.TTIME).show();

                    }

                }
            }, runTime);
        }
    };

    @Override
    public void init() {
        pull_to_refresh_list = findViewById(R.id.pull_to_refresh_list);
        home_list = findViewById(R.id.home_list);
        pull_to_refresh_list.setOnHeaderRefreshListener(listHeadListener);
        pull_to_refresh_list.setOnFooterRefreshListener(listFootListener);

        gallery_bottom = findViewById(R.id.gallery_bottom);
        home_icon_txt = findViewById(R.id.home_icon_txt);
        home_icon_tag = findViewById(R.id.home_icon_tag);
       //
        home_icon_btn0 = findViewById(R.id.home_icon_btn0);
        home_icon_btn1 = findViewById(R.id.home_icon_btn1);
        home_icon_btn2 = findViewById(R.id.home_icon_btn2);
        gallery_content = findViewById(R.id.gallery_content);
        gallery_content.post(new Runnable() {
            @Override
            public void run() {
                double height = gallery_content.getHeight();
               double fix = 135.0/80.0;
               double width = height*fix;
              //  int count = 10;
               // int temp = FileUtils.dip2px(HomeActivity.this, 10)*(count-2);
               // gallery_bottom.setLayoutParams(new LinearLayout.LayoutParams((int)((count*width)+temp),(int)height));
                homeEcoAdapter = new HomeEcoAdapter(HomeActivity.this,(int)width,(int)height);
               gallery_bottom.setAdapter(homeEcoAdapter);

            }
        });
        home_list.post(new Runnable() {
            @Override
            public void run() {
                double width = home_list.getMeasuredWidth();
                double fix = 42.0/272.0;
                double height = width*fix;
                homeListAdapter = new HomeListAdapter(HomeActivity.this,(int)width,(int)height);
                home_list.setAdapter(homeListAdapter);

            }
        });
        home_icon_btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MyBikeActivity.class);
                startActivity(intent);
            }
        });
        home_icon_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,DrillActivity.class);
                startActivity(intent);
            }
        });
        home_icon_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SportActivity.class);
                startActivity(intent);
            }
        });
        home_icon_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        home_icon_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PersonCenterActivity.class);
                startActivity(intent);
            }
        });
    }
}
