package com.scy.android.tomatotaskdo.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 沈程阳
 * created by scy on 2019/4/8 09:31
 * 邮箱：1797484636@qq.com
 */
public class User extends LitePalSupport {

    private long id;
    private String uid;
    @Column(unique = true, defaultValue = "unknown")
    private String username;
    private String password;
    private String sex;
    private String birthday;
    private String address;
    private List<Task> mTasks = new ArrayList<>();
    private List<FocusTime> mFocusTimes = new ArrayList<>();
    private String headerImageUri;

    private int dz;

    public User() {
        super();
        uid = UUID.randomUUID().toString();
        dz = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }

    public List<FocusTime> getFocusTimes() {
        return mFocusTimes;
    }

    public void setFocusTimes(List<FocusTime> focusTimes) {
        mFocusTimes = focusTimes;
    }

    public String getHeaderImageUri() {
        return headerImageUri;
    }

    public void setHeaderImageUri(String headerImageUri) {
        this.headerImageUri = headerImageUri;
    }

    public int getDz() {
        return dz;
    }

    public void setDz(int dz) {
        this.dz = dz;
    }
}
