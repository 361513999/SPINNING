package com.hhkj.spinning.www.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.hhkj.spinning.www.bean.CenterItem1Edit;
import com.hhkj.spinning.www.common.BaseApplication;

import java.util.ArrayList;

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
    public static synchronized DB getInstance() {
        if (dao == null) {
            synchronized (DB.class) {
                if (dao == null) {
                    dao = new DB();
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
    public void addCenterItem1Edit(String tog,long time){
        db.execSQL("insert into tog_time(tog,time) values(?,?)",new Object[]{tog,time});
    }

    /**
     * 获得目标列表
     * @param list
     */
    public void getCenterItemEdits(ArrayList<CenterItem1Edit> list, Handler handler){
        list.clear();
        String sql = "select tog,time from tog_time";
        Cursor cursor = null;
        String result = null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql,null);
           while(cursor.moveToNext()){
               CenterItem1Edit edit = new CenterItem1Edit();
               edit.setTime(getLong(cursor,"time"));
               edit.setTog(getString(cursor,"tog"));
                list.add(edit);
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
