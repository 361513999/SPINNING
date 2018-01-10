package com.hhkj.spinning.www.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.db.DB;
import com.hhkj.spinning.www.ui.WelcomeActivity;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/1/9/009.
 */

public class SpinningService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Intent notificationIntent = new Intent(this, WelcomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.noty)
                .setWhen(TimeUtil.getNow())
                .setTicker("FitNow")
                .setContentTitle("FitNow服务运行中")
                .setContentText("快速打开应用")
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();
         /*使用startForeground,如果id为0，那么notification将不会显示*/
        startForeground(1,notification);
    }
public void showNoty(long time,String tog){
    Intent notificationIntent = new Intent(this, WelcomeActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    Notification notification = new Notification.Builder(this)
            .setSmallIcon(R.mipmap.noty)
            .setWhen(TimeUtil.getNow())
            .setTicker("FitNow")
            .setAutoCancel(true)
            .setContentTitle("FitNow提醒服务")
            .setContentText(TimeUtil.getTimeH(time)+"的目标"+tog+"卡")
            .setOngoing(false)

            .setContentIntent(pendingIntent)
            .build();
    //使用默认的声音
    notification.defaults |= Notification.DEFAULT_SOUND;

//使用默认的震动
    notification.defaults |= Notification.DEFAULT_VIBRATE;

         /*使用startForeground,如果id为0，那么notification将不会显示*/
    startForeground(1,notification);
}
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        flags = START_STICKY;
        if(intent!=null&&intent.hasExtra("watch")){
            if( !Common.isRunning){
                Common.isRunning = true;
                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        while (true){
                          //  P.c("监听进行中");
                            Intent listerServiceIntent = new Intent(context, WatchService.class);
                            listerServiceIntent.putExtra("listener","");
                            listerServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(listerServiceIntent);
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //进行数据库操作

                            CenterItem1Edit edit =  DB.getInstance().getTask();
                            if(edit!=null){
                                Intent intent1 = new Intent();
                                intent1.setAction("spinning.spinning.www");
                                intent1.setPackage(BaseApplication.getName());
                                intent1.putExtra("noty",new String[]{String.valueOf(edit.getTime()),edit.getTog()});
                                startService(intent1);

                            }
                        }
                    }
                }.start();
            }

        }else  if(intent!=null&&intent.hasExtra("noty")){
                String temps[] = intent.getStringArrayExtra("noty");

                showNoty(Long.parseLong(temps[0]),temps[1]);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean stopService(Intent name) {
        Common.isRunning = false;
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        P.c("com.spinning.destroy");
        stopForeground(true);
        Intent intent = new Intent();
        intent.setAction("com.spinning.destroy");
        sendBroadcast(intent);
    }
}
