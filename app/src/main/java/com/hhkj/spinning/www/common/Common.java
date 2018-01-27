package com.hhkj.spinning.www.common;

import android.os.Environment;

import java.util.UUID;

/**
 * Created by Administrator on 2017/12/13/013.
 */

public class Common {
    public  volatile  static boolean isRunning = false;
    //应用签名    ba2928e597b995ae1cb5a75b059f71d5
    public static final String Appkey = "5a38a65ab27b0a6b3a00052a";
    public final static String DB_DIR = "data/data/"+BaseApplication.application.getPackageName()+"/databases/";
    public static final String DB_NAME = "store.spinning";
    public static final int DB_VERSION = 2;
    public static final String config = "CONFIG";
    public static final int SHOW_NUM = 8;
    public static final int TTIME = 200;
    public static String BASE_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    public static String DIR = "/SPINNING_CACHE/";
    public static String SD = BASE_DIR+DIR;
    public static final String STAFF_IMAGE = "IMAGES/";
    public static final String CACHE_STAFF_IMAGES = SD+STAFF_IMAGE;
    public static String APK_LOG = SD+"LOG/";
    public static int MUSIC_INDEX = -1;

//    service = 49535343-fe7d-4ae5-8fa9-9fafd205e455, character = 49535343-1e4d-4bd9-ba61-23c647249616
    private static String service = "49535343-fe7d-4ae5-8fa9-9fafd205e455";
    private static String character = "49535343-1e4d-4bd9-ba61-23c647249616";
    public static UUID UUID_SERVICE = UUID.fromString(service);
    public static  UUID UUID_CHARACTER = UUID.fromString(character);
    public  static int RUN_TIME = 0;
    public static final String RUN_TIME_ACTION = "spinning.time";
}
