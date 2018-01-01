package com.hhkj.spinning.www.base;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.SharedUtils;

import java.lang.ref.WeakReference;
import java.util.List;


public abstract class BaseFragmentActivity extends FragmentActivity {
    /**
     * 返回键调用
     */

    public SharedUtils sharedUtils;
    public LayoutInflater inflater;
    private Base_Handler  base_handler;


    public void backActivity(View v){
        AppManager.getAppManager().finishActivity(this);
    }
    public Handler getHandler(){
        return  base_handler;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        sharedUtils = new SharedUtils(Common.config);
        base_handler = new Base_Handler(BaseFragmentActivity.this);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        AppManager.getAppManager().addActivity(this);


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
    public abstract  void process(Message msg);
    private class Base_Handler extends Handler {
        WeakReference<BaseFragmentActivity> mLeakActivityRef;
        public Base_Handler(BaseFragmentActivity leakActivity) {
            mLeakActivityRef = new WeakReference<BaseFragmentActivity>(leakActivity);
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
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {

                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {

            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }
    /**
     * 空间数据初始化方法
     */
    public abstract void  init();
}
