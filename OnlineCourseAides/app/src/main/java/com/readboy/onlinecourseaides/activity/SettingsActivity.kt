package com.readboy.onlinecourseaides.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.readboy.onlinecourseaides.R
import com.readboy.onlinecourseaides.base.BaseVMActivity
import com.readboy.onlinecourseaides.databinding.ActivitySettingsBinding
import com.readboy.onlinecourseaides.viewmodel.SettingsViewModel

class SettingsActivity : BaseVMActivity<ActivitySettingsBinding, SettingsViewModel>() {
    //通过泛型参数和属性委托，指定fragment需要的具体viewModel，也可以使用共享viewModel
    override val viewModel by viewModels<SettingsViewModel>()

    override fun initViewBinding(): ActivitySettingsBinding =
        DataBindingUtil.setContentView(this, R.layout.activity_settings)

    override fun initView() {
        binding.viewModel = viewModel
        //for audio player test only
    }

    override fun subscribeUIState() {

    }


}