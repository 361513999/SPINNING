package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.Three_Data;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.NewToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/16/016.
 */

public class BlindPhoneActivity extends BaseActivity {
    private TextView get_code;
    private EditText phone,code_ma;
    private TextView blind;
    private   Three_Data three_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_phone_layout);
        Intent intent = getIntent();
        if(intent.hasExtra("obj")){
            three_data = (Three_Data) intent.getSerializableExtra("obj");
            P.c(three_data.getIcon());
        }
    }

    @Override
    public void process(Message msg) {
        switch (msg.what){
            case 1:
                int i =msg.arg1;
                if(i==60){
                    NewToast.makeText(BlindPhoneActivity.this,"验证码已下发",Common.TTIME).show();
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
    public void init() {
        code_ma = findViewById(R.id.code_ma);
        phone = findViewById(R.id.phone);
        get_code = findViewById(R.id.get_code);
        blind = findViewById(R.id.blind);
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().toString().length()==0){
                    NewToast.makeText(BlindPhoneActivity.this,"请输入手机号码", Common.TTIME).show();
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
                            NewToast.makeText(BlindPhoneActivity.this,data,Common.TTIME).show();
                        }

                        @Override
                        public void unLogin() {

                        }
                    });
                }

            }
        });

        blind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phone.getText().toString().length()==0){
                    NewToast.makeText(BlindPhoneActivity.this,"请输入手机号码",Common.TTIME).show();
                }else if(code_ma.getText().toString().length()==0){
                    NewToast.makeText(BlindPhoneActivity.this,"请输入验证码",Common.TTIME).show();
                }else {
                    //绑定
                    final JSONObject jsonObject = new JSONObject();
//                    string number, string uId, string name, string url, bool sex
                    try {
                        jsonObject.put("number",phone.getText().toString());
                        jsonObject.put("uId",three_data.getUuid());
                        jsonObject.put("name",three_data.getName());
                        jsonObject.put("url",three_data.getIcon());
                        jsonObject.put("sex",three_data.getSex().equals("男")?true:false);
                        jsonObject.put("code",code_ma.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    httpString("BindUser", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {
                            try {
                                sharedUtils.setStringValue("token",data.getString("Value"));
                                 JSONObject obj = new JSONObject(data.getString("Result"));
                                 sharedUtils.setStringValue("userName",obj.getString("UserName"));
                                 sharedUtils.setStringValue("icon",obj.getString("Url"));
                                 sharedUtils.setBooleanValue("Sex",obj.getBoolean("Sex"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void error(String data) {
                            NewToast.makeText(BlindPhoneActivity.this,data,Common.TTIME).show();
                        }

                        @Override
                        public void unLogin() {

                        }
                    });

                }
            }
        });
    }
}
