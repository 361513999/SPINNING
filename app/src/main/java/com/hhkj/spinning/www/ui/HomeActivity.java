package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.HomeEcoAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.HorizontalListView;
import com.hhkj.spinning.www.widget.us.feras.ecogallery.EcoGallery;

/**
 * Created by cloor on 2017/12/19.
 */

public class HomeActivity extends BaseActivity {
    private CircleImageView home_icon_tag;
    private TextView home_icon_txt;
    private HorizontalListView gallery_bottom;
    private HomeEcoAdapter homeEcoAdapter;
    private LinearLayout home_icon_btn0,home_icon_btn1,home_icon_btn2,gallery_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
    }

    @Override
    public void process(int what) {

    }

    @Override
    public void init() {
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
