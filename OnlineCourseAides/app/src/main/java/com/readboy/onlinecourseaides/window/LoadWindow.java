package com.readboy.onlinecourseaides.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BaseInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.readboy.onlinecourseaides.databinding.FloatWinAnimBinding;
import com.readboy.onlinecourseaides.utils.GlobalParam;
import com.readboy.onlinecourseaides.utils.ParamType;
import com.readboy.onlinecourseaides.utils.SettingsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 进度条加载view
 * setProgress(int progress, long animTime)  可以设置动画
 */
public class LoadWindow extends View{

    public static final String SETTINGS_NET_SPEED = "SETTINGS_NET_SPEED";
    public static final String SETTINGS_CLEAR = "SETTINGS_CLEAR";
    public static final String SETTINGS_CARE_EYE = "SETTINGS_CARE_EYE";
    public static final String SETTINGS_NO_MSG = "SETTINGS_NO_MSG";

    FloatWinAnimBinding binding;

    private Handler handler;

    private HashMap<String, Integer> mStatus;

    private final static String TAG = "FloatWindow";
    private Context mContext; // 声明一个上下文对象
    private WindowManager wm; // 声明一个窗口管理器对象
    private static WindowManager.LayoutParams wmParams;
    public View mContentView; // 声明一个内容视图对象
    private boolean isShowing = false; // 是否正在显示

    public LoadWindow(Context context) {
        super(context);
        // 从系统服务中获取窗口管理器，后续将通过该管理器添加悬浮窗
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }

        // 录屏去掉悬浮窗的设置 start --------------------------------------
        // 原理就是和系统录屏保持一致
        wmParams.type = WindowManager.LayoutParams.TYPE_SEARCH_BAR;
        wmParams.setTitle("com.readboy.screenrecord.rbSiri");
        wmParams.format = PixelFormat.RGBA_8888;// TRANSLUCENT;
        // initialize as not-focusable
        wmParams.flags =
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // 录屏去掉悬浮窗的设置 end --------------------------------------

        mContext = context;
        handler = new Handler();

        mStatus = new HashMap<>();
        mStatus.put(SETTINGS_NET_SPEED, 1);
        mStatus.put(SETTINGS_CLEAR, 1);
        mStatus.put(SETTINGS_CARE_EYE, 1);
        mStatus.put(SETTINGS_NO_MSG, 1);
    }

    // 设置悬浮窗的内容布局
    public void setLayout() {
        // 从指定资源编号的布局文件中获取内容视图对象
        binding = FloatWinAnimBinding.inflate(LayoutInflater.from(mContext));
        mContentView = binding.getRoot();
    }

    private void initTextContent() {
        // 获取护眼模式设置
        if (SettingsUtils.isEyesCareEnable()) {
            binding.loadFloatTvTitleCareEyeStatus.setText("已开启");
        } else {
            binding.loadFloatTvTitleCareEyeStatus.setText("已关闭");
        }

        // 获取免打扰设置
        if (SettingsUtils.isNoDisturbEnable()) {
            binding.loadFloatTvTitleNoMsgStatus.setText("已开启");
        } else {
            binding.loadFloatTvTitleNoMsgStatus.setText("已关闭");
        }

        // 获取加速设置
        if (SettingsUtils.isAccelerateEnabled()) {
            binding.loadFloatTvTitleNetSpeedStatus.setText("已开启");
            binding.loadFloatTvTitleClearStatus.setText("已优化");

        } else {
            binding.loadFloatTvTitleNetSpeedStatus.setText("已关闭");
            binding.loadFloatTvTitleClearStatus.setText("未优化");
        }

    }

    // 显示悬浮窗
    public void show() {
        setLayout();
        initTextContent();
        if (mContentView != null) {
            // 设置为TYPE_SYSTEM_ALERT类型，才能悬浮在其它页面之上
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                // 注意TYPE_SYSTEM_ALERT从Android8.0开始被舍弃了
                wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            } else {
                // 从Android8.0开始悬浮窗要使用TYPE_APPLICATION_OVERLAY
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.alpha = 1.0f; // 1.0为完全不透明，0.0为完全透明
            // 对齐方式为靠左且靠上，因此悬浮窗的初始位置在屏幕的左上角
            wmParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
            // 设置悬浮窗的宽度和高度为自适应
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //开启进度条动画
            int[] ints = mListener.onFloatAnim();
            startAnim(ints[0],ints[1]);
            // 添加自定义的窗口布局，然后屏幕上就能看到悬浮窗了
            wm.addView(mContentView, wmParams);
            isShowing = true;
        }
        // 自动关闭
        handler.postDelayed(this::close,1500);
    }

    // 关闭悬浮窗
    public void close() {
        if(isShow()) {
            if (mContentView != null) {
                binding.laAw.cancelAnimation();
                // 移除自定义的窗口布局
//                wm.removeView(mContentView);
                wm.removeViewImmediate(mContentView); // 同步remove
                mContentView = null;
                binding = null;
                isShowing = false;
            }
        }
    }

    // 判断悬浮窗是否打开
    public boolean isShow() {
        return isShowing;
    }

    private LoadFloatClickListener mListener; // 声明一个悬浮窗的点击监听器对象
    // 设置悬浮窗的点击监听器
    public void setOnLoadWinListener(LoadFloatClickListener listener) {
        mListener = listener;
    }

    // 定义一个悬浮窗的点击监听器接口，用于触发点击行为
    public interface LoadFloatClickListener {
        int[] onFloatAnim();
    }

    // 开启动画
    public void startAnim(int progress, long time) {
        // 添加动画
//        binding.laAw.pauseAnimation();
        binding.laAw.playAnimation();
    }
}
