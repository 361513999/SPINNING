package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;

/**
 * Created by Administrator on 2018/1/3/003.
 */

public class PlayerActivity extends BaseActivity {
    @Override
    public void process(Message msg) {
        switch (msg.what){
            case 0:
                int process = mediaPlayer.getCurrentPosition();
                item1.setProgress(process);
                item0.setText(formatTime(process));
                break;
        }
    }
    private int PLAY_TAG= 0;
    private CheckBox item3;
    private Button control;
    private int voice = 0;
    private SeekBar item1;
    private TextView item0,item2,title;
    /*
* 毫秒转化
*/
    private   String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strMinute + ":" + strSecond;
    }
    @Override
    public void init() {
        suf = findViewById(R.id.suf);
        item1 = findViewById(R.id.item1);
        item1.setEnabled(false);
        item0 = findViewById(R.id.item0);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        control = findViewById(R.id.control);
        title = findViewById(R.id.title);
        Intent intent = getIntent();
        if(intent.hasExtra("param")){
            String param = intent.getStringExtra("param");

            String temp[] = param.split(";");

            title.setText(temp[0]);
            url = temp[1];
            P.c("点播地址"+url);
//
        }

        mediaPlayer = new AliVcMediaPlayer(PlayerActivity.this,suf);
        voice =  mediaPlayer.getVolume();
        if (mediaPlayer != null) {
            mediaPlayer.prepareToPlay(url);

        }
        showLimite();
//        sumSeek();
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

        mediaPlayer.setBufferingUpdateListener(new MediaPlayer.MediaPlayerBufferingUpdateListener() {
            @Override
            public void onBufferingUpdateListener(int i) {


            }
        });
        mediaPlayer.setSeekCompleteListener(new MediaPlayer.MediaPlayerSeekCompleteListener() {
            @Override
            public void onSeekCompleted() {

            }
        });
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
                    case -1:
                        mediaPlayer.seekTo(0);
                        item0.setText(formatTime(0));
                        control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                        PLAY_TAG = 1;
                        mediaPlayer.play();
                        break;
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
                //准备完毕可以进行seekbar释放
                P.c("onPrepared");

                int max = mediaPlayer.getDuration();
                item1.setEnabled(true);
                item1.setMax(mediaPlayer.getDuration());
                item2.setText(formatTime(max));
                if(PLAY_TAG==-1){
                    return;
                }
                new Thread() {
                    public void run() {

                        while (true) {
                            if(mediaPlayer.isPlaying()){
                                getHandler().sendEmptyMessage(0);
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    };

                }.start();

            }
        });
        item1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int process = seekBar.getProgress();
                mediaPlayer.seekTo(process);
                item0.setText(formatTime(process));
                control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                PLAY_TAG = 1;
                mediaPlayer.play();
                showLimite();
            }
        });

        mediaPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
            @Override
            public void onCompleted() {
                control.setBackgroundResource(R.drawable.jz_click_replay_selector);
                PLAY_TAG = -1;
                showLimite();
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.prepareToPlay(url);
            }
        });
        mediaPlayer.setVideoSizeChangeListener(new MediaPlayer.MediaPlayerVideoSizeChangeListener() {
            @Override
            public void onVideoSizeChange(int i, int i1) {

            }
        });

        item3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mediaPlayer.setVolume(0);
                }else{
                    mediaPlayer.setVolume(voice);
                }
            }
        });



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
    String url = "https://player.alicdn.com/video/aliyunmedia.mp4";
    private SurfaceView suf;
    private AliVcMediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);

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
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.destroy();
        }
    }
}
