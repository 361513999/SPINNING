package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;

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

    private void time(){
        new Thread() {
            public void run() {
                for (int i = 1; i >= 0; i--) {
                    if (i == 0) {
                        sendEmptyMessage(0);
                    } else {


                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }
            };
        }.start();
    }

}
