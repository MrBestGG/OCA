package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

/**
 * @Author jll
 * @Date 2022/12/19
 */
public class AppItem implements ItemBean{

    @SerializedName("app_name")
    private String appName;

    @SerializedName("icon")
    private String appIcon;

    @SerializedName("pack_name")
    private String packageName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppItem{" +
                "appName='" + appName + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return null;
    }
}
