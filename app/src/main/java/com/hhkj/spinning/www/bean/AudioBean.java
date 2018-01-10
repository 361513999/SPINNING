package com.hhkj.spinning.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10/010.
 */

public class AudioBean implements Serializable {
    private String image;
    private String title;
    private String tip;
    private ArrayList<Map<String,String>> maps;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public ArrayList<Map<String, String>> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<Map<String, String>> maps) {
        this.maps = maps;
    }
}
