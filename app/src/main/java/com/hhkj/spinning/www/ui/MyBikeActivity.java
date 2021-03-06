package com.hhkj.spinning.www.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.db.DB;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.utils.ClientManager;
import com.hhkj.spinning.www.widget.ColorArcProgressBar;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by Administrator on 2017/12/20/020.
 */

public class MyBikeActivity extends BaseActivity {
    private ColorArcProgressBar bike_cicle;
    private TextView bt_click;
    private AlertDialog dlgBluetoothOpen;

    private void showForceTurnOnBluetoothDialog() {
        if (dlgBluetoothOpen == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("打开蓝牙");
            builder.setNegativeButton("拒绝", null);
            builder.setPositiveButton("好的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //查询蓝牙情况
                            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(intent);
                        }
                    });
            dlgBluetoothOpen = builder.create();
        }
        dlgBluetoothOpen.show();
    }
    private boolean RUN = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bike_layout);

        if(!ClientManager.getClient().isBluetoothOpened()){
            showForceTurnOnBluetoothDialog();
        }
    time();

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

    private void time(){
        new Thread(){
            @Override
            public void run() {
                super.run();

                while (RUN){
                    //蓝牙时间操作
                    int status = ClientManager.getClient().getConnectStatus(sharedUtils.getStringValue("bt_mac"));
//                    P.c("status"+status);
                    if(status==2){
                        Common.RUN_TIME++;
                    }else{
                        Common.RUN_TIME = 0;
                    }
                    getHandler().sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private boolean NOT_FOUND = true;
    @Override
    public void process(Message msg) {
        switch (msg.what){
            case 0:
                String time = Common.RUN_TIME!=0?Common.RUN_TIME/60+":"+Common.RUN_TIME%60:"00:00";
                if(canTime){
                   bottom_2.setText(time);
                   bottom_2.setTag(time);
                }


                break;
            case 1:
                CenterItem1Edit edit = (CenterItem1Edit) msg.obj;
                if(edit!=null){
                    float max = Float.valueOf(edit.getTog());
                    bike_cicle.setMaxValues(max);
                    bike_cicle.setUnit("目标"+edit.getTog()+"CAL");
                    bike_cicle.setTitle("进行中");


                }else{
                    bike_cicle.setMaxValues(0);
                    bike_cicle.setUnit("目标"+0+"CAL");
                    bike_cicle.setTitle("无最新目标");
                }
                break;
            case 2:
                //连接


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
                            connent_status.setText("未找到");
                        }
                    }

                    @Override
                    public void onSearchCanceled() {

                    }
                });


                //ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);

                break;
            case 3:
                Intent intent =new Intent(MyBikeActivity.this,BtListActivity.class);
                P.c("实际"+bottom_0.getTag());
                if(bottom_0.getTag()!=null){
                    intent.putExtra("xl",bottom_0.getTag().toString());
                    intent.putExtra("sd",bottom_1.getTag().toString());
                    intent.putExtra("sj",bottom_2.getTag().toString());
                    intent.putExtra("cal",bottom_3.getTag().toString());
                    intent.putExtra("lc",bottom_4.getTag().toString());
                    intent.putExtra("zlc",bottom_5.getTag().toString());
                }
                startActivityForResult(intent,100);
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
                   // ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);
                }
            }
        });
    }



    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager)  getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    private void getInit(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.ParamSet");
            jsonObject.put("method","GetData");
            jsonObject.put("param","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpPostSON("Post", jsonObject.toString(), new Result() {
            @Override
            public void success(JSONObject data) {
                try {
                    String result = data.getString("Result");

                    JSONArray jsonArray =   new JSONArray(result);
                    int len =  jsonArray.length();
                    SharedUtils sharedUtils = new SharedUtils(Common.initMap);
                    sharedUtils.clear();
                    for(int i=0;i<len;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        sharedUtils.setStringValue(object.getString("key"),object.getString("value"));
//                        Common.initMaps.put(object.getString("key"),object.getString("value"));
                    }
                    getHandler().sendEmptyMessage(3);
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


    private LinearLayout content;
    private TextView title,connent_status,title0;
    private ImageView share;
    private TextView bottom_0,bottom_1,bottom_2,bottom_3,bottom_4,bottom_5;
    @Override
    public void init() {
        content = findViewById(R.id.content);
        share = findViewById(R.id.share);
        connent_status = findViewById(R.id.connent_status);
        title = findViewById(R.id.title);
        bt_click = findViewById(R.id.bt_click);
        bike_cicle = findViewById(R.id.bike_cicle);
//        int diameter = (int)(216.0 * getScreenWidth() / 452);
//        bike_cicle.setDiameter(diameter);
        bottom_0 = findViewById(R.id.bottom_0);
        bottom_1 = findViewById(R.id.bottom_1);
        bottom_2 = findViewById(R.id.bottom_2);
        bottom_3 = findViewById(R.id.bottom_3);
        bottom_4 = findViewById(R.id.bottom_4);
        bottom_5 = findViewById(R.id.bottom_5);
        title0 = findViewById(R.id.title0);



        bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtils sharedUtils = new SharedUtils(Common.initMap);
                if(sharedUtils.getKeys().size()==0){
                    getInit();
                }else{
                    getHandler().sendEmptyMessage(3);
                }

               /* intent.putExtra("xl","200");
                intent.putExtra("sd","201");
                intent.putExtra("sj","300");
                intent.putExtra("cal","301");
                intent.putExtra("lc","400");
                intent.putExtra("zlc","401");*/


            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share.setVisibility(View.GONE);
                content.setDrawingCacheEnabled(true);
                Bitmap tBitmap = content.getDrawingCache();
                // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
                tBitmap = tBitmap.createBitmap(tBitmap);
                content.setDrawingCacheEnabled(false);
                share.setVisibility(View.VISIBLE);
                UMImage image = new UMImage(MyBikeActivity.this,tBitmap);//本地文件
                new ShareAction(MyBikeActivity.this)
                        .withMedia(image)
                        .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)

                        .setCallback(umShareListener)
                        .open();
            }
        });
    }

    private  UMShareListener umShareListener = new UMShareListener(){

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            P.c("onResult");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            P.c("onError");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

            P.c("onCancel");
        }
    };
    private String connect_mac = "";
    private String connect_name = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==1000){
            //连接成功
                //开始发送连接
            P.c("onActivityResult");
            connect_mac = sharedUtils.getStringValue("bt_mac");
            connect_name = sharedUtils.getStringValue("bt_name");
           // ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);

        }
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
    private     boolean contain2(String input, String regex) {
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
                FileUtils.writeLog(ByteUtils.byteToString(value),"接收");
                title.setText( ByteUtils.byteToString(value));
                LUNJING = getVlue();
                String result = ByteUtils.byteToString(value);
                if(contain2(result,"F0B036CA")){
                    //初始化成功

                    write("F0A136CA91");
                }
                if (contain2(result,"F0B136CA")) {
                    //轮经
                    int s = getChar(result,8,2);
                    int g = getChar(result,10,2);
                    //暂时启用40的数值
                   // LUNJING = LUNJING = (s*10)+FileUtils.formatDouble(g/10);

                  /*  LUNJING = getVlue();
                    write("F0A236CA92");*/

                   // NewToast.makeText(MyBikeActivity.this,(s*10)+g,Common.TTIME).show();
                    doSend();
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
                   double sd =FileUtils.formatDouble( cir * prm*60/10000);
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
                    bottom_0.setTag(String.valueOf(xl));

                    bottom_1.setText(sd+" km/h");
                    bottom_1.setTag(String.valueOf(sd));

                    bottom_3.setText(String.valueOf(cal));
                    bottom_3.setTag(String.valueOf(cal));
                    double tlc = 0;
                    try {
                        tlc =   Double.parseDouble(bottom_4.getText().toString());

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    bottom_4.setText(String.valueOf(lc+tlc));
                    bottom_4.setTag(String.valueOf(lc+tlc));
                    double zlc = 0;
                    try {
                          zlc = Double.parseDouble(sharedUtils.getStringValue("zlc"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int z = sharedUtils.getIntValue("TKM");

                    bottom_5.setText( String.valueOf(lc+tlc+zlc+z));
                    bottom_5.setTag( String.valueOf(lc+tlc+zlc+z));
                    if(cal!=0){
                        String temp = String.valueOf(cal/bike_cicle.getMaxValues());
                        float now = FileUtils.formatFloat(Float.parseFloat(temp))*100;
                        P.c("当前"+now);
                        if(now>=bike_cicle.getMaxValues()){
                            bike_cicle.setTitle("已完成");
                        }else{
                            bike_cicle.setCurrentValues(now);
                        }
                    }
                }
            }
        }

        @Override
        public void onResponse(int code) {
            P.c("notify开启结果"+code);
            if (code == REQUEST_SUCCESS) {
                //开启成功之后就开始发送数据

                write("F0A036CA90");
            } else {

            }
        }
    };
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
    private Timer timer;
    private boolean canTime = false;
    private void doSend(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //在这里进行操作
                canTime = true;
              Common.RUN_TIME = 0;
                write("F0A236CA92");
            }
        };
        timer.schedule(task,5000,1000);
    }

    private void write(String param){
        FileUtils.writeLog(param,"发送");
        title0.setText("发送"+param);
        P.c("发送数据"+param);
        ClientManager.getClient().write(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER,
                ByteUtils.stringToBytes(param), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    private void unnotify(){
        ClientManager.getClient().unnotify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, new BleUnnotifyResponse() {
            @Override
            public void onResponse(int code) {
                P.c("断开unnotify");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        canTime = false;
        RUN = false;
        unnotify();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        if(connect_mac.length()!=0) {
            ClientManager.getClient().unregisterConnectStatusListener(connect_mac, mConnectStatusListener);
        }
        }
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            BluetoothLog.v(String.format("CharacterActivity.onConnectStatusChanged status = %d", status));

            if (status == STATUS_DISCONNECTED) {
                //断开连接
                P.c("连接失败");
                connent_status.setText("已断开");
            }else if(status==STATUS_CONNECTED){
                P.c("连接中");
                connent_status.setText("已连接");
                ClientManager.getClient().notify(connect_mac, Common.UUID_SERVICE, Common.UUID_CHARACTER, mNotifyRsp);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
      //  title0.setText("");
        if(!RUN){
            RUN = true;
            time();
        }
        P.c("onResume");
        connect_mac = sharedUtils.getStringValue("bt_mac");
        connect_name = sharedUtils.getStringValue("bt_name");
        if(connect_mac.length()!=0){
            ClientManager.getClient().registerConnectStatusListener(connect_mac, mConnectStatusListener);
            int status = ClientManager.getClient().getConnectStatus(connect_mac);
            P.c("连接状态"+status);
            if(status!=2){
                getHandler().sendEmptyMessage(2);
                connent_status.setText("已断开");
            }else{
                connent_status.setText("已连接");
            }
        }else {
            connent_status.setText("未连接");
            bottom_0.setText("- -");
            bottom_1.setText("- -");
            bottom_2.setText("- -");
            bottom_3.setText("- -");
            bottom_4.setText("- -");
            bottom_5.setText("- -");
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                CenterItem1Edit edit =  DB.getInstance().getNowSport();
                Message msg = new Message();
                msg.what =1;
                msg.obj = edit;
                getHandler().sendMessage(msg);
            }
        }.start();
    }
}
