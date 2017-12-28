package com.hhkj.spinning.www.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.ui.LoginActivity;
import com.hhkj.spinning.www.widget.LoadView;
import com.zc.http.okhttp.OkHttpUtils;
import com.zc.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/28/028.
 */

public class BaseFragment extends Fragment {
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
    public void httpPost(String DIRECT, String content, final Result result, Activity activity){
        if(loadView==null){
            loadView = new LoadView(activity);
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
}
