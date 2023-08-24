package com.readboy.onlinecourseaides.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.readboy.onlinecourseaides.utils.GlobalParam;

/**
 * @Author jll
 * @Date 2022/12/1
 *
 * 接收广播，监听，网课app有没有启动
 */
public class DetectionAppReceiver extends BroadcastReceiver {
    private static final String TAG = "DetectionAppReceiver";

    public DetectionAppReceiver(DetectionAppListener serviceDoTaskListener) {
        this.detectionAppListener = serviceDoTaskListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String pkgName = intent.getStringExtra("package_name");
        if(intent.getAction().equals(GlobalParam.NOTIFY_SERVICE_ONCLINECOURSE_START)) {
            detectionAppListener.doTask(pkgName,0);
            Log.d(TAG, "onReceive: app start " + pkgName);
        }else if(intent.getAction().equals(GlobalParam.NOTIFY_SERVICE_ONCLINECOURSE_STOP)){
            detectionAppListener.doTask(pkgName,1);
            Log.d(TAG, "onReceive: app pause stop " + pkgName);
        }
    }

    private DetectionAppListener detectionAppListener;

    public interface DetectionAppListener {
        //[0:开始,1:结束]
        void doTask(String packageName,int index);
    }
}
