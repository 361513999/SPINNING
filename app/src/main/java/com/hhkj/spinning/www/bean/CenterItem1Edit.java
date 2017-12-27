package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by cloor on 2017/12/26.
 */

public class CenterItem1Edit implements Serializable {
    private int i;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    private String tog;
    private long time;

    public String getTog() {
        return tog;
    }

    public void setTog(String tog) {
        this.tog = tog;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
