package com.readboy.onlinecourseaides.utils

import android.view.View
import com.readboy.onlinecourseaides.Application.MyApplication

class SingleClicker(
    private val interval: Long = 500L,
    private val onSingleClick: ((v: View?) -> Unit)? = null
) : View.OnClickListener {

    private var lastClickTimeMillis = 0L

    override fun onClick(v: View?) {
        if (!isFastDoubleClick())
            onSingleClick?.invoke(v)
    }

    fun isFastDoubleClick(): Boolean {
        if (System.currentTimeMillis() - lastClickTimeMillis < interval) {
            return true
        }
        lastClickTimeMillis = System.currentTimeMillis()
        return false
    }
}