package com.readboy.onlinecourseaides.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.readboy.onlinecourseaides.activity.RequestActivity;

//家长管理密码页面返回 处理
public class ReceiverMsgReceiver extends BroadcastReceiver {

    public static final String PMG_MSG ="BROADCAST_PMG_MSG";
    public static final String PMG_MSG_TYPE_PMG_CLASS_ADD = "PMG_CLASS_ADD";
    public static final String PMG_MSG_TYPE_PMG_CLASS_DEL = "PMG_CLASS_DEL";

    public ReceiverMsgReceiver(ReceiverMsgReceiver.ReceiverPmgMsgListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PMG_MSG)) {
            String code = intent.getStringExtra(RequestActivity.CODE);
            String type = intent.getStringExtra(RequestActivity.TYPE);
            Log.d("ReceiverMsgReceiver", "onReceive: code "+code);
            listener.getPmgMsgCode(code, type);
        }
    }

    private ReceiverMsgReceiver.ReceiverPmgMsgListener listener;

    public interface ReceiverPmgMsgListener {
        void getPmgMsgCode(String code, String type);
    }
}
