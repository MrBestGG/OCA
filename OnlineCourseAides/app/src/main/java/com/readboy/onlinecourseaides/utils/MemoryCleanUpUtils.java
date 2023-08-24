package com.readboy.onlinecourseaides.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * @Author jll
 * @Date 2022/12/20
 */
public class MemoryCleanUpUtils {

    private static final String callPkgName = "com.readboy.killapp";
    public static final String AUTOHORITY = "com.readboy.killapp_rby_shared_data";
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTOHORITY + "/MsgCenter");
    public static final String ACTION_FUN_NAME = "exsist_helpRemoveTaskFromRecentAppList";

    /**
     * 判断设置中是否支持这个函数的访问
     * @param context
     * @param actionFunName
     * @return
     */
    public static boolean checkCallMessageGetExsist(Context context, String actionFunName){
        try {
            Context ctx = context.createPackageContext(callPkgName, Context.CONTEXT_IGNORE_SECURITY);
            ContentResolver contentResolver = ctx.getContentResolver();
            Bundle bundleIn = new Bundle();
            bundleIn.putString("callme", context.getPackageName());

            Bundle bundle = null;
            bundle = contentResolver.call(NOTIFY_URI, actionFunName, null, bundleIn);
            if (bundle != null && bundle.containsKey(actionFunName) && bundle.getBoolean(actionFunName, false)){
                // 有这个功能
                Log.d(TAG, "=4=====divhee===================check_CallMessageGetExsist=======");
                return true;
            }
        } catch (Exception e){
            Log.d(TAG, "=4=====divhee===================check_CallMessageGetExsist======="+e);
        }
        return false;
    }


    public static void doCallMessageHelperSettingsReceiver200(Context context,String pkgNames){
        Bundle bundleIn = new Bundle();
        bundleIn.putString("callme", context.getPackageName());
        Log.d(TAG, "doCallMessageHelperSettingsReceiver200: pkgname=>"+pkgNames);
        bundleIn.putString("ignorePkgList", pkgNames);
        bundleIn.putString("removeAllTask", "1");

        try {
            Context ctx = context.createPackageContext(callPkgName, Context.CONTEXT_IGNORE_SECURITY);
            ContentResolver contentResolver = ctx.getContentResolver();
            Bundle bundle = null;
            bundle = contentResolver.call(NOTIFY_URI, "helpRemoveTaskFromRecentAppList", null, bundleIn);

            Log.d(TAG, "doCallMessageHelperSettingsReceiver200: dotask success");
        } catch (Exception e){
            Log.d("", "=4=====divhee===================call_helpRemoveTaskFromRecentAppList======="+e);
        }
    }

    private static final String TAG = "MemoryCleanUpUtils";
}
