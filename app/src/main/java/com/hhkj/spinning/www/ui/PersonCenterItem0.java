package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.bean.PersonCenter0;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.sql.Time;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem0 extends BaseFragment {
    private Handler handler;
    private CircleImageView person_icon;
    private ImageView person_edit_btn;
    private LinearLayout person_icon_content,person_icon_con0;
    private TextView person_tag;
    private SharedUtils sharedUtils;
    private Activity activity;
    private  PersonCenter0 center0 = null;
    private Base_Handler base_handler;
    public PersonCenterItem0(Activity activity,Handler handler){
        this.handler = handler;
        this.activity = activity;
        sharedUtils = new SharedUtils(Common.config);
        base_handler = new Base_Handler(PersonCenterItem0.this);
    }
    private class Base_Handler extends Handler {
        WeakReference<PersonCenterItem0> mLeakActivityRef;

        public Base_Handler(PersonCenterItem0 leakActivity) {
            mLeakActivityRef = new WeakReference<PersonCenterItem0>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mLeakActivityRef.get() != null) {
                switch (msg.what){
                    case 1:
                        ImageLoader.getInstance().displayImage(sharedUtils.getStringValue("icon"),person_icon);
                        person_tag.setText(sharedUtils.getStringValue("userName"));
                        break;
                }
            }
        }
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
                if(center0!=null){
                    intent.putExtra("obj",center0);
                }
                startActivityForResult(intent,100);
            }
        });
        base_handler.sendEmptyMessage(1);
        init();
    }
    private void init(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method","GetUserInfo");
            jsonObject.put("cls","Sys.User");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("number",sharedUtils.getStringValue("phone"));
            jsonObject.put("param",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {
                    center0 = new PersonCenter0();
                    JSONObject obj = new JSONObject(data.getString("Result"));
                    sharedUtils.setStringValue("userName",obj.getString("UserName"));
                    sharedUtils.setStringValue("id",obj.getString("Id"));
                    sharedUtils.setStringValue("icon",obj.getString("Url").startsWith("http")?obj.getString("Url"):"http://"+ U.IP+"/"+obj.getString("Url"));
                    sharedUtils.setBooleanValue("Sex",obj.getBoolean("Sex"));
                    sharedUtils.setStringValue("phone",obj.getString("Phone"));
                    //
                    center0.setBirthday(TimeUtil.getTimeCh(TimeUtil.parseTime(obj.getString("Birthday"))));
                    center0.setHeight(obj.getString("Height"));
                    center0.setIdealWeight(obj.getString("IdealWeight"));
                    center0.setWeight(obj.getString("Weight"));
                    center0.setSex(obj.getBoolean("Sex"));
                    center0.setUrl(obj.getString("Url").startsWith("http")?obj.getString("Url"):"http://"+ U.IP+"/"+obj.getString("Url"));
                    center0.setUserName(obj.getString("UserName"));

                    base_handler.sendEmptyMessage(1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String data) {

            }

            @Override
            public void unLogin() {
                sharedUtils.clear("token");
                Intent intent = new Intent(activity,LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishAllActivity();
            }
        },activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==1000){
            P.c("是反馈的");
           init();
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
