package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhkj.spinning.www.R;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem1 extends Fragment {
    private TextView add_tog;
    private Handler handler;
    private Activity activity;
    public PersonCenterItem1(Activity activity,Handler handler){
        this.handler = handler;
        this.activity =activity;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_tog = view.findViewById(R.id.add_tog);
        add_tog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,PersonCenterItem1_Edit.class);
                startActivityForResult(intent,100);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center_item1, container, false);
        return view;

    }
}
