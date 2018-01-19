package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.PlayOnlineAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.PlayOnlinePerson;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.media.NEMediaController;
import com.hhkj.spinning.www.media.NEVideoView;
import com.hhkj.spinning.www.widget.NewToast;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.constant.NEType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloor on 2018/1/4.
 */

public class PlayOnlineActivity extends BaseActivity {
    private ListView lists;
    private PlayOnlineAdapter playOnlineAdapter;
    private LinearLayout bottom_content;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void backActivity(View v) {
        super.backActivity(v);

        exitOnline();
    }

    @Override
    public void process(Message msg) {
        switch (msg.what){
            case  0:

//                showBottom();
                break;
            case 1:
                playOnlineAdapter.updata(personArrayList);
                break;
        }
    }
    private volatile boolean isRun = true;
    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    private LinearLayout over,person_list;
    private RelativeLayout title_layout;
    String onlineId ;
    private View mBuffer;
    private Button control;
    @Override
    public void init() {
        over = findViewById(R.id.over);
        title = findViewById(R.id.title);
        title_layout = findViewById(R.id.title_layout);
        person_list = findViewById(R.id.person_list);
        swipeRefreshLayout = findViewById(R.id.pull_to_refresh_list);
        swipeRefreshLayout.setOnRefreshListener(listener);
        bottom_content = findViewById(R.id.bottom_content);
        videoView = findViewById(R.id.video_view);
        mBuffer = findViewById(R.id.buffering_prompt);
        control = findViewById(R.id.control);
        lists = findViewById(R.id.lists);
        bottom_content.post(new Runnable() {
            @Override
            public void run() {
                over.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,bottom_content.getMeasuredHeight()));
            }
        });


        //直播操作
        Intent intent = getIntent();
        if(intent.hasExtra("param")){
            String param = intent.getStringExtra("param");
            String temp[] = param.split(";");
            onlineId = temp[0];
            title.setText(temp[2]);
            url = temp[1];
//            url = "http://live.jw100.com/111/3.flv";
//
            P.c("直播地址"+url);
        }

      //  url = "rtmp://v68f25ff4.live.126.net/live/11371c6d02574bd4b20f38c1a2312282";

        videoView.setMediaType("livestream");
//        videoView.setBufferStrategy(NEType.NELPANTIJITTER); //点播抗抖动
        videoView.setBufferStrategy(NEType.NELPLOWDELAY); //直播低延时
        videoView.setBufferingIndicator(mBuffer);
        videoView.setHardwareDecoder(false);
        videoView.setEnableBackgroundPlay(false);
        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                PLAY_TAG = 1;
                showLimite();
                new CountDownTimer(3000,1000){

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        over.setVisibility(View.GONE);
                        person_list.setVisibility(View.GONE);
                        title_layout.setVisibility(View.GONE);
                    }
                }.start();
            }
        });

        videoView.setOnVideoParseErrorListener(new NELivePlayer.OnVideoParseErrorListener() {
            @Override
            public void onVideoParseError(NELivePlayer neLivePlayer) {
                control.setBackgroundResource(R.drawable.jz_click_replay_selector);
                PLAY_TAG = -1;
                NewToast.makeText(PlayOnlineActivity.this,"直播异常,请重试", Common.TTIME).show();
                showLimite();

            }
        });
        videoView.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                P.c("播放异常");
                control.setBackgroundResource(R.drawable.jz_click_replay_selector);
                PLAY_TAG = -1;
                NewToast.makeText(PlayOnlineActivity.this,"直播异常,请重试", Common.TTIME).show();
                showLimite();

                return true;
            }
        });
        videoView.setOnVideoParseErrorListener(new NELivePlayer.OnVideoParseErrorListener() {
            @Override
            public void onVideoParseError(NELivePlayer neLivePlayer) {
                P.c("解析异常");
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showLimite();
                if(over.getVisibility()==View.VISIBLE){
                    over.setVisibility(View.GONE);
                    person_list.setVisibility(View.GONE);
                    title_layout.setVisibility(View.GONE);
                }else{
                    over.setVisibility(View.VISIBLE);
                    person_list.setVisibility(View.VISIBLE);
                    title_layout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLimite();


                switch (PLAY_TAG){
                    case -1:

                        control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                        PLAY_TAG = 1;
                        videoView.resume();
                        break;
                    case  0:
                        control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                        PLAY_TAG = 1;
                        videoView.start();
                        break;
                    case 1:
                        control.setBackgroundResource(R.drawable.jz_click_play_selector);
                        PLAY_TAG = 0;
                        videoView.pause();
                        break;
                }
            }
        });
        lists.post(new Runnable() {
            @Override
            public void run() {
                //128   40
                int width = lists.getMeasuredWidth();
                int height = (int) ((40.0/128.0)*width);
                playOnlineAdapter = new PlayOnlineAdapter(PlayOnlineActivity.this,height,getHandler(),personArrayList);
                lists.setAdapter(playOnlineAdapter);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                while (isRun){
                                    addPerson();
                                    getPersonList();
                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();

                    }
                });
            }
        });



    }

    private CountDownTimer countDownTimer;
    private void showLimite(){
        videoView.setEnabled(false);
        control.setVisibility(View.VISIBLE);
        //
        if(countDownTimer==null){
            countDownTimer = new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    videoView.setEnabled(true);
                    control.setVisibility(View.GONE);
                }
            };
        }

        countDownTimer.cancel();
        countDownTimer.start();

    }

    private Handler handler = new Handler();
    private ArrayList<PlayOnlinePerson> personArrayList = new ArrayList<>();
    private void addPerson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cls","Sys.VideoInfo");
            jsonObject.put("method","AddOnlineUser");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("Id",onlineId);
            object.put("Kll","1");
            object.put("Km","2");
            object.put("Sd","3");
            object.put("Xl","4");
            object.put("Kcal",5);
            jsonObject.put("param",object.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {


            }

            @Override
            public void error(String data) {

            }

            @Override
            public void unLogin() {

            }
        },false);
    }
    private void getPersonList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cls","Sys.VideoInfo");
            jsonObject.put("method","GetOnlineUserList");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("Id",onlineId);
            jsonObject.put("param",object.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {

                    String result = data.getString("Result");

                    JSONArray jsonArray = new JSONArray(result);
                    int len  = jsonArray.length();
                    if(len!=0){
                        personArrayList.clear();
                    }
                    for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        PlayOnlinePerson person = new PlayOnlinePerson();
                        person.setIco(object.getString("Url"));
                        person.setName(object.getString("Name"));
                        person.setKcal(object.getString("Kcal"));
                        person.setKll(object.getString("Kll"));
                        person.setKm(object.getString("Km"));
                        person.setSd(object.getString("Sd"));
                        person.setXl(object.getString("Xl"));
                        personArrayList.add(person);

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
        },false);
    }
    private void exitOnline(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cls","Sys.VideoInfo");
            jsonObject.put("method","RemoveOnlineUser");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("Id",onlineId);
            jsonObject.put("param",object.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {

            }

            @Override
            public void error(String data) {

            }

            @Override
            public void unLogin() {

            }
        },false);
    }

    private CountDownTimer bottomTimer;
    private void showBottom(){

        bottom_content.setVisibility(View.VISIBLE);
        //
        if(bottomTimer==null){
            bottomTimer = new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    bottom_content.setVisibility(View.GONE);
                }
            };
        }

        bottomTimer.cancel();
        bottomTimer.start();

    }



    private int PLAY_TAG = 1;
    String url;
    private NEVideoView videoView;
    private TextView title;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;

        videoView.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(videoView!=null){
            control.setBackgroundResource(R.drawable.jz_click_play_selector);
            PLAY_TAG = 0;
            videoView.pause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_online_layout);

    }
}
