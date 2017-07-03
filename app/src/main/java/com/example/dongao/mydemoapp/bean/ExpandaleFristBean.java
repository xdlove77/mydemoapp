package com.example.dongao.mydemoapp.bean;

import java.util.List;

/**
 * Created by xd on 2017/5/25.
 */

public class ExpandaleFristBean {
    private String kindName;
    private String className;

    private ExpandaleTwoBean bean;

    public ExpandaleFristBean(String kindName, String className, ExpandaleTwoBean bean) {
        this.kindName = kindName;
        this.className = className;
        this.bean = bean;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ExpandaleTwoBean getBean() {
        return bean;
    }

    public void setBean(ExpandaleTwoBean bean) {
        this.bean = bean;
    }
}
