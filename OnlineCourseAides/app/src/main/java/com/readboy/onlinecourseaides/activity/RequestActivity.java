package com.readboy.onlinecourseaides.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.receiver.ReceiverMsgReceiver;
import com.readboy.onlinecourseaides.utils.ParentManagerUtils;

/**
 * 请求家长管理
 */
public class RequestActivity extends Activity {

    public static final int REQUEST_CODE_PMG = 1001;
    public static final String CODE_SUCCESS = "true";
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String CODE_ERROR = "false";

    private String requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 确定请求来源
        Intent intent1 = getIntent();
        requestType = intent1.getStringExtra(TYPE);

        Intent intent = new Intent();
        intent.setAction("android.readboy.parentmanager.INPUT_PASSWORD");
        startActivityForResult(intent, REQUEST_CODE_PMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(ReceiverMsgReceiver.PMG_MSG);
        if(requestCode == REQUEST_CODE_PMG && resultCode == ParentManagerUtils.PASSWORD_MANAGER_CORRECT) {
            intent.putExtra(CODE, CODE_SUCCESS);
        }else {
            intent.putExtra(CODE, CODE_ERROR);
            //家长管理 密码错误 弹出信息
            MyApplication.getInstances().showToast("密码未输入或密码未设置");
        }
        intent.putExtra(TYPE, requestType);
        sendBroadcast(intent);
        finish();
    }
}
