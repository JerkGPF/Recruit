package com.gpfei.recruit.beans;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class MyUser extends BmobUser {
    private Boolean sex;
    private String nick;
    private Integer age;
    private Integer height;
    private String qq;
    private String head;
    private String motto;
    private String profile;
    private String experience;
    private String name;
    private String birthday;
    private Boolean isHR;
    private BmobFile file;
    private BmobFile headFile;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Boolean getHR() {
        return isHR;
    }

    public void setHR(Boolean HR) {
        isHR = HR;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public BmobFile getHeadFile() {
        return headFile;
    }

    public void setHeadFile(BmobFile headFile) {
        this.headFile = headFile;
    }
}
