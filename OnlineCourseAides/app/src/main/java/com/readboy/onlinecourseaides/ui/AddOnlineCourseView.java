package com.readboy.onlinecourseaides.ui;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.CourseInfo;
import com.readboy.onlinecourseaides.utils.NetWorkUtils;
import com.readboy.onlinecourseaides.utils.ParamType;

/**
 * @Author jll
 * @Date 2023/1/3
 */
public class AddOnlineCourseView extends CenterPopupView {

    private TextView nameTitle;
    private TextView urlTitle;
    private TextView makeSure;
    private TextView cancel;
    private TextView title;

    private EditText inputName;
    private EditText inputUrl;

    private CourseInfo inputClassData;
    private String text;
    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public AddOnlineCourseView(@NonNull Context context) {
        super(context);
    }

    public AddOnlineCourseView(@NonNull Context context, AddOnlineCourseDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_online_class;
    }
    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        title = findViewById(R.id.class_add_tv_title);
        cancel = findViewById(R.id.class_btn_cancel);
        makeSure = findViewById(R.id.class_btn_make);
        nameTitle = findViewById(R.id.class_tab_area);
        urlTitle = findViewById(R.id.class_tab_area2);
        inputName = findViewById(R.id.input_area);
        inputUrl = findViewById(R.id.input_area2);

        nameTitle.setText("名称");
        urlTitle.setText("网址");
        title.setText("添加网站到白名单");
        cancel.setText("取消");
        makeSure.setText("确认");

        cancel.setOnClickListener(v -> {
            listener.cancel();
            dismiss(); // 关闭弹窗
        });

        makeSure.setOnClickListener(v->{
            ParamType info = getCourseInfo();
            listener.makeSure(info, inputClassData);
            dismiss();
        });
    }

    public ParamType getCourseInfo() {
        String areaName = inputName.getText().toString();
        String courseUrl = inputUrl.getText().toString();

        if(areaName.isEmpty() || courseUrl.isEmpty()) {
            MyApplication.getInstances().showToast("输入内容为空");
            return ParamType.RESULT_DO_ERROR;
        }

        inputClassData = new CourseInfo();
        inputClassData.setTitle(areaName);
        // 判断URL是否合法
        if(NetWorkUtils.isValidUrl(courseUrl)){
            inputClassData.setUrl(courseUrl);
            return ParamType.RESULT_DO_SUCCESS;
        }else {
            MyApplication.getInstances().showToast("输入URL不合法");
            return ParamType.RESULT_DO_ERROR;
        }
    }


    // 设置最大宽度，看需要而定，
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }
    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }
    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }
    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
//    protected int getPopupWidth() {
//        return 0;
//    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
//    protected int getPopupHeight() {
//        return 0;
//    }
    private AddOnlineCourseDialogListener listener;

    public interface AddOnlineCourseDialogListener {
        void makeSure(ParamType type, CourseInfo info);
        void cancel();
    }

    public CourseInfo getInputClassData() {
        return inputClassData;
    }

    @Override
    public void dismiss() {
        if(inputName != null && inputUrl != null) {
            InputMethodManager imm=(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputName.getWindowToken(),0);
            imm.hideSoftInputFromWindow(inputUrl.getWindowToken(),0);
        }
        super.dismiss();
    }
}
