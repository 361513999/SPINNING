package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;

/**
 * Created by cloor on 2018/1/4.
 */

public class PlayOnlineActivity extends BaseActivity {
    @Override
    public void process(Message msg) {

    }

    @Override
    public void init() {
        suf = findViewById(R.id.suf);
        control = findViewById(R.id.control);
        mediaPlayer = new AliVcMediaPlayer(PlayOnlineActivity.this,suf);

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
            }
        });
        showLimite();
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
    private int PLAY_TAG = 0;
    String url = "rtmp://live.jw100.com/fitnow/mingrizhizi?auth_key=1514998777-0-0-37884c721a2b55b182a01c9b91bc3803";
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
