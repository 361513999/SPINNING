package com.hhkj.spinning.www.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

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
    public void showLoadView(Activity activity){
        if(loadView==null){
            loadView = new LoadView(activity);
            loadView.showSheet();
        }
    }
    public void httpPost(String DIRECT, String content, final Result result,Activity activity){
        httpPostSON(DIRECT,content,result,activity,true);
    }
    public void debugList(final AbsListView listView, final SwipeRefreshLayout refreshLayout){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listView != null && listView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);
            }});
    }
    /**
     *
     * @param DIRECT 访问函数
     * @param content 访问请求参数
     * @param result 返回结果
     */
    public void httpPostSON(String DIRECT, String content, final Result result, Activity activity,boolean show){
            if(show){
                showLoadView(activity);
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
