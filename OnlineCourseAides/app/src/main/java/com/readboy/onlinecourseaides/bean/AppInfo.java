package com.readboy.onlinecourseaides.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Objects;

/**
 * 应用列表类
 *
 * @type {0:系统应用,1:读书郎应用,2:第三方应用}
 * @currentStatus {1:运行或后台运行,0:应用退出}
 * @currentAppInfoStatus {0:没添加到主页，1:已经添加}
 */
public class AppInfo implements OnlineClassData ,Serializable{
    public String appName = "";
    public String packageName = "";
    public String versionName = "";
    public String appIconUrl = "";
    public int versionCode = 0;
    public transient Drawable appIcon = null;
    private int type = 0;
    private int currentStatus = 0;
    private int currentAppInfoStatus = 0;
    private boolean enabledDelAppInfo = false;

    public AppInfo() {
    }

    public AppInfo(String appName, String packageName, String versionName) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
    }

    public AppInfo(String appName, String packageName, String versionName, int versionCode, Drawable appIcon, int type, int currentStatus) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.appIcon = appIcon;
        this.type = type;
        this.currentStatus = currentStatus;
    }

    public boolean isEnabledDelAppInfo() {
        return enabledDelAppInfo;
    }

    public void setEnabledDelAppInfo(boolean enabledDelAppInfo) {
        this.enabledDelAppInfo = enabledDelAppInfo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getCurrentAppInfoStatus() {
        return currentAppInfoStatus;
    }

    public void setCurrentAppInfoStatus(int currentAppInfoStatus) {
        this.currentAppInfoStatus = currentAppInfoStatus;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", appIconUrl='" + appIconUrl + '\'' +
                ", versionCode=" + versionCode +
                ", type=" + type +
                ", currentStatus=" + currentStatus +
                ", currentAppInfoStatus=" + currentAppInfoStatus +
                ", enabledDelAppInfo=" + enabledDelAppInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppInfo)) return false;
        AppInfo appInfo = (AppInfo) o;
        return getPackageName().equals(appInfo.getPackageName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPackageName());
    }
}
