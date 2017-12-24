package com.hhkj.spinning.www.inter;

import org.json.JSONObject;

/**
 * Created by cloor on 2017/12/24.
 */

public interface Result {
    public void success(JSONObject data);
    public void error(String data);
    public void unLogin();
}
