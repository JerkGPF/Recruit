package com.gpfei.recruit.beans;

import cn.bmob.v3.BmobObject;

public class SignInBean extends BmobObject {
    private User user;
    private int intergal;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIntergal() {
        return intergal;
    }

    public void setIntergal(int intergal) {
        this.intergal = intergal;
    }
}
