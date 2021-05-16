package com.gpfei.recruit.beans;

import cn.bmob.v3.BmobObject;

public class PartAndResume extends BmobObject {
    private User user;
    private Boolean delivery;
    private Boolean collect;
    private WeekendBean weekendBean;

    public Boolean getDelivery() {
        return delivery;
    }
    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }
    public Boolean getCollect() {
        return collect;
    }
    public void setCollect(Boolean collect) {
        this.collect = collect;
    }

//    public DayBean getDayBean() {
//        return dayBean;
//    }
//
//    public DayBean getdayBean(){
//        return dayBean;
//    }
//    public void setDayBean(DayBean dayBean) {
//        this.dayBean = dayBean;
//    }

    public WeekendBean getweekendBean() {
        return weekendBean;
    }

    public WeekendBean getWeekendBean() {
        return weekendBean;
    }

    public void setWeekendBean(WeekendBean weekendBean) {
        this.weekendBean = weekendBean;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
