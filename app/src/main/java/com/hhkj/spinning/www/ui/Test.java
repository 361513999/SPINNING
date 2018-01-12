package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.media.NEVideoView;
import com.hhkj.spinning.www.receiver.NEPhoneCallStateObserver;
import com.hhkj.spinning.www.receiver.NEScreenStateObserver;
import com.hhkj.spinning.www.receiver.Observer;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.constant.NEType;

/**
 * Created by Administrator on 2018/1/12/012.
 */

public class Test extends BaseActivity {
    @Override
    public void process(Message msg) {

    }

    @Override
    public void init() {

    }
    private boolean isScreenOff;
    private boolean isBackground;
    private NEVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mVideoView = findViewById(R.id.video_view);
//        mVideoView.setBufferingIndicator(mBuffer);
        mVideoView.setMediaType("livestream");
        mVideoView.setBufferStrategy(NEType.NELPLOWDELAY);
        mVideoView.setHardwareDecoder(true);
        mVideoView.setEnableBackgroundPlay(true);

        mVideoView.setVideoPath("http://live.jw100.com/111/3.flv");
        mVideoView.requestFocus();
        mVideoView.start();

        mVideoView.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                P.c("什么问题"+neLivePlayer.getMediaInfo().mVideoStreamType);
                return false;
            }
        });
        mVideoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                P.c("完成");
                mVideoView.start();
            }
        });
    }



    Observer<NEScreenStateObserver.ScreenStateEnum> screenStateObserver = new Observer<NEScreenStateObserver.ScreenStateEnum>() {
        @Override
        public void onEvent(NEScreenStateObserver.ScreenStateEnum screenState) {
            if (screenState == NEScreenStateObserver.ScreenStateEnum.SCREEN_ON) {

                if (isScreenOff) {
                    mVideoView.restorePlayWithForeground();
                }
                isScreenOff = false;
            } else if (screenState == NEScreenStateObserver.ScreenStateEnum.SCREEN_OFF) {

                isScreenOff = true;
                if (!isBackground) {
                    mVideoView.stopPlayWithBackground();
                }
            } else {

//				isScreenOff = false;
            }

        }
    };

    Observer<Integer> localPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer phoneState) {
            if (phoneState == TelephonyManager.CALL_STATE_IDLE) {
                mVideoView.restorePlayWithCall();
            } else if (phoneState == TelephonyManager.CALL_STATE_RINGING) {
                mVideoView.stopPlayWithCall();
            } else {

            }

        }
    };
}
