package com.readboy.onlinecourseaides.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.bean.CourseInfo;
import com.readboy.onlinecourseaides.databinding.DialogOnlineClassBinding;
import com.readboy.onlinecourseaides.utils.NetWorkUtils;
import com.readboy.onlinecourseaides.utils.ParamType;

/**
 * 更多弹窗中 点击添加classOnline弹窗
 */
public class OnlineClassDialog extends Dialog {

    private MyApplication application;

    private DialogOnlineClassBinding classBinding;

    private CourseInfo inputClassData;

    public OnlineClassDialog(@NonNull Context context, InputOnlineClassListener listener) {
        super(context);
        application = MyApplication.getInstances();
        this.inputOnlineClassListener = listener;
        initView();
    }

    private void initView() {
//        final LayoutInflater inflater = LayoutInflater.from(getContext());
//        final View view = inflater.inflate(R.layout.dialog_online_class, null);
        classBinding = DialogOnlineClassBinding.inflate(LayoutInflater.from(getContext()));
        View view = classBinding.getRoot();

        setContentView(view);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);

        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        lp.width = getContext().getResources().getDisplayMetrics().widthPixels;
        //设置宽
        lp.width = (int) (window.getWindowManager().getDefaultDisplay().getHeight() * 0.8);
        view.setLayoutParams(lp);

        classBinding.classBtnMake.setOnClickListener(v->{
            ParamType info = getCourseInfo();
            inputOnlineClassListener.doWork(info);
        });

        classBinding.classBtnCancel.setOnClickListener(v->{
            dismiss();
        });
    }

    public ParamType getCourseInfo() {
        String areaName = classBinding.inputArea.getText().toString();
        String courseUrl = classBinding.inputArea2.getText().toString();

        if(areaName.isEmpty() || courseUrl.isEmpty()) {
            application.showToast("输入内容为空");
            return ParamType.RESULT_DO_ERROR;
        }

        inputClassData = new CourseInfo();
        inputClassData.setTitle(areaName);
        // 判断URL是否合法
        if(NetWorkUtils.isValidUrl(courseUrl)){
            inputClassData.setUrl(courseUrl);
            return ParamType.RESULT_DO_SUCCESS;
        }else {
            application.showToast("输入URL不合法");
            return ParamType.RESULT_DO_ERROR;
        }
    }

    public CourseInfo getInputClassData() {
        return inputClassData;
    }

    public void setInputClassData(CourseInfo inputClassData) {
        this.inputClassData = inputClassData;
    }

    private InputOnlineClassListener inputOnlineClassListener;

    public interface InputOnlineClassListener {
        void doWork(ParamType info);
    }
}
