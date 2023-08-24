package com.readboy.onlinecourseaides.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *  接收器  控制录制
 */
public class RecordReceiver extends BroadcastReceiver {

    public static final String START_SOUND_RECORD = "RECORD_START_SOUND_RECORD";
    public static final String STOP_SOUND_RECORD = "RECORD_STOP_SOUND_RECORD";
    public static final String START_SCREEN_RECORD = "RECORD_START_SCREEN_RECORD";
    public static final String STOP_SCREEN_RECORD = "RECORD_STOP_SCREEN_RECORD";
    public static final String START_SCREEN_SHOTS = "RECORD_START_SCREEN_SHOTS";
    public static final String TYPE_NORMAL = "type_normal";
    public static final String TYPE_START = "type_start";
    public static final String TYPE_STOP = "type_stop";
    public static final String CODE_SUCCESS = "type_stop";
    public static final String CODE_ERROR = "type_stop";

    private RecordControlListener listener;

    public RecordReceiver(RecordControlListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case START_SOUND_RECORD:
                listener.doSoundRecordTask(TYPE_START);
                Log.d(TAG, "onReceive: TYPE_START");
                break;
            case STOP_SOUND_RECORD:
                listener.doSoundRecordTask(TYPE_STOP);
                Log.d(TAG, "onReceive: TYPE_STOP");
                break;
            case START_SCREEN_RECORD:
                listener.doScreenRecordTask(TYPE_START);
                Log.d(TAG, "onReceive: START_SCREEN_RECORD TYPE_START");
                break;
            case STOP_SCREEN_RECORD:
                listener.doScreenRecordTask(TYPE_STOP);
                Log.d(TAG, "onReceive: STOP_SCREEN_RECORD TYPE_STOP");
                break;
            case START_SCREEN_SHOTS:
                listener.doScreenShotsTask(TYPE_NORMAL);
                break;
        }
    }

    public interface RecordControlListener {
        void doSoundRecordTask(String type);
        void doScreenRecordTask(String type);
        void doScreenShotsTask(String type);
    }

    private static final String TAG = "RecordReceiver";
}
