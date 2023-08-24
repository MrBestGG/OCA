package com.readboy.onlinecourseaides.bean;

import android.net.Uri;

/**
 * @Author jll
 * @Date 2022/12/1
 */
public class ScreenShotRecord {
    private String imgPath;
    private String screenShotTime;
    private String fileName;
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
        return "ScreenShotRecord{" +
                "imgPath='" + imgPath + '\'' +
                ", screenShotTime='" + screenShotTime + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getScreenShotTime() {
        return screenShotTime;
    }

    public void setScreenShotTime(String screenShotTime) {
        this.screenShotTime = screenShotTime;
    }
}
