package com.hhkj.spinning.www.widget.line;


import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.widget.line.view.AxisXView;
import com.hhkj.spinning.www.widget.line.view.AxisYView_NormalType;
import com.hhkj.spinning.www.widget.line.view.LineView;
import com.hhkj.spinning.www.widget.line.view.TitleView;

public class ChartsActivity extends BaseActivity {
	private LinearLayout axisYLayout = null;
	private LinearLayout axisXLayout = null;
	private LinearLayout threndLine_Layout = null;
	private LinearLayout title_layout = null;

	private TitleView titleView;
	private LineView lineView;
	private AxisYView_NormalType axisY_2;
	private AxisXView axisX;

	private XY xy = new XY();
	private float oldX = 0;
	private float oldY = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charts_activity);
		//视图
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		LineUtils.screenWidth = mDisplayMetrics.widthPixels;
		LineUtils.screenHeight = mDisplayMetrics.heightPixels;

		//设置图区宽高、内容宽高
		LineUtils.layoutWidth = LineUtils.screenWidth *2/2;
		LineUtils.layoutHeight = LineUtils.screenHeight * 6/8;
		LineUtils.viewWidth = LineUtils.screenWidth *2/2;
		LineUtils.viewHeight = LineUtils.screenHeight *6/8;
		init();

		handler.sendEmptyMessage(1);
		//监听双击事件
//		threndLine_Layout.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				//滑动不会触发up事件，故在up触发时计数
//				if(MotionEvent.ACTION_UP == event.getAction()){
//					count++;
//					if(count == 1){
//						firClick = System.currentTimeMillis();
//						new Thread(new Runnable(){
//						    public void run(){
//						        try {
//									Thread.sleep(500);
//									count=0;
//									firClick=0;
//									secClick=0;
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//						    }
//						}).start();
//
//					} else if (count == 2){
//						secClick = System.currentTimeMillis();
//						if(secClick - firClick < 500){
//							if(mp==false)
//								mp=true;
//							else
//								mp=false;
//							addView();
//						}
//						count = 0;
//						firClick = 0;
//						secClick = 0;
//					}
//				}
//				return true;
//			}
//		});

		//自定义参数

	}

	@Override
	public void process(int what) {

	}

	@Override
	public void init() {
		title_layout = (LinearLayout) findViewById(R.id.titleView);
		axisXLayout = (LinearLayout) findViewById(R.id.axisX);
		RelativeLayout.LayoutParams xParams = (LayoutParams) axisXLayout.getLayoutParams();
		xParams.height = LineUtils.layoutHeight;
		xParams.width = LineUtils.layoutWidth;
		//xParams.bottomMargin
		xParams.setMargins(xParams.leftMargin+LineUtils.YWidth, xParams.topMargin, xParams.rightMargin, xParams.bottomMargin);
		axisXLayout.setLayoutParams(xParams);
		axisYLayout = (LinearLayout) findViewById(R.id.axisY);
		RelativeLayout.LayoutParams yParams = (LayoutParams) axisYLayout.getLayoutParams();
		yParams.height = LineUtils.layoutHeight;
		yParams.setMargins(yParams.leftMargin, yParams.topMargin, yParams.rightMargin, yParams.bottomMargin + LineUtils.XHeight);
		axisYLayout.setLayoutParams(yParams);
		//
		threndLine_Layout = (LinearLayout) findViewById(R.id.thrend_line);
		RelativeLayout.LayoutParams hParams = (LayoutParams) threndLine_Layout.getLayoutParams();
		hParams.height = LineUtils.layoutHeight;
		hParams.width = LineUtils.layoutWidth;
		hParams.setMargins(hParams.leftMargin+LineUtils.YWidth, hParams.topMargin, hParams.rightMargin, hParams.bottomMargin + LineUtils.XHeight);
		threndLine_Layout.setLayoutParams(hParams);



		//实例化View
		axisY_2 = new AxisYView_NormalType(this);
		axisX = new AxisXView(this);
		lineView = new LineView(this);
		titleView = new TitleView(this);
	}

	float linkedList[] = new float[]{1,2,3,4} ;


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

	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 1:

					//setTitle();
					setYName();
					setKey();
					setAxis();
					setData();

					//填充
					addView();
					break;

				default:
					break;
			}
		};
	};
	/**
	 * 初始化各绘图组件
	 * 包括设置高宽、位置
	 */

	private void setData() {
		LineData data1 = new LineData();
		data1.setName("测试");
		data1.setData(linkedList);
		data1.setColor(0xff8d77ea);

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

	private void setTitle(){
		LineUtils.title = "浓度百分比（%）";
		LineUtils.secondTitle = "Leakage(%)";
		LineUtils.titleX = 40;
		LineUtils.titleY = 70;
		LineUtils.StitleX =50;
		LineUtils.StitleY = 110;
		LineUtils.titleColor = Color.GRAY;
	}

	private void setYName(){
		LineUtils.YName = "测试001";
		LineUtils.YName2Left = 40;
		LineUtils.YName2Top = 450;
		LineUtils.titleColor = Color.GRAY;
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
		BigDecimal   b1   =   new   BigDecimal(t);
		//保留2位小数
		double   total_v1   =   b1.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();
		return Float.parseFloat(String.valueOf(total_v1));
	}
	private void setAxis(){
		//设置轴参数
		LineUtils.xScaleArray = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
//		Common.xScaleColor = Color.YELLOW;

		//yScaleArray需要比levelName和color多出一个数
		System.out.println("最大"+max(linkedList));
		float step = max(linkedList)/4;
		System.out.println("步长"+step);
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

		title_layout.removeAllViews();
		title_layout.addView(titleView);

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
}
