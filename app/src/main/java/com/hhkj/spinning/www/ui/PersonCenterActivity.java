package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.PersonLeftAdapter;
import com.hhkj.spinning.www.base.BaseFragmentActivity;
import com.hhkj.spinning.www.common.P;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class PersonCenterActivity extends BaseFragmentActivity {
    private PersonLeftAdapter personLeftAdapter;
    private ListView person_left;
    private FrameLayout main_content;
    private FragmentManager fragmentManager;
    int INDEX = 0;

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
    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {

                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {

            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }
    @Override
    public void process(int what) {
        P.c("收到"+what);
    }
    private int CONTENT = R.id.main_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_center_layout);
        personLeftAdapter = new PersonLeftAdapter(PersonCenterActivity.this);
        person_left = findViewById(R.id.person_left);
        person_left.setAdapter(personLeftAdapter);
        main_content = findViewById(R.id.main_content);
        fragmentManager = getSupportFragmentManager();


        person_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                select(i);
            }
        });

//        person_left.setSelection(0);
        person_left.setItemChecked(INDEX,true);
        select(INDEX);
    }
    private PersonCenterItem0 personCenterItem0;
    private PersonCenterItem1 personCenterItem1;
    private PersonCenterItem2 personCenterItem2;
    private PersonCenterItem3 personCenterItem3;
    private void select(int KEYCODE) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (KEYCODE) {
            case 0://点餐
                personCenterItem0 = new PersonCenterItem0(PersonCenterActivity.this, getHandler());
                transaction.replace(CONTENT, personCenterItem0);
                break;
            case 1://支付
                personCenterItem1 = new PersonCenterItem1(PersonCenterActivity.this, getHandler());
                transaction.replace(CONTENT, personCenterItem1);

                break;
            case 2://点餐
                personCenterItem2 = new PersonCenterItem2(PersonCenterActivity.this, getHandler());
                transaction.replace(CONTENT, personCenterItem2);
                break;
            case 3://支付
                personCenterItem3 = new PersonCenterItem3(PersonCenterActivity.this, getHandler());
                transaction.replace(CONTENT, personCenterItem3);

                break;
        }
        INDEX = KEYCODE;
        transaction.commitAllowingStateLoss();
    }
    @Override
    public void init() {

    }
}
