package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.DrillItem0Adapter;
import com.hhkj.spinning.www.adapter.DrillItem1RightAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.bean.AudioBean;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.service.IMusicService;
import com.hhkj.spinning.www.service.MusicService;
import com.hhkj.spinning.www.widget.NewToast;
import com.hhkj.spinning.www.widget.PullToRefreshView;
import com.hhkj.spinning.www.widget.xlist.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by cloor on 2017/12/30.
 */

@SuppressLint("ValidFragment")
public class DrillItem0 extends BaseFragment {
         private Activity activity;
         private SurfaceView suf;
        private DrillItem0Adapter drillItem0Adapter ;
        private Handler handler;
        private SwipeRefreshLayout drill_item0_refr;
    private XListView xListView;
        private SharedUtils sharedUtils;
        public DrillItem0(Activity activity, Handler handler,SharedUtils sharedUtils){
            this.activity = activity;
            this.handler = handler;
            this.sharedUtils = sharedUtils;
        }
private XListView.IXListViewListener ixListViewListener =new XListView.IXListViewListener(){

    @Override
    public void onRefresh() {
          P.c(" 刷新");
        xListView.stopRefresh();

    }

    @Override
    public void onLoadMore() {
        P.c(" 更多");
        if(CURRENT_LIST_MORE){
            //加载
            getAudio();
        }else {
            NewToast.makeText(activity, "没有数据可加载", Common.TTIME).show();

        }
        xListView.stopLoadMore();
    }
};
    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            drill_item0_refr.setRefreshing(true);
            xListView.setRefreshTime(TimeUtil.getTime(TimeUtil.getNow()));
            CURRENT_LIST_PAGE = 1;
            audioBeans.clear();
            getAudio();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("music.start");
        filter.addAction("music.click");
        filter.addAction("music.complete");
        activity.registerReceiver(musicReceiver,filter);
        xListView = view.findViewById(R.id.grids);
        drill_item0_refr = view.findViewById(R.id.drill_item0_refr);
        drill_item0_refr.setOnRefreshListener(listener);
        suf = view.findViewById(R.id.suf);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(false);
        xListView.setXListViewListener(ixListViewListener);
        //85/335
        xListView.post(new Runnable() {
            @Override
            public void run() {
                int height = (int) ((85/335.0)*(xListView.getMeasuredWidth()/2));
                drillItem0Adapter = new DrillItem0Adapter(activity,height,audioBeans,drill_handler);
                xListView.setAdapter(drillItem0Adapter);


                getAudio();
            }
        });
        mediaPlayer = new AliVcMediaPlayer(activity,suf);
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
    private AliVcMediaPlayer mediaPlayer;
    private boolean CURRENT_LIST_MORE = true;
    private int CURRENT_LIST_PAGE = 1;
    private ArrayList<AudioBean> audioBeans = new ArrayList<>();
    private void getAudio(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cls","Sys.AudioType");
            jsonObject.put("method","LoadAudioType");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            object.put("pageSize", Common.SHOW_NUM);
            object.put("pageIndex", CURRENT_LIST_PAGE);
            object.put("app", "");
            jsonObject.put("param", object.toString());
            httpPost("Post", jsonObject.toString(), new Result() {
                @Override
                public void success(JSONObject data) {
                    ;
                    try {
                        String result = data.getString("Result");
                        JSONArray jsonArray = new JSONArray(result);

                        int len = jsonArray.length();
                        if (len < Common.SHOW_NUM) {
                            CURRENT_LIST_MORE = false;
                            //  getHandler().sendEmptyMessage(-2);
                        } else {
                            CURRENT_LIST_PAGE = CURRENT_LIST_PAGE + 1;
                        }

                        for(int i=0;i<len;i++){
                            AudioBean audioBean = new AudioBean();

                            JSONObject jsob = jsonArray.getJSONObject(i);
                            audioBean.setTitle(jsob.getString("Title"));
                            audioBean.setImage(jsob.getString("TitleUrl"));
                            audioBean.setTip(jsob.getString("Remark"));

                            JSONArray array = jsob.getJSONArray("Audios");
                            int jen = array.length();
                            ArrayList<Map<String,String>> maps = new ArrayList<>();
                            for(int j=0;j<jen;j++){
                                JSONObject obj = array.getJSONObject(j);
                                Map<String,String> map = new HashMap<>();
                                map.put("title",obj.getString("Title"));
                                map.put("url",obj.getString("Url"));
                                map.put("time",obj.getString("LongTime"));
                                map.put("id",obj.getString("Id"));
                                maps.add(map);
                            }
                            audioBean.setMaps(maps);
                            audioBeans.add(audioBean);
                            drill_handler.sendEmptyMessage(1);

                        }


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
            },activity);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drill_item0, container, false);
        drill_handler = new Drill_Handler(DrillItem0.this);

        return view;

    }




    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
    private boolean is = false;
    private boolean click3 = false;
    private Drill_Handler drill_handler;
    private class Drill_Handler extends Handler {
        WeakReference<DrillItem0> mLeakActivityRef;
        public Drill_Handler(DrillItem0 leakActivity) {
            mLeakActivityRef = new WeakReference<DrillItem0>(leakActivity);
        }
        @Override
        public void dispatchMessage(final Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){

               switch (msg.what){
                   case 0:
                       Map<String,String> map = (Map<String, String>) msg.obj;
                       Intent intent = new Intent(activity,PlayerActivity.class);
//                       intent.putExtra("obj",map);
                       intent.putExtra("param",map.get("title")+";"+FileUtils.addImage(map.get("url")));
                       startActivity(intent);
                       break;
                   case 1:
                       if (drill_item0_refr != null) {
                           drill_item0_refr.setRefreshing(false);
                       }

                        drillItem0Adapter.updata(audioBeans);

                       try {
                           String result =  BaseApplication.iMusicService.isPlay();
                           if(result!=null){
                               String []temp = result.split("_");
                               drillItem0Adapter.playing(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
                               drillItem0Adapter.open(true);
                               drillItem0Adapter.index(Integer.parseInt(temp[0]));

                           }
                       } catch (RemoteException e) {
                           e.printStackTrace();
                       }


                       break;

                   case 2:

                       is = true;
                       drillItem0Adapter.open(is);
                       //在这里进行操作是否展开下级
                       //開始播放
                       final int index = msg.arg1;
                         int play = msg.arg2;
                         //临时使用的已完成变量
                         final int playCom = play;
                         P.c("播放"+index+"=="+play);
                       if(audioBeans.size()!=0){
                             final AudioBean bean = audioBeans.get(index);
//                               Common.MUSIC_INDEX = index;

                               if(play==bean.getMaps().size()){
                                   play = 0;
                               }
                              // final Map<String,String> music = bean.getMaps().get(play);
                           Common.SAudioBeans.clear();
                           Common.SAudioBeans.addAll(audioBeans);


//                               mediaPlayer.prepareToPlay(music.get("url"));
//                               mediaPlayer.play();
                           try {

                              BaseApplication.iMusicService.play(index,play,bean.getMaps().size());
                           } catch (RemoteException e) {
                               e.printStackTrace();
                           }

                               drillItem0Adapter.playing(index,play);


                       }
                       click3 = false;

                       break;

               }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(musicReceiver!=null){
            activity.unregisterReceiver(musicReceiver);
        }
    }

    private BroadcastReceiver musicReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
               if(action.equals("music.click")){
                   int pos = intent.getIntExtra("pos",0);
                   int ind = intent.getIntExtra("ind",0);
                   String id = audioBeans.get(pos).getMaps().get(ind).get("id");
                   click(id);

                    NewToast.makeText(activity,"开始播放",Common.TTIME).show();
                   drillItem0Adapter.playing(pos,ind);

               }else if(action.equals("music.complete")){
                   //已完成
                   int pos = intent.getIntExtra("pos",0);
                   int ind = intent.getIntExtra("ind",0);
                   Message msg0 = new Message();
                   msg0.what = 2;
                   msg0.arg1 = pos;
                   msg0.arg2 = ind;
                   drill_handler.sendMessage(msg0);
               }else if(action.equals("music.start")){
                   //开始
                   int pos = intent.getIntExtra("pos",0);
                   int ind = intent.getIntExtra("ind",0);
                   String title = audioBeans.get(pos).getMaps().get(ind).get("title");

                   NewToast.makeText(activity,"即将播放:"+title,Common.TTIME).show();
               }
        }
    };



    private void click(String id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            jsonObject.put("cls","Sys.PlayAudio");
            jsonObject.put("method","SetPalyCount");
            JSONObject object = new JSONObject();
            object.put("Id",Integer.parseInt(id));
            jsonObject.put("param",object.toString());
        } catch (Exception e) {
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
        },activity,false);
    }

}
