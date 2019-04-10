package com.scy.android.tomatotaskdo.entity;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 沈程阳
 * created by scy on 2019/4/8 09:49
 * 邮箱：1797484636@qq.com
 */
public class FocusTime extends LitePalSupport {

    private long id;
    //专注时间，以分钟为单位
    private int time;
    //所属用户
    private User user;
    //日期
    private String mDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
