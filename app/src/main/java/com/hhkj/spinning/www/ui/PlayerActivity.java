package com.hhkj.spinning.www.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.VideoBean;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.utils.ClientManager;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.zane.androidupnpdemo.Config;
import com.zane.androidupnpdemo.ui.UpnpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by Administrator on 2018/1/3/003.
 */

public class PlayerActivity extends BaseActivity {
    private boolean NOT_FOUND = true;
    @Override
    public void process(Message msg) {
        switch (msg.what){
            case -1:
                String time = Common.RUN_TIME!=0?Common.RUN_TIME/60+":"+Common.RUN_TIME%60:"00:00";

                bottom_2.setText(time);
                break;
            case 0:
                int process = mediaPlayer.getCurrentPosition();
                item1.setProgress(process);
                item0.setText(formatTime(process));
                break;
            case 2:
                ClientManager.getClient().unnotify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
                SearchRequest request = new SearchRequest.Builder()
                        .searchBluetoothLeDevice(2000, 2).build();
                ClientManager.getClient().search(request, new SearchResponse() {
                    @Override
                    public void onSearchStarted() {

                    }

                    @Override
                    public void onDeviceFounded(SearchResult device) {
                        if(device.getName().equals(connect_name)&&device.getAddress().equals(connect_mac)){
                            //存在就开始重连
                            connnectBt(device);
                            //立即停止
                            ClientManager.getClient().stopSearch();
                            NOT_FOUND = false;
                        }

                    }

                    @Override
                    public void onSearchStopped() {

                        if(NOT_FOUND){
                        //在这里进行提醒操作

                            showForceTurnOnBluetoothDialog();
                        }
                    }

                    @Override
                    public void onSearchCanceled() {

                    }
                });


                ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);

                break;
        }
    }
    private void connnectBt(final SearchResult result){
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
        ClientManager.getClient().connect(result.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if(code==REQUEST_SUCCESS){
                    //
                    P.c("重新连接成功");
                    ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);
                }
            }
        });
    }
    private Timer timer;
    private void doSend(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //在这里进行操作
                P.c("发送数据");
                write("F0A136CA91");
                write("F0A236CA92");
            }
        };
        timer.schedule(task,1000,1000);
    }
    private double getVlue(){
        SharedUtils initUtils  =  new SharedUtils(Common.initMap);
        ArrayList<String> keys =  initUtils.getKeys();
        for(int i=0;i<keys.size();i++){
            if(sharedUtils.getStringValue("bt_name").contains( keys.get(i))){
                double d = 0;
                try {
                    d = Double.parseDouble(initUtils.getStringValue(keys.get(i)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return d;
            }
        }
        return 40;
    }
    private double LUNJING = 40;
    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(Common.UUID_SERVICE) && character.equals(Common.UUID_CHARACTER)) {
                P.c("收到的数据"+ ByteUtils.byteToString(value));
//                title.setText( ByteUtils.byteToString(value));
                LUNJING = getVlue();
                String result = ByteUtils.byteToString(value);
                if(result.startsWith("F0B036CA")){
                    //初始化成功
                    doSend();
                }
                if (result.startsWith("F0B136CA")) {
                    //轮经
                    int s = getChar(result,8,2);
                    int g = getChar(result,10,2);
                   // LUNJING = LUNJING = (s*10)+FileUtils.formatDouble(g/10);

                  /*  LUNJING = getVlue();
                    write("F0A236CA92");*/
                    // NewToast.makeText(MyBikeActivity.this,(s*10)+g,Common.TTIME).show();

                }
                if(result.startsWith("F0B236CA")){
                    //RPM 和心率
                    //F0B236CA00000000A2
                         /*
                         RPM=转速
                         PULSE=心率

                        基本公式：
                            里程=RPM*輪徑
                            速度=里程/时间
                         */
                    int prm =  (getChar(result,8,2)*100)+getChar(result,10,2);
                    double cir =   FileUtils.formatDouble(LUNJING*Math.PI);
                    double sd =FileUtils.formatDouble( cir * prm*60/1000);
                    double lc = FileUtils.formatDouble(prm*LUNJING);
                    double xl = (getChar(result,12,2)*100)+getChar(result,14,2);
                    double weight = 0;
                    double h = Common.RUN_TIME/60.0/60.0;
                    try {
                        weight = Double.parseDouble(sharedUtils.getStringValue("Weight"));
                    }catch (Exception e){
                        weight = 0;
                    }
                    double cal = sd*weight*1.05*h;
                    //Weight      消耗的卡路里（kcal）=时速(km/h)×体重(kg)×1.05×运动时间(h)
                    bottom_0.setText(String.valueOf(xl));
                    bottom_1.setText(sd+" km/h");
                    bottom_3.setText(String.valueOf(cal));
                    bottom_4.setText(String.valueOf(lc));
                    bottom_5.setText(String.valueOf(prm));

                }
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                //开启成功之后就开始发送数据
                write("F0A036CA90");
            } else {

            }
        }
    };
    private void write(String param){
        ClientManager.getClient().write(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER,
                ByteUtils.stringToBytes(param), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    private int getChar(String str,int start,int length){
        StringBuffer buffer = new StringBuffer();
        boolean can = false;
        for(int i=0;i<str.length();i++){
            if(i>=start&&i<(start+length)){
                can = true;
            }else{
                can = false;
            }
            if(can){
                char temp  = str.charAt(i);
                buffer.append(temp);
            }
        }
        return  Integer.parseInt(buffer.toString(),16);
    }
    private int PLAY_TAG= 0;
    private CheckBox item3,item4;
    private Button control;
    private int voice = 0;
    private SeekBar item1;
    private TextView item0,item2,title;
    private LinearLayout upnp_send;
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
    private volatile boolean  TIME_RUNNING = true;
    private VideoBean videoBean;
    private LinearLayout bottom_content,control_layout1,bottom_control;
    private RelativeLayout title_layout;
    private View buffering_prompt;
    private TextView bottom_0,bottom_1,bottom_2,bottom_3,bottom_4,bottom_5;
    @Override
    public void init() {
        buffering_prompt = findViewById(R.id.buffering_prompt);
        title_layout = findViewById(R.id.title_layout);
        bottom_content = findViewById(R.id.bottom_content);
        bottom_control = findViewById(R.id.bottom_control);
        control_layout1 = findViewById(R.id.control_layout1);
        bottom_0 = findViewById(R.id.bottom_0);
        bottom_1 = findViewById(R.id.bottom_1);
        bottom_2 = findViewById(R.id.bottom_2);
        bottom_3 = findViewById(R.id.bottom_3);
        bottom_4 = findViewById(R.id.bottom_4);
        bottom_5 = findViewById(R.id.bottom_5);
        upnp_send = findViewById(R.id.upnp_send);
        suf = findViewById(R.id.suf);
        item1 = findViewById(R.id.item1);
        item1.setEnabled(false);
        item0 = findViewById(R.id.item0);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item4 = findViewById(R.id.item4);
        control = findViewById(R.id.control);
        title = findViewById(R.id.title);
        bottom_content.post(new Runnable() {
            @Override
            public void run() {
                control_layout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,bottom_content.getMeasuredHeight()));

            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra("param")){
            videoBean = (VideoBean) intent.getSerializableExtra("param");



            title.setText(videoBean.getTitle());
            url = FileUtils.addImage(videoBean.getUrl());
            P.c("点播地址"+url);
//
        }

        mediaPlayer = new AliVcMediaPlayer(PlayerActivity.this,suf);
        voice =  mediaPlayer.getVolume();
        if (mediaPlayer != null) {
            mediaPlayer.prepareToPlay(url);

        }
//        showLimite();
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
               showLimite(true);

                return false;
            }
        });
        upnp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                P.c("投屏操作");
                Config.TEST_URL = url;
                Intent intent = new Intent(PlayerActivity.this,UpnpActivity.class);
                intent.putExtra("title",videoBean.getTitle());
                startActivity(intent);

            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showLimite(true);
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
                buffering_prompt.setVisibility(View.GONE);
                P.c("onPrepared");
                click();
                int max = mediaPlayer.getDuration();
                item1.setEnabled(true);
                item1.setMax(mediaPlayer.getDuration());
//                item2.setText(formatTime(max));
                P.c("播放时长"+videoBean.getTime());
                item2.setText(videoBean.getTime());
                if(PLAY_TAG==-1){
                    return;
                }
                control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                PLAY_TAG = 1;
               // mediaPlayer.play();
                //将这个播放改到蓝牙连接成功处
                showLimite(false);
                new Thread() {
                    public void run() {

                        while (true) {
                            if(mediaPlayer.isPlaying()){
                                getHandler().sendEmptyMessage(0);
                            }
                           /* if(TIME_RUNNING){
                                if(mediaPlayer.isPlaying()){
                                    getHandler().sendEmptyMessage(0);
                                }

                            }else{
                                if(mediaPlayer.isPlaying()){
                                    TIME_RUNNING = true;
                                }
                            }*/
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
                TIME_RUNNING = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int process = seekBar.getProgress();
                mediaPlayer.seekTo(process);
                item0.setText(formatTime(process));
                control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                PLAY_TAG = 1;
                mediaPlayer.play();
              //  TIME_RUNNING = true;
                showLimite(true);
            }
        });

        mediaPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
            @Override
            public void onCompleted() {
                control.setBackgroundResource(R.drawable.jz_click_replay_selector);
                PLAY_TAG = -1;
                showLimite(true);
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
    private void click(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.PlayVideo");
            jsonObject.put("method","SetPalyCount");
            JSONObject object = new JSONObject();
            object.put("Id",videoBean.getId());
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


    private CountDownTimer countDownTimer;
    private void showLimite(boolean flag){
        if(flag){
            suf.setEnabled(false);
            control.setVisibility(View.VISIBLE);
            bottom_control.setVisibility(View.VISIBLE);
            title_layout.setVisibility(View.VISIBLE);
            //只关闭不提前显示
        }

         //
        if(countDownTimer==null){
            countDownTimer = new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    suf.setEnabled(true);
                    bottom_control.setVisibility(View.GONE);
                    title_layout.setVisibility(View.GONE);
                    control.setVisibility(View.GONE);
                }
            };
        }

        countDownTimer.cancel();
        countDownTimer.start();

    }
//    String url = "https://player.alicdn.com/video/aliyunmedia.mp4";
    String url = "rtmp://live.jw100.com/111/3";
    private SurfaceView suf;

    private AliVcMediaPlayer mediaPlayer;
    private boolean RUN = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        try {
            BaseApplication.iMusicService.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(!ClientManager.getClient().isBluetoothOpened()){
           Intent intent  = new Intent(PlayerActivity.this,MyBikeActivity.class);
           startActivity(intent);
            AppManager.getAppManager().finishActivity(PlayerActivity.this);
        }
            time();
    }
    private void time(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (RUN){
                    //蓝牙时间操作
                    int status = ClientManager.getClient().getConnectStatus(sharedUtils.getStringValue("bt_mac"));

                    if(status==2){
                        Common.RUN_TIME++;
                    }else{
                        Common.RUN_TIME = 0;
                    }
                    getHandler().sendEmptyMessage(-1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            BluetoothLog.v(String.format("CharacterActivity.onConnectStatusChanged status = %d", status));

            if (status == STATUS_DISCONNECTED) {
                //断开连接
                P.c("连接失败");

            }else if(status==STATUS_CONNECTED){
                P.c("连接中");
                if(mediaPlayer!=null){
                    mediaPlayer.play();
                }
            }
        }
    };
    private String connect_mac = "";
    private String connect_name = "";
    @Override
    protected void onResume() {
        super.onResume();
        if(!RUN){
            RUN = true;
            time();
        }
        connect_mac = sharedUtils.getStringValue("bt_mac");
        connect_name = sharedUtils.getStringValue("bt_name");
        if(connect_mac.length()!=0){
            ClientManager.getClient().registerConnectStatusListener(connect_mac, mConnectStatusListener);
            int status = ClientManager.getClient().getConnectStatus(connect_mac);
          //  P.c("连接状态"+status);
            if(status!=2){
                getHandler().sendEmptyMessage(2);
            }else{
                ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);
                if(mediaPlayer!=null){
                    mediaPlayer.play();
                }
            }
        }else {
            showForceTurnOnBluetoothDialog();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String bt_mac = sharedUtils.getStringValue("bt_mac");
            ClientManager.getClient().disconnect(bt_mac);
            AppManager.getAppManager().finishActivity(this);
        }
        return true;

    }
    private AlertDialog dlgBluetoothOpen;

    private void showForceTurnOnBluetoothDialog() {
        if (dlgBluetoothOpen == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("请先连接设备");
            //builder.setNegativeButton("取消", null);
            builder.setPositiveButton("好的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //查询蓝牙情况
                            Intent intent  = new Intent(PlayerActivity.this,MyBikeActivity.class);
                            startActivity(intent);
                            AppManager.getAppManager().finishActivity(PlayerActivity.this);
                        }
                    });
            dlgBluetoothOpen = builder.create();
        }
        dlgBluetoothOpen.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
            control.setBackgroundResource(R.drawable.jz_click_play_selector);
            PLAY_TAG = 0;
            mediaPlayer.pause();
        }
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        if(connect_mac.length()!=0) {
            ClientManager.getClient().unregisterConnectStatusListener(connect_mac, mConnectStatusListener);
        }
        RUN = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.destroy();
        }
        RUN = false;

    }
}
