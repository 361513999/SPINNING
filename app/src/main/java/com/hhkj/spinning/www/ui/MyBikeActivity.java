package com.hhkj.spinning.www.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.widget.ColorArcProgressBar;

/**
 * Created by Administrator on 2017/12/20/020.
 */

public class MyBikeActivity extends BaseActivity {
    private ColorArcProgressBar bike_cicle;
    private TextView bt_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bike_layout);
    }

    @Override
    public void process(Message msg) {

    }

    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager)  getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    @Override
    public void init() {
        bt_click = findViewById(R.id.bt_click);
        bike_cicle = findViewById(R.id.bike_cicle);
//        int diameter = (int)(216.0 * getScreenWidth() / 452);
//        bike_cicle.setDiameter(diameter);
        bike_cicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bike_cicle.getCurrentValues()==100){
                    bike_cicle.setCurrentValues(0);
                }else{
                    bike_cicle.setCurrentValues(100);
                }
            }
        });
        bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
