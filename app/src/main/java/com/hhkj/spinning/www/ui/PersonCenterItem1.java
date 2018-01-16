package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.CenterItem1Adapter;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.bean.DateWeek;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.db.DB;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.widget.CommonTips;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem1 extends BaseFragment {
    private TextView add_tog;
    private ListView tog_list;
    private LinearLayout tog_title,s_l,date_view;
    private RadioGroup s_list;
    private CenterItem1Adapter centerItem1Adapter;
    private Handler handler;
    private ArrayList<DateWeek> dateWeeks = new ArrayList<>();
    private Activity activity;
    private Base_Handler base_handler;
    private TextView tog_tips,week_view;

    private ArrayList<CenterItem1Edit> centerItem1Edits = new ArrayList<>();
    public PersonCenterItem1(Activity activity,Handler handler){
        this.handler = handler;
        this.activity =activity;
        base_handler = new Base_Handler(PersonCenterItem1.this);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_tog = view.findViewById(R.id.add_tog);
        add_tog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,PersonCenterItem1_Edit.class);
                startActivityForResult(intent,100);
            }
        });
        week_view = view.findViewById(R.id.week_view);
        date_view = view.findViewById(R.id.date_view);
        tog_tips =view.findViewById(R.id.tog_tips);
        s_list = view.findViewById(R.id.s_list);
        s_l =view.findViewById(R.id.s_l);
        tog_title = view.findViewById(R.id.tog_title);
        tog_list = view.findViewById(R.id.tog_list);
        centerItem1Adapter = new CenterItem1Adapter(activity,centerItem1Edits,base_handler);
        tog_list.setAdapter(centerItem1Adapter);

        for(int i=0;i<7;i++){
            String date =  TimeUtil.getFetureDate(i);
            String week = TimeUtil.dateToWeek(date);
            DateWeek dateWeek = new DateWeek();
            dateWeek.setDate(date);
            dateWeek.setWeek(week);
            String end = date+"  "+"23:59";
            String start = date+"  "+"00:00";
            dateWeek.setDate_end(TimeUtil.parseTime(end));
            dateWeek.setDate_start(TimeUtil.parseTime(start));
            dateWeeks.add(dateWeek);
        }
        base_handler.sendEmptyMessage(2);
    }



    private int SELECT = 0;
    @Override
    public void onResume() {
        super.onResume();
        DB.getInstance().isCenterItem1List(base_handler);
        if(s_list.getChildCount()!=0){
            ((RadioButton)s_list.getChildAt(SELECT)).setChecked(true);
        }

    }

    private class Base_Handler extends Handler {
        WeakReference<PersonCenterItem1> mLeakActivityRef;

        public Base_Handler(PersonCenterItem1 leakActivity) {
            mLeakActivityRef = new WeakReference<PersonCenterItem1>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mLeakActivityRef.get() != null) {
                switch (msg.what){
                    case 3:
                        int o = msg.arg1;
                        Intent intent = new Intent(activity,PersonCenterItem1_Edit.class);
                        final CenterItem1Edit edit = (CenterItem1Edit) msg.obj;
                        if(o==0){
                            //编辑
                        intent.putExtra("obj",edit);
                        intent.putExtra("mod",0);
                        startActivityForResult(intent,100);
                        }else if(o==1){
                            //删除
                            CommonTips commonTips = new CommonTips(activity,null);
                            commonTips.init(null,null,"删除该目标");
                            commonTips.setI(new Tips() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void sure() {
                                    P.c("操作删除");
                                    DB.getInstance().clearById("tog_time",edit.getI());
                                    check();
                                }
                            });
                            commonTips.showSheet();
                        }
                        break;
                    case 0:
                        if(msg.arg1!=0){
                            tog_list.setVisibility(View.VISIBLE);
                            tog_title.setVisibility(View.VISIBLE);
                            s_l.setVisibility(View.VISIBLE);
                            tog_tips.setVisibility(View.GONE);
                            date_view.setVisibility(View.VISIBLE);
                        }else{
                            tog_list.setVisibility(View.GONE);
                            tog_title.setVisibility(View.GONE);
                            s_l.setVisibility(View.GONE);
                            tog_tips.setVisibility(View.VISIBLE);
                            date_view.setVisibility(View.GONE);
                        }
                        break;
                    case  1:
                        centerItem1Adapter.updata(centerItem1Edits);

                        break;
                    case 2:
                        //绘制一周数据
                        P.c("选择"+SELECT);
                        for(int i=0;i<dateWeeks.size();i++){

                            RadioButton rb = (RadioButton) RadioButton.inflate(activity,R.layout.child_rb_btn,null);
                            rb.setText(dateWeeks.get(i).getWeek());
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT,1);
                            rb.setLayoutParams(params);
                            s_list.addView(rb);
                        }

                        s_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                              /*  int count = s_list.getChildCount();
                                for(int k=0;k<count;k++){
                                    if(s_list.getChildAt(k).getId()==i){
                                        SELECT = k;
                                        DateWeek dateWeek = dateWeeks.get(k);
                                        week_view.setText(dateWeek.getDate());
                                        DB.getInstance().getCenterItemEdits(centerItem1Edits,base_handler,dateWeek);
                                    }
                                }*/
                              check();


                            }
                        });
                        //默认先给一个
                        ((RadioButton)s_list.getChildAt(SELECT)).setChecked(true);

                        break;
                }
            }
        }
    }
    private void check(){
        int count = s_list.getChildCount();
        for(int k=0;k<count;k++){
            if(s_list.getChildAt(k).getId()==s_list.getCheckedRadioButtonId()){
                DateWeek dateWeek = dateWeeks.get(k);
                week_view.setText(dateWeek.getDate());
                DB.getInstance().getCenterItemEdits(centerItem1Edits,base_handler,dateWeek);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==1000){

           check();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center_item1, container, false);
        return view;

    }
}
