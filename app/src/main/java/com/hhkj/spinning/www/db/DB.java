package com.hhkj.spinning.www.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hhkj.spinning.www.common.BaseApplication;

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
}
