package com.hhkj.spinning.www.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.PlayOnlinePerson;
import com.hhkj.spinning.www.bean.VideoBean;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.media.NEMediaController;
import com.hhkj.spinning.www.media.NEVideoView;
import com.hhkj.spinning.www.utils.ClientManager;
import com.hhkj.spinning.www.widget.NewToast;
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
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.constant.NEType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

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
    private boolean NOT_FOUND = true;
    @Override
    public void process(Message msg) {
        switch (msg.what){
            case -1:
                String time = Common.RUN_TIME!=0?Common.RUN_TIME/60+":"+Common.RUN_TIME%60:"00:00";

                bottom_2.setText(time);
                break;
            case 2:
                unnotify();
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
                            return;
                        }

                    }

                    @Override
                    public void onSearchStopped() {

                        if(NOT_FOUND){
                            showForceTurnOnBluetoothDialog();
                        }
                    }

                    @Override
                    public void onSearchCanceled() {

                    }
                });


                ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);

                break;
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
    private LinearLayout over,over1,person_list;
    private RelativeLayout title_layout;
    String onlineId ;
    private View mBuffer;
    private Button control;
    private TextView bottom_0,bottom_1,bottom_2,bottom_3,bottom_4,bottom_5;
    private String play = null;
    @Override
    public void init() {
        bottom_0 = findViewById(R.id.bottom_0);
        bottom_1 = findViewById(R.id.bottom_1);
        bottom_2 = findViewById(R.id.bottom_2);
        bottom_3 = findViewById(R.id.bottom_3);
        bottom_4 = findViewById(R.id.bottom_4);
        bottom_5 = findViewById(R.id.bottom_5);

        over1 = findViewById(R.id.over1);
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,bottom_content.getMeasuredHeight());
                over.setLayoutParams(params);
                over1.setLayoutParams(params);
            }
        });


        //直播操作
        Intent intent = getIntent();
        if(intent.hasExtra("param")){
            String param = intent.getStringExtra("param");
            String temp[] = param.split(";");
            if(temp.length==4){
                play = temp[3];
            }
            onlineId = temp[0];
            title.setText(temp[2]);
            url = temp[1];
//            url = "http://live.jw100.com/111/3.flv";
//
            P.c("直播地址"+url);
        }



        videoView.setMediaType("livestream");
//        videoView.setBufferStrategy(NEType.NELPANTIJITTER); //点播抗抖动
        videoView.setBufferStrategy(NEType.NELPLOWDELAY); //直播低延时
        videoView.setBufferingIndicator(mBuffer);
        videoView.setHardwareDecoder(false);
        videoView.setEnableBackgroundPlay(false);
        videoView.setVideoPath(url);
        videoView.requestFocus();

        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                control.setBackgroundResource(R.drawable.jz_click_pause_selector);
                PLAY_TAG = 1;
               // showLimite();
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
                P.c("播放异常"+i+"--"+i);
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
        videoView.setOnCompletionListener(new NELivePlayer.OnCompletionListener() {
            @Override
            public void onCompletion(NELivePlayer neLivePlayer) {
                //直播结束
                    if(play!=null){
                        Intent inten0 = new Intent(PlayOnlineActivity.this,PlayerActivity.class);
                        VideoBean bean = new VideoBean();
                        bean.setUrl(play);
                        bean.setTitle("点播");
                        inten0.putExtra("param",bean);
                        startActivity(inten0);
                        AppManager.getAppManager().finishActivity(PlayOnlineActivity.this);

                    }
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               // showLimite();
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

    private void unnotify(){
        ClientManager.getClient().unnotify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, new BleUnnotifyResponse() {
            @Override
            public void onResponse(int code) {
                P.c("断开unnotify");
            }
        });
    }

    private int PLAY_TAG = 1;
    String url;
    private NEVideoView videoView;
    private TextView title;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
        RUN = false;
        videoView.destroy();
        unnotify();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(videoView!=null){
            control.setBackgroundResource(R.drawable.jz_click_play_selector);
            PLAY_TAG = 0;
            videoView.pause();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_online_layout);
        try {
            BaseApplication.iMusicService.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(!ClientManager.getClient().isBluetoothOpened()){
            Intent intent  = new Intent(PlayOnlineActivity.this,MyBikeActivity.class);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(PlayOnlineActivity.this);
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
                 //   P.c("status"+status);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String bt_mac = sharedUtils.getStringValue("bt_mac");
            ClientManager.getClient().disconnect(bt_mac);
            AppManager.getAppManager().finishActivity(this);
        }
        return true;

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
                if(videoView!=null){
                    videoView.start();
                }
            }
        }
    };
    private String connect_mac = "";
    private String connect_name = "";
    private boolean RUN = true;
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
                            Intent intent  = new Intent(PlayOnlineActivity.this,MyBikeActivity.class);
                            startActivity(intent);
                            AppManager.getAppManager().finishActivity(PlayOnlineActivity.this);
                        }
                    });
            dlgBluetoothOpen = builder.create();
        }
        dlgBluetoothOpen.show();
    }
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
            P.c("连接状态"+status);
            if(status!=2){
                getHandler().sendEmptyMessage(2);
            }else{
                ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);
                if(videoView!=null){
                    videoView.start();
                }
            }
        }else {
            showForceTurnOnBluetoothDialog();
        }

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
                    return d;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        }
        return 40;
    }
    private    boolean contain2(String input, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        boolean result = m.find();
        return result;
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
                if(contain2(result,"F0B036CA")){
                    //初始化成功
                    doSend();
                }
                if (result.startsWith("F0B136CA")) {
                    //轮经
                    int s = getChar(result,8,2);
                    int g = getChar(result,10,2);
                   // LUNJING = LUNJING = (s*10)+ FileUtils.formatDouble(g/10);

                    /*LUNJING = getVlue();
                    write("F0A236CA92");
                    */
                    // NewToast.makeText(MyBikeActivity.this,(s*10)+g,Common.TTIME).show();

                }
                if(contain2(result,"F0B236CA")){
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

}
