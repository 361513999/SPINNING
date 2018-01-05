package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.PlayOnlineAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;

/**
 * Created by cloor on 2018/1/4.
 */

public class PlayOnlineActivity extends BaseActivity {
    private ListView lists;
    private PlayOnlineAdapter playOnlineAdapter;
    private LinearLayout bottom_content;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void process(Message msg) {
        switch (msg.what){
            case  0:
                showBottom();
                break;
        }
    }
    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void init() {
        swipeRefreshLayout = findViewById(R.id.pull_to_refresh_list);
        swipeRefreshLayout.setOnRefreshListener(listener);

        bottom_content = findViewById(R.id.bottom_content);
        suf = findViewById(R.id.suf);
        control = findViewById(R.id.control);
        mediaPlayer = new AliVcMediaPlayer(PlayOnlineActivity.this,suf);
        lists = findViewById(R.id.lists);
        lists.post(new Runnable() {
            @Override
            public void run() {
                //128   40
                int width = lists.getMeasuredWidth();
                int height = (int) ((40.0/128.0)*width);
                playOnlineAdapter = new PlayOnlineAdapter(PlayOnlineActivity.this,height,getHandler());
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
            mediaPlayer.prepareToPlay(url);

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
        mediaPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
            @Override
            public void onPrepared() {
                P.c("准备完成");
                mediaPlayer.play();
            }
        });
        showLimite();


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
    String url = "https://player.alicdn.com/video/aliyunmedia.mp4";
    private SurfaceView suf;
    private AliVcMediaPlayer mediaPlayer;
    private Button control;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.destroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_online_layout);

    }
}
