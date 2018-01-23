package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.PersonCenter0;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.inter.Result;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/14/014.
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    public void init() {
        time();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        load();
    }
    private void load(){
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
        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {

                    JSONObject obj = new JSONObject(data.getString("Result"));
                    sharedUtils.setStringValue("userName",obj.getString("UserName"));
                    sharedUtils.setStringValue("id",obj.getString("Id"));
                    sharedUtils.setStringValue("icon",obj.getString("Url"));
                    sharedUtils.setBooleanValue("Sex",obj.getBoolean("Sex"));
                    sharedUtils.setStringValue("phone",obj.getString("Phone"));
                    sharedUtils.setStringValue("Height",obj.getString("Height"));
                    sharedUtils.setStringValue("Weight",obj.getString("Weight"));
                    sharedUtils.setStringValue("Birthday",obj.getString("Birthday"));
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
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishAllActivity();
            }
        },false);
    }

    @Override
    public void process(Message msg) {
        switch (msg.what){
            case 0:
                Intent intent = null;
               if( sharedUtils.getStringValue("token").length()==0){
                     intent = new Intent(WelcomeActivity.this,LoginActivity.class);

               }else{
                     intent = new Intent(WelcomeActivity.this,HomeActivity.class);

               }
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
                break;
        }
    }
    private CountDownTimer countDownTimer;
    private void time(){
        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                sendEmptyMessage(0);
            }
        };
        countDownTimer.start();
    }

}
