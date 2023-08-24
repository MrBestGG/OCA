package com.readboy.onlinecourseaides.viewmodel

import android.util.Log
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.readboy.onlinecourseaides.arch.Event
import com.readboy.onlinecourseaides.base.BaseViewModel
import com.readboy.onlinecourseaides.utils.Logger
import com.readboy.onlinecourseaides.utils.SettingsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel() {
    val _isAidesEnabled = MutableLiveData<Boolean>() // 网课模式开关
    val isAidesFloatWinEnabled: LiveData<Boolean> = _isAidesEnabled
    val _isCourseAidesFloatWindowEnabled = MutableLiveData<Boolean>() //悬浮窗开关 可变数据
    val isFloatWindowEnabled: LiveData<Boolean> = _isCourseAidesFloatWindowEnabled // 不可变数据
    val _isCourseAidesAccelerateEnabled = MutableLiveData<Boolean>()
    val isAccelerateEnabled: LiveData<Boolean> = _isCourseAidesAccelerateEnabled
    val _isCourseAidesNoDisturbEnabled = MutableLiveData<Boolean>()
    val isNoDisturbEnabled: LiveData<Boolean> = _isCourseAidesNoDisturbEnabled
    val _isEyesCareModeEnabled = MutableLiveData<Boolean>()
    val isEyesCareModeEnabled: LiveData<Boolean> = _isEyesCareModeEnabled

    val _floatWindowAlpha = MutableLiveData<Int>()
    val floatWindowAlpha: LiveData<Int> = _floatWindowAlpha

    init {
        //如果不需要在比如旋转屏幕时重新加载数据，而且初始化过程不需要外部参数，可以在构造时进行初始化
        Logger.d(TAG, "init: wocao")
        updateSettingsState()
    }

    private fun updateSettingsState() {
        viewModelScope.launch(Dispatchers.Main) {
            //注意，设置页面仅读取了开关应该处于的状态，涉及系统调用比如护眼卫士的开启这里没有做，统一由监控service设置
            _isAidesEnabled.value = SettingsUtils.isOnlineCourseAidesEnabled()
            _isCourseAidesFloatWindowEnabled.value = SettingsUtils.isFloatWindowEnabled()
            _floatWindowAlpha.value = SettingsUtils.getFloatWindowAlpha()
            _isCourseAidesAccelerateEnabled.value = SettingsUtils.isAccelerateEnabled()
            _isCourseAidesNoDisturbEnabled.value = SettingsUtils.isNoDisturbEnable()
            _isEyesCareModeEnabled.value = SettingsUtils.isEyesCareEnable()
            Log.d(TAG, "init SettingsState:"
                    + " _isAidesEnabled="
                    + _isAidesEnabled.value
                    + ", _isCourseAidesFloatWindowEnabled=" + _isCourseAidesFloatWindowEnabled.value
                    + ", _floatWindowAlpha=" + _floatWindowAlpha.value
                    + ", _isCourseAidesAccelerateEnabled=" + _isCourseAidesAccelerateEnabled.value
                    + ", _isCourseAidesNoDisturbEnabled=" + _isCourseAidesNoDisturbEnabled.value
                    + ", _isEyesCareModeEnabled=" + _isEyesCareModeEnabled.value
            )
        }
    }

    // 网课模式开关
    fun onFloatWinAidesEnableChanged(button: CompoundButton, checked: Boolean) {
        if (button.isPressed) {
            SettingsUtils.setOnlineCourseAidesEnabled(checked)
            Logger.d(
                    "wocao",
                    "onFloatWindowEnableChanged: ${_isAidesEnabled.value}"
            )
        } else {
            Logger.d(
                    "wocao",
                    "onFloatWindowEnableChanged: from program: ${isAidesFloatWinEnabled.value!!} ->  $checked"
            )
        }
    }

    /**
     *开启后显示小助手悬浮窗
     */
    fun onFloatWindowEnableChanged(button: CompoundButton, checked: Boolean) {
        if (button.isPressed) {
            SettingsUtils.setFloatWindowEnabled(checked)
            Logger.d(
                    "wocao",
                    "onFloatWindowEnableChanged: ${_isCourseAidesFloatWindowEnabled.value}"
            )
        } else {
            Logger.d(
                    "wocao",
                    "onFloatWindowEnableChanged: from program: ${isFloatWindowEnabled.value!!} ->  $checked"
            )
        }
    }

    /**
     * 笔记悬浮窗闲置时透明度设置
     */
    fun onFloatWindowAlphaChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        SettingsUtils.setFloatWindowAlpha(progress)
    }

    fun onAccelerateEnableChanged(button: CompoundButton, checked: Boolean) {
        SettingsUtils.setAccelerateEnabled(checked);
    }

    fun onNoDisturbEnableChanged(button: CompoundButton, checked: Boolean) {
        if (button.isPressed) {
            SettingsUtils.setNoDisturbEnabled(checked)
            button.isChecked = checked
//                _toastEvent.value = Event("设置失败")
        } else {
            Logger.d(
                    "wocao",
                    "onNoDisturbEnableChanged: from program: ${isNoDisturbEnabled.value!!} -> $checked"
            )
        }

    }

    fun onEyesCareEnableChanged(button: CompoundButton, checked: Boolean) {
        if (button.isPressed) {
            SettingsUtils.setEyesCareEnabled(checked, true)
//            if () {
//
//            } else {
//                button.isChecked = !checked
//                _toastEvent.value = Event("设置失败")
//            }

            Logger.d(
                    "wocao",
                    "onEyesCareEnableChanged: ${isEyesCareModeEnabled.value} -> $checked"
            )
        } else {
            Logger.d(
                    "wocao",
                    "onEyesCareEnableChanged: from program: ${isEyesCareModeEnabled.value!!} -> $checked"
            )
        }

    }
}