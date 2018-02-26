package com.hhkj.spinning.www.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.base.BaseFragment;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.TimeUtil;
import com.hhkj.spinning.www.widget.line.LineData;
import com.hhkj.spinning.www.widget.line.LineUtils;
import com.hhkj.spinning.www.widget.line.XY;
import com.hhkj.spinning.www.widget.line.view.AxisXView;
import com.hhkj.spinning.www.widget.line.view.AxisYView_NormalType;
import com.hhkj.spinning.www.widget.line.view.LineView;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/19/019.
 */

@SuppressLint("ValidFragment")
public class PersonCenterItem2 extends BaseFragment {
    private Handler handler;
    private Activity activity;
    public PersonCenterItem2(Activity activity, Handler handler){
        this.handler = handler;
        this.activity =activity;
        base_handler = new Base_Handler(PersonCenterItem2.this);
    }
    private LinearLayout axisYLayout = null;
    private LinearLayout axisXLayout = null;
    private LinearLayout threndLine_Layout = null;
    private RelativeLayout main_content;
    private LineView lineView;
    private AxisYView_NormalType axisY_2;
    private AxisXView axisX;

    private XY xy = new XY();
    private float oldX = 0;
    private float oldY = 0;
    private TextView date,l_d,r_d;
    private void init(View view){
        date = view.findViewById(R.id.date);
        l_d = view.findViewById(R.id.l_d);
        r_d = view.findViewById(R.id.r_d);
        main_content =view. findViewById(R.id.main_content);
        axisXLayout = view.findViewById(R.id.axisX);
        axisYLayout =view.findViewById(R.id.axisY);
        threndLine_Layout = view. findViewById(R.id.thrend_line);
        main_content.post(new Runnable() {
            @Override
            public void run() {
                LineUtils.screenWidth = main_content.getMeasuredWidth();
                LineUtils.screenHeight = main_content.getMeasuredHeight();

                //设置图区宽高、内容宽高
                LineUtils.layoutWidth = LineUtils.screenWidth ;
                LineUtils.layoutHeight = (int) (LineUtils.screenHeight * 0.9);
                LineUtils.viewWidth = LineUtils.screenWidth ;
                LineUtils.viewHeight = (int) (LineUtils.screenHeight *0.9);


                RelativeLayout.LayoutParams xParams = (RelativeLayout.LayoutParams) axisXLayout.getLayoutParams();
                xParams.height = LineUtils.layoutHeight;
                xParams.width = LineUtils.layoutWidth;
                //xParams.bottomMargin
                xParams.setMargins(xParams.leftMargin+LineUtils.YWidth, xParams.topMargin, xParams.rightMargin, xParams.bottomMargin);
                axisXLayout.setLayoutParams(xParams);

                RelativeLayout.LayoutParams yParams = (RelativeLayout.LayoutParams) axisYLayout.getLayoutParams();
                yParams.height = LineUtils.layoutHeight;
                yParams.setMargins(yParams.leftMargin, yParams.topMargin, yParams.rightMargin, yParams.bottomMargin + LineUtils.XHeight);
                axisYLayout.setLayoutParams(yParams);
                //

                RelativeLayout.LayoutParams hParams = (RelativeLayout.LayoutParams) threndLine_Layout.getLayoutParams();
                hParams.height = LineUtils.layoutHeight;
                hParams.width = LineUtils.layoutWidth;
                hParams.setMargins(hParams.leftMargin+LineUtils.YWidth, hParams.topMargin, hParams.rightMargin, hParams.bottomMargin + LineUtils.XHeight);
                threndLine_Layout.setLayoutParams(hParams);



                //实例化View
                axisY_2 = new AxisYView_NormalType(activity);
                axisX = new AxisXView(activity);
                lineView = new LineView(activity);

                base_handler.sendEmptyMessage(1);

                l_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        date.setText(TimeUtil.getNextDay(date.getText().toString(),false));
                    }
                });
                r_d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        date.setText(TimeUtil.getNextDay(date.getText().toString(),true));
                    }
                });
                date.setText(TimeUtil.getDate_(System.currentTimeMillis()));
            }
        });



    }
    float linkedList[] = new float[]{2,8,9,4,6,6,3,4,7,7,8,2,5} ;
    private float max(float[] linkedList){
        float []nums = new float[linkedList.length];
        for(int i=0;i<linkedList.length;i++){
            nums[i] = linkedList[i];
        }

        for (int i = 0; i <= nums.length - 1; i++) {
            for (int j = i + 1; j <= nums.length - 1; j++) {
                if (nums[i] > nums[j]) {
                    float temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
        }
        float max = nums[nums.length - 1];

        if(max>0.01&&max<0.1){
            if((max*100)%2==0){
                return max+0.02f;
            }else {
                return max+0.01f;
            }
        }else if (max>0.1&&max<1) {
            if((max*10)%2==0){
                return max+0.2f;
            }else {
                return max+0.1f;
            }
        }else {
            //计算最大值和步长
            int temp_max = (int)max;
            if(max>temp_max){
                //表示丢失精度
                //取最大为
                if((temp_max+1)%2==0){
                    return temp_max+1;

                }else {
                    return temp_max+2;
                }
            }else {
                if(temp_max%2==0){
                    return temp_max;

                }else {
                    return temp_max+1;
                }

            }
        }

    }
    private Base_Handler base_handler = null;
    private class Base_Handler extends Handler {
        WeakReference<PersonCenterItem2> mLeakActivityRef;
        public Base_Handler(PersonCenterItem2 leakActivity) {
            mLeakActivityRef = new WeakReference<PersonCenterItem2>(leakActivity);
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(mLeakActivityRef.get()!=null){
                 switch (msg.what){
                     case 1:

                         //setTitle();
                        // setYName();
                         setKey();
                         setAxis();
                         setData();
                         //填充
                         addView();
                         break;
                 }
            }
        }
    }
    /**
     * 初始化各绘图组件
     * 包括设置高宽、位置
     */

    private void setData() {
        LineData data1 = new LineData();
        data1.setName("测试");
        data1.setData(linkedList);
        data1.setColor(Color.WHITE);

//		MyData data2 = new MyData();
//		data2.setName("CO");
//		data2.setData( new int[]{-1,210,190,-1,240,250,
//								80,85,90,230,100,220,
//								70,30,70,80,130,40,
//								30,80,40,160,100,210} );
//		data2.setColor(0xff43ce17);

        LineUtils.DataSeries = new ArrayList<LineData>();
        LineUtils.DataSeries.add(data1);
//		Common.DataSeries.add(data2);

    }




    private void setKey(){
        //设置图例参数
        LineUtils.keyWidth = 30;
        LineUtils.keyHeight = 10;
        LineUtils.keyToLeft = 300;
        LineUtils.keyToTop = 80;
        LineUtils.keyToNext = 80;
        LineUtils.keyTextPadding = 5;
    }
    private float get(float t){
        BigDecimal b1   =   new   BigDecimal(t);
        //保留2位小数
        double   total_v1   =   b1.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return Float.parseFloat(String.valueOf(total_v1));
    }
    private void setAxis(){
        //设置轴参数
        LineUtils.xScaleArray = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
//		Common.xScaleColor = Color.YELLOW;

        //yScaleArray需要比levelName和color多出一个数
        P.c("最大"+max(linkedList));
        float step = max(linkedList)/2;
        P.c("步长"+step);
        LineUtils.yScaleArray = new float[]{0,get(step),get(2*step),get(3*step),get(4*step),get(5*step)};
        LineUtils.levelName = new String[]{"优","良","轻度","中度","重度","严重"};
        LineUtils.yScaleColors = new int[]{0xff00ff00,0xffffff00,0xffffa500,0xffff4500,0xffdc143c,0xffa52a2a};
    }

    private void addView(){
        int width=0;
//		if(mp==false)
//			width=Common.screenWidth*7/8+10;
//		else
        width=LineUtils.viewWidth;

        //设定初始定位Y坐标
        xy.y = LineUtils.viewHeight - LineUtils.layoutHeight;

        lineView.initValue(width, LineUtils.viewHeight, true);//传入宽、高、是否在折线图上显示数据
        lineView.scrollTo(0, xy.y);

        axisY_2.initValue(LineUtils.viewHeight);//传入高度
        axisY_2.scrollTo(0, xy.y);

        axisX.initValue(width, LineUtils.viewHeight);//传入高度
        axisX.scrollTo(0, xy.y);

        axisYLayout.removeAllViews();
        axisYLayout.addView(axisY_2);

        axisXLayout.removeAllViews();
        axisXLayout.addView(axisX);

        threndLine_Layout.removeAllViews();
        threndLine_Layout.addView(lineView);



        //监听滑动事件
        lineView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    oldX = event.getX();
                    oldY = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    parseXY( xy.x+=oldX-event.getX() , xy.y+=oldY-event.getY() , lineView.getWidth() , lineView.getHeight() , threndLine_Layout);
                    lineView.scrollTo(xy.x, xy.y);
                    axisY_2.scrollTo(0, xy.y);
                    axisX.scrollTo(xy.x, LineUtils.viewHeight - LineUtils.layoutHeight);
                    oldX = event.getX();
                    oldY = event.getY();
                }
                return true;
            }
        });
    }

    protected void parseXY(float x,float y,int width,int height,LinearLayout parent) {
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        if(x<0)
            xy.x = 0;
        else if(x > width-parentWidth)
            xy.x = width-parentWidth;
        else
            xy.x = (int) x;

        if(y<0)
            xy.y = 0;
        else if(y > height-parentHeight)
            xy.y = height-parentHeight;
        else
            xy.y = (int) y;

        //初步防抖
        if(width<=parentWidth)
            xy.x = 0;
        if(height<=parentHeight)
            xy.y = 0;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_center_item2, container, false);
        return view;

    }
}
