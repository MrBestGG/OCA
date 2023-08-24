package com.readboy.onlinecourseaides.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.readboy.onlinecourseaides.R;

public abstract class BaseDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置 dialog 的背景为 null
        getDialog().getWindow().setBackgroundDrawable(null);
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(true);
        //是否可以取消,会影响上面那条属性
        setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //去除标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window win = getDialog().getWindow();
        WindowManager.LayoutParams params = win.getAttributes();//获取LayoutParams
        params.width = (int) getContext().getResources().
                getDimension(R.dimen.dialog_select_width);
        params.height = (int) getContext().getResources().
                getDimension(R.dimen.dialog_select_height);
        win.setAttributes(params);
        win.setAttributes(params);
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.gravity = Gravity.BOTTOM; //底部
//
//        window.setAttributes(lp);

        return createView(inflater, container);
    }

    //重写此方法，设置布局文件
    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(-1,-2 );
    }
}
