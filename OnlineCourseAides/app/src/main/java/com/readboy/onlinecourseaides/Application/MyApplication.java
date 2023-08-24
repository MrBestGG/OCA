package com.readboy.onlinecourseaides.Application;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.readboy.onlinecourseaides.BuildConfig;
import com.readboy.onlinecourseaides.utils.Logger;

import java.util.Map;

public class MyApplication extends Application {

    private static MyApplication application;

    public static volatile Map<String, Integer> sEyesCareSwitchMap;
    public static volatile Map<String, Integer> mainEyesCareSwitchMap;

    public static volatile int notifySoundNumber = 0;

    private Intent data;

    private int code;

    public boolean isError = false;
    public boolean isError2 = false;

    public void onCreate() {
        super.onCreate();
        application = this;

        Logger.setIsEnableLog(BuildConfig.DEBUG);
    }

    public static MyApplication getInstances() {
        return application;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Intent getData() {
        return data;
    }

    public void setData(Intent data) {
        this.data = data;
    }

    /**
     * 显示提示信息
     * @param message：需要提示的信息
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
