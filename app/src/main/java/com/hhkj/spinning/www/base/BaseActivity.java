package com.hhkj.spinning.www.base;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;

import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.SharedUtils;


import java.lang.ref.WeakReference;


public abstract class BaseActivity extends Activity {
    /**
     * 返回键调用
     */

    public SharedUtils sharedUtils;
    public LayoutInflater inflater;
    private Base_Handler  base_handler;


    public void backActivity(){
        AppManager.getAppManager().finishActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        sharedUtils = new SharedUtils(Common.config);
        base_handler = new Base_Handler(BaseActivity.this);
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

    /**
     * 接收handler消息处理方法
     * @param what
     */
    public abstract  void process(int what);
    private class Base_Handler extends Handler {
        WeakReference<BaseActivity> mLeakActivityRef;
        public Base_Handler(BaseActivity leakActivity) {
            mLeakActivityRef = new WeakReference<BaseActivity>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                    process(msg.what);
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
