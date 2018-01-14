package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by cloor on 2018/1/14.
 */

public class SportList implements Serializable {
    private String id;
    private String name;
    private String tip;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
