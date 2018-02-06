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
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.inter.TipsText;

import java.util.Calendar;

import library.NumberPickerView;
import library.view.GregorianLunarCalendarOneView;


public class CommonOneInfo {
    private Context context;
    private Handler handler;
    private IDialog dlg;

    private LayoutInflater inflater;
    private int min,max;

    public CommonOneInfo(Context context , Handler handler,int min,int max) {
        this.context = context;
        this.handler = handler;
        this.max = max;
        this.min = min;

        inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private String t0,t1,tips,hint;
    private String []arr;
    private int now;
    public void init(String t0,String t1,String tips,String hint,String []arr,int now){
        this.t0= t0;
        this.t1 = t1;
        this.tips = tips;
        this.hint = hint;
        this.arr = arr;
        this.now = now;
    }
    private TipsText tpps = null;
    public void setI( TipsText tpps){
        this.tpps = tpps;
    }
    public Dialog showSheet() {
        dlg = new IDialog(context, R.style.load_pop_style);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.common_one_info, null);
        TextView item0 = (TextView) layout.findViewById(R.id.item0);
        TextView item1 = (TextView) layout.findViewById(R.id.item1);
        TextView txt = (TextView) layout.findViewById(R.id.txt);
        final NumberPickerView numberPickerView = layout.findViewById(R.id.picker_);
        numberPickerView.setHintText(hint);
        numberPickerView.setDisplayedValues(arr);
        numberPickerView.setMinValue(min);
        numberPickerView.setMaxValue(max);
        numberPickerView.setValue(now);
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


                    tpps.sure(numberPickerView.getContentByCurrValue());
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
