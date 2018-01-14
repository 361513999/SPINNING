package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by cloor on 2018/1/13.
 */

public class SportMenu implements Serializable {
    private String id;
    private String name;

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
}
