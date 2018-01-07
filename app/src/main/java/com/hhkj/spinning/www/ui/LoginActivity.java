package com.hhkj.spinning.www.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.Three_Data;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.CommonTips;
import com.hhkj.spinning.www.widget.LoadView;
import com.hhkj.spinning.www.widget.NewToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.zc.http.okhttp.OkHttpUtils;
import com.zc.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
    private CircleImageView login_icon;

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

                    Set set = data.entrySet();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        String key = it.next().toString();
                        P.c(key + "自动//结果" + data.get(key));
                    }


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
            case 4:
                String path = (String) msg.obj;
                ImageLoader.getInstance().displayImage(FileUtils.addImage(path),login_icon);
                break;
            case 3:
                Three_Data three_data = (Three_Data) msg.obj;
                Intent intent3 = new Intent(LoginActivity.this,BlindPhoneActivity.class);
                intent3.putExtra("obj",three_data);
                startActivity(intent3);
                AppManager.getAppManager().finishActivity(LoginActivity.this);
                break;
            case 2:
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity(LoginActivity.this);
                break;
            case 1:

                int i =msg.arg1;
                if(i==60){
                    NewToast.makeText(LoginActivity.this,"验证码已下发",Common.TTIME).show();
                }
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
       // getInfo(SHARE_MEDIA.QQ);




    }
    private void parseLogin (JSONObject data) throws JSONException {
        sharedUtils.setStringValue("token",data.getString("Value"));
        JSONObject obj = new JSONObject(data.getString("Result"));
        sharedUtils.setStringValue("userName",obj.getString("UserName"));
        sharedUtils.setStringValue("id",obj.getString("Id"));
        sharedUtils.setStringValue("icon",obj.getString("Url"));
        sharedUtils.setBooleanValue("Sex",obj.getBoolean("Sex"));
        sharedUtils.setStringValue("phone",obj.getString("Phone"));
        sharedUtils.setStringValue("Height",obj.getString("Height"));
        sharedUtils.setStringValue("Weight",obj.getString("Weight"));
        sharedUtils.setStringValue("Birthday",obj.getString("Birthday"));
        getHandler().sendEmptyMessage(2);
    }
    private EditText phone,code_ma;
    private TextView login;
    @Override
    public void init() {
      /*  CommonTips commonTips = new CommonTips(LoginActivity.this,"测试一下",null);
        commonTips.showSheet();*/
        login = findViewById(R.id.login);
        login_icon = findViewById(R.id.login_icon);
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
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    String temp = editable.toString();
                    if(temp.length()==11){
                        httpGet("GetHeadUrl?number=" + temp, new Result() {
                            @Override
                            public void success(JSONObject data) {

                                try {
                                    Message msg = new Message();
                                    msg.what =4;
                                    msg.obj =data.getString("Result");
                                    getHandler().sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void error(String data) {

                            }

                            @Override
                            public void unLogin() {

                            }
                        });
                    }
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
                    httpPost("GetCode", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {

                            int i = 60;
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

                    httpPost("NumberLogin", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {
                            try {
                                parseLogin(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
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
          P.c("platform"+platform);
            getInfo(platform);
           /* if (platform == SHARE_MEDIA.QQ) {
                P.c(data.get("uid"));
                P.c(data.get("openid"));
                P.c(data.get("accessToken"));
                // P.c(data.get("expiration"));
              *//*uid 用户id
              openid
              accessToken （6.2以前用access_token）
              expiration （6.2以前用expires_in）过期时间*//*
            }
            if(platform == SHARE_MEDIA.WEIXIN){
                Set set = data.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    P.c(key + "微信//结果" + data.get(key));
                }
                getInfo(SHARE_MEDIA.WEIXIN);
            }*/

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


    private void getInfo(SHARE_MEDIA platform){
        //---------

        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this,platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
              showLoadView();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, final Map<String, String> map) {
               cancleLoadView();
                if (map != null) {
                    Set set = map.entrySet();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        String key = it.next().toString();
                        P.c(key + "------" + map.get(key));
                    }

                    JSONObject jsonObject  =new JSONObject();
                    try {
                        jsonObject.put("uId",map.get("openid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    httpPost("CheckUId", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {
                            try {
                                if(data.getString("Result").length()==0){

                                    Three_Data three_data = new Three_Data();
                                    three_data.setIcon(map.get("profile_image_url"));
                                    three_data.setName(map.get("screen_name"));
                                    three_data.setSex(map.get("gender"));
                                    three_data.setUuid(map.get("openid"));
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.obj = three_data;
                                    getHandler().sendMessage(msg);
                                }else{
                                    try {
                                        parseLogin(data);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void error(String data) {

                        }

                        @Override
                        public void unLogin() {

                        }
                    });
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                cancleLoadView();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                cancleLoadView();
            }
        });
    }


}
