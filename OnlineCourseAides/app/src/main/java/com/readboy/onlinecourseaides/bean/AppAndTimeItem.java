package com.readboy.onlinecourseaides.bean;

/**
 * @Author jll
 * @Date 2022/12/5
 */
public class AppAndTimeItem {
    private String time;
    private String appName;

    @Override
    public String toString() {
        return "AppAndTimeItem{" +
                "time='" + time + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
