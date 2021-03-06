package com.hhkj.spinning.www.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.inter.TipsText;

import java.util.Calendar;

import library.view.GregorianLunarCalendarOneView;


public class CommonOneDate {
    private Context context;
    private Handler handler;
    private IDialog dlg;

    private LayoutInflater inflater;
    public CommonOneDate(Context context , Handler handler) {
        this.context = context;
        this.handler = handler;
        inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private String t0,t1,tips;
    public void init(String t0,String t1,String tips){
        this.t0= t0;
        this.t1 = t1;
        this.tips = tips;
    }
    private TipsText tpps = null;
    public void setI( TipsText tpps){
        this.tpps = tpps;
    }
    public Dialog showSheet() {
        dlg = new IDialog(context, R.style.load_pop_style);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.common_one_date, null);
        TextView item0 = (TextView) layout.findViewById(R.id.item0);
        TextView item1 = (TextView) layout.findViewById(R.id.item1);
        TextView txt = (TextView) layout.findViewById(R.id.txt);
        final GregorianLunarCalendarOneView calendar_view_start = layout.findViewById(R.id.calendar_view_start);
        calendar_view_start.init();
        if(t0!=null){
            item0.setText(t0);
        }
        if(t1!=null){
            item1.setText(t1);
        }
        if(tips!=null){
            txt.setText(tips);
        }

        item0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle();
                if(tpps!=null){
                    tpps.cancel(null);
                }

            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancle();
                if(tpps!=null){
                    GregorianLunarCalendarOneView.CalendarData calendarData0 = calendar_view_start.getCalendarData();
                    final Calendar calendar0 = calendarData0.getCalendar();
                    final String show = calendar0.get(Calendar.YEAR) + "-"
                            + (calendar0.get(Calendar.MONTH) + 1) + "-"
                            + calendar0.get(Calendar.DAY_OF_MONTH);

                    tpps.sure(show);
                }
            }
        });


        dlg.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                // TODO Auto-generated method stub

            }
        });
        dlg.setCanceledOnTouchOutside(true);
        dlg.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub

            }
        });
        dlg.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub

            }
        });
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }


    public void cancle(){
        if(dlg!=null){
            dlg.cancel();
            dlg = null;
        }
    }
}
