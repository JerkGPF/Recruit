package com.gpfei.recruit.beans;


import cn.bmob.v3.BmobObject;

public class WeekendBean extends BmobObject {
    private String title_weekend;
    private String money_weekend;
    private String company_weekend;
    private String address_weekend;
    private int wCount;
    private String url;

    private HrUser author;

    public int getwCount() {
        return wCount;
    }

    public void setwCount(int wCount) {
        this.wCount = wCount;
    }


    private String url_weekend;

    public String getUrl_weekend() {
        return url_weekend;
    }

    public void setUrl_weekend(String url_weekend) {
        this.url_weekend = url_weekend;
    }

    public String getTitle_weekend() {
        return title_weekend;
    }

    public void setTitle_weekend(String title_weekend) {
        this.title_weekend = title_weekend;
    }

    public String getMoney_weekend() {
        return money_weekend;
    }

    public void setMoney_weekend(String money_weekend) {
        this.money_weekend = money_weekend;
    }

    public String getCompany_weekend() {
        return company_weekend;
    }

    public void setCompany_weekend(String company_weekend) {
        this.company_weekend = company_weekend;
    }

    public String getAddress_weekend() {
        return address_weekend;
    }

    public void setAddress_weekend(String address_weekend) {
        this.address_weekend = address_weekend;
    }

    public HrUser getAuthor() {
        return author;
    }

    public void setAuthor(HrUser author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
