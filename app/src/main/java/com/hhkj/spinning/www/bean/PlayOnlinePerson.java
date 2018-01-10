package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by cloor on 2018/1/7.
 */

public class PlayOnlinePerson implements Serializable {
    private String kll;
    private String km;
    private String xl;
    private String sd;
    private String ico;
    private String name;
    private String kcal;

    public String getKll() {
        return kll;
    }

    public void setKll(String kll) {
        this.kll = kll;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}
