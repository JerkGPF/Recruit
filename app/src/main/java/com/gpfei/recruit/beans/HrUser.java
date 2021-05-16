package com.gpfei.recruit.beans;

import cn.bmob.v3.BmobObject;

public class HrUser extends BmobObject {
    private String company_name;
    private String company_birthday;
    private String company_phone;
    private String company_email;
    private String company_introduce;
    private String company_free;
    private User userInfo;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_birthday() {
        return company_birthday;
    }

    public void setCompany_birthday(String company_birthday) {
        this.company_birthday = company_birthday;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getCompany_introduce() {
        return company_introduce;
    }

    public void setCompany_introduce(String company_introduce) {
        this.company_introduce = company_introduce;
    }

    public String getCompany_free() {
        return company_free;
    }

    public void setCompany_free(String company_free) {
        this.company_free = company_free;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
