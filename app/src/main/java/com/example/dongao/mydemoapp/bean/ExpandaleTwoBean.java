package com.example.dongao.mydemoapp.bean;

import java.util.List;

/**
 * Created by xd on 2017/5/25.
 */

public class ExpandaleTwoBean {

    private List<ExpandaleThridBean> thridBeanList;

    public ExpandaleTwoBean(List<ExpandaleThridBean> thridBeanList) {
        this.thridBeanList = thridBeanList;
    }

    public List<ExpandaleThridBean> getThridBeanList() {
        return thridBeanList;
    }

    public void setThridBeanList(List<ExpandaleThridBean> thridBeanList) {
        this.thridBeanList = thridBeanList;
    }

}
