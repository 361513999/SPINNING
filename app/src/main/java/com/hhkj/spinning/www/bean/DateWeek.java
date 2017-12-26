package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by cloor on 2017/12/27.
 */

public class DateWeek implements Serializable {
    private String date;
    private String week;
    private long date_start;
    private long date_end;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public long getDate_start() {
        return date_start;
    }

    public void setDate_start(long date_start) {
        this.date_start = date_start;
    }

    public long getDate_end() {
        return date_end;
    }

    public void setDate_end(long date_end) {
        this.date_end = date_end;
    }
}
