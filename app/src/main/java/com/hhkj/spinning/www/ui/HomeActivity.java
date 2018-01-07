package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.HomeEcoAdapter;
import com.hhkj.spinning.www.adapter.HomeListAdapter;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.HomeOnlineList;
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
    private LinearLayout home_icon_btn0,home_icon_btn1,home_icon_btn2,gallery_content;
    private PullToRefreshView pull_to_refresh_list;
    private ListView home_list;
    private SwipeRefreshLayout home_refre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
    }

    @Override
    public void process(Message msg) {
            switch (msg.what){
                case 1:
                    if(home_refre!=null){
                        home_refre.setRefreshing(false);
                    }
                      homeListAdapter.updata(onlineLists);
                    break;
                case 2:
                    final HomeOnlineList online = (HomeOnlineList) msg.obj;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("toKen",sharedUtils.getStringValue("token"));
                        jsonObject.put("cls","Sys.VideoInfo");
                        jsonObject.put("method","GetPlayUrl");
                        JSONObject object = new JSONObject();
                        object.put("Id",online.getId());
                        jsonObject.put("param",object.toString());

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
                                if(len!=0){
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.obj = online.getId()+";"+jsonArray.getString(0)+";"+online.getTitle();
                                    getHandler().sendMessage(msg);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void error(String data) {
                                NewToast.makeText(HomeActivity.this,data,Common.TTIME).show();
                        }

                        @Override
                        public void unLogin() {

                        }
                    });
                    break;
                case 3:
                    String param = (String) msg.obj;
                    Intent intent = new Intent(HomeActivity.this,PlayOnlineActivity.class);
                    intent.putExtra("param",param);
                    startActivity(intent);

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
        home_icon_txt.setText(sharedUtils.getStringValue("userName"));
        ImageLoader.getInstance().displayImage(FileUtils.addImage(sharedUtils.getStringValue("icon")),home_icon_tag);
    }
    private boolean CURRENT_LIST_MORE = true;
    private int CURRENT_LIST_PAGE = 1;
    private ArrayList<HomeOnlineList> onlineLists = new ArrayList<>();
    private void getOnlineList(){
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.VideoInfo");
            jsonObject.put("method","LoadVideoInfo");
            JSONObject object = new JSONObject();
            object.put("pageSize",Common.SHOW_NUM);
            object.put("pageIndex",CURRENT_LIST_PAGE);
            object.put("app","");
            jsonObject.put("param",object.toString());
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
//                        getHandler().sendEmptyMessage(2);
                    } else {
                        CURRENT_LIST_PAGE = CURRENT_LIST_PAGE + 1;
                    }
                    for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        HomeOnlineList onlineList = new HomeOnlineList();
                        onlineList.setId(object.getString("Id"));
                        onlineList.setIco(FileUtils.addImage(object.getString("UserUrl")));
                        onlineList.setTitle(object.getString("Title"));
                        onlineList.setStartTime(TimeUtil.getTimeHome(TimeUtil.parseTime_(object.getString("BeginTime"))));
                        onlineList.setTime(object.getString("PlayLongTime"));
                        onlineList.setName(object.getString("UserName"));
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
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
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
            getOnlineList();
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
       //
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
               double fix = 135.0/80.0;
               double width = height*fix;
              //  int count = 10;
               // int temp = FileUtils.dip2px(HomeActivity.this, 10)*(count-2);
               // gallery_bottom.setLayoutParams(new LinearLayout.LayoutParams((int)((count*width)+temp),(int)height));
                homeEcoAdapter = new HomeEcoAdapter(HomeActivity.this,(int)width,(int)height);
               gallery_bottom.setAdapter(homeEcoAdapter);

            }
        });
        home_list.post(new Runnable() {
            @Override
            public void run() {
                double width = home_list.getMeasuredWidth();
                double fix = 42.0/272.0;
                double height = width*fix;
                homeListAdapter = new HomeListAdapter(HomeActivity.this,(int)width,(int)height,onlineLists,getHandler());
                home_list.setAdapter(homeListAdapter);

            }
        });
        home_icon_btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MyBikeActivity.class);
                startActivity(intent);
            }
        });
        home_icon_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,DrillActivity.class);
                startActivity(intent);
            }
        });
        home_icon_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SportActivity.class);
                startActivity(intent);
            }
        });
        home_icon_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        home_icon_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PersonCenterActivity.class);
                startActivity(intent);
            }
        });
        if(CURRENT_LIST_PAGE==1){

            getOnlineList();
        }

    }
}
