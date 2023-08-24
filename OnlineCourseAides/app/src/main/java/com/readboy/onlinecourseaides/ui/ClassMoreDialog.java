package com.readboy.onlinecourseaides.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.MainActivity;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.activity.RequestActivity;
import com.readboy.onlinecourseaides.adapter.OnlineCourseAdapter;
import com.readboy.onlinecourseaides.adapter.SpacesItemsDecoration;
import com.readboy.onlinecourseaides.bean.CourseInfo;
import com.readboy.onlinecourseaides.databinding.DialogClassMoreBinding;
import com.readboy.onlinecourseaides.receiver.ReceiverMsgReceiver;

import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/26
 */
public class ClassMoreDialog extends Dialog {
    
    private static final String TAG = "ClassMoreDialog";

    private DialogClassMoreBinding binding;

    private Context context;

    // app class 公用一个Dialog
    // 所有的类型变量枚举
    private OnlineCourseAdapter onlineCourseAdapter;
    private CourseInfo currentPoint;
    private List<CourseInfo> mData;
    private boolean isStartDelMode = false;
    //是否开启编辑
    private boolean isEnableStartEdit = false;
    // 是否请求家长管理
    private boolean isSendStart = true;

    public ClassMoreDialog(@NonNull Context context) {
        super(context);
    }

    public ClassMoreDialog(@NonNull Context context, List<CourseInfo> mData, SelectClassDialogListener listener) {
        // ActionSheetDialogStyle 设置去掉边框
//        super(context,R.style.ActionSheetDialogStyle);
        // 设置默认风格
        super(context, R.style.NormalDialogStyle);

        this.context = context;
        this.mData = mData;
        this.onSelectDialogListener = listener;

        binding = DialogClassMoreBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        setContentView(view);

        Window window = getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        //设置宽
        lp.width = (int) (window.getWindowManager().getDefaultDisplay().getWidth() * 0.95);
        lp.height = (int) (window.getWindowManager().getDefaultDisplay().getHeight() * 0.85);
        view.setLayoutParams(lp);

        init();
    }

    private void init() {
        initData();
        initView();
        setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
    }

    private void initData() {
    }

    private void initView() {
        onlineCourseAdapter = new OnlineCourseAdapter(getContext(), mData, new OnlineCourseAdapter.OnlineCourseListener() {
            @Override
            public void onCourseClick() {
                MainActivity activity = (MainActivity) context;
                currentPoint = onlineCourseAdapter.getCurrentPoint();
                activity.doPreStartApp();
                activity.startTheAppsAndCourse(onlineCourseAdapter.getCurrentPoint());
                onSelectDialogListener.itemOnClick();
            }

            @Override
            public void delCourseClick(CourseInfo index) {
                onSelectDialogListener.delItem(index);
            }
        });
        Log.d(TAG, "init: onlineCourseAdapter");
        GridLayoutManager manager2 = new GridLayoutManager(getContext(), 3);
        int spanCount = 3; // 3 columns
        int spacing = 30; // 50px
        // 设置间距，实际上是padding
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        binding.mainRecyclerViewClass.setLayoutManager(manager);
        binding.mainRecyclerViewClass.addItemDecoration(new SpacesItemsDecoration(spanCount, spacing, false));
        binding.mainRecyclerViewClass.setAdapter(onlineCourseAdapter);

        binding.dialogClassMoreAddContent.setOnClickListener(v->{
            onSelectDialogListener.gotoMoreClick();
        });
        binding.dialogClassMoreEditContent.setOnClickListener(v->{
            Log.d(TAG, "dialogClassMoreEditContent  isSendStart="+isSendStart+", isEnableStartEdit = " +isEnableStartEdit);
            boolean isHaveEdit = false;
            for (CourseInfo info : mData) {
                if(info.isEnabledDel()) {
                    isHaveEdit = true;
                    break;
                }
            }
            if(isHaveEdit || isStartDelMode) {
                if(isSendStart) {
                    Intent intent = new Intent();
                    intent.putExtra(RequestActivity.TYPE, ReceiverMsgReceiver.PMG_MSG_TYPE_PMG_CLASS_DEL);
                    intent.setClass(context, RequestActivity.class);
                    context.startActivity(intent);
                }else {
                    refreshEditStatus();
                }
            }else {
                MyApplication.getInstances().showToast("暂无可删除的选项");
            }

        });
    }

    public void backStatus() {
        binding.dialogEdit.setText("编辑");
        isStartDelMode = false;
        onlineCourseAdapter.setEnabledDelMode(false);
    }

    private SelectClassDialogListener onSelectDialogListener;

    public interface SelectClassDialogListener {
        // 更多按钮
        void gotoMoreClick();
        // 点击item
        void itemOnClick();
        // 删除自己设置的白名单
        void delItem(CourseInfo index);
        boolean editItem();
    }

    public CourseInfo getCurrentPoint() {
        return currentPoint;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onlineCourseAdapter.setEnabledDelMode(false);
        binding.dialogEdit.setText("编辑");
        isStartDelMode = false;
    }

    public void refreshEditStatus() {
        Log.d(TAG, "dialogClassMoreEditContent refreshEditStatus  isSendStart="+isSendStart+", isEnableStartEdit = " +isEnableStartEdit);
        if(isEnableStartEdit) {
            isStartDelMode = !isStartDelMode;
            onlineCourseAdapter.setEnabledDelMode(isStartDelMode);
            isSendStart = false;
        }

        if(isStartDelMode) {
            binding.dialogEdit.setText("取消编辑");
        }else {
            binding.dialogEdit.setText("编辑");
        }
    }

    public void setStartDelMode(boolean startDelMode) {
        isStartDelMode = startDelMode;
    }

    public boolean isEnableStartEdit() {
        return isEnableStartEdit;
    }

    public void setEnableStartEdit(boolean enableStartEdit) {
        isEnableStartEdit = enableStartEdit;
    }

    public boolean isSendStart() {
        return isSendStart;
    }

    public void setSendStart(boolean sendStart) {
        isSendStart = sendStart;
    }

    public void refreshClassData(List<CourseInfo> data) {
        Log.d(TAG, "refreshClassData: data"+data);
        if (onlineCourseAdapter != null) {
            onlineCourseAdapter.refreshData(data);
        }
    }

    public void refreshView() {
        onlineCourseAdapter.refreshView("ClassMore");
    }

    public void refreshItemClassData(CourseInfo data) {
        if (onlineCourseAdapter != null) {
            onlineCourseAdapter.refreshItemData(data, onlineCourseAdapter.getItemCount());
        }
    }

    public void addItem(CourseInfo info) {
        if (onlineCourseAdapter != null && info != null) {
            onlineCourseAdapter.addItemData(info);
        }
    }

    @Override
    public void show() {
        super.show();
        isSendStart = true;
        isEnableStartEdit = false;
    }
}
