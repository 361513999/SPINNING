package com.hhkj.spinning.www.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.widget.NewToast;

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
    private int pos = 0;
    private int ind = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }


    IMusicService.Stub stub = new IMusicService.Stub() {
        @Override
        public void play(final int position, final int index, final int total) throws RemoteException {
            if(pos==position&&ind==index&&mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
                return;
            }
            mediaPlayer.stop();
            mediaPlayer.reset();
            pos = position;
            if(index==total){
                ind = 0;
            }else {
                ind = index;
            }
            Intent intent0 = new Intent();
            intent0.setAction("music.start");
            intent0.putExtra("pos",index);
            intent0.putExtra("ind",ind);
            sendBroadcast(intent0);
            mediaPlayer.prepareToPlay( Common.SAudioBeans.get(position).getMaps().get(ind).get("url"));
            mediaPlayer.play();
            mediaPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
                @Override
                public void onPrepared() {
                    Intent intent = new Intent();
                    intent.setAction("music.click");
                    intent.putExtra("pos",position);
                    intent.putExtra("ind",index);
                    sendBroadcast(intent);
                }
            });
            mediaPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
                @Override
                public void onCompleted() {

                    try {
                        play(position,index+1,total);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.setAction("music.complete");
                    intent.putExtra("pos",position);
                    intent.putExtra("ind",index+1);
                    sendBroadcast(intent);

                }
            });
        }

        @Override
        public String isPlay() throws RemoteException {
            if(mediaPlayer.isPlaying()){
                return pos+"_"+ind;
            }

            return null;
        }

        @Override
        public void stop() throws RemoteException {

                mediaPlayer.stop();
                mediaPlayer.reset();


        }

        @Override
        public boolean status() throws RemoteException {
            return mediaPlayer.isPlaying();
        }


    };
}
