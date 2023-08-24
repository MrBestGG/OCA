package com.readboy.onlinecourseaides.ui;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.adapter.AppInfoAdapter;
import com.readboy.onlinecourseaides.adapter.SpacesItemDecoration;
import com.readboy.onlinecourseaides.bean.AppInfo;
import com.readboy.onlinecourseaides.databinding.DialogAppAddBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 更多弹窗中 点击添加AppInfo弹窗
 */
public class AppInfoDialog extends Dialog {

    private Context context;
    private MyApplication application;

    private DialogAppAddBinding binding;

    private AppInfo selectData;
    private List<AppInfo> data;

    private List<AppInfo> copyData;

    private AppInfoAdapter appInfoAdapter;

    public AppInfoDialog(@NonNull Context context, AppInfoDialog.InputAppsListener listener) {
        // 安卓10 目前默认不加边框，所以需要设置
        super(context);
//        super(context, R.style.NormalDialogStyle);
        this.context = context;
        this.data = new ArrayList<>();
        application = MyApplication.getInstances();
        this.inputAppsListener = listener;
        initView();
        setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        binding = DialogAppAddBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding.getRoot();

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp1 = window.getAttributes();
        lp1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp1);
        //android.R.color.transparent
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(view);

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        //设置宽
        lp.width = (int) (window.getWindowManager().getDefaultDisplay().getWidth() * 0.85);
        lp.height = (int) (window.getWindowManager().getDefaultDisplay().getHeight() * 0.90);
        view.setLayoutParams(lp);
        appInfoAdapter = new AppInfoAdapter(context, data, new AppInfoAdapter.AppInfoListener() {
            @Override
            public void appInfoOnClick() {
                selectData = appInfoAdapter.getCurrentAppInfo();
                inputAppsListener.doWork();
            }

            @Override
            public void delAppInfo() {
            }
        });
        appInfoAdapter.setEnabledAdd(true);
        binding.mainRecyclerViewApps.setAdapter(appInfoAdapter);
        GridLayoutManager manager = new GridLayoutManager(context, 5);
        int space = 60;
        // 设置间距，实际上是padding
        binding.mainRecyclerViewApps.addItemDecoration(new SpacesItemDecoration(space));
        binding.mainRecyclerViewApps.setLayoutManager(manager);

        // 监听回车确认键
        binding.dialogAppAddSearch.setOnEditorActionListener((textView, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE) { // 搜索按钮,搜索会出问题不能自动收起键盘   返回false 收起键盘
                getAppInfo();
                appInfoAdapter.refreshData(data);
                if(data.size() == 0) {
                    binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
                    binding.dialogAddAppNoContent.setVisibility(View.VISIBLE);
                }else {
                    binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
                    binding.dialogAddAppNoContent.setVisibility(View.INVISIBLE);
                }
                hideInputKeyboard(binding.dialogAppAddSearch);
                return false;
            }
            return false;
        });
        binding.dialogAppAddSearch.setOnTouchListener((view12, motionEvent) -> {
            if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                hideInputKeyboard(binding.dialogAppAddSearch);
                showInputKeyboard(binding.dialogAppAddSearch);
                Log.d("Keyboard", "onTouch: ");
            }
            return false;
        });

        //监听输入变化
        binding.dialogAppAddSearch.addTextChangedListener(textWatcher);
    }

    public void hideInputKeyboard(EditText editText){
        InputMethodManager imm=(InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.dialogAppAddSearch.getWindowToken(),0);
        editText.setCursorVisible(false);//显示光标
        editText.clearFocus();
    }

    public void showInputKeyboard(EditText editText){
        editText.requestFocus();
        editText.setCursorVisible(true);
        InputMethodManager manager =(InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText, 0);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            getAppInfo();
            appInfoAdapter.refreshData(data);
            if(data.size() == 0) {
                binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
                binding.dialogAddAppNoContent.setVisibility(View.VISIBLE);
            }else {
                binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
                binding.dialogAddAppNoContent.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void getAppInfo() {
        String inputName = binding.dialogAppAddSearch.getText().toString();
        if(!inputName.isEmpty() && copyData != null && inputName != null) {
            List<AppInfo> list = new ArrayList<>();
            for (AppInfo appInfo : copyData) {
                if(appInfo.getVersionName() == null || "".equals(appInfo.getVersionName())) {
                    continue;
                }
                String all = appInfo.getAppName().toLowerCase();
                String input = inputName.toLowerCase();
                if(all.contains(input)&&!list.contains(appInfo)) {
                    list.add(appInfo);
                }
            }
            data = list;
        }else if(inputName.equals("")){
            data = copyData;
        }
    }

    private AppInfoDialog.InputAppsListener inputAppsListener;

    public interface InputAppsListener {
        void doWork();
    }

    public List<AppInfo> getData() {
        return data;
    }

    public void setData(List<AppInfo> data) {
        this.data = data;
        this.copyData = data;
        appInfoAdapter.refreshData(data);
    }

    public AppInfo getSelectData() {
        return selectData;
    }

    public void setSelectData(AppInfo selectData) {
        this.selectData = selectData;
    }

    @Override
    public void dismiss() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.dialogAppAddSearch.getWindowToken(), 0); // 解决键盘无法关闭问题
        binding.dialogAppAddSearch.setText("");
        super.dismiss();
    }
}
