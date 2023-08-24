package com.readboy.onlinecourseaides.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Sophon
 * SystemUI 内置功能工具类
 **/
public class SystemUISettings {

    private static final String TAG = "SystemUISettings";
    private static final Uri SYSTEMUI_SETTINGS_URI = Uri.parse("content://com.readboy.systemui.settings/settings");

    private static void putStringSettings(ContentResolver resolver, String name, String value) {
        ContentValues values = new ContentValues(2);
        values.put("name", name);
        values.put("value", value);
        resolver.insert(SYSTEMUI_SETTINGS_URI, values);
        resolver.notifyChange(SYSTEMUI_SETTINGS_URI, null);
    }

    private static String getStringSettings(ContentResolver resolver, String name) {
        Cursor cursor = resolver.query(SYSTEMUI_SETTINGS_URI, new String[]{"value"}, "name=?", new String[]{name}, null);
        if (cursor != null) {
            String result = "";
            if (cursor.moveToFirst()) {
                int column = cursor.getColumnIndex("value");
                if (column >= 0) {
                    result = cursor.getString(column);
                }
            }
            cursor.close();
            return result;
        }
        return "";
    }

    /**
     * 获取SystemUI内置Settings Uri，用于注册ContentObserver监听
     */
    public static Uri getUri() {
        return SYSTEMUI_SETTINGS_URI;
    }

    /**
     * 获取Settings内容
     *
     * @param resolver 内容提供器
     * @param name     对应key值
     * @return 对应value
     */
    public static String get(ContentResolver resolver, String name) {
        try {
            return getStringSettings(resolver, name);
        } catch (Exception e) {
            Log.e(TAG, "Get Settings error", e);
        }
        return "";
    }

    /**
     * 写入Settings内容
     *
     * @param resolver 内容提供器
     * @param name     对应key值
     * @param value    待写入内容
     * @return 是否成功
     */
    public static boolean put(ContentResolver resolver, String name, String value) {
        try {
            putStringSettings(resolver, name, value);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Put Settings error", e);
        }
        return false;
    }

    /**
     * 获取Int内容
     *
     * @see SystemUISettings#get
     */
    public static int getInt(ContentResolver resolver, String name, int defValue) {
        try {
            String value = getStringSettings(resolver, name);
            return TextUtils.isEmpty(value) ? defValue : Integer.parseInt(value);
        } catch (Exception e) {
            Log.e(TAG, "Get Settings error", e);
        }
        return defValue;
    }

    /**
     * 写入Int内容
     *
     * @see SystemUISettings#put
     */
    public static boolean putInt(ContentResolver resolver, String name, int value) {
        return put(resolver, name, String.valueOf(value));
    }

    /**
     * 获取Float内容
     *
     * @see SystemUISettings#get
     */
    public static float getFloat(ContentResolver resolver, String name, float defValue) {
        try {
            String value = getStringSettings(resolver, name);
            return TextUtils.isEmpty(value) ? defValue : Float.parseFloat(value);
        } catch (Exception e) {
            Log.e(TAG, "Get Settings error", e);
        }
        return defValue;
    }

    /**
     * 写入Float内容
     *
     * @see SystemUISettings#put
     */
    public static boolean putFloat(ContentResolver resolver, String name, int value) {
        return put(resolver, name, String.valueOf(value));
    }

    /**
     * 获取Boolean内容
     *
     * @see SystemUISettings#get
     */
    public static boolean getBoolean(ContentResolver resolver, String name, boolean defValue) {
        try {
            String value = getStringSettings(resolver, name);
            return TextUtils.isEmpty(value) ? defValue : Boolean.parseBoolean(value);
        } catch (Exception e) {
            Log.e(TAG, "Get Settings error", e);
        }
        return defValue;
    }

    /**
     * 写入Boolean内容
     *
     * @see SystemUISettings#put
     */
    public static boolean putBoolean(ContentResolver resolver, String name, int value) {
        return put(resolver, name, String.valueOf(value));
    }
}
