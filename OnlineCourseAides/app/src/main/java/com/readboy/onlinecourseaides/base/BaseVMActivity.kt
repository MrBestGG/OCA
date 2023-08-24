package com.readboy.onlinecourseaides.base

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.readboy.onlinecourseaides.arch.EventObserver

abstract class BaseVMActivity<VDB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {
    protected val TAG: String = javaClass.simpleName

    private var _viewBinding: VDB? = null
    val binding: VDB get() = _viewBinding!!

    protected abstract val viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 隐藏状态栏
        // 隐藏状态栏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val actionBar = supportActionBar
        actionBar?.hide()
        _viewBinding = initViewBinding().apply { lifecycleOwner = this@BaseVMActivity }
        setContentView(binding.root)

        initView()
        //监听默认的toast事件
        viewModel.toastEvent.observe(this, EventObserver {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            ).show()
        })
        subscribeUIState()
    }

    abstract fun initViewBinding(): VDB

    /**
     * 初始化view控件。
     */
    abstract fun initView()

    /**
     * 订阅viewModel的数据源，处理ui状态的刷新。
     */
    abstract fun subscribeUIState()
}