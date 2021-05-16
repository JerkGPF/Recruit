package com.gpfei.recruit.beans;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class DayBean extends BmobObject {
    private String title_day;
    private String money_day;
    private String address_day;
    private String company_day;
    private String url;
    private int dCount;
    private HrUser author;

    public String getTableName() {
        return "DayBean";
    }

    public int getdCount() {
        return dCount;
    }

    public void setdCount(int dCount) {
        this.dCount = dCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMoney_day() {
        return money_day;
    }

    public void setMoney_day(String money_day) {
        this.money_day = money_day;
    }




    public String getTitle_day() {
        return title_day;
    }

    public void setTitle_day(String title_day) {
        this.title_day = title_day;
    }


    public String getAddress_day() {
        return address_day;
    }

    public void setAddress_day(String address_day) {
        this.address_day = address_day;
    }

    public String getCompany_day() {
        return company_day;
    }

    public void setCompany_day(String company_day) {
        this.company_day = company_day;
    }

    public HrUser getAuthor() {
        return author;
    }

    public void setAuthor(HrUser author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "DayBean{" +
                "title_day='" + title_day + '\'' +
                ", money_day='" + money_day + '\'' +
                ", address_day='" + address_day + '\'' +
                ", company_day='" + company_day + '\'' +
                ", url='" + url + '\'' +
                ", dCount=" + dCount +
                ", author=" + author +
                '}';
    }
}
