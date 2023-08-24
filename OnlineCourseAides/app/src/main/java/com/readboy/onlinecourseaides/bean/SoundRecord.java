package com.readboy.onlinecourseaides.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * @Author jll
 * @Date 2022/11/30
 */
public class SoundRecord implements Serializable {
    private String packageName;
    private String soundTime;
    private String soundPath;
    private String soundToText;
    private ScreenShotRecord soundScreenShot; // 截图名称
    // 记录时长是秒数需要转化成  时分秒类型
    private int soundLengthTime;
    private String fileName;
    private transient Uri uri;

    private long timeCount;

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

    public long getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(long timeCount) {
        this.timeCount = timeCount;
    }

    @Override
    public String toString() {
        return "SoundRecord{" +
                "packageName='" + packageName + '\'' +
                ", soundTime='" + soundTime + '\'' +
                ", soundPath='" + soundPath + '\'' +
                ", soundToText='" + soundToText + '\'' +
                ", soundScreenShot=" + soundScreenShot +
                ", soundLengthTime=" + soundLengthTime +
                ", fileName='" + fileName + '\'' +
                ", uri=" + uri +
                ", timeCount=" + timeCount +
                '}';
    }

    public int getSoundLengthTime() {
        return soundLengthTime;
    }

    public void setSoundLengthTime(int soundLengthTime) {
        this.soundLengthTime = soundLengthTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSoundTime() {
        return soundTime;
    }

    public void setSoundTime(String soundTime) {
        this.soundTime = soundTime;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public String getSoundToText() {
        return soundToText;
    }

    public void setSoundToText(String soundToText) {
        this.soundToText = soundToText;
    }

    public ScreenShotRecord getSoundScreenShot() {
        return soundScreenShot;
    }

    public void setSoundScreenShot(ScreenShotRecord soundScreenShot) {
        this.soundScreenShot = soundScreenShot;
    }
}
