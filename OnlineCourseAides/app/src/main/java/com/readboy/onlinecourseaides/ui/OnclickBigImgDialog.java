package com.readboy.onlinecourseaides.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.readboy.onlinecourseaides.databinding.DialogAppAddBinding;
import com.readboy.onlinecourseaides.databinding.HistorySelectTobigImgBinding;

/**
 * @Author jll
 * @Date 2022/12/8
 */
public class OnclickBigImgDialog extends Dialog {

    private HistorySelectTobigImgBinding binding;
    private View view;
    private Uri nowUri;

    public Uri getNowUri() {
        return nowUri;
    }

    public void setNowUri(Uri nowUri) {
        this.nowUri = nowUri;
    }

    public OnclickBigImgDialog(@NonNull Context context) {
        super(context);
        binding = HistorySelectTobigImgBinding.inflate(LayoutInflater.from(getContext()), null,false);
        view = binding.getRoot();

        initView();
        setContentView(view);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.getDecorView().setMinimumHeight(WindowManager.LayoutParams.MATCH_PARENT);
        window.getDecorView().setMinimumWidth(WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    private static final String TAG = "OnclickBigImgDialog";
    private void initView() {
        binding.cropImageView.setOnCropImageCompleteListener((view, result) -> {
            Log.d(TAG, "initView: result=>"+result);
            Bitmap bitmap = result.getBitmap();
            listener.doTask(nowUri, bitmap);
        });
        binding.dBigSelect.setOnClickListener(v -> {
            binding.cropImageView.getCroppedImageAsync();
            this.dismiss();
        });
        binding.dBigCancel.setOnClickListener(v->{
            this.dismiss();
        });
    }

    private Uri imgUri;

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
        binding.cropImageView.setImageUriAsync(imgUri);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    public interface OnclickBigImgDialogListener {
        void doTask(Uri uri, Bitmap bitmap);
    }

    private OnclickBigImgDialogListener listener;

    public void setListener(OnclickBigImgDialogListener listener) {
        this.listener = listener;
    }
}
