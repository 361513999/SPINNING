package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.PersonCenterItem1_EditLeftAdapter;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.db.DB;
import com.hhkj.spinning.www.widget.NewToast;
import com.umeng.socialize.media.Base;

import java.util.ArrayList;
import java.util.Calendar;

import library.NumberPickerView;
import library.view.GregorianLunarCalendarOneView;
import library.view.GregorianLunarCalendarView;

/**
 * Created by Administrator on 2017/12/26/026.
 */

public class PersonCenterItem1_Edit extends BaseActivity {
    private ListView person_left;
    private PersonCenterItem1_EditLeftAdapter personCenterItem1_editLeftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_center_item1_edit);
    }

    @Override
    public void process(Message msg) {

    }
    private LinearLayout right_content,layout0,layout1;
    private TextView btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn_ac,btn0,btn_delete,btn_view;
    private NumberPickerView picker_minute,picker_hour;
    private GregorianLunarCalendarOneView calendarView;
    private TextView save;
    @Override
    public void init() {
        btn_view  =findViewById(R.id.btn_view);
        btn_view.setText("0卡");
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn_ac = findViewById(R.id.btn_ac);
        btn0 = findViewById(R.id.btn0);
        btn_delete = findViewById(R.id.btn_delete);
        right_content = findViewById(R.id.right_content);
        layout0 = findViewById(R.id.layout0);
        layout1 = findViewById(R.id.layout1);
        save = findViewById(R.id.save);
        personCenterItem1_editLeftAdapter = new PersonCenterItem1_EditLeftAdapter(PersonCenterItem1_Edit.this);
        person_left = findViewById(R.id.person_left);
        person_left.setAdapter(personCenterItem1_editLeftAdapter);
        person_left.setItemChecked(0,true);
        person_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i==0){
                        layout0.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                    }else if(i==1){
                        layout1.setVisibility(View.VISIBLE);
                        layout0.setVisibility(View.GONE);
                    }
            }
        });
         picker_minute = (NumberPickerView) findViewById(R.id.picker_minute);
         picker_hour = (NumberPickerView) findViewById(R.id.picker_hour);
         calendarView = (GregorianLunarCalendarOneView) findViewById(R.id.calendar_view_start);
         calendarView.init();
            save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long data = 0;
                GregorianLunarCalendarOneView.CalendarData calendarData0 = calendarView.getCalendarData();
                final Calendar calendar0 = calendarData0.getCalendar();
                final String show = calendar0.get(Calendar.YEAR) + "-"
                        + (calendar0.get(Calendar.MONTH) + 1) + "-"
                        + calendar0.get(Calendar.DAY_OF_MONTH)+"  "+picker_hour.getContentByCurrValue()+":"+picker_minute.getContentByCurrValue();
                      data = TimeUtil.parseTime(show);
                    if(data==0){
                        NewToast.makeText(PersonCenterItem1_Edit.this,"目标时间不合法",Common.TTIME).show();
                        return;
                    }else if(data<=System.currentTimeMillis()){
                        NewToast.makeText(PersonCenterItem1_Edit.this,"目标时间应该在当前之后",Common.TTIME).show();
                        return;
                    }
                   String last = btn_view.getText().toString();
                   String temp = last.substring(0,last.length()-1);
                try {
                    int var =  Integer.parseInt(temp);
                    if(var==0){
                        NewToast.makeText(PersonCenterItem1_Edit.this,"请输入合理的目标数据",Common.TTIME).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //校验合格进行键入数据库
                DB.getInstance().addCenterItem1Edit(temp,data);
                setResult(1000);
                AppManager.getAppManager().finishActivity(PersonCenterItem1_Edit.this);


            }
        });
        right_content.post(new Runnable() {
            @Override
            public void run() {

                int width = (int) ((50.0/540.0)*right_content.getMeasuredWidth());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,width);
                btn1.setLayoutParams(layoutParams);
                btn2.setLayoutParams(layoutParams);
                btn3.setLayoutParams(layoutParams);
                btn4.setLayoutParams(layoutParams);
                btn5.setLayoutParams(layoutParams);
                btn6.setLayoutParams(layoutParams);
                btn7.setLayoutParams(layoutParams);
                btn8.setLayoutParams(layoutParams);
                btn9.setLayoutParams(layoutParams);
                btn0.setLayoutParams(layoutParams);
                btn_ac.setLayoutParams(layoutParams);
                btn_delete.setLayoutParams(layoutParams);
                btn1.setOnClickListener(clickListener);
                btn2.setOnClickListener(clickListener);
                btn3.setOnClickListener(clickListener);
                btn4.setOnClickListener(clickListener);
                btn5.setOnClickListener(clickListener);
                btn6.setOnClickListener(clickListener);
                btn7.setOnClickListener(clickListener);
                btn8.setOnClickListener(clickListener);
                btn9.setOnClickListener(clickListener);
                btn0.setOnClickListener(clickListener);
                btn_ac.setOnClickListener(clickListener);
                btn_delete.setOnClickListener(clickListener);
            }
        });
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view instanceof  TextView){
               String txt =  ((TextView) view).getText().toString();
               String last = btn_view.getText().toString();
               if(txt.equals("AC")){
                   btn_view.setText("0卡");
               }else if(txt.equals("←")){
                   try {
                       String temp = last.substring(0,last.length()-2);
                        if(temp.length()==0){
                            btn_view.setText("0卡");
                        }else{
                            btn_view.setText(temp+"卡");
                        }

                   } catch (Exception e) {
                       e.printStackTrace();
                       btn_view.setText("0卡");
                   }


               }else{
                   if(last.length()>1){
                       String temp = last.substring(0,last.length()-1);

                       try {
                           int num = Integer.parseInt(temp);
                           if(num!=0){
                               txt = temp+txt;
                           }
                       } catch (NumberFormatException e) {
                           e.printStackTrace();
                           NewToast.makeText(PersonCenterItem1_Edit.this,"超过最大数值", Common.TTIME).show();
                           return;
                       }
                   }
                   btn_view.setText(txt+"卡");
               }

            }
        }
    };
}
