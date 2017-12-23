package com.hhkj.spinning.www.widget.line.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.widget.line.LineData;
import com.hhkj.spinning.www.widget.line.LineUtils;

/**
 * 标题及图例等
 * @author ZLL
 */
@SuppressLint("DrawAllocation")
public class TitleView extends View {
    public TitleView(Context context) {
        super(context);
    }

    /**
     * 实例化值
     *
     */
    public void initValue() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);// 设置画笔样式
        paint.setAntiAlias(true);// 去锯齿
        paint.setColor(Color.BLACK);// 设置颜色
        paint.setTextSize(Integer.parseInt(getResources().getString(R.string.textsizeX)));// 设置字体
        paint.setStrokeWidth(1);

        
        drawTitle(canvas);
        drawRect(canvas);
        drawYName(canvas);
    }
    
    private void drawTitle(Canvas canvas){
    	// 设置画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);// 设置画笔样式
        paint.setAntiAlias(true);// 去锯齿
        paint.setColor(LineUtils.titleColor);// 设置颜色
        paint.setTextSize(LineUtils.bigSize);// 设置字体
        
        canvas.drawText(LineUtils.title, LineUtils.titleX, LineUtils.titleY, paint);
        
        paint.setTextSize(LineUtils.smallSize);
        canvas.drawText(LineUtils.secondTitle, LineUtils.StitleX, LineUtils.StitleY , paint);
    }    
    
    private void drawRect(Canvas canvas){
    	int count = 0;
    	
    	int width = LineUtils.keyWidth;
    	int height = LineUtils.keyHeight;
    	int toLeft = LineUtils.keyToLeft;
    	int toTop = LineUtils.keyToTop;
    	int toNext = LineUtils.keyToNext;
    	int textPadding = LineUtils.keyTextPadding;
    	
    	for(LineData data: LineUtils.DataSeries){
    		Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);// 设置画笔样式
            paint.setAntiAlias(true);// 去锯齿
            paint.setColor(data.getColor());// 设置颜色
            
            if(toLeft+toNext*count+width > LineUtils.screenWidth){
            	toTop += height*3/2;
            	count = 0;
            }
	        canvas.drawRect(toLeft+toNext*count, toTop, toLeft+toNext*count+width, toTop+height, paint);
	        canvas.drawText(data.getName(), toLeft+toNext*count+width+textPadding, toTop+height, paint);
            
            count++;
    	}
    }
    
    private void drawYName(Canvas canvas){
    	
    	Paint paint = new Paint();
    	paint.setStyle(Paint.Style.FILL_AND_STROKE);
    	paint.setAntiAlias(true);
    	paint.setTextSize(LineUtils.bigSize);
    	paint.setColor(LineUtils.titleColor);
    	canvas.rotate(-90, LineUtils.YName2Left, LineUtils.YName2Top);
    	canvas.drawText(LineUtils.YName, LineUtils.YName2Left, LineUtils.YName2Top, paint);
    }
}
