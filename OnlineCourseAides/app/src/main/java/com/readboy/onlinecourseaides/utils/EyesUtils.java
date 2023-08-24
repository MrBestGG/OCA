package com.readboy.onlinecourseaides.utils;

import static com.readboy.onlinecourseaides.utils.Logger.d;
import static com.readboy.onlinecourseaides.utils.Logger.e;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;

import com.readboy.onlinecourseaides.Application.MyApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * 护眼卫士调用-用于网课助手调试 2022/12/5 19：20
 */
public class EyesUtils {
    private static final String TAG = EyesUtils.class.getSimpleName();

    //护眼卫士状态
    public static Uri URL_SWITCH = Uri.parse("content://com.dream.launcher_provider_new/switch");
    public static final String KEY_BRIGHT = "switch_bright";//感光
    public static final String KEY_DARK = "switch_dark";//暗光
    public static final String KEY_PROXIMITY = "switch_proximity";//距离
    public static final String KEY_OVERTURN = "switch_overturn";//翻转
    public static final String KEY_REST = "switch_rest";//休息
    public static final String KEY_SHAKE = "switch_shake";//抖动
    public static final String KEY_OPEN = "switch_open";//开关
    public static final String KEY_EXERCISE = "switch_exercise";//眼保健操
    public static final String KEY_NO_EXIT_WHEN_REST = "switch_no_exit_when_rest";//休息时不退出
    public static final String KEY_NOTICE = "switch_notice";//通知
    public static final String KEY_DISPLAY_DALTONIZER = "switch_display_daltonizer";//黑白阅读
    public static final String KEY_WARM_MODE = "switch_warm_mode";//滤蓝光
    public static final String KEY_TIMING = "switch_timing";//定时
    public static final String[] keys = new String[]{KEY_BRIGHT, KEY_DARK, KEY_PROXIMITY, KEY_OVERTURN, KEY_REST, KEY_SHAKE,
            KEY_OPEN, KEY_EXERCISE, KEY_NO_EXIT_WHEN_REST, KEY_NOTICE, KEY_DISPLAY_DALTONIZER, KEY_WARM_MODE, KEY_TIMING};
    public static int sit_value = -1;//坐姿-存储变化之前的坐姿开关值
    public static volatile boolean switch_open = false;//护眼总开关是否打开 ，默认false
    public static volatile boolean switch_warm_mode = false;//滤蓝光默认false

    /**
     * 仅开启护眼卫士的滤蓝光功能 2022/12/05 19:25
     */
    public static int openOnlyWarm() {
        long a = SystemClock.elapsedRealtime();

        ContentResolver mResolver = MyApplication.getInstances().getContentResolver();
        closeAllStatus();
        ContentValues values = new ContentValues();
        values.put("switchNum", 1);//1打开， 0关闭
        int row = mResolver.update(URL_SWITCH, values, "switchName in (?, ?) ", new String[]{KEY_OPEN, KEY_WARM_MODE});
        d(TAG, "openOnlyWarm: " + row + ", finished in " + (SystemClock.elapsedRealtime() - a) + " ms");
        return row;
    }

    /**
     * 恢复护眼卫士原来的用户设计的功能模式 2022/12/05 20:00
     *
     * @param switchMap 状态
     */
    public static boolean backStatus(Map<String, Integer> switchMap) {
        try {
            ContentResolver mResolver = MyApplication.getInstances().getContentResolver();
            for (String key : keys) {
                ContentValues values = new ContentValues();
                values.put("switchNum", switchMap.get(key));
                mResolver.update(URL_SWITCH, values, "switchName=?", new String[]{key});
            }

            sendBroadcast(sit_value);
            return true;
        } catch (Exception e) {
            Logger.me(TAG, "backStatus: failed", e);
            return false;
        }
    }

    public static boolean closeNotify() {
        String[] keys1 = new String[]{KEY_PROXIMITY, KEY_OVERTURN, KEY_REST, KEY_SHAKE, KEY_TIMING, KEY_EXERCISE, KEY_BRIGHT};
        HashMap<String, Integer> switchMap = new HashMap<>();
        switchMap.put(KEY_PROXIMITY, 0);
        switchMap.put(KEY_OVERTURN, 0);
        switchMap.put(KEY_REST, 0);
        switchMap.put(KEY_SHAKE, 0);
        switchMap.put(KEY_TIMING, 0);
        switchMap.put(KEY_EXERCISE, 0);
        switchMap.put(KEY_BRIGHT, 0);
        try {
            ContentResolver mResolver = MyApplication.getInstances().getContentResolver();
            for (String key : keys1) {
                ContentValues values = new ContentValues();
                values.put("switchNum", switchMap.get(key));
                mResolver.update(URL_SWITCH, values, "switchName=?", new String[]{key});
            }
            sendBroadcast(0);
            return true;
        } catch (Exception e) {
            Logger.me(TAG, "backStatus: failed", e);
            return false;
        }
    }

    /**
     * 关闭所有护眼状态 2022/12/05 19:25
     */
    public static int closeAllStatus() {
        ContentResolver mResolver = MyApplication.getInstances().getContentResolver();
        ContentValues values = new ContentValues();
        values.put("switchNum", 0);
        //13个key
        int row = mResolver.update(URL_SWITCH, values, "switchName in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", keys);

        sendBroadcast(0);
        return row;
    }

    /***
     * 获取护眼卫士当前状态 2022/12/05 19:25
     * @return 一个map数组，方便后续恢复护眼功能更新
     */
    @SuppressLint("Range")
    public static Map<String, Integer> getSwitch() {
        long a = SystemClock.elapsedRealtime();
        ContentResolver mResolver = MyApplication.getInstances().getContentResolver();

        Cursor cursor = null;
        Map<String, Integer> switchMap = new HashMap<>();

        try {
            cursor = mResolver.query(URL_SWITCH, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("switchName"));
                    int value = cursor.getInt(cursor.getColumnIndex("switchNum"));
                    switchMap.put(name, value);
                }
            }
        } catch (Exception e) {
            //
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        switch_open = getSwitch(switchMap, KEY_OPEN);
        switch_warm_mode = getSwitch(switchMap, KEY_WARM_MODE);
        sit_value = Settings.Global.getInt(mResolver, "enable_magic_facedetect", -1);//存储当前状态的坐姿开关值
        e(TAG, "sit_value: " + sit_value);
        d(TAG, "getSwitch: finished in " + (SystemClock.elapsedRealtime() - a) + " ms");
        return switchMap;
    }

    public static boolean getSwitch(Map<String, Integer> map, String key) {
        if (map == null)
            return false;
        Integer i = map.get(key);

        if (i == null) {
            return false;
        }
        return i == 1;
    }

    /**
     * 发送坐姿识别开关广播 2022/12/5 20:00
     *
     * @param value 0 关闭/ 1 打开
     */
    public static void sendBroadcast(int value) {
        Context ctx = MyApplication.getInstances();
        ContentResolver mResolver = ctx.getContentResolver();
        int sit_en = Settings.Global.getInt(mResolver, "enable_magic_facedetect", -1);
        if (sit_en == -1 || value == sit_en) {
            //负一不显示不处理
            return;
        }
        String action = "android.intent.action.RecordDataToSystemProviderBySettings";
        Intent intent2 = new Intent(action);
        intent2.putExtra("table_name", "Global");
        intent2.putExtra("key_name", "enable_magic_facedetect");
        intent2.putExtra("key_value", value);
        intent2.putExtra("key_Type", "int");//String, int, float, long
        ctx.sendBroadcast(intent2);
        e(TAG, "sed: " + value);
    }

}
