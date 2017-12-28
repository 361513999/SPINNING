package com.hhkj.spinning.www.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.LoadView;
import com.jph.takephoto.app.TakePhotoActivity;
import com.zc.http.okhttp.OkHttpUtils;
import com.zc.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/22/022.
 */

public abstract class TPActivity extends TakePhotoActivity {
    /**
     * 返回键调用
     */

    public SharedUtils sharedUtils;
    public LayoutInflater inflater;
    private Base_Handler base_handler;


    public void backActivity(View v){
        AppManager.getAppManager().finishActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        sharedUtils = new SharedUtils(Common.config);
        base_handler = new Base_Handler(TPActivity.this);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        AppManager.getAppManager().addActivity(this);
    }
    public Handler getHandler(){
        return  base_handler;
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);
    }
    private LoadView loadView = null;
    private void cancleLoadView(){
        if(loadView!=null){
            loadView.cancle();
            loadView = null;
        }
    }
    /**
     *
     * @param DIRECT 访问函数
     * @param content 访问请求参数
     * @param result 返回结果
     */
    public void httpPost(String DIRECT, String content, final Result result){
        if(loadView==null){
            loadView = new LoadView(TPActivity.this);
            loadView.showSheet();
        }
        OkHttpUtils.postString().url(U.VISTER()+DIRECT).mediaType(U.json).content(content).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                cancleLoadView();
            }

            @Override
            public void onResponse(String response, int id) {
                cancleLoadView();
                P.c(response);
                if(result!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getBoolean("Success")){
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    //禁止在返回中直接使用
                                    result.success(jsonObject);
                                }
                            }.start();
                        }else{
                            if(jsonObject.getString("Result").equals("login")){
                                result.unLogin();
                            }else{
                                result.error(jsonObject.getString("Error"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        P.c("格式错误");
                    }
                }
            }
        });
    }

    /**
     * 接收handler消息处理方法
     * @param what
     */
    public abstract  void process(Message msg);
    private class Base_Handler extends Handler {
        WeakReference<TPActivity> mLeakActivityRef;
        public Base_Handler(TPActivity leakActivity) {
            mLeakActivityRef = new WeakReference<TPActivity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                process(msg);
            }
        }
    }

    /**
     * 发送message
     * @param what
     */
    public void sendEmptyMessage(int what){
        base_handler.sendEmptyMessage(what);
    }
    public void sendMessage(int what,Object object){
        Message msg  = new Message();
        msg.what = what;
        msg.obj = object;
        base_handler.sendEmptyMessage(what);
    }


    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    /**
     * 空间数据初始化方法
     */
    public abstract void  init();
}
