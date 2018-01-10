package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseFragmentActivity;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/12/21/021.
 */

/**
 * 开始训练
 */
public class DrillActivity extends BaseFragmentActivity {
    private int CONTENT = R.id.main_content;
    int INDEX = 0;
    private RadioGroup contr;
    private FragmentManager fragmentManager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 自己记录fragment的位置,防止activity被系统回收时，fragment错乱的问题
        // super.onSaveInstanceState(outState);
        outState.putInt("index", INDEX);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        INDEX = savedInstanceState.getInt("index");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drill_layout);
    }

    @Override
    public void process(Message msg) {

    }


    @Override
    public void init() {
        contr = findViewById(R.id.contr);
        fragmentManager = getSupportFragmentManager();
        contr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for(int k=0;k< contr.getChildCount();k++){
                    if(i==contr.getChildAt(k).getId()){
                        select(k);
                        return;
                    }
                }
            }
        });
        contr.check(contr.getChildAt(0).getId());
    }
    private DrillItem1 drillItem1;
    private DrillItem0 drillItem0;
    private void select(int KEYCODE) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (KEYCODE) {
            case 0:
                drillItem0 = new DrillItem0(DrillActivity.this, getHandler(),sharedUtils);
                transaction.replace(CONTENT, drillItem0);
                break;
            case 1:

                drillItem1 = new DrillItem1(DrillActivity.this, getHandler(),sharedUtils);
                transaction.replace(CONTENT, drillItem1);

                break;

        }
        INDEX = KEYCODE;
        transaction.commitAllowingStateLoss();
    }
}
