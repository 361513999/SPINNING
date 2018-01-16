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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.widget.CommonTips;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem3 extends BaseFragment {
    private Handler handler;
    private Activity activity;
    private SharedUtils sharedUtils;
    public PersonCenterItem3(Activity activity, Handler handler){
        this.handler = handler;
        this.activity =activity;
        sharedUtils = new SharedUtils(Common.config);
    }

    private void getAbout(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.About");
            jsonObject.put("method","GetAbout");
            jsonObject.put("param","");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        httpPost("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {

            }

            @Override
            public void error(String data) {

            }

            @Override
            public void unLogin() {

            }
        },activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView exit = view.findViewById(R.id.exit);
        RelativeLayout item0 = view.findViewById(R.id.item0);
        RelativeLayout item1 = view.findViewById(R.id.item1);
        item0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,MessageActivity.class);
                startActivity(intent);
            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getAbout();
                Intent intent = new Intent(activity,CommonWeb.class);
                intent.putExtra("title","关于我们");
                intent.putExtra("url", U.BASE_IMAGE+"ueditor/WriteHtml.html?Id=0");
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonTips commonTips = new CommonTips(activity,null);
                commonTips.init(null,null,"是否退出账户");
                commonTips.setI(new Tips() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void sure() {
                        P.c("退出应用");
                        sharedUtils.clear("token");
                        Intent intent = new Intent(activity,LoginActivity.class);
                        startActivity(intent);
                        AppManager.getAppManager().finishAllActivity();
                    }
                });
                commonTips.showSheet();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center_item3, container, false);
        return view;

    }
}
