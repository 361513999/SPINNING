package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
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
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.CircleImageView;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ISRUN = false;
    }

    private volatile   boolean ISRUN = true;
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
    }
    private void getNormal(final HomeOnlineList online){

        cancleLoadView();
        ImageLoader.getInstance().displayImage(FileUtils.addImage(online.getImage()), item_left0);
        ImageLoader.getInstance().displayImage(FileUtils.addImage(online.getIco()), item_left1);
        item_left2.setText(online.getName());
        item_left3.setText(online.getPlayTime() + " 分钟");
        item_left4.setText(online.getTitle());
        if (online.getStatus() == 0) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    long from = System.currentTimeMillis();
                    long to = TimeUtil.parseTime_(online.getBeginTime());
                    long minutes = (to - from) / 1000;
                    ISRUN = true;
                    P.c("计算未开始"+minutes);
                    time(minutes,0);
                }
            }.start();


            item_left6.setText("未开始");
        } else if (online.getStatus() == 1||online.getStatus() == 2) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    long from = System.currentTimeMillis();
//                                long to = TimeUtil.parseTime_(online.getBeginTime())+(1000*60*online.getPlayTime());
                    long to = TimeUtil.getFetureSec(online.getPlayTime());
                    P.c(TimeUtil.getTime(TimeUtil.parseTime_(online.getBeginTime()))+"=="+TimeUtil.getTime(to));
                    long minutes = (to - from) / 1000;
                    ISRUN = true;
                    P.c(online.getPlayTime()+"计算已开始"+minutes);
                    time(minutes,1);
                }
            }.start();
            item_left6.setText(online.getStatus()==1?"正在直播":"暂时停播");
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
    @Override
    public void process(Message msg) {
        switch (msg.what) {
            case -3:
                final int index = msg.arg1;
                final HomeOnlineList list = (HomeOnlineList) msg.obj;
                ISRUN = false;
                showLoadView();
                item_left6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        home_list.setItemChecked(index,true);
                        getNormal(list);
                    }
                },1200);

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

                                getHandler().sendEmptyMessage(5);
                                return;
                            }

                            JSONArray jsonArray = new JSONArray(result);
                            int len = jsonArray.length();
                            if (len != 0) {
                                Message msg = new Message();
                                msg.what = 3;
                                msg.obj = online.getId() + ";" + jsonArray.getString(0) + ";" + online.getTitle();
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
                String param = (String) msg.obj;
                Intent intent = new Intent(HomeActivity.this, PlayOnlineActivity.class);
                intent.putExtra("param", param);
                startActivity(intent);

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
        ISRUN = true;
        home_icon_txt.setText(sharedUtils.getStringValue("userName"));
        ImageLoader.getInstance().displayImage(FileUtils.addImage(sharedUtils.getStringValue("icon")), home_icon_tag);
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
            ISRUN = false;
            item_left6.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOnlineList();
                }
            },1200);

        }
    };

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

            }
        });
        home_list.post(new Runnable() {
            @Override
            public void run() {
                double width = home_list.getMeasuredWidth();
                double fix = 42.0 / 272.0;
                double height = width * fix;
                homeListAdapter = new HomeListAdapter(HomeActivity.this, (int) width, (int) height, onlineLists, getHandler());
                home_list.setAdapter(homeListAdapter);
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
