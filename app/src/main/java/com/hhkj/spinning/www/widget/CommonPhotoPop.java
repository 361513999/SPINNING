package com.hhkj.spinning.www.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.inter.PhotoSelect;

public class CommonPhotoPop {


    private CommonPhotoPop() {

    }

    public static Dialog showSheet(Context context, LayoutInflater inflater,final PhotoSelect select, final int index) {
        final Dialog dlg = new Dialog(context, R.style.delete_pop_style);

        View layout =  inflater.inflate(
                R.layout.common_photo_pop, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);
        TextView camer = (TextView) layout.findViewById(R.id.camer);
        TextView photo = (TextView) layout.findViewById(R.id.photo);
        TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
        camer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                select.camcer( );
                dlg.dismiss();
            }
        });
        photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select.photos( );
                dlg.dismiss();
            }
        });

        mCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				actionSheetSelected.onClick(object);
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setContentView(layout);
        dlg.show();

        return dlg;
    }

    public void cancle() {

    }

}
