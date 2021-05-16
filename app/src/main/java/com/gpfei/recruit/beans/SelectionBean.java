package com.gpfei.recruit.beans;


import cn.bmob.v3.BmobObject;

public class SelectionBean extends BmobObject {
    private String title_selection;
    private String url_selection;
    private String address_selection;
    private String money_selection;
    private String company_selection;
    private int sCount;
    private HrUser author;

    public int getsCount() {
        return sCount;
    }

    public void setsCount(int sCount) {
        this.sCount = sCount;
    }

    public String getTitle_selection() {
        return title_selection;
    }

    public void setTitle_selection(String title_selection) {
        this.title_selection = title_selection;
    }

    public String getUrl_selection() {
        return url_selection;
    }

    public void setUrl_selection(String url_selection) {
        this.url_selection = url_selection;
    }

    public String getAddress_selection() {
        return address_selection;
    }

    public void setAddress_selection(String address_selection) {
        this.address_selection = address_selection;
    }

    public String getMoney_selection() {
        return money_selection;
    }

    public void setMoney_selection(String money_selection) {
        this.money_selection = money_selection;
    }

    public String getCompany_selection() {
        return company_selection;
    }

    public void setCompany_selection(String company_selection) {
        this.company_selection = company_selection;
    }

    public HrUser getAuthor() {
        return author;
    }

    public void setAuthor(HrUser author) {
        this.author = author;
    }
}
