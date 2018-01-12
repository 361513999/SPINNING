package com.hhkj.spinning.www.ui;

import android.content.Intent;
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
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.PlayOnlineAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.PlayOnlinePerson;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.inter.Result;

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
    private WebView webView;
    @Override
    public void backActivity(View v) {
        super.backActivity(v);

        exitOnline();
    }

    @Override
    public void process(Message msg) {
        switch (msg.what){
            case  0:

                showBottom();
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
    String onlineId ;
    @Override
    public void init() {
        webView = findViewById(R.id.webView);
        title = findViewById(R.id.title);
        swipeRefreshLayout = findViewById(R.id.pull_to_refresh_list);
        swipeRefreshLayout.setOnRefreshListener(listener);
        bottom_content = findViewById(R.id.bottom_content);
        suf = findViewById(R.id.suf);
        control = findViewById(R.id.control);
        mediaPlayer = new AliVcMediaPlayer(PlayOnlineActivity.this,suf);

//        mediaPlayer.setMediaType(MediaPlayer.MediaType.Live);
        mediaPlayer.setInfoListener(new MediaPlayer.MediaPlayerInfoListener() {
            @Override
            public void onInfo(int i, int i1) {

            }
        });
   /*     Intent intent0 = new Intent(PlayOnlineActivity.this,CommonWeb.class);
//        intent0.putExtra("url","http://admin.pooboofit.com/index.html");
        intent0.putExtra("url","http://player.alicdn.com/demo/live/pc.html?key=1");
        startActivity(intent0);*/
        lists = findViewById(R.id.lists);

        Intent intent = getIntent();
        if(intent.hasExtra("param")){
            String param = intent.getStringExtra("param");

            String temp[] = param.split(";");
            onlineId = temp[0];
           // url = "rtmp://live.jw100.com/fitnow/123?auth_key=1515682074-0-0-e297fe01d236acdb8d9ad58e640f2d87";
            title.setText(temp[2]);
            url = temp[1];
            P.c("拉流地址"+url);
//
        }
        url = "rtmp://live.jw100.com/111/3";
//        url = "rtmp://live.jw100.com/fitnow/123?auth_key=1515652417-0-0-32be550eee35893b7c38cd7b6fec83aa";
        lists.post(new Runnable() {
            @Override
            public void run() {
                //128   40
                int width = lists.getMeasuredWidth();
                int height = (int) ((40.0/128.0)*width);
                playOnlineAdapter = new PlayOnlineAdapter(PlayOnlineActivity.this,height,getHandler(),personArrayList);
                lists.setAdapter(playOnlineAdapter);

            }
        });
        suf.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if(mediaPlayer!=null){
                    mediaPlayer.setVideoSurface(surfaceHolder.getSurface());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (mediaPlayer != null) {
                    mediaPlayer.setSurfaceChanged();
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        if (mediaPlayer != null) {
            mediaPlayer.prepareAndPlay(url);


        }

        suf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showLimite();

                return false;
            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLimite();


                switch (PLAY_TAG){

                    case  0:
                        control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                        PLAY_TAG = 1;
                        mediaPlayer.play();
                        break;
                    case 1:
                        control.setBackgroundResource(R.drawable.jz_click_play_selector);
                        PLAY_TAG = 0;
                        mediaPlayer.pause();
                        break;
                }
            }
        });

        mediaPlayer.enableNativeLog();
        mediaPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
            @Override
            public void onPrepared() {
                P.c("准备完成");
                mediaPlayer.setVolume(100);
                mediaPlayer.play();

            }
        });
        mediaPlayer.setPcmDataListener(new MediaPlayer.MediaPlayerPcmDataListener() {
            @Override
            public void onPcmData(byte[] bytes, int i) {
                P.c(i+"~~~~");
            }
        });

        mediaPlayer.setErrorListener(new MediaPlayer.MediaPlayerErrorListener() {
            @Override
            public void onError(int i, String s) {
                P.c(i+"视频错误"+s);
            }
        });
        showLimite();


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


    private CountDownTimer countDownTimer;
    private void showLimite(){
        suf.setEnabled(false);
        control.setVisibility(View.VISIBLE);
        //
        if(countDownTimer==null){
            countDownTimer = new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    suf.setEnabled(true);
                    control.setVisibility(View.GONE);
                }
            };
        }

        countDownTimer.cancel();
        countDownTimer.start();

    }
    private int PLAY_TAG = 1;
    String url;
    private SurfaceView suf;
    private AliVcMediaPlayer mediaPlayer;
    private Button control;
    private TextView title;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.destroy();
        }
        isRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
            control.setBackgroundResource(R.drawable.jz_click_play_selector);
            PLAY_TAG = 0;
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_online_layout);

    }
}
