package com.gpfei.recruit.beans;

import cn.bmob.v3.BmobObject;

public class PracticeAndResume extends BmobObject {
    private User user;
    private Boolean delivery;
    private Boolean collect;
    private SelectionBean selectionBean;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public SelectionBean getSelectionBean() {
        return selectionBean;
    }

    public void setSelectionBean(SelectionBean selectionBean) {
        this.selectionBean = selectionBean;
    }
}
