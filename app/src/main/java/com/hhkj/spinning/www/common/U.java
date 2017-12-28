package com.hhkj.spinning.www.common;


import com.hhkj.spinning.www.utils.RegExpValidatorUtils;

import okhttp3.MediaType;

/**
 * Created by cloor on 2017/8/6.
 */

public class U {
    public static String IP = "39.104.61.90";
    public static String BASEIP = IP+":"+"8001";
    private static final String BASE_URL = "/DataService.svc/";
    public static MediaType json = MediaType.parse("application/json; charset=utf-8");
    

    public static String VISTER(){
        SharedUtils sharedUtils = new SharedUtils(Common.config);
        String ip = sharedUtils.getStringValue("IP");
        if(ip.length()!=0&& RegExpValidatorUtils.isIP(ip)){
            return "http://"+ip+BASE_URL;
        }
        return "http://"+BASEIP+BASE_URL;
    }
}
