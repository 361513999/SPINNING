package com.spinning.www.common;

import android.os.Environment;

/**
 * Created by Administrator on 2017/12/13/013.
 */

public class Common {

    public static final String config = "CONFIG";
    public static String BASE_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    public static String DIR = "/SPINNING_CACHE/";
}
