package com.readboy.onlinecourseaides.bean;

import android.net.Uri;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author jll
 * @Date 2022/11/30
 */
public class ScreenRecord implements Serializable {
    public int index;
    private String packageName;
    private String screenRecordTime;
    private String screenRecordPath;
    private String fileName;
    private ScreenShotRecord screenRecordScreenShot;
    // 记录时长是秒数需要转化成  时分秒类型
    private int screenRecordLengthTime;

    private transient Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ScreenRecord{" +
                "packageName='" + packageName + '\'' +
                ", screenRecordTime='" + screenRecordTime + '\'' +
                ", screenRecordPath='" + screenRecordPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", screenRecordImgPath='" + screenRecordScreenShot + '\'' +
                ", screenRecordLengthTime=" + screenRecordLengthTime +
                '}';
    }

    public int getScreenRecordLengthTime() {
        return screenRecordLengthTime;
    }

    public void setScreenRecordLengthTime(int screenRecordLengthTime) {
        this.screenRecordLengthTime = screenRecordLengthTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getScreenRecordTime() {
        return screenRecordTime;
    }

    public void setScreenRecordTime(String screenRecordTime) {
        this.screenRecordTime = screenRecordTime;
    }

    public String getScreenRecordPath() {
        return screenRecordPath;
    }

    public void setScreenRecordPath(String screenRecordPath) {
        this.screenRecordPath = screenRecordPath;
    }

    public ScreenShotRecord getScreenRecordScreenShot() {
        return screenRecordScreenShot;
    }

    public void setScreenRecordScreenShot(ScreenShotRecord screenRecordScreenShot) {
        this.screenRecordScreenShot = screenRecordScreenShot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScreenRecord)) return false;
        ScreenRecord that = (ScreenRecord) o;
        return Objects.equals(getPackageName(), that.getPackageName()) && Objects.equals(getScreenRecordTime(), that.getScreenRecordTime()) && Objects.equals(getScreenRecordPath(), that.getScreenRecordPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPackageName(), getScreenRecordTime(), getScreenRecordPath());
    }
}
