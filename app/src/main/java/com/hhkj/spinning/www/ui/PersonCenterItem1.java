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
import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.bean.DateWeek;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.db.DB;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem1 extends Fragment {
    private TextView add_tog;
    private ListView tog_list;
    private LinearLayout tog_title,s_l;
    private RadioGroup s_list;
    private CenterItem1Adapter centerItem1Adapter;
    private Handler handler;
    private ArrayList<DateWeek> dateWeeks = new ArrayList<>();
    private Activity activity;
    private Base_Handler base_handler;
    private TextView tog_tips;
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
        tog_tips =view.findViewById(R.id.tog_tips);
        s_list = view.findViewById(R.id.s_list);
        s_l =view.findViewById(R.id.s_l);
        tog_title = view.findViewById(R.id.tog_title);
        tog_list = view.findViewById(R.id.tog_list);
        centerItem1Adapter = new CenterItem1Adapter(activity,centerItem1Edits);
        tog_list.setAdapter(centerItem1Adapter);
        DB.getInstance().getCenterItemEdits(centerItem1Edits,base_handler);
        for(int i=0;i<7;i++){
            String date =  TimeUtil.getFetureDate(i);
            String week = TimeUtil.dateToWeek(date);
            DateWeek dateWeek = new DateWeek();
            dateWeek.setDate(date);
            dateWeek.setWeek(week);
            P.c(date+"--"+week);
            dateWeeks.add(dateWeek);
        }
        base_handler.sendEmptyMessage(2);
    }

    @Override
    public void onResume() {
        super.onResume();

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
                    case  1:
                        centerItem1Adapter.updata(centerItem1Edits);
                        if(centerItem1Edits.size()!=0){
                            tog_list.setVisibility(View.VISIBLE);
                            tog_title.setVisibility(View.VISIBLE);
                            s_l.setVisibility(View.VISIBLE);
                            tog_tips.setVisibility(View.GONE);
                        }else{
                            tog_list.setVisibility(View.GONE);
                            tog_title.setVisibility(View.GONE);
                            s_l.setVisibility(View.GONE);
                            tog_tips.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        //绘制一周数据
                        P.c("数据大小"+dateWeeks.size());
                        for(int i=0;i<dateWeeks.size();i++){
                            P.c("--->"+dateWeeks.get(i).getWeek());
                            RadioButton rb = (RadioButton) RadioButton.inflate(activity,R.layout.child_rb_btn,null);
                            rb.setText(dateWeeks.get(i).getWeek());
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT,1);
                            rb.setLayoutParams(params);
                            s_list.addView(rb);
                        }

                        break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==1000){
            DB.getInstance().getCenterItemEdits(centerItem1Edits,base_handler);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center_item1, container, false);
        return view;

    }
}
