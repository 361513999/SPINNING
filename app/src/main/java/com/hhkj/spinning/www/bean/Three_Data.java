package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/25/025.
 */

public class Three_Data implements Serializable {
    private String uuid;
    private String name;
    private String sex;
    private String icon;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
