package com.hhkj.spinning.www.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;

/**
 * @author  
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	public static String getTimeAll(long time){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		return formatter.format(time);
	}
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(time));
	}
	public static String getTimeYear(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return format.format(new Date(time));
	}
	public static String getTimeCh(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}
	public static String getTimePri(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		return format.format(new Date(time));
	}
	public static String getTimeH(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}
	public static String get_HH(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		return format.format(new Date(time));
	}
	public static String get_mm(long time) {
		SimpleDateFormat format = new SimpleDateFormat("mm");
		return format.format(new Date(time));
	}

	public static String getTimeLog(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date(time));
	}
	public static String getDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
		return format.format(new Date(time));
	}
	public static long parseTime(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
		try {
			return  format.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  0;
	}
	public static long parseTime_(String time){
//		1991-11-15 00:00:00
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return  format.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  0;
	}
	public static String getDate_(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}
	public static String getNextDay(String date,boolean next){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = df.parse(date);
			Calendar g = Calendar.getInstance();
			g.setTime(d1);
			if(next){
				g.add(Calendar.DAY_OF_MONTH,1);
			}else{
				g.add(Calendar.DAY_OF_MONTH,-1);
			}
			Date d2 = g.getTime();
			return  df.format(d2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDate(System.currentTimeMillis());
	}

	public static String getNextDate(String date,boolean next){
		DateFormat df = new SimpleDateFormat("yyyy.M");
		try {
			Date d1 = df.parse(date);
			Calendar g = Calendar.getInstance();
			g.setTime(d1);
			if(next){
				g.add(Calendar.MONTH,1);
			}else{
				g.add(Calendar.MONTH,-1);
			}
			Date d2 = g.getTime();
			return  df.format(d2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getSelectDate(System.currentTimeMillis());
	}
	public static String getSelectDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.M");
		return format.format(new Date(time));
	}
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getTime(timesamp);
			break;
		}

		return result;
	}
	/**
	 * 获取过去第几天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getPastDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取未来 第 past 天的日期
	 * @param past
	 * @return
	 */
	public static String getFetureDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);

		return result;
	}
	/**
	 * 日期转周
	 *
	 * @param datetime
	 * @return
	 */
	public static String dateToWeek(String datetime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
}
