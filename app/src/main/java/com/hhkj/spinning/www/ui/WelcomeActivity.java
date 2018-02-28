package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.media.NEVideoView;
import com.hhkj.spinning.www.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2017/12/14/014.
 */

public class WelcomeActivity extends BaseActivity {
    private SurfaceView suf;
    private AliVcMediaPlayer mediaPlayer;
    private TextView enter;






    @Override
    public void init() {


       // time();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        suf = findViewById(R.id.suf);
        mediaPlayer = new AliVcMediaPlayer(WelcomeActivity.this,suf);
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
        File file = new File(Common.SD+ "welcome.flv");
        if(file.exists()){
            getHandler().sendEmptyMessage(1);
        }else{
            showLoadView();
            FileUtils.getInstance(WelcomeActivity.this).copyAssetsToSD("film",Common.DIR).setFileOperateCallback(new FileUtils.FileOperateCallback() {
                @Override
                public void onSuccess() {
                    getHandler().sendEmptyMessage(1);
                    P.c("复制成功");
                }

                @Override
                public void onFailed(String error) {
                    P.c("复制失败"+error);
                }
            });
        }

        mediaPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
            @Override
            public void onCompleted() {
                getHandler().sendEmptyMessage(0);
            }
        });
        enter = findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null){
                    mediaPlayer.pause();
                    mediaPlayer.reset();
                }
                getHandler().sendEmptyMessage(0);
            }
        });


        load();
    }
    private void load(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method","GetUserInfo");
            jsonObject.put("cls","Sys.User");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("number",sharedUtils.getStringValue("phone"));
            jsonObject.put("param",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {

                    JSONObject obj = new JSONObject(data.getString("Result"));
                    sharedUtils.setStringValue("userName",obj.getString("UserName"));
                    sharedUtils.setStringValue("id",obj.getString("Id"));
                    sharedUtils.setStringValue("icon",obj.getString("Url"));
                    sharedUtils.setBooleanValue("Sex",obj.getBoolean("Sex"));
                    sharedUtils.setStringValue("phone",obj.getString("Phone"));
                    sharedUtils.setStringValue("Height",obj.getString("Height"));
                    sharedUtils.setStringValue("Weight",obj.getString("Weight"));
                    sharedUtils.setStringValue("Birthday",obj.getString("Birthday"));
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
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
               /* Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishAllActivity();*/
            }
        },false);
    }

    @Override
    public void process(Message msg) {
        switch (msg.what){
            case 0:
                Intent intent = null;
               if( sharedUtils.getStringValue("token").length()==0){
                     intent = new Intent(WelcomeActivity.this,LoginActivity.class);

               }else{
                     intent = new Intent(WelcomeActivity.this,HomeActivity.class);

               }
                startActivity(intent);
              //  AppManager.getAppManager().finishActivity();
                break;
            case 1:
                cancleLoadView();
                if (mediaPlayer != null) {
                    mediaPlayer.prepareToPlay(Common.SD+ "welcome.flv");
                    // mediaPlayer.prepareAndPlay();
                }
                mediaPlayer.play();
                break;
        }
    }
    private CountDownTimer countDownTimer;
    private void time(){
        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                sendEmptyMessage(0);
            }
        };
        countDownTimer.start();
    }

}
