package com.readboy.onlinecourseaides.service.record;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemPropertiesInvoke {
    private static final String TAG = "SystemPropertiesInvoke";
    private static Method getStringMethod = null;

    public static String getStringByKey(String key, String def){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return getStringAndroid9(key,def);
        }else{
            return getString2(key,def);
        }
    }
    public static int getIntByKey(String key, int def){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return getIntAndroid9(key,def);
        }else{
            return getInt(key,def);
        }
    }

    // key=ro.readboy.study_focus_mode==1 表示支持专注模式
    public static int getInt(String key, int def) {
        try {
            if (getStringMethod == null) {
                getStringMethod = Class.forName("android.os.SystemProperties")
                        .getMethod("getInt", String.class, int.class);
            }
            return (int) getStringMethod.invoke(null, key, def);
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
            return def;
        }
    }

    public static int getIntAndroid9(String key,int def) {
        try {
            Log.e(TAG, "---------------- 1");
            if (getStringMethod == null) {
                Log.e(TAG, "---------------- 2");
                getStringMethod = Class.forName("android.os.SystemProperties").getMethod("get",String.class);
                Log.e(TAG, "---------------- 3");
            }
            Log.e(TAG, "---------------- 4:"+getStringMethod.invoke(null,key));
            int val=Integer.valueOf((String) getStringMethod.invoke(null,key));
            Log.e(TAG, "---------------- 5:"+val);
            return val;
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
            return def;
        }
    }


    public static String getString(String key, String def) {
        try {
            if (getStringMethod == null) {
                getStringMethod = Class.forName("android.os.SystemProperties")
                        .getMethod("get", String.class, String.class);
            }
            return (String) getStringMethod.invoke(null, key, def);
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
            return def;
        }
    }
    public static String getString2(String key, String def) {
        Class<?> cls = null;
        try {
            cls = Class.forName("android.os.SystemProperties");
            Method hideMethod = cls.getMethod("get", String.class);
//                Object object = cls.newInstance();
//                mFrontCameraType = (String) hideMethod.invoke(object, "ro.readboy.front.camera.type");
            String   mFrontCameraType = (String) hideMethod.invoke(null, key);
            if(!TextUtils.isEmpty(mFrontCameraType)) {
                return mFrontCameraType;
            }
        } catch (ClassNotFoundException e) {
            Log.e("getArType", "get error() ", e);
        } catch (NoSuchMethodException e) {
            Log.e("getArType", "get error() ", e);
        } catch (IllegalAccessException e) {
            Log.e("getArType", "get error() ", e);
        } catch (IllegalArgumentException e) {
            Log.e("getArType", "get error() ", e);
        } catch (InvocationTargetException e) {
            Log.e("getArType", "get error() ", e);
        } catch (Exception e) {
            Log.e("getArType", "get error() ", e);
            e.printStackTrace();
        }

        return def;
    }

    public static String getStringAndroid9(String key, String def) {
        try {
            Log.e(TAG, "---------------- 1");
            if (getStringMethod == null) {
                Log.e(TAG, "---------------- 2");
                getStringMethod = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
                Log.e(TAG, "---------------- 3");
            }
            Log.e(TAG, "---------------- 4");
            return (String) getStringMethod.invoke(null, key);
        } catch (Exception e) {
            Log.e(TAG, "Platform error: " + e.toString());
            return def;
        }
    }
}
