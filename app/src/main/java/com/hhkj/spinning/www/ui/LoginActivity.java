package com.hhkj.spinning.www.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.CommonTips;
import com.hhkj.spinning.www.widget.NewToast;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.zc.http.okhttp.OkHttpUtils;
import com.zc.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/14/014.
 */

public class LoginActivity extends BaseActivity {
    private ImageView weixin_btn, qq_btn;
    private TextView get_code;


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
    public void process(Message msg) {

        switch (msg.what){
            case 2:
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case 1:

                int i =msg.arg1;

                if(i==0){
                    get_code.setText("获取验证码");
                    get_code.setEnabled(true);
                }else{
                    get_code.setEnabled(false);
                    get_code.setText(i+"秒后重试");
                }
                break;
        }
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
    private EditText phone,code_ma;
    private TextView login;
    @Override
    public void init() {
        CommonTips commonTips = new CommonTips(LoginActivity.this,"测试一下",null);
        commonTips.showSheet();
        login = findViewById(R.id.login);

        code_ma = findViewById(R.id.code_ma);
        phone = findViewById(R.id.phone);
        get_code = findViewById(R.id.get_code);
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
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().toString().length()==0){
                    NewToast.makeText(LoginActivity.this,"请输入手机号码", Common.TTIME).show();
                }else{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("number",phone.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    httpString("GetCode", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {

                            int i = 60;
                            P.c(Thread.currentThread().getName());
                            while(i>=0){

                               // P.c("这里");

                                Message msg = new Message();
                                msg.what = 1;
                                msg.arg1 = i;
                                getHandler().sendMessage(msg);
                                i--;
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void error(String data) {
                                    NewToast.makeText(LoginActivity.this,data,Common.TTIME).show();
                        }

                        @Override
                        public void unLogin() {

                        }
                    });
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHandler().sendEmptyMessage(2);
                if(phone.getText().toString().length()==0){
                    NewToast.makeText(LoginActivity.this,"请输入手机号码",Common.TTIME).show();
                }else if(code_ma.getText().toString().length()==0){
                    NewToast.makeText(LoginActivity.this,"请输入验证码",Common.TTIME).show();
                }else {
                    //登录
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("number",phone.getText().toString());
                        jsonObject.put("code",code_ma.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    httpString("NumberLogin", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {
                            ;getHandler().sendEmptyMessage(2);
                        }

                        @Override
                        public void error(String data) {
                                NewToast.makeText(LoginActivity.this,data,Common.TTIME).show();
                        }

                        @Override
                        public void unLogin() {

                        }
                    });

                }
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
