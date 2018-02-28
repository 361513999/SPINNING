package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.SportListAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.SportList;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.U;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/22/022.
 */

public class JpActivity extends BaseActivity {
    private ListView sport_list;
    private LinearLayout list_content;
    private PullToRefreshView pull_to_refresh_list;
    private SportListAdapter sportListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_layout);
        list_content = findViewById(R.id.list_content);
        sport_list = findViewById(R.id.sport_list);
        pull_to_refresh_list = findViewById(R.id.pull_to_refresh_list);
        pull_to_refresh_list.setOnHeaderRefreshListener(listHeadListener);
        pull_to_refresh_list.setOnFooterRefreshListener(listFootListener);
        list_content.post(new Runnable() {
            @Override
            public void run() {
                int width = list_content.getMeasuredWidth();
                double fix = 90.0/505.0;
                int height = (int) (width*fix);
                sportListAdapter = new SportListAdapter(JpActivity.this,width,height,sportLists);
                sport_list.setAdapter(sportListAdapter);
                sport_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(JpActivity.this,CommonWeb.class);
                        intent.putExtra("title",sportLists.get(i).getName());
                        intent.putExtra("url", U.BASE_IMAGE+"ueditor/WriteHtml.html?Id="+sportLists.get(i).getId());
                        startActivity(intent);
                    }
                });
            }
        });
        getListById();
    }

    @Override
    public void process(Message msg) {
    switch (msg.what){
        case 1:
            sportListAdapter.updata(sportLists);
            break;
    }
    }


    @Override
    public void init() {

    }
    private final int runTime = 400;
    private PullToRefreshView.OnHeaderRefreshListener listHeadListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onHeaderRefreshComplete();
                    CURRENT_LIST_PAGE = 1;
                    sportLists.clear();
                    getListById();

                }
            }, runTime);
        }
    };
    private boolean CURRENT_LIST_MORE = true;
    private int CURRENT_LIST_PAGE = 1;
    private ArrayList<SportList> sportLists = new ArrayList<>();

    private void getListById(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.Sport");
            jsonObject.put("method","LoadSport");
            JSONObject object = new JSONObject();
            object.put("pageSize", Common.SHOW_NUM);
            object.put("pageIndex", CURRENT_LIST_PAGE);
            object.put("app", "");
            object.put("type","1");
            jsonObject.put("param", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        httpPost("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {
                    String result = data.getString("Result");
                    JSONArray jsonArray  = new JSONArray(result);
                    int len = jsonArray.length();
                    if (len < Common.SHOW_NUM) {
                        CURRENT_LIST_MORE = false;
                        //  getHandler().sendEmptyMessage(-2);
                    } else {
                        CURRENT_LIST_PAGE = CURRENT_LIST_PAGE + 1;
                    }
                    for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        SportList list = new SportList();
                        list.setId(object.getString("Id"));
                        list.setImage(object.getString("TitleUrl"));
                        list.setName(object.getString("Title"));
                        list.setTip(object.getString("Remark"));
                        sportLists.add(list);
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

            }
        });
    }
    private PullToRefreshView.OnFooterRefreshListener listFootListener = new PullToRefreshView.OnFooterRefreshListener() {
        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            pull_to_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_to_refresh_list.onFooterRefreshComplete();
                    if (CURRENT_LIST_MORE) {
                                getListById();
                    } else {
                        NewToast.makeText(JpActivity.this, "没有数据可加载", Common.TTIME).show();

                    }

                }
            }, runTime);
        }
    };
}
