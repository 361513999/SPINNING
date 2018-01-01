package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by cloor on 2017/12/30.
 */

public class DrillItem1Left implements Serializable {
    private String bg;
    private String icon;
    private String title;
    private boolean play;

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }
}
