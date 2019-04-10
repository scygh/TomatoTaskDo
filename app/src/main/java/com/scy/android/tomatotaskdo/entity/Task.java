package com.scy.android.tomatotaskdo.entity;

import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.UUID;

/**
 * 沈程阳
 * created by scy on 2019/4/8 09:44
 * 邮箱：1797484636@qq.com
 */
public class Task extends LitePalSupport{

    private long id;
    private String tid;
    private String tno;
    private String startTime;
    private String endTime;
    private String isFinished;
    private User user;

    public Task() {
       super();
       tid = UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
