package com.hhkj.spinning.www.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.bean.DateWeek;
import com.hhkj.spinning.www.common.BaseApplication;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.common.TimeUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/12/26/026.
 */

public class DB {
    public   static DB dao;
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;

    private void DB() {
    }
    /**
     * 单列数据库操作对象
     *
     * @return
     */
    private static SharedUtils sharedUtils;
    public static synchronized DB getInstance() {
        if (dao == null) {
            synchronized (DB.class) {
                if (dao == null) {
                    dao = new DB();
                    sharedUtils = new SharedUtils(Common.config);
                    dbHelper = new DBHelper(BaseApplication.application);
                    db = dbHelper.getWritableDatabase();
                }
            }
        }
        return dao;
    }
    /**
     * 根据字段名字获得数据
     *
     * @param cursor
     * @param indexName
     * @return
     */
    // --------------------------
    // ---------------获取数据处理
    private String getString(Cursor cursor, String indexName) {
        return cursor.getString(cursor.getColumnIndex(indexName));
    }

    private int getInt(Cursor cursor, String indexName) {
        return cursor.getInt(cursor.getColumnIndex(indexName));
    }

    private double getDouble(Cursor cursor, String indexName) {
        return cursor.getDouble(cursor.getColumnIndex(indexName));
    }

    private long getLong(Cursor cursor, String indexName) {
        return cursor.getLong(cursor.getColumnIndex(indexName));
    }

    private boolean getBoolean(Cursor cursor, String indexName) {
        return cursor.getInt(cursor.getColumnIndex(indexName)) == 1 ? true
                : false;
    }
    public void clearAll(){
        db.execSQL("DELETE FROM tog_time");
    }
    public void clear(String tableName){
        db.execSQL("DELETE FROM "+tableName);
    }
    public void clearById(String tableName,int i){
        db.execSQL("DELETE FROM "+tableName+" WHERE i="+i);
    }
    //清除当前时间前一天的记录
    public void clearByDay(String tableName){
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DAY_OF_YEAR,-1);
        db.execSQL("DELETE FROM "+tableName+" where time<="+nowTime.getTime().getTime());
    }

    public void addCenterItem1Edit(String tog,long time){
        db.execSQL("insert into tog_time(phone,tog,time,show) values(?,?,?,?)",new Object[]{sharedUtils.getStringValue("phone"),tog,time,false});
    }
    public void updateCenterItem1Edit(String tog,long time,int i){
        db.execSQL("update tog_time set tog=?,time=? where i=?",new Object[]{tog,time,i});
    }
    public void updateCenterItem1EditSuccess(int i){
        db.execSQL("update tog_time set show=? where i=?",new Object[]{true,i});
    }

    /**
     * 是否存在目标
     * @param handler
     */
    public void isCenterItem1List(Handler handler){
        String sql = "select count(*) from tog_time where phone=?";
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql,new String[]{sharedUtils.getStringValue("phone")});
            if(cursor.moveToFirst()){
                count = getInt(cursor,"count(*)");
                cursor.close();

                    if(handler!=null){
                        Message msg = new Message();
                        msg.what = 0;
                        msg.arg1 = count;
                        handler.sendMessage(msg);
                        P.c("获取的数据"+count);
                    }


            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }
    public CenterItem1Edit getTask(){

//        clearByDay("tog_time");
        String sql = "select i,tog,time from tog_time where phone=? and show='0' and time>? order by time ";
        Cursor cursor = null;
        CenterItem1Edit edit = null;
        try {
            cursor = db.rawQuery(sql,new String[]{sharedUtils.getStringValue("phone"),String.valueOf(TimeUtil.getNow())});
            P.c(TimeUtil.getNow()+"数据数量"+cursor.getCount());
            if(cursor.moveToFirst()){
              long time =   getLong(cursor,"time");
              long now = TimeUtil.getNow();
              long starttime = now-(1000*60);
              long endtime = now+(1000*60);
              P.c(TimeUtil.getTime(time)+"-----"+TimeUtil.getTime(starttime)+"=="+TimeUtil.getTime(endtime));
              if(time>=starttime&&time<=endtime){
                  edit = new CenterItem1Edit();
                  edit.setI(getInt(cursor,"i"));
                  edit.setTog(getString(cursor,"tog"));
                  edit.setTime(getLong(cursor,"time"));
                  updateCenterItem1EditSuccess(edit.getI());
              }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return  edit;
    }


    /**
     * 获得目标列表
     * @param list
     */
    public void getCenterItemEdits(ArrayList<CenterItem1Edit> list, Handler handler, DateWeek dateWeek){
        list.clear();
        String sql = "select i,tog,time from tog_time where phone=? ";
        Cursor cursor = null;
        String result = null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql,new String[]{sharedUtils.getStringValue("phone")});
           while(cursor.moveToNext()){
               long time = getLong(cursor,"time");

               if(dateWeek.getDate_start()<=time&&dateWeek.getDate_end()>=time){
                   CenterItem1Edit edit = new CenterItem1Edit();
                   edit.setTime(time);
                   edit.setI(getInt(cursor,"i"));
                   edit.setTog(getString(cursor,"tog"));
                   list.add(edit);
               }

           }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        if(handler!=null){
            handler.sendEmptyMessage(1);
        }

    }
}
