package com.hhkj.spinning.www.common;

import android.os.Environment;

/**
 * Created by Administrator on 2017/12/13/013.
 */

public class Common {
    //应用签名    ba2928e597b995ae1cb5a75b059f71d5
    public static final String Appkey = "5a38a65ab27b0a6b3a00052a";
    public final static String DB_DIR = "data/data/"+BaseApplication.application.getPackageName()+"/databases/";
    public static final String DB_NAME = "store.spinning";
    public static final String config = "CONFIG";
    public static String BASE_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    public static String DIR = "/SPINNING_CACHE/";
    public static String SD = BASE_DIR+DIR;
    public static String APK_LOG = SD+"LOG/";
}
