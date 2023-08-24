package com.readboy.onlinecourseaides.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.readboy.onlinecourseaides.arch.Event
import com.readboy.onlinecourseaides.utils.SingleClicker

abstract class BaseViewModel : ViewModel() {

    protected val TAG: String = javaClass.simpleName

    protected val _toastEvent = MutableLiveData<Event<String>>()
    val toastEvent: LiveData<Event<String>> = _toastEvent

    protected open val mSingleClicker by lazy { SingleClicker() }

    open fun isFastDoubleClick(): Boolean {
        return mSingleClicker.isFastDoubleClick()
    }

    //共通的操作放在基类
    fun exampleAction() {
        //do nothing by default
    }
}