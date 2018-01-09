package com.hhkj.spinning.www.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;

/**
 * Created by Administrator on 2018/1/9/009.
 */

public class WatchService extends Service {
    private Handler handler = new Handler();
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
    }
    private CountDownTimer countDownTimer;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null&&intent.hasExtra("listener")){
            flags = START_STICKY;
            if(countDownTimer==null){
                countDownTimer = new CountDownTimer(15000,1000) {
                    @Override
                    public void onTick(long l) {
                         //   P.c("监听倒计时==");
                    }

                    @Override
                    public void onFinish() {
                        Intent startServiceIntent = new Intent(context, SpinningService.class);
                        startServiceIntent.putExtra("watch","");
                        startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startService(startServiceIntent);
                    }
                };
            }else{
                countDownTimer.cancel();
             //   P.c("取消监听倒计时==");
            }

            countDownTimer.start();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean stopService(Intent name) {

        return super.stopService(name);
    }
}
