package com.readboy.onlinecourseaides.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 返回结果接收器
 */
public class RecordCodeReceiver extends BroadcastReceiver {

    // 服务名称
    public static final String SERVICE_NAME = "SERVICE_NAME";
    public static final String SERVICE_NAME_SOUND = "SERVICE_NAME_SOUND";
    public static final String SERVICE_NAME_RECORD = "SERVICE_NAME_RECORD";
    public static final String SERVICE_NAME_SCREENSHOTS = "SERVICE_NAME_SCREENSHOTS";

    //服务类型 用于接收服务返回参数  一个服务有两个功能才有
    public static final String SERVICE_TYPE = "SERVICE_TYPE";
    public static final String SERVICE_TYPE_SOUND = "SERVICE_TYPE_SOUND";
    public static final String SERVICE_TYPE_RECORD = "SERVICE_TYPE_RECORD";
    public static final String SERVICE_TYPE_SCREEN_SHOTS = "SERVICE_TYPE_SCREEN_SHOTS";

    public static final String ACTION_RECORD_RESULT = "ACTION_RECORDED_RESULT";
    public static final String RECORD_CODE_SUCCESS = "success";
    public static final String RECORD_CODE_ERROR = "error";
    public static final String RECORD_RESULT_CODE = "code";

    public static final String RESULT_FILE_PATH = "filePath";
    public static final String RESULT_FILE_NAME = "fileName";
    public static final String RESULT_FILE_SCREEN_SHOTS_PATH = "screenShotsFilePath";
    public static final String RESULT_FILE_SCREEN_SHOTS_NAME = "screenShotsFileName";

    private RecordCodeListener listener;

    public RecordCodeReceiver(RecordCodeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_RECORD_RESULT)) {
            listener.doRecordTask(intent);
        }
    }

    public interface RecordCodeListener {
        void doRecordTask(Intent type);
    }
}
