package com.hhkj.spinning.www.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/14/014.
 */

public class LoginActivity extends BaseActivity {
    @Override
    public void process(int what) {

    }
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        progressDialog = new ProgressDialog(LoginActivity.this);
        UMShareAPI.get(LoginActivity.this).fetchAuthResultWithBundle(LoginActivity.this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                SocializeUtils.safeShowDialog(progressDialog);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                P.c("onRestoreInstanceState Authorize succeed");
                SocializeUtils.safeCloseDialog(progressDialog);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                P.c("onRestoreInstanceState Authorize onError");

                SocializeUtils.safeCloseDialog(progressDialog);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                P.c("onRestoreInstanceState Authorize onCancel");


                SocializeUtils.safeCloseDialog(progressDialog);
            }
        });
        UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this,SHARE_MEDIA.QQ,new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                SocializeUtils.safeShowDialog(progressDialog);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                P.c("////onRestoreInstanceState Authorize succeed");
                SocializeUtils.safeCloseDialog(progressDialog);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                P.c("/////onRestoreInstanceState Authorize onError");

                SocializeUtils.safeCloseDialog(progressDialog);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                P.c("/////onRestoreInstanceState Authorize onCancel");


                SocializeUtils.safeCloseDialog(progressDialog);
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
        UMShareAPI.get(LoginActivity.this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void init() {

    }
}
