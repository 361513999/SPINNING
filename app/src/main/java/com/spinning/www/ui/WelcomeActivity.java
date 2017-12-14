package com.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;

import com.hhkj.spinning.www.R;
import com.spinning.www.base.AppManager;
import com.spinning.www.base.BaseActivity;
import com.spinning.www.common.P;

/**
 * Created by Administrator on 2017/12/14/014.
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    public void init() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        time();

    }
    private void time(){
        new Thread() {
            public void run() {
                for (int i = 3; i >= 0; i--) {
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
    @Override
    public void process(int what) {
        switch (what){
            case 0:
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity();
                break;
        }
    }
}
