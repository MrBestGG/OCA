package com.readboy.onlinecourseaides

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.readboy.onlinecourseaides.utils.Logger
import com.readboy.onlinecourseaides.utils.SettingsUtils
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsUtilsTest {

    @Test
    fun testIsEyesCareEnable() {
        Logger.d("wocao", "testIsEyesCareEnable: ${SettingsUtils.isEyesCareEnable()}")
    }

    @Test
    fun test001() {
        // 开启音频录制服务
        val intent = Intent()
        intent.setClass(mAppContext, DoSoundRecordService::class.java)
    }
}