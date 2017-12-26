package com.hhkj.spinning.www.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.hhkj.spinning.www.common.Common;


public class DBHelper extends SQLiteOpenHelper{
	/**
	 * 数据库操作
	 * @param context
	 * @param name
	 * @param factory
	 * @param versionê
	 */
	private String DATABASE_PATH ; 
	@SuppressWarnings("unused")
	private Context context;
	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, Common.DB_NAME, null, Common.DB_VERSION);
		 
	}
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
		DATABASE_PATH ="/data/data/"+context.getPackageName()+"/databases/";

	}	
	private void create (SQLiteDatabase db){
	db.beginTransaction();
	//目标管理
    db.execSQL("create table tog_time(i integer primary key autoincrement,tog varchar,time long)");

    db.setTransactionSuccessful();
	db.endTransaction();
	}
	private void drop(SQLiteDatabase db){
		db.beginTransaction();
		db.execSQL("DROP TABLE IF EXISTS tog_time");

		db.setTransactionSuccessful();
		db.endTransaction();
		//此处是删除数据表，在实际的业务中一般是需要数据备份的
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		create(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		drop(db);
		create(db);
	}
	  
    
    public boolean checkDataBase() {  
        SQLiteDatabase db = null;  
        try {  
            String databaseFilename = DATABASE_PATH + Common.DB_NAME;  
            db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READWRITE);  
        } catch (SQLiteException e) {  
  
        }  
        if (db != null) {  
            db.close();  
        }  
        return db != null ? true : false;  
    }   
  

}
