package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.HomeEcoAdapter;
import com.hhkj.spinning.www.adapter.HomeListAdapter;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.HomeOnlineList;
import com.hhkj.spinning.www.bean.VideoBean;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.CommonTips;
import com.hhkj.spinning.www.widget.HorizontalListView;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cloor on 2017/12/19.
 */

public class HomeActivity extends BaseActivity {
    private CircleImageView home_icon_tag;
    private TextView home_icon_txt;
    private HorizontalListView gallery_bottom;
    private HomeEcoAdapter homeEcoAdapter;
    private HomeListAdapter homeListAdapter;
    private LinearLayout home_icon_btn0, home_icon_btn1, home_icon_btn2, gallery_content;
    private PullToRefreshView pull_to_refresh_list;
    private ListView home_list;
    private SwipeRefreshLayout home_refre;
    private ImageView item_left0;
    private CircleImageView item_left1;
    private TextView item_left2, item_left3, item_left4, item_left5, item_left6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        getInit();
    }
    private void getInit(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.ParamSet");
            jsonObject.put("method","GetData");
            jsonObject.put("param","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {
                    String result = data.getString("Result");

                    JSONArray jsonArray =   new JSONArray(result);
                    int len =  jsonArray.length();
                    P.c("保存===="+len);
                    SharedUtils sharedUtils = new SharedUtils(Common.initMap);
                    sharedUtils.clear();
                    for(int i=0;i<len;i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        sharedUtils.setStringValue(object.getString("key"),object.getString("value"));
//                        Common.initMaps.put(object.getString("key"),object.getString("value"));

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
        },false);

    }

    private long exitTime = 0;
    private void exit(){

        if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
        { NewToast.makeText(HomeActivity.this, "再按一次退出程序", Common.TTIME).show();

            exitTime = System.currentTimeMillis();
        } else {
            try {
                BaseApplication.iMusicService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AppManager.getAppManager().finishAllActivity();

        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }

    }


/*    private volatile   boolean ISRUN = true;
    private   void time(long midTime,int flag) {

        while (midTime > 0&&ISRUN) {
            midTime--;
            long hh = midTime / 60 / 60 % 60;
            long mm = midTime / 60 % 60;
            long ss = midTime % 60;
            Message msg = new Message();
            msg.what = 4;
            msg.arg1 = flag;
            msg.obj = hh + "小时" + mm + "分钟" + ss + "秒";
            getHandler().sendMessage(msg);

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
    private void getNormal(final HomeOnlineList online){


        ImageLoader.getInstance().displayImage(FileUtils.addImage(online.getImage()), item_left0);
        ImageLoader.getInstance().displayImage(FileUtils.addImage(online.getIco()), item_left1);
        item_left2.setText(online.getName());
        item_left3.setText(online.getPlayTime() + " 分钟");
        item_left4.setText(online.getTitle());
        midStatus = online.getStatus();
        if (online.getStatus() == 0) {
            long from = System.currentTimeMillis();
            long to = TimeUtil.parseTime_(online.getBeginTime());
            long minutes = (to - from) / 1000;
            midTime = minutes;
            midFlag = 0;
            item_left6.setText("未开始");
        } else {
            long from = System.currentTimeMillis();
//                                long to = TimeUtil.parseTime_(online.getBeginTime())+(1000*60*online.getPlayTime());
            long to = TimeUtil.getFetureSec(online.getPlayTime());
            P.c(TimeUtil.getTime(TimeUtil.parseTime_(online.getBeginTime()))+"=="+TimeUtil.getTime(to));
            long minutes = (to - from) / 1000;
            midFlag = 1;
            midTime  = minutes;

            item_left6.setText(online.getStatus()==1?"正在直播":"已结束");
        }
        item_left6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = online;
                getHandler().sendMessage(msg);
            }
        });
    }
    private long midTime = 0;
    private int midFlag = 0;
    private int midStatus = -1;
    @Override
    public void process(Message msg) {
        switch (msg.what) {
            case -3:
                final int index = msg.arg1;
                final HomeOnlineList list = (HomeOnlineList) msg.obj;

                home_list.setItemChecked(index,true);
                getNormal(list);

                break;
            case -2:
                NewToast.makeText(HomeActivity.this, "最后一页", 1000).show();
                break;
            case 1:
                if (home_refre != null) {
                    home_refre.setRefreshing(false);
                }
                int size = onlineLists.size();
                if (size != 0) {
                    getNormal(onlineLists.get(0));
                    home_list.setItemChecked(0,true);
                }else{
                    ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.video_default, item_left0);
                    ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.logo, item_left1);
                    item_left2.setText("");
                    item_left3.setText("");
                    item_left4.setText("");
                    item_left6.setText("未开始");
                }
                homeListAdapter.updata(onlineLists);
                break;
            case 2:

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

                                JSONArray ja = new JSONArray(result);
                                if(ja.length()==1){
                                    //说明是存在播放地址的
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.arg1 = -1;
                                    msg.obj = ja.getString(0);
                                    getHandler().sendMessage(msg);


                                    return;
                                }

                                getHandler().sendEmptyMessage(5);
                                return;
                            }

                            JSONArray jsonArray = new JSONArray(result);
                            int len = jsonArray.length();
                            if (len != 0) {
                                Message msg = new Message();
                                msg.what = 3;
                                if(len==3){
                                    msg.obj = online.getId() + ";" + jsonArray.getString(0) + ";" + online.getTitle();
                                }else if(len==4){
                                    msg.obj = online.getId() + ";" + jsonArray.getString(0) + ";" + online.getTitle()+";"+jsonArray.getString(3);
                                }

                                getHandler().sendMessage(msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void error(String data) {
                        NewToast.makeText(HomeActivity.this, data, Common.TTIME).show();
                    }

                    @Override
                    public void unLogin() {

                    }
                });
                break;
            case 3:
                if(msg.arg1 == -1){
                    Intent inten0 = new Intent(HomeActivity.this,PlayerActivity.class);
                    VideoBean bean = new VideoBean();
                    bean.setUrl((String) msg.obj);
                    bean.setTitle("点播");
                    inten0.putExtra("param",bean);
                    startActivity(inten0);
                }else{
                    String param = (String) msg.obj;
                    Intent intent = new Intent(HomeActivity.this, PlayOnlineActivity.class);
                    intent.putExtra("param", param);
                    startActivity(intent);
                }


                break;
            case 4:
                String obj = (String) msg.obj;
                if(msg.arg1 ==0){
                    item_left5.setText("距离开始:" + obj);
                }else{
                    item_left5.setText("距离结束:" + obj);
                }

                break;
            case 5:
                NewToast.makeText(HomeActivity.this, "还未开播", Common.TTIME).show();
                break;
            case 6:
                    homeEcoAdapter.updata(videoBeans);
                break;
            case 7:
                VideoBean bean = (VideoBean) msg.obj;
                Intent intent7 = new Intent(HomeActivity.this,PlayerActivity.class);
                intent7.putExtra("param",bean);
                startActivity(intent7);
                break;
        }
    }

    private final int runTime = 400;
    private PullToRefreshView.OnHeaderRefreshListener listHeadListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onHeaderRefreshComplete();

//                    getOnlineList();

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
                    if (CURRENT_LIST_MORE) {
                        getOnlineList();
                    } else {
                        NewToast.makeText(HomeActivity.this, "没有数据可加载", Common.TTIME).show();

                    }

                }
            }, runTime);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        createTime();

        home_icon_txt.setText(sharedUtils.getStringValue("userName"));
        ImageLoader.getInstance().displayImage(FileUtils.addImage(sharedUtils.getStringValue("icon")), home_icon_tag);

        String temp  =sharedUtils.getStringValue("Weight");

       try {
          int i =  Integer.parseInt(temp);
          if(i<=0){
              goEdit();
          }
       }catch (Exception e){
           goEdit();
       }


    }

    private void goEdit(){
        CommonTips tips = new CommonTips(HomeActivity.this,null);
       tips.init("取消","前往","为了确保数据的准确性,请前往编辑您的体重信息");
       tips.setI(new Tips() {
           @Override
           public void cancel() {

           }

           @Override
           public void sure() {
                    Intent intent = new Intent(HomeActivity.this,ModPersonActivity.class);
                    startActivity(intent);
           }
       });
       tips.showSheet();
    }

    private boolean CURRENT_LIST_MORE = true;
    private int CURRENT_LIST_PAGE = 1;
    private ArrayList<HomeOnlineList> onlineLists = new ArrayList<>();

    private void getOnlineList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toKen", sharedUtils.getStringValue("token"));
            jsonObject.put("cls", "Sys.VideoInfo");
            jsonObject.put("method", "LoadVideoInfo");
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
                    getHandler().sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String data) {

            }

            @Override
            public void unLogin() {
                sharedUtils.clear("token");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishAllActivity();
            }
        });

    }


    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            CURRENT_LIST_PAGE = 1;
            onlineLists.clear();
            home_refre.setRefreshing(true);
            item_left6.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOnlineList();
                }
            },1200);

        }
    };
    private  Timer timer;
    private void createTime(){
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // 需要做的事:发送消息
                midTime--;
                long hh = midTime / 60 / 60 % 60;
                long mm = midTime / 60 % 60;
                long ss = midTime % 60;
                Message msg = new Message();
                msg.what = 4;
                msg.arg1 = midFlag;
                if(midTime<0){
                    msg.obj = "00小时 00分钟 00秒";
                }else{
                    if(midStatus==2||midStatus==3||midStatus==4){
                        msg.obj = "00小时 00分钟 00秒";
                    }else{
                        msg.obj = hh + "小时" + mm + "分钟" + ss + "秒";
                    }

                }

                getHandler().sendMessage(msg);
            }
        };
        timer.schedule(task,1000,1000);
    }
    @Override
    public void init() {
        pull_to_refresh_list = findViewById(R.id.pull_to_refresh_list);
        home_list = findViewById(R.id.home_list);
//        pull_to_refresh_list.setOnHeaderRefreshListener(listHeadListener);
        pull_to_refresh_list.setOnFooterRefreshListener(listFootListener);
        pull_to_refresh_list.setEnablePullTorefresh(false);
        gallery_bottom = findViewById(R.id.gallery_bottom);
        home_icon_txt = findViewById(R.id.home_icon_txt);
        home_icon_tag = findViewById(R.id.home_icon_tag);
        item_left0 = findViewById(R.id.item_left0);
        item_left1 = findViewById(R.id.item_left1);
        item_left2 = findViewById(R.id.item_left2);
        item_left3 = findViewById(R.id.item_left3);
        item_left4 = findViewById(R.id.item_left4);
        item_left5 = findViewById(R.id.item_left5);
        item_left6 = findViewById(R.id.item_left6);


        home_refre = findViewById(R.id.home_refre);
        home_refre.setOnRefreshListener(listener);
        home_icon_btn0 = findViewById(R.id.home_icon_btn0);
        home_icon_btn1 = findViewById(R.id.home_icon_btn1);
        home_icon_btn2 = findViewById(R.id.home_icon_btn2);
        gallery_content = findViewById(R.id.gallery_content);
        gallery_content.post(new Runnable() {
            @Override
            public void run() {
                double height = gallery_content.getHeight();
                double fix = 135.0 / 80.0;
                double width = height * fix;
                //  int count = 10;
                // int temp = FileUtils.dip2px(HomeActivity.this, 10)*(count-2);
                // gallery_bottom.setLayoutParams(new LinearLayout.LayoutParams((int)((count*width)+temp),(int)height));
                homeEcoAdapter = new HomeEcoAdapter(HomeActivity.this, (int) width, (int) height,videoBeans,getHandler());
                gallery_bottom.setAdapter(homeEcoAdapter);
                gallery_bottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Message msg = new Message();
                        msg.what = 7;
                        msg.obj = videoBeans.get(i);
                        getHandler().sendMessage(msg);
                    }
                });

            }
        });
        home_list.post(new Runnable() {
            @Override
            public void run() {
                double width = home_list.getMeasuredWidth();
                double fix = 45.0 / 272.0;
                double height = width * fix;
                homeListAdapter = new HomeListAdapter(HomeActivity.this, (int) width, (int) height, onlineLists, getHandler());
                home_list.setAdapter(homeListAdapter);
                debugList(home_list,home_refre);
                home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });

            }
        });
        home_icon_btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyBikeActivity.class);
                startActivity(intent);
            }
        });
        home_icon_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DrillActivity.class);
                startActivity(intent);
            }
        });
        home_icon_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SportActivity.class);
                startActivity(intent);
            }
        });
        home_icon_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        home_icon_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PersonCenterActivity.class);
                startActivity(intent);
            }
        });


        CURRENT_LIST_PAGE = 1;
        onlineLists.clear();
        getOnlineList();
        getNewVideo();
    }

    private ArrayList<VideoBean> videoBeans = new ArrayList<>();
    private void getNewVideo(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cls", "Sys.PlayVideo");
            jsonObject.put("method", "LoadPlayVideo");
            jsonObject.put("toKen", sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("pageSize", Common.SHOW_NUM);
            object.put("pageIndex", 1);
            object.put("app", "");
            object.put("column","EditTime");
            object.put("orderByType","desc");

            jsonObject.put("param", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {
                    videoBeans.clear();
                    String result = data.getString("Result");
                    JSONArray jsonArray = new JSONArray(result);
                    int len = jsonArray.length();

                    for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        VideoBean bean = new VideoBean();
                        bean.setImage(object.getString("TitleUrl"));
                        bean.setUrl(object.getString("Url"));
                        bean.setTitle(object.getString("Title"));
                        bean.setTime(object.getString("LongTime"));
                        bean.setId(object.getInt("Id"));
                        videoBeans.add(bean);
                    }
                    getHandler().sendEmptyMessage(6);

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
        },false);
    }


}
