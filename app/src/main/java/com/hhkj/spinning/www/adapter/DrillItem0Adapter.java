package com.hhkj.spinning.www.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.bean.AudioBean;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.widget.InScrollListView;
import com.hhkj.spinning.www.widget.NewToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cloor on 2017/8/9.
 */

public class DrillItem0Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int height;

    private ArrayList<AudioBean> audioBeans;
    private Handler handler;
    public DrillItem0Adapter(Context context,int height,ArrayList<AudioBean> audioBeans, Handler handler){
        this.context = context;
        this.height = height;
        this.audioBeans = audioBeans;
        this.handler = handler;
        inflater = LayoutInflater.from(context);

    }
    public void updata(ArrayList<AudioBean> audioBeans){
        this.audioBeans = audioBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return audioBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return audioBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {

        LinearLayout child,content0,play_music;
        TextView item0,item1,play_time;
        InScrollListView content;
        ImageView item2;
        Button slo;
    }
    int playIndex = -1;
      boolean is ;
      private int index = - 1;
    public boolean getIs(){
        return  is;
    }
    public int getIndex(){
        return  index;
    }
    public void open(boolean is){
        this.is = is;
        notifyDataSetChanged();
    }
    public void playing(int index,int playIndex){
        this.index = index;
        this.playIndex = playIndex;

        notifyDataSetChanged();
    }
    public void index(int index){
        this.index = index;
        notifyDataSetChanged();

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null
                || convertView.getTag(R.mipmap.ic_launcher + position) == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_drill_item0, null);
//            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.child = convertView.findViewById(R.id.child);
            viewHolder.slo = convertView.findViewById(R.id.slo);
            viewHolder.content0 = convertView.findViewById(R.id.content0);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.item0 = convertView.findViewById(R.id.item0);
            viewHolder.item1 = convertView.findViewById(R.id.item1);
            viewHolder.item2 = convertView.findViewById(R.id.item2);
            viewHolder.play_music = convertView.findViewById(R.id.play_music);
            viewHolder.play_time = convertView.findViewById(R.id.play_time);
            convertView.setTag(R.mipmap.ic_launcher + position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.mipmap.ic_launcher
                    + position);
        }
        final AudioBean bean = audioBeans.get(position);
        viewHolder.item0.setText(bean.getTip());
        viewHolder.item1.setText(bean.getTitle());

       ArrayList<Map<String,String>> map = bean.getMaps();
       long times = 0;
        for(int i=0;i<map.size();i++){
            try {
                times+= TimeUtil.getTimepmm(map.get(i).get("time"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if(times!=0){

            viewHolder.play_time.setText(TimeUtil.getTimemm(times));

        }else{
            viewHolder.play_time.setText("00:00");
        }


        ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.music_ico,viewHolder.item2);
        //FileUtils.addImage(bean.getImage())
        viewHolder.content0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
        DrillItem0ItemAdapter adapter = new DrillItem0ItemAdapter(context,bean.getMaps(),handler,position,playIndex);
        viewHolder.content.setAdapter(adapter);
        P.c("是否展开"+is);
        if(is&&index==position){
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.slo.setText("- 全部歌单");

            try {
                String temp = BaseApplication.iMusicService.isPlay();
                if(temp!=null){
                    String result[] = temp.split("_");
                    int ind = Integer.parseInt(result[1]);
                    int pos = Integer.parseInt(result[0]);
                    if(pos==position){
                        adapter.play(ind);
                    }else{
                        adapter.play(-1);
                    }

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }else{
            viewHolder.slo.setText("+ 全部歌单");
            viewHolder.content.setVisibility(View.GONE);
        }
      /*  if(is){

        }else{


        }*/

         viewHolder.slo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(bean.getMaps().size()!=0){
                if(viewHolder.content.getVisibility()==View.VISIBLE){
                    is = false;
//                viewHolder.content.setVisibility(View.GONE);

                }else{
                    is = true;
//                viewHolder.content.setVisibility(View.VISIBLE);
                }
                index= position;
                notifyDataSetChanged();
            }else{
                NewToast.makeText(context,"无更多资源", Common.TTIME).show();
            }


        }
    });

        viewHolder.play_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bean.getMaps().size()!=0){
                    boolean status = false;
                    try {
                          status = BaseApplication.iMusicService.status();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if(status){
                        try {
                            BaseApplication.iMusicService.stop();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }else{
                        P.c("播放音频");
                        Message msg = new Message();
                        msg.what = 2;
                        msg.arg1 = position;
                        msg.arg2 = 0;
                        handler.sendMessage(msg);
                    }

                }else{
                    NewToast.makeText(context,"无更多资源", Common.TTIME).show();
                }

            }
        });

        return  convertView;
    }
}
