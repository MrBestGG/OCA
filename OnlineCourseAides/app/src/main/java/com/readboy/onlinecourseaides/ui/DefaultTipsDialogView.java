package com.readboy.onlinecourseaides.ui;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.readboy.onlinecourseaides.R;

/**
 * @Author jll
 * @Date 2022/12/26
 * XPopUp的自定义弹窗
 */
public class DefaultTipsDialogView extends CenterPopupView {
    private TextView dialogContentText;
    private TextView makeSure;
    private TextView cancel;
    private TextView title;
    private String text;

    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public DefaultTipsDialogView(@NonNull Context context) {
        super(context);
    }

    public DefaultTipsDialogView(@NonNull Context context, DefaultTipsDialogListener listener, String content) {
        super(context);
        this.listener = listener;
        text = content;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_del_default;
    }
    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        cancel = findViewById(R.id.btn_negative);
        cancel.setText("取消");
        cancel.setOnClickListener(v -> {
            listener.cancel();
            dismiss(); // 关闭弹窗
        });
        title = findViewById(R.id.tv_title);
        title.setText("温馨提示");
        makeSure = findViewById(R.id.btn_make_sure);
        makeSure.setText("确认");
        makeSure.setOnClickListener(v->{
            listener.makeSure();
            dismiss();
        });
        // 设置内容
        dialogContentText = findViewById(R.id.tv_message);
        dialogContentText.setText(text);
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
    private DefaultTipsDialogListener listener;

    public interface DefaultTipsDialogListener {
        void makeSure();
        void cancel();
    }
}
