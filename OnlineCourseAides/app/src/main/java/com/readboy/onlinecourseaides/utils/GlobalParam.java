package com.readboy.onlinecourseaides.utils;

import android.Manifest;

/**
 * 全局配置参数
 */
public class GlobalParam {
    public static final String MY_PREFIX = "com.readboy.onlinecourseaides.";
    // 广播参数
    public static final String NOTIFY_SERVICE_START_ANIM = MY_PREFIX + "START_ANIM";  //通知关闭或者打开工具窗口
    public static final String NOTIFY_SERVICE_ONCLINECOURSE_START = "com.readboy.parentmanager.ACTION_START_PACKAGE";  //通知打开应用
    public static final String NOTIFY_SERVICE_ONCLINECOURSE_STOP = "com.readboy.parentmanager.ACTION_PAUSE_PACKAGE";  //通知网课应用关闭

    public static final String NOTIFY_SERVICE_REFRESH_CACHE = "com.readboy.parentmanager.ACTION_SERVICE_REFRESH_CACHE";  //通知服务更新缓存
    public static final String NOTIFY_SERVICE_REFRESH_APPLIST = "com.readboy.parentmanager.ACTION_SERVICE_REFRESH_APPLIST";  //通知服务更新APP列表
    public static final String NOTIFY_SERVICE_REFRESH_APP = "com.readboy.parentmanager.ACTION_SERVICE_REFRESH_APP";  //通知服务更新APP
    public static final String NOTIFY_SERVICE_REFRESH_DATA = "REFRESH_STATUS_DATA";
    public static final String NOTIFY_SERVICE_REFRESH_DATA_APP_LIST = "REFRESH_STATUS_DATA";
    public static final String NOTIFY_SERVICE_REFRESH_DATA_APP_ITEM = "REFRESH_STATUS_DATA_ITEM";
    //系统动画时长
    //进度条
    //延迟消失时长

    // 服务
    public static final String ACTION_SERVICE_RECORD_SCREEN_START = MY_PREFIX + "RECORD_SCREEN_START";
    public static final String ACTION_SERVICE_RECORD_SOUND_START = MY_PREFIX + "RECORD_SOUND_START";
    public static final String ACTION_SERVICE_RECORD_SOUND_STOP = MY_PREFIX + "RECORD_SOUND_STOP";
    public static final String ACTION_SERVICE_RECORD_SCREEN_STOP = MY_PREFIX + "RECORD_SCREEN_STOP";

    // 权限
    public static final String[] PERMISSIONS = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // 本应用包名
    public static final String MY_APP_PACKAGENAME = "com.readboy.onlinecourseaides";
    public static final String GREENBROWSER_APP_PACKAGENAME = "cn.dream.android.greenbrowser";

    // 请求需要的  包名:app_id  鉴权码:app_sec
    //AI 精准学 =>  app_id:第三方应用要求必须有,app_sec: 如下
    public static final String APP_SEC_COURSE =  "c985f40c7605b753bbdff31e0a50d7cf";

    //家长助手接口 => 绿色上网平板端 => app_id:必须有,app_sec:必须有
    public static final String APP_ID_GREEN =  "aide.readboy.com";
    public static final String APP_SEC_GREEN =  "2aeb3869d9b8ed736d6c572b08db1851";

    //ACache 缓存设置 KAY
    public static final String APP_LIST_CACHE = "APP_LIST_CACHE";
    public static final String APP_DEFAULT_LIST_CACHE = "APP_DEFAULT_LIST_CACHE";
    public static final String COURSE_LIST_CACHE = "COURSE_LIST_CACHE";
    public static final String COURSE_DEFAULT_LIST_CACHE = "COURSE_DEFAULT_LIST_CACHE";
    public static final String HISTORY_LIST_CACHE = "OAC_HISTORY_LIST_CACHE";
    public static final String HISTORY_LIST_CACHE_KEYS = "OAC_HISTORY_LIST_CACHE_KEYS";
}
