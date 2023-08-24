package com.readboy.onlinecourseaides.utils

import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.annotation.IntRange
import androidx.core.text.isDigitsOnly
import com.readboy.onlinecourseaides.Application.MyApplication
import com.readboy.onlinecourseaides.utils.SettingsUtils.getEyesCareSwitch
import com.readboy.onlinecourseaides.utils.SettingsUtils.isEyesCareEnable
import com.readboy.onlinecourseaides.utils.cache.ACache


object SettingsUtils {

    private const val TAG = "SettingsUtils"
    const val KEY_AIDES_SPEED_ENABLE = "speed_enabled"
    const val  KEY_AIDES_ENABLE = "aides_enabled"
    const val KEY_FLOAT_WINDOW_ENABLED = "float_window_enabled"
    const val KEY_FLOAT_WINDOW_ALPHA = "float_window_alpha"
    const val KEY_EYES_CARE_ENABLED = "eys_care_enabled"
    const val KEY_NO_DISTURB_ENABLED = "enable_no_disturb"

    // 网课模式的开启和关闭
    @JvmStatic
    fun isOnlineCourseAidesEnabled(): Boolean {
        val aCache = ACache.get(MyApplication.getInstances())
        val userEnabled = aCache.getAsString(KEY_AIDES_ENABLE)
        return userEnabled?.equals("1") ?: true
    }

    @JvmStatic
    fun setOnlineCourseAidesEnabled(enable: Boolean) {
        val aCache = ACache.get(MyApplication.getInstances())
        aCache.put(KEY_AIDES_ENABLE, if (enable) "1" else "0")
    }

    // 悬浮窗的开启和关闭
    @JvmStatic
    fun isFloatWindowEnabled(): Boolean {
        //查找用户在设置界面设置的值，默认开启
        val aCache = ACache.get(MyApplication.getInstances())
        val userEnabled = aCache.getAsString(KEY_FLOAT_WINDOW_ENABLED)
        return userEnabled?.equals("1") ?: true
    }

    @JvmStatic
    fun setFloatWindowEnabled(enable: Boolean) {
        val aCache = ACache.get(MyApplication.getInstances())
        aCache.put(KEY_FLOAT_WINDOW_ENABLED, if (enable) "1" else "0")
    }

    // 悬浮窗透明度设置
    @JvmStatic
    fun getFloatWindowAlpha(): Int {
        //查找用户在设置界面设置的值，默认100
        val aCache = ACache.get(MyApplication.getInstances())
        val alpha = aCache.getAsString(KEY_FLOAT_WINDOW_ALPHA)
        return if (alpha?.isDigitsOnly() == true) {
            alpha.toInt()
        } else {
            100
        }
    }

    @JvmStatic
    fun setFloatWindowAlpha(@IntRange(from = 0, to = 100) alpha: Int) {
        val aCache = ACache.get(MyApplication.getInstances())
        aCache.put(KEY_FLOAT_WINDOW_ALPHA, alpha.toString())
    }

    // 免打扰设置
    @JvmStatic
    fun setNoDisturbEnabled(enable: Boolean): Boolean {
//        val writeSuccess = SystemUISettings.putInt(
//            MyApplication.getInstances().contentResolver,
//            KEY_NO_DISTURB_ENABLED,
//            if (enable) 1 else 0
//        )
//        if (writeSuccess) {
//            ACache.get(MyApplication.getInstances())
//                .put(KEY_NO_DISTURB_ENABLED, if (enable) "1" else "0")
//        }
        ACache.get(MyApplication.getInstances())
                .put(KEY_NO_DISTURB_ENABLED, if (enable) "1" else "0")
        return !enable
    }

    @JvmStatic
    fun isNoDisturbEnable(): Boolean {
        //查找用户在设置界面设置的值，默认开启
        val aCache = ACache.get(MyApplication.getInstances())
        val userEnabled = aCache.getAsString(KEY_NO_DISTURB_ENABLED)
        return userEnabled?.equals("1") ?: true
    }

    // 护眼模式设置
    @JvmStatic
    fun getEyesCareSwitch() {
        MyApplication.sEyesCareSwitchMap = EyesUtils.getSwitch()
        Log.d(TAG, "getEyesCareSwitch: "+MyApplication.sEyesCareSwitchMap)
    }

    @JvmStatic
    fun setEyesCareEnabled(isUseInternalConfig: Boolean): Boolean {
        return if (isUseInternalConfig) {
            //进入网课模式，使用网课助手记录的设置来重置护眼卫士功能开关
            setEyesCareEnabled(isEyesCareEnable(), isUseInternalConfig)
        } else {
            //回退护眼卫士功能开关的状态，必须先记录之前的状态，否则返回false
            setEyesCareEnabled(false, isUseInternalConfig)
        }
    }

    @JvmStatic
    fun setEyesCareEnabled(enable: Boolean, isUseInternalConfig: Boolean): Boolean {
        //仅开启眼卫士的滤蓝光功能 2022/12/01 11：51
//        val writeSuccess = if (isUseInternalConfig) {
//            if (enable) {
//                //关掉所有护眼功能，只打开滤蓝光
//                EyesUtils.openOnlyWarm() > 0
//            } else {
//                //关掉所有护眼功能
//                EyesUtils.closeAllStatus() > 0
//            }
//        } else {
//            //回退护眼卫士功能开关的状态，必须先记录之前的状态
//            EyesUtils.backStatus(MyApplication.sEyesCareSwitchMap)
//        }
        val aCache = ACache.get(MyApplication.getInstances())
        aCache.put(KEY_EYES_CARE_ENABLED, if (enable) "1" else "0")
        return false
    }

    @JvmStatic
    fun isEyesCareEnable(): Boolean {
        //查找用户在设置界面设置的值，默认开启
        val aCache = ACache.get(MyApplication.getInstances())
        val userEnabled = aCache.getAsString(KEY_EYES_CARE_ENABLED)
        return userEnabled?.equals("1") ?: true
    }

    // 启用加速
    @JvmStatic
    fun setAccelerateEnabled(enable: Boolean) {
        val aCache = ACache.get(MyApplication.getInstances())
        aCache.put(KEY_AIDES_SPEED_ENABLE, if (enable) "1" else "0")
    }

    @JvmStatic
    fun isAccelerateEnabled(): Boolean {
        val aCache = ACache.get(MyApplication.getInstances())
        val userEnabled = aCache.getAsString(KEY_AIDES_SPEED_ENABLE)
        return userEnabled?.equals("1") ?: true
    }

    private var startEyesCare = false

    // 开启护眼
    @JvmStatic
    fun enableEyesCare() {
        //关掉所有护眼功能，只打开滤蓝光
        Log.d(TAG, "enableEyesCare: $startEyesCare")
        if(!startEyesCare) {
            getEyesCareSwitch();
            EyesUtils.openOnlyWarm()
            startEyesCare = true;
        }
    }

    var isCareEyes = false
    // 开启免打扰
    @JvmStatic
    fun enableNoDisturb() {
        isCareEyes = false
        if (!isEyesCareEnable()) {
            MyApplication.sEyesCareSwitchMap = EyesUtils.getSwitch()
            if(MyApplication.sEyesCareSwitchMap[EyesUtils.KEY_OPEN] == 1) {
                isCareEyes = true
                EyesUtils.closeNotify()
            }
        }

        var isEnadbled = SystemUISettings.putInt(MyApplication.getInstances().contentResolver, KEY_NO_DISTURB_ENABLED, 1);
        if(!isEnadbled) {
            Log.d(TAG, "enableNoDisturb: 设置免打扰失败")
        }
    }
    @JvmStatic
    fun closeNotifySound(manager: AudioManager) {
        //获取当前系统音量
        var streamVolume = manager.getStreamVolume(AudioManager.STREAM_RING)
        Log.d(TAG, "closeNotifySound: now NotifySound s = " + streamVolume)
        MyApplication.notifySoundNumber = streamVolume;
//        manager.adjustStreamVolume (AudioManager.STREAM_RING, 0, AudioManager.FLAG_SHOW_UI)
        // 第三个参数未零表示不展示系统UI
        // 中间参数代表修改的
        manager.setStreamVolume(AudioManager.STREAM_RING, 0, 0)
    }
    @JvmStatic
    fun backNotifySound(manager: AudioManager) {
        //获取当前系统音量
        var streamVolume = manager.getStreamVolume(AudioManager.STREAM_RING)
        Log.d(TAG, "backNotifySound: backNotifySound 设置音量 ring = " + streamVolume +"to notifySoundNumber = " + MyApplication.notifySoundNumber)
        //确认用户未调节音量
        if(streamVolume == 0) {
            manager.setStreamVolume (AudioManager.STREAM_RING, MyApplication.notifySoundNumber, 0);
        }

    }

    // 执行清理内存
    @JvmStatic
    fun doTaskClearRam(pkgName: String) {
        if (MemoryCleanUpUtils.checkCallMessageGetExsist(MyApplication.getInstances(), MemoryCleanUpUtils.ACTION_FUN_NAME)){
            MemoryCleanUpUtils.doCallMessageHelperSettingsReceiver200(MyApplication.getInstances(),pkgName);
        }
    }

    // 关闭免打扰
    @JvmStatic
    fun stopNoDisturb() {
        if (!isEyesCareEnable() && isCareEyes) {
            EyesUtils.backStatus(MyApplication.sEyesCareSwitchMap)
        }
        val isEnnobled = SystemUISettings.putInt(MyApplication.getInstances().contentResolver, KEY_NO_DISTURB_ENABLED, 0);
        if(!isEnnobled) {
            Log.d(TAG, "stopNoDisturb: 设置免打扰失败")
        }
    }

    // 关闭护眼
    @JvmStatic
    fun stopEyesCare() {
        //回退护眼卫士功能开关的状态，必须先记录之前的状态
        Log.d(TAG, "enableEyesCare:  stopEyesCare $startEyesCare")
        if(startEyesCare) {
            EyesUtils.backStatus(MyApplication.sEyesCareSwitchMap)
            startEyesCare = false;
        }
    }
}