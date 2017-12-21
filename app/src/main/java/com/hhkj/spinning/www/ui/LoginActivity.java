package com.hhkj.spinning.www.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/14/014.
 */

public class LoginActivity extends BaseActivity {
    private ImageView weixin_btn, qq_btn;

    @Override
    public void process(int what) {

    }

    //    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.umeng_blue));

        }
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

//        progressDialog = new ProgressDialog(LoginActivity.this);
        UMShareAPI.get(LoginActivity.this).fetchAuthResultWithBundle(LoginActivity.this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                // SocializeUtils.safeShowDialog(progressDialog);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                P.c("onRestoreInstanceState Authorize succeed");
                //  SocializeUtils.safeCloseDialog(progressDialog);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                P.c("onRestoreInstanceState Authorize onError");

                // SocializeUtils.safeCloseDialog(progressDialog);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                P.c("onRestoreInstanceState Authorize onCancel");


                //  SocializeUtils.safeCloseDialog(progressDialog);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(LoginActivity.this).onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(LoginActivity.this).release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        P.c("返回了");
        UMShareAPI.get(LoginActivity.this).onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> data) {
                if (data != null) {
                    Set set = data.entrySet();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        String key = it.next().toString();
                        P.c(key + "结果" + data.get(key));
                    }
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    @Override
    public void init() {
        weixin_btn = findViewById(R.id.weixin_btn);
        qq_btn = findViewById(R.id.qq_btn);
        qq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
            }
        });
        weixin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
            }
        });
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //  SocializeUtils.safeShowDialog(progressDialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            P.c("////onRestoreInstanceState Authorize succeed");
            //SocializeUtils.safeCloseDialog(progressDialog);
          /* if(data!=null){
                Set set = data.entrySet();
                Iterator it = set.iterator();
                while(it.hasNext()){
                    String key =   it.next().toString();
                    P.c(key+"结果"+data.get(key));
                }
            }*/
            if (platform == SHARE_MEDIA.QQ) {
                P.c(data.get("uid"));
                P.c(data.get("openid"));
                P.c(data.get("accessToken"));
                // P.c(data.get("expiration"));
              /*uid 用户id
              openid
              accessToken （6.2以前用access_token）
              expiration （6.2以前用expires_in）过期时间*/
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            P.c("/////onRestoreInstanceState Authorize onError");

            // SocializeUtils.safeCloseDialog(progressDialog);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            P.c("/////onRestoreInstanceState Authorize onCancel");


            // SocializeUtils.safeCloseDialog(progressDialog);
        }
    };


}
