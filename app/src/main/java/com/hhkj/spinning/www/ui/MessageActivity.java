package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.NewToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/16/016.
 */

public class MessageActivity extends BaseActivity {
    @Override
    public void process(Message msg) {
    switch (msg.what){
        case 1:
            NewToast.makeText(MessageActivity.this,"提交成功",Common.TTIME).show();
            AppManager.getAppManager().finishActivity(MessageActivity.this);
            break;
    }
    }

    @Override
    public void init() {
        item1 = findViewById(R.id.item0);
        item2 = findViewById(R.id.item1);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item2.getText().toString().length()==0){
                    NewToast.makeText(MessageActivity.this,"请输入反馈内容", Common.TTIME).show();
                    return;

                }

                if(item1.getText().toString().length()==0){
                    NewToast.makeText(MessageActivity.this,"请输入反馈标题", Common.TTIME).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("toKen",sharedUtils.getStringValue("token"));
                    jsonObject.put("cls","Sys.FeedBack");
                    jsonObject.put("method","AddFeedBack");
                    JSONObject object = new JSONObject();
                    object.put("Content",item2.getText().toString());
                    object.put("Title",item1.getText().toString());
                    jsonObject.put("param",object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                httpPost("Post", jsonObject.toString(), new Result() {
                    @Override
                    public void success(JSONObject data) {
                            getHandler().sendEmptyMessage(1);
                    }

                    @Override
                    public void error(String data) {

                    }

                    @Override
                    public void unLogin() {

                    }
                });
            }
        });
    }
    private TextView send;
    private TextView item1,item2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
    }
}
