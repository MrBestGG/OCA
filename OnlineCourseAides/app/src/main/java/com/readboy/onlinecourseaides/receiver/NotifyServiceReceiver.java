package com.readboy.onlinecourseaides.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.readboy.onlinecourseaides.utils.GlobalParam;

/**
 * @Author jll
 * @Date 2022/12/7
 */
public class NotifyServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "NotifyServiceReceiver";

    public NotifyServiceReceiver(NotifyServiceListener serviceDoTaskListener) {
        this.notifyServiceListener = serviceDoTaskListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(GlobalParam.NOTIFY_SERVICE_REFRESH_CACHE)) {
            Log.d(TAG, "onReceive: NotifyService refresh cache");
            String recordList = intent.getStringExtra(GlobalParam.NOTIFY_SERVICE_REFRESH_DATA);
            notifyServiceListener.doTask(recordList);
        }else if(intent.getAction().equals(GlobalParam.NOTIFY_SERVICE_REFRESH_APPLIST)) {
            Log.d(TAG, "onReceive: NotifyService refresh default appList");
            String appList = intent.getStringExtra(GlobalParam.NOTIFY_SERVICE_REFRESH_DATA_APP_LIST);
            notifyServiceListener.refreshDefaultAppList(appList);
        }
    }

    private NotifyServiceListener notifyServiceListener;

    public interface NotifyServiceListener {
        //[0:开始,1:结束]
        void doTask(String s);
        // 添加APP， 刷新默认APP列表
        void refreshDefaultAppList(String s);
    }
}
