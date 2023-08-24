package com.readboy.onlinecourseaides.activity

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.readboy.onlinecourseaides.Application.MyApplication
import com.readboy.onlinecourseaides.MainActivity
import com.readboy.onlinecourseaides.R
import com.readboy.onlinecourseaides.service.TaskSupportService

// 录制权限丢失  intentData 参数为空无法获取 mMediaProjection 无法获取
class PermissionsActivity : AppCompatActivity() {
    private val REQUEST_CODE = 101

    private val TAG = "PermissionsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenRecordPermission();
    }

    // 请求获取参数 intent:data 为获取mMediaProjection 做准备
    private fun screenRecordPermission() {
        val projectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val captureIntent = projectionManager.createScreenCaptureIntent()
        startActivityForResult(captureIntent, REQUEST_CODE)
        Log.d(TAG, "screenRecordPermission:isRecordStartSettings  ")
    }

    // 跳转返回的回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RESULT_OK == resultCode && REQUEST_CODE == requestCode) {
            MyApplication.getInstances().data = data
            MyApplication.getInstances().code = resultCode
        }
        finish()
    }
}