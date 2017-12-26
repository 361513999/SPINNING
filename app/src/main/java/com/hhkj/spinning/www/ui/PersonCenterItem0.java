package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem0 extends Fragment {
    private Handler handler;
    private CircleImageView person_icon;
    private ImageView person_edit_btn;
    private LinearLayout person_icon_content,person_icon_con0;
    private TextView person_tag;
    private SharedUtils sharedUtils;
    private Activity activity;
    public PersonCenterItem0(Activity activity,Handler handler){
        this.handler = handler;
        this.activity = activity;
        sharedUtils = new SharedUtils(Common.config);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        person_tag = view.findViewById(R.id.person_tag);
        person_icon_content = view.findViewById(R.id.person_icon_content);
        person_icon = view.findViewById(R.id.person_icon);
        person_edit_btn = view.findViewById(R.id.person_edit_btn);
        person_icon_con0 = view.findViewById(R.id.person_icon_con0);
        person_icon_content.post(new Runnable() {
            @Override
            public void run() {
                int height = person_icon_content.getHeight();
                person_icon_con0.setLayoutParams(new LinearLayout.LayoutParams(height,height));
            }
        });
        person_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ModPersonActivity.class);
                startActivityForResult(intent,100);
            }
        });
        ImageLoader.getInstance().displayImage(sharedUtils.getStringValue("icon"),person_icon);
        person_tag.setText(sharedUtils.getStringValue("userName"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==1000){
            P.c("是反馈的");
        }else{
            P.c("不是反馈的");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center_item0, container, false);
        return view;

    }
}
