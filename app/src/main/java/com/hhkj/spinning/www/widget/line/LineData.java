package com.hhkj.spinning.www.widget.line;

public class LineData {
	/**
	 * 数据名称
	 */
	private String name;
	
	/**
	 * 数据内容
	 */
	private float[] data;
	
	/**
	 * 数据颜色
	 */
	private int color;
	
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	 
	public float[] getData() {
		return data;
	}
	public void setData(float[] data) {
		this.data = data;
	}
	public void setColor(int color){
		this.color = color;
	}
	public int getColor(){
		return color;
	}
}
