package com.readboy.onlinecourseaides.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by Liming on 2020/6/5 16:31.
 * Call me : 13800138000
 * 用于设置语音助手状态，防止反复唤起语音助手导致系统重启
 * 主要逻辑： 在开始录音时关闭语音助手，在 onPause（onSuspend）中恢复，再次开始录音则重新关闭
 */
public  class VoiceAssistantUtil {

    private static String TAG = "VoiceSettingUtil";

    private  static final String DB_KEY_AI_ASSIST_WAKEUP_SWITCH = "readboy_ai_assist_wakeup_switch_enable";

    public  static  final int CLOSE_STATE = 0;
    public  static  final int OPEN_STATE = 1;

    public  int  originalState = -1 ;              // 语音助手原始状态
    public  boolean  isFirstRecord = true ;

    /**
     * 语音助手状态，0 表示关闭
     * @return
     */
    public static int getVoiceAssiatnceState(Context context){

        int state = Settings.System.getInt( context.getContentResolver(), DB_KEY_AI_ASSIST_WAKEUP_SWITCH, 1);

        return  state;
    }


    public static int setVoiceAssisitantState( Context context, int state ){

        int oldState = getVoiceAssiatnceState(context);

        if (state == oldState) {
            return oldState;
        }

//        Log.d(TAG, "setVoiceAssisitantClose: -------- setState----- aiAssistWakeupMode =" + state );
        Intent intent = new Intent();
        intent.setAction("android.intent.action.RecordDataToSystemProviderBySettings" );
        intent.putExtra("table_name","System");
        intent.putExtra("key_name",DB_KEY_AI_ASSIST_WAKEUP_SWITCH );
        intent.putExtra("key_value",state+"");
        intent.setPackage("com.android.settings");
        context.sendBroadcast( intent, null );

        return oldState;
    }

    public static boolean isTargetDevice( ){
//        String deviceName = android.os.Build.MODEL;
//        if( "Readboy_C20Pro".equals(deviceName) ){
//            return true;
//        }
//        return false;
        return Utils.isSupportMultipleRecord();
    }

    public static boolean closeVoiceAssistant(Context context) {
        if(isTargetDevice()){
            if(getVoiceAssiatnceState(context) != 0) {
                setVoiceAssisitantState(context, VoiceAssistantUtil.CLOSE_STATE);
                Log.d(TAG, "closeVoiceAssistant: success close voiceAides");
                return true;
            }
            return false;
        }
        return false;
    }

    public static void backVoiceAssistant(Context context) {
        if(isTargetDevice()){
            setVoiceAssisitantState(context, VoiceAssistantUtil.OPEN_STATE);
            Log.d(TAG, "closeVoiceAssistant: success back voiceAides");
        }
    }
}
