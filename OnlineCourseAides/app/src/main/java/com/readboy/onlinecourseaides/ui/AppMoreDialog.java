package com.readboy.onlinecourseaides.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.MainActivity;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.adapter.AppInfoAdapter;
import com.readboy.onlinecourseaides.adapter.OnlineCourseAdapter;
import com.readboy.onlinecourseaides.adapter.SpacesItemDecoration;
import com.readboy.onlinecourseaides.bean.AppInfo;
import com.readboy.onlinecourseaides.bean.CourseInfo;
import com.readboy.onlinecourseaides.databinding.DialogAppMoreBinding;
import com.readboy.onlinecourseaides.utils.ParamType;

import java.util.List;

/**
 * 首页更多弹窗
 */
public class AppMoreDialog extends Dialog {
    private static final String TAG = "AppMoreDialog";

    private DialogAppMoreBinding binding;

    private Context context;

    // app class 公用一个Dialog
    // 所有的类型变量枚举
    private ParamType dialogType;

    RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;
    private OnlineCourseAdapter onlineCourseAdapter;

    private AppInfo currentPoint;

    private List<AppInfo> mData;

    TextView moreTvBtn;
    private boolean isStartDelMode = false;

    public AppMoreDialog(@NonNull Context context, List<AppInfo> mData, OnSelectDialogListener listener, ParamType dialogType) {
        // ActionSheetDialogStyle 设置去掉边框
        super(context);
        Log.d(TAG, "AppMoreDialog:  dialogType " + dialogType);
        this.context = context;
        this.mData = mData;
        this.onSelectDialogListener = listener;
        this.dialogType = dialogType;
        initView();
        init();
        setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
    }

    public void init() {
        Log.d(TAG, "init: appInfoAdapter beagin dialogType" + dialogType);
        appInfoAdapter = new AppInfoAdapter(getContext(), mData, new AppInfoAdapter.AppInfoListener() {
            @Override
            public void appInfoOnClick() {
                MainActivity activity = (MainActivity) context;
                currentPoint = appInfoAdapter.getCurrentAppInfo();
                activity.doPreStartApp();
                int i = activity.startTheAppsAndCourse(appInfoAdapter.getCurrentAppInfo());
                if(i == MainActivity.START_APP_OPEN_SUCCESS) {
                    onSelectDialogListener.itemOnClick(dialogType);
                }
            }

            @Override
            public void delAppInfo() {
                onSelectDialogListener.selectOnClick(ParamType.APP_MORE_DEL_APP);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(context, 5);
        int space = 60;
        // 设置间距，实际上是padding
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(appInfoAdapter);
        recyclerView.setLayoutManager(manager);
        Log.d(TAG, "init: appInfoAdapter end dialogType" + dialogType);
    }

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    public void initView() {
        binding = DialogAppMoreBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        setContentView(view);
        moreTvBtn = findViewById(R.id.dialog_img_add);

        Window window = getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        //设置宽
        lp.width = (int) (window.getWindowManager().getDefaultDisplay().getWidth() * 0.85);
        lp.height = (int) (window.getWindowManager().getDefaultDisplay().getHeight() * 0.90);
        view.setLayoutParams(lp);

        recyclerView = findViewById(R.id.main_RecyclerView_apps);
        recyclerView.setVisibility(View.VISIBLE);

        binding.dialogAppMoreAddContent.setOnClickListener(v->{
            onSelectDialogListener.selectOnClick(ParamType.TYPE_APP_DIALOG);
        });
        binding.dialogAppMoreEditContent.setOnClickListener(v->{
            boolean isDel = false;
            for (AppInfo info : mData) {
                if(info.isEnabledDelAppInfo()) {
                    isDel = true;
                    break;
                }
            }
            if(!isDel) {
                enableStartDel = false;
                binding.dialogEdit.setText("编辑");
                Log.d(TAG, "refreshView: 灰色");
                MyApplication.getInstances().showToast("没有可删除的选项");
            }else {
                enableStartDel = true;
            }

            if(enableStartDel) {
                isStartDelMode = !isStartDelMode;
                appInfoAdapter.setStartEditMode(isStartDelMode);

                if(isStartDelMode) {
                    binding.dialogEdit.setText("取消编辑");
                }else {
                    binding.dialogEdit.setText("编辑");
                }
            }
        });
    }

    private boolean enableStartDel = true;

    public void setStartDelMode(boolean startDelMode) {
        isStartDelMode = startDelMode;
        appInfoAdapter.setStartEditMode(isStartDelMode);
        if(isStartDelMode) {
            binding.dialogEdit.setText("取消编辑");
        }else {
            binding.dialogEdit.setText("编辑");
        }
    }

    public AppInfo getCurrentPoint() {
        return currentPoint;
    }

    private OnSelectDialogListener onSelectDialogListener;

    public interface OnSelectDialogListener {
        void selectOnClick(ParamType paramType);

        void itemOnClick(ParamType paramType);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setStartDelMode(false);
    }

    public void refreshAppData(List<AppInfo> data) {
        if(data.size() == 0) {
            binding.dialogNoContent.setVisibility(View.VISIBLE);
            binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
        }else {
            binding.dialogNoContent.setVisibility(View.INVISIBLE);
            binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
        }
        if (appInfoAdapter != null) {
            appInfoAdapter.refreshData(data);
            Log.d(TAG, "refreshAppData: AppInfo1 =>" + data);
        }
        Log.d(TAG, "refreshAppData: AppInfo =>" + data);
    }

    public void refreshItemAppData(AppInfo data) {
        if (appInfoAdapter != null) {
            appInfoAdapter.refreshItemData(data, appInfoAdapter.getItemCount());
        }
    }

    public void refreshView() {
        boolean isDel = false;
        for (AppInfo info : mData) {
            if(info.isEnabledDelAppInfo()) {
                isDel = true;
                break;
            }
        }
        if(!isDel) {
            enableStartDel = false;
            binding.dialogEdit.setText("编辑");
            binding.dialogEdit.setTextColor(context.getResources().getColor(R.color.normal_click));
            binding.dialogImgEditImg.setBackgroundResource(R.mipmap.dialog_more_edit_click);
            Log.d(TAG, "refreshView: 灰色");
        }else {
            enableStartDel = true;
        }
        appInfoAdapter.refreshView();
    }

    public void addItem(CourseInfo info) {
        if (onlineCourseAdapter != null && info != null) {
            onlineCourseAdapter.addItemData(info);
        }
    }
}
