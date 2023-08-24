package com.readboy.onlinecourseaides.bean;

import java.util.Objects;

/**
 * 网课云平台数据类
 */
public class CourseInfo implements OnlineClassData {
    // 地区
    private String title;

    // 链接
    private String url;

    private boolean enabledDel = false;

    // 白名单ID  用于删除
    private String witheId;

    private int source;

    private int collect;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public CourseInfo() {
    }

    public CourseInfo(String title) {
        this.title = title;
    }

    public CourseInfo(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWitheId() {
        return witheId;
    }

    public void setWitheId(String witheId) {
        this.witheId = witheId;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", enabledDel=" + enabledDel +
                ", witheId='" + witheId + '\'' +
                ", source=" + source +
                ", collect=" + collect +
                '}';
    }

    public boolean isEnabledDel() {
        return enabledDel;
    }

    public void setEnabledDel(boolean enabledDel) {
        this.enabledDel = enabledDel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseInfo)) return false;
        CourseInfo that = (CourseInfo) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getUrl(), that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getUrl());
    }
}
