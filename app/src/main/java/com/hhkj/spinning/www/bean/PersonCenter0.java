package com.hhkj.spinning.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/28/028.
 */

public class PersonCenter0 implements Serializable {
    private String userName;
    private String url;
    private String Birthday;
    private boolean Sex;
    private String Height;
    private String Weight;
    private String IdealWeight;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public boolean isSex() {
        return Sex;
    }

    public void setSex(boolean sex) {
        Sex = sex;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getIdealWeight() {
        return IdealWeight;
    }

    public void setIdealWeight(String idealWeight) {
        IdealWeight = idealWeight;
    }
}
