package com.gpfei.recruit.beans;


import cn.bmob.v3.BmobObject;

public class SelectAndResume extends BmobObject {
    private DayBean dayBean ;
    private User user;
    private Boolean delivery;
    private Boolean collect;
    private MyUser myUser;
    private PostAndUser postAndUser;

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

    public DayBean getDayBean() {
        return dayBean;
    }

    public DayBean getdayBean(){
        return dayBean;
    }
    public void setDayBean(DayBean dayBean) {
        this.dayBean = dayBean;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SelectAndResume{" +
                "dayBean=" + dayBean +
                ", user=" + user +
                ", delivery=" + delivery +
                ", collect=" + collect +
                '}';
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public PostAndUser getPostAndUser() {
        return postAndUser;
    }

    public void setPostAndUser(PostAndUser postAndUser) {
        this.postAndUser = postAndUser;
    }
}
