package com.hhkj.spinning.www.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alivc.player.AliVcMediaPlayer;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;

/**
 * Created by Administrator on 2018/2/2/002.
 */

public class MusicService extends Service {
    private AliVcMediaPlayer mediaPlayer;
    private SurfaceView suf;
    @Override
    public void onCreate() {
        super.onCreate();
        suf = new SurfaceView(this);
        mediaPlayer = new AliVcMediaPlayer(this,suf);
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

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }


    IMusicService.Stub stub = new IMusicService.Stub() {
        @Override
        public void play(int position, int index) throws RemoteException {
            mediaPlayer.prepareToPlay( Common.musicMAP.get("url"));
                               mediaPlayer.play();
        }


    };
}
