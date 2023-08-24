package com.readboy.onlinecourseaides.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParentManagerUtils {
    private static final String PM_COLUMN_ID = "_id";
    private static final String PM_COLUMN_PASSWORD = "password";
    private static final String PM_COLUMN_STATE = "state";
    private static Uri PM_URI = Uri.parse("content://com.readboy.parentmanager.AppContentProvider/user_info");

    /**
     * 家长管理 resultCode 输入密码错误
     */
    public static final int PASSWORD_MANAGER_ERROR = 0;

    /**
     * 家长管理 resultCode 输入密码正确
     */
    public static final int PASSWORD_MANAGER_CORRECT = 1;

    /**
     * 家长管理 resultCode 没有密码
     */
    public static final int PASSWORD_MANAGER_EMPTY = 2;

    /**
     * 家长管理 resultCode 创建密码失败，已经有密码
     */
    public static final int PASSWORD_MANAGER_EXIST = 3;

    public static boolean popupPMPasswordInput(Activity context, int requestCode) {
        if (null == context || context.isFinishing() || context.isDestroyed()) {
            return false;
        }
        try {
            // 调用家长管理
            Intent intent = new Intent();
            intent.setAction("android.readboy.parentmanager.INPUT_PASSWORD");
            context.startActivityForResult(intent, requestCode);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取密码
    public static String getPMPassword(Context context) {
        Cursor cursor = null;
        try {
            ContentResolver mResolver = context.getContentResolver();
            cursor= mResolver.query(PM_URI,null, PM_COLUMN_ID + " > ? ", new String[]{0 + ""}, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(PM_COLUMN_PASSWORD));
                    //int state=c.getInt(c.getColumnIndex(COLUMN_STATE))
                    return password;
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
        return null;
    }

    //设置状态 state 1-开启 0-关闭
    public static boolean insertPMOpenState(Context context, String state){
        boolean isOk = false;
        Uri uri = Uri.parse("content://com.readboy.parentmanager.AppContentProvider/open_state");
        try{
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("state",state);
            resolver.insert(uri, values);
            isOk = true;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
        return isOk;
    }

    //修改密码
    public  static boolean updatePMPassWord(Context context, String password){
        if (null != context) {
            try {
                ContentResolver resolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(PM_COLUMN_PASSWORD, password);
                values.put(PM_COLUMN_STATE, 1);
                int state = resolver.update(PM_URI, values, PM_COLUMN_ID + " > ?", new String[]{0 + ""});
                //state=0 操作失败 state=1 操作成功
                return state == 1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
        return false;
    }

    //设置密码
    public static boolean insertPMPassWord(Context context, String password){
        if (null != context) {
            try {
                ContentResolver resolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(PM_COLUMN_PASSWORD, password);
                values.put(PM_COLUMN_STATE, 1);
                Uri rUri = resolver.insert(PM_URI, values);
                return rUri != null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
        return false;
    }

    // hasPassword true-有密码  false-无密码
    public static boolean setPMPasswordState(ContentResolver cr, boolean hasPassword) {
        boolean isOk = false;
        try {
            Class clazz = Class.forName("android.provider.Settings$ParentManager");
            Method method = clazz.getDeclaredMethod("setCanLimitedState", ContentResolver.class, Boolean.TYPE);
            method.setAccessible(true);
            method.invoke(null, cr, hasPassword);
            isOk = true;
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     *	更新答案状态 (注:首次进入应用,调用getSwitch获取状态如果返回-1则调用一次updateSwitch此方法便于告诉服务器该应用是否支持远程管控答案 ,之后只需在切换答案开关时调用即可)
     * apkName 应用包名
     * switch_state 答案开关 0-关  1-开
     */
    public static int updateAnswerSwitch(Context context, String apkName,int switch_state){
        int state = 0;
        Cursor cursor=null;
        try {
            Uri uri = Uri
                    .parse("content://com.readboy.parentmanager.AppContentProvider/answer_control");
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("package_name",apkName);
            values.put("switch_state", switch_state);
            state = resolver.update(uri,values,null,null);
            //state=0 操作失败 state=1 操作成功
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return state;
    }

    /**
     * 根据应用包名获取答案状态 (可在进入应用获取一次答案开关)
     *  package_name 应用包名
     *  switch_state 答案开关 0-关  1-开
     * 注: 该方法返回-1 表示不支持远程管控答案
     */
    public static int getAnswerSwitch(Context context, String package_name) {
        Cursor cursor = null;
        try {
            ContentResolver mResolver = context.getContentResolver();
            Uri uri = Uri
                    .parse("content://com.readboy.parentmanager.AppContentProvider/answer_control");
            cursor= mResolver.query(uri,null, "package_name=?", new String[]{package_name}, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("package_name"));

                    @SuppressLint("Range") int switch_state = cursor.getInt(cursor.getColumnIndex("switch_state"));
//                    Log.i("zhh","cursor name: "+name);
//                    Log.i("zhh","cursor switch_state: "+switch_state);
                    return switch_state;
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
        return -1;
    }


    public static Uri getAnswerSwitchUri() {
        return Uri.parse("content://com.readboy.parentmanager.AppContentProvider/answer_control");
    }

//    /**
//     * 注册ContentObserver 监听数据变化
//     */
//    ContentResolver mResolver = this.getContentResolver();
//    mResolver.registerContentObserver(Uri.parse("content://com.readboy.parentmanager.AppContentProvider/answer_control"), true, mAnswerObserver);
//	mResolver.unregisterContentObserver(mAnswerObserver);


}
