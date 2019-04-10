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
    private String tDescription;
    private String startTime;
    private String isFinished;
    private String priority;
    private User user;

    public Task() {
       super();
       tid = UUID.randomUUID().toString();
    }

    public Task(String tDescription, String priority) {
        this.tDescription = tDescription;
        this.priority = priority;
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

    public String gettDescription() {
        return tDescription;
    }

    public void settDescription(String tDescription) {
        this.tDescription = tDescription;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
