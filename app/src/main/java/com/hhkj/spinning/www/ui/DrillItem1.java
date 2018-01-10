package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.DrillItem1LeftAdapter;
import com.hhkj.spinning.www.adapter.DrillItem1RightAdapter;
import com.hhkj.spinning.www.adapter.SportListAdapter;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.bean.HomeOnlineList;
import com.hhkj.spinning.www.bean.VideoBean;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;
import com.hhkj.spinning.www.widget.indicator.CircleIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by cloor on 2017/12/30.
 */

@SuppressLint("ValidFragment")
public class DrillItem1 extends BaseFragment {
    private DrillItem1RightAdapter drillItem1RightAdapter ;
    private Activity activity;
    private Handler handler;
    private ViewPager item0;
    private SwipeRefreshLayout drill_item1_refr;
    private GridView drill_list;
    private PullToRefreshView pull_to_refresh_list;
    private DrillItem1LeftAdapter drillItem1LeftAdapter;
    private ArrayList<View> lefts = new ArrayList<>();
    private SharedUtils sharedUtils;
    private LinearLayout item1;

    public DrillItem1(Activity activity, Handler handler,SharedUtils sharedUtils){
        this.activity = activity;
        this.sharedUtils = sharedUtils;
        this.handler = handler;
    }
    private final int runTime = 400;
    private PullToRefreshView.OnHeaderRefreshListener listHeadListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onHeaderRefreshComplete();


                }
            }, runTime);
        }
    };
    private PullToRefreshView.OnFooterRefreshListener listFootListener = new PullToRefreshView.OnFooterRefreshListener() {
        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onFooterRefreshComplete();

                    if(CURRENT_LIST_MORE){
                        //加载
                        getVideos();
                    }else {
                        NewToast.makeText(activity, "没有数据可加载", Common.TTIME).show();

                    }

                }
            }, runTime);
        }
    };
    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            drill_item1_refr.setRefreshing(true);
            CURRENT_LIST_PAGE = 1;
            videoBeans.clear();
            getVideos();
        }
    };
    private boolean CURRENT_LIST_MORE = true;
    private int CURRENT_LIST_PAGE = 1;
    private ArrayList<VideoBean> videoBeans = new ArrayList<>();
    private void getVideos() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cls", "Sys.PlayVideo");
            jsonObject.put("method", "LoadPlayVideo");
            jsonObject.put("toKen", sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("pageSize", Common.SHOW_NUM);
            object.put("pageIndex", CURRENT_LIST_PAGE);
            object.put("app", "");
            jsonObject.put("param", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {
                    String result = data.getString("Result");
                    JSONArray jsonArray = new JSONArray(result);
                    int len = jsonArray.length();
                    if (len < Common.SHOW_NUM) {
                        CURRENT_LIST_MORE = false;
                        //  getHandler().sendEmptyMessage(-2);
                    } else {
                        CURRENT_LIST_PAGE = CURRENT_LIST_PAGE + 1;
                    }
                    for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        VideoBean bean = new VideoBean();
                        bean.setImage(object.getString("TitleUrl"));
                        bean.setUrl(object.getString("Url"));
                        bean.setTitle(object.getString("Title"));
                        bean.setTime(object.getString("LongTime"));
                        videoBeans.add(bean);
                    }
                    drill_handler.sendEmptyMessage(1);

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
        },activity);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        item0 = view.findViewById(R.id.item0);
        item1 = view.findViewById(R.id.item1);
        //276   80
        drill_item1_refr = view.findViewById(R.id.drill_item1_refr);
        drill_item1_refr.setOnRefreshListener(listener);
        drill_list = view.findViewById(R.id.drill_list);
        pull_to_refresh_list = view.findViewById(R.id.pull_to_refresh_list);
//        pull_to_refresh_list.setOnHeaderRefreshListener(listHeadListener);
        pull_to_refresh_list.setEnablePullTorefresh(false);
        pull_to_refresh_list.setOnFooterRefreshListener(listFootListener);
        drill_list.post(new Runnable() {
            @Override
            public void run() {
                int div  = FileUtils.dip2px(activity,4 );
                int width = (drill_list.getMeasuredWidth()-div)/2;
                double height = (80.0/136.0)*width;
                P.c(width+"==="+height);
                drillItem1RightAdapter = new DrillItem1RightAdapter(activity,width, (int) height,drill_handler,videoBeans);
                drill_list.setAdapter(drillItem1RightAdapter);
            }
        });
        getVideos();
        getOnlineList();

    }
    private void addView(){
        lefts.clear();
        int size = onlineLists.size();
        for(int i=0;i<size;i++){
            View  view = LinearLayout.inflate(activity,R.layout.item_drill_item1_left,null);
            lefts.add(view);
        }
       // drillItem1LeftAdapter.update(lefts);
    }
    private ArrayList<HomeOnlineList> onlineLists = new ArrayList<>();
    private void getOnlineList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toKen", sharedUtils.getStringValue("token"));
            jsonObject.put("cls", "Sys.VideoInfo");
            jsonObject.put("method", "LoadVideoInfo");
            JSONObject object = new JSONObject();
            object.put("pageSize", 4);
            object.put("pageIndex", 1);
            object.put("app", "");
            jsonObject.put("param", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {

                try {
                    onlineLists.clear();
                    String result = data.getString("Result");
                    JSONArray jsonArray = new JSONArray(result);
                    int len = jsonArray.length();

                    for (int i = 0; i < len; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        HomeOnlineList onlineList = new HomeOnlineList();
                        onlineList.setId(object.getString("Id"));
                        onlineList.setIco(FileUtils.addImage(object.getString("UserUrl")));
                        onlineList.setTitle(object.getString("Title"));
                        onlineList.setStartTime(TimeUtil.getTimeHome(TimeUtil.parseTime_(object.getString("BeginTime"))));
                        onlineList.setTime(object.getString("PlayLongTime"));
                        onlineList.setName(object.getString("UserName"));
                        onlineList.setImage(object.getString("Url"));
                        onlineList.setStatus(object.getInt("PlayStatus"));
                        onlineList.setBeginTime(object.getString("BeginTime"));
                        onlineList.setPlayTime(object.getInt("PlayLongTime"));
                        onlineLists.add(onlineList);
                    }
                    drill_handler.sendEmptyMessage(3);
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
        },activity,false);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drill_item1, container, false);
        drill_handler = new Drill_Handler(DrillItem1.this);
        return view;

    }
    private Drill_Handler drill_handler;
    private class Drill_Handler extends Handler {
        WeakReference<DrillItem1> mLeakActivityRef;
        public Drill_Handler(DrillItem1 leakActivity) {
            mLeakActivityRef = new WeakReference<DrillItem1>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){

            switch (msg.what){
                case  0:
                    VideoBean bean = (VideoBean) msg.obj;
                    Intent intent = new Intent(activity,PlayerActivity.class);
                    intent.putExtra("param",bean.getTitle()+";"+FileUtils.addImage(bean.getUrl()));
                    startActivity(intent);
                    break;
                case 1:
                    if(drill_item1_refr!=null){
                        drill_item1_refr.setRefreshing(false);
                    }
                    drillItem1RightAdapter.updata(videoBeans);
                    break;
                case 3:
                    addView();
                    drillItem1LeftAdapter = new DrillItem1LeftAdapter(lefts,onlineLists,drill_handler);
                    item0.setAdapter(drillItem1LeftAdapter);
                    item1.removeAllViews();
                    CircleIndicator indicator = (CircleIndicator) CircleIndicator.inflate(activity,R.layout.item_left_drill_item1,null);
                    indicator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,40));
                    item1.addView(indicator);
                    indicator.setViewPager(item0);
                    indicator.setIndicatorLayoutGravity(CircleIndicator.Gravity.RIGHT);
                    break;
                case 4:
                    final HomeOnlineList online = (HomeOnlineList) msg.obj;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("toKen", sharedUtils.getStringValue("token"));
                        jsonObject.put("cls", "Sys.VideoInfo");
                        jsonObject.put("method", "GetPlayUrl");
                        JSONObject object = new JSONObject();
                        object.put("Id", online.getId());
                        jsonObject.put("param", object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    httpPost("Post", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {
                            try {
                                String result = data.getString("Result");
                                String value = data.getString("Value");
                                JSONObject object = new JSONObject(value);
                                if( object.getInt("PlayStatus")!=1){

                                    drill_handler.sendEmptyMessage(6);
                                    return;
                                }

                                JSONArray jsonArray = new JSONArray(result);
                                int len = jsonArray.length();
                                if (len != 0) {
                                    Message msg = new Message();
                                    msg.what = 5;
                                    msg.obj = online.getId() + ";" + jsonArray.getString(0) + ";" + online.getTitle();
                                   drill_handler.sendMessage(msg);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void error(String data) {
                            NewToast.makeText(activity, data, Common.TTIME).show();
                        }

                        @Override
                        public void unLogin() {

                        }
                    },activity);
                    break;
                case 5:
                    String param = (String) msg.obj;
                    Intent intent5 = new Intent(activity, PlayOnlineActivity.class);
                    intent5.putExtra("param", param);
                    startActivity(intent5);

                    break;
                case 6:
                    NewToast.makeText(activity, "还未开播", Common.TTIME).show();
                    break;
             }

            }
        }
    }

}
