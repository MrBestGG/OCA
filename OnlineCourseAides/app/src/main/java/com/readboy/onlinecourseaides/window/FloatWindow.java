package com.readboy.onlinecourseaides.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.databinding.FloatWinSmallBinding;
import com.readboy.onlinecourseaides.databinding.FloatWinToolsBinding;
import com.readboy.onlinecourseaides.utils.ParamType;
import com.readboy.onlinecourseaides.utils.SettingsUtils;
import com.readboy.onlinecourseaides.utils.SingleClicker;

import kotlin.Unit;

@SuppressLint("ClickableViewAccessibility")
public class FloatWindow extends View {

    private volatile String recordTimeUI;

    FloatWinToolsBinding binding;
    FloatWinSmallBinding binding2;

    private ParamType winType = ParamType.FLOAT_WIN_TOOLS;

    private final static String TAG = "FloatWindow";
    private Context mContext; // 声明一个上下文对象
    private WindowManager wm; // 声明一个窗口管理器对象
    private static WindowManager.LayoutParams wmParams;
    public View mContentView; // 声明一个内容视图对象
    public View mContentSmallView; // 声明一个内容视图对象
    private float mScreenX, mScreenY; // 触摸点在屏幕上的横纵坐标
    private float mLastX, mLastY; // 上次触摸点的横纵坐标
    private float mDownX, mDownY; // 按下点的横纵坐标
    private boolean isShowing = false; // 是否正在显示
    // 0 停止录制 1 开始录制
    private ParamType videoStatus;
    private ParamType soundStatus;
    // 录音录屏不能同时发生
    private ParamType recordStatus;

    public static final String FLOAT_WIN_SOUND = "录音";
    public static final String FLOAT_WIN_SCREEN_RECORD = "录屏";

    public void updateVideoTime(String text) {
        if(winType == ParamType.FLOAT_WIN_TOOLS) {
            binding.floatWinToolsScreenRecordTime.setText(text);
        }else if(winType == ParamType.FLOAT_WIN_SMALL) {
            binding2.floatToolsSmallRecordTime.setText(text);
        }
        Log.d(TAG, "updateVideoTime: text = "+text);
    }

    public void updateSoundTime(String text) {
        if(winType == ParamType.FLOAT_WIN_TOOLS) {
            binding.floatWinToolsRecordingTime.setText(text);
        }else if(winType == ParamType.FLOAT_WIN_SMALL) {
            binding2.floatToolsSmallRecordTime.setText(text);
        }
        Log.d(TAG, "updateSoundTime: text = "+text);
    }


    public FloatWindow(Context context) {
        super(context);
        // 初始化时间
        recordTimeUI = "00:00";
        // 从系统服务中获取窗口管理器，后续将通过该管理器添加悬浮窗
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();

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
        }
        mContext = context;

        soundStatus = ParamType.SERVICE_TOOLS_STOP_RECORD;
        videoStatus = ParamType.SERVICE_TOOLS_STOP_RECORD;
        recordStatus = ParamType.SERVICE_TOOLS_NO_RECORD;
    }

    // 设置悬浮窗的内容布局
    public void setLayout() {
        // 从指定资源编号的布局文件中获取内容视图对象
        binding = FloatWinToolsBinding.inflate(LayoutInflater.from(mContext));
        mContentView = binding.getRoot();

        binding2 = FloatWinSmallBinding.inflate(LayoutInflater.from(mContext));
        mContentSmallView = binding2.getRoot();

        // 接管悬浮窗的触摸事件，使之即可随手势拖动，又可处理点击动作
        // 在发生触摸事件时触发
        setmListenerX(mContentView);
        setmListenerX(mContentSmallView);
        if(winType == ParamType.FLOAT_WIN_TOOLS) {
            initView();
        }else if(winType == ParamType.FLOAT_WIN_SMALL) {
            initSmallView();
        }
    }

    private void initSmallView() {
        if(soundStatus == ParamType.SERVICE_TOOLS_START_RECORD ||videoStatus == ParamType.SERVICE_TOOLS_START_RECORD) {
            binding2.floatToolsSmallContentTitle.setVisibility(View.INVISIBLE);
            binding2.floatToolsSmallContentRecord.setVisibility(View.VISIBLE);
            binding2.floatToolsSmallRecordImg.setVisibility(View.VISIBLE);
            updateSoundTime(recordTimeUI);
        }else {
            binding2.floatToolsSmallContentTitle.setVisibility(View.VISIBLE);
            binding2.floatToolsSmallContentRecord.setVisibility(View.INVISIBLE);
            binding2.floatToolsSmallRecordImg.setVisibility(View.INVISIBLE);
        }
    }

    private void setmListenerX(View view) {
        view.setOnTouchListener((v,e)->{
            mScreenX = e.getRawX();
            mScreenY = e.getRawY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: // 手指按下
                    mDownX = mScreenX;
                    mDownY = mScreenY;
                    break;
                case MotionEvent.ACTION_MOVE: // 手指移动
                    updateViewPosition(); // 更新视图的位置
                    break;
                case MotionEvent.ACTION_UP: // 手指松开
                    updateViewPosition(); // 更新视图的位置
                    // 响应悬浮窗的点击事件
                    if (Math.abs(mScreenX - mDownX) < 3
                            && Math.abs(mScreenY - mDownY) < 3) {
                        if (mListener != null) {
                            mListener.onFloatClick(v, ParamType.SERVICE_TOOLS_START_RECORD);
                        }
                    }
                    break;
            }
            mLastX = mScreenX;
            mLastY = mScreenY;
            return false;
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initView() {
        if(soundStatus == ParamType.SERVICE_TOOLS_START_RECORD) {
            binding.floatWinToolsRecording.setBackgroundResource(R.mipmap.f_sound_recording);
            binding.floatWinToolsRecordingTime.setVisibility(View.VISIBLE);
            updateSoundTime(recordTimeUI); //FloatToolsWin 刷新时间
        }else {
            binding.floatWinToolsRecording.setBackgroundResource(R.mipmap.f_sound_record);
            binding.floatWinToolsRecordingTime.setText(FLOAT_WIN_SOUND);
        }
        if(videoStatus == ParamType.SERVICE_TOOLS_START_RECORD) {
            binding.floatWinToolsScreenRecord.setBackgroundResource(R.mipmap.f_sound_recording);
            binding.floatWinToolsScreenRecordTime.setVisibility(View.VISIBLE);
            updateVideoTime(recordTimeUI); //FloatToolsWin 刷新时间
        }else {
            binding.floatWinToolsScreenRecord.setBackgroundResource(R.mipmap.f_screen_record);
            binding.floatWinToolsScreenRecordTime.setText(FLOAT_WIN_SCREEN_RECORD);
        }

        binding.floatWinToolsMinimize.setOnClickListener(v->{
            mListener.onFloatClick(v, ParamType.FLOAT_WIN_SMALL);
        });

        binding.floatWinToolsScreenRecordContent.setOnClickListener(new SingleClicker(1000,v->{
            ParamType type = null;
            if(videoStatus == ParamType.SERVICE_TOOLS_STOP_RECORD) {
                type = ParamType.SERVICE_TOOLS_START_RECORD;
            }else if(videoStatus == ParamType.SERVICE_TOOLS_START_RECORD) {
                type = ParamType.SERVICE_TOOLS_STOP_RECORD;
            }
            mListener.onFloatClick(v, type);
            return Unit.INSTANCE;
        }));

        binding.floatWinToolsSoundContent.setOnClickListener(new SingleClicker(1000,v->{
            ParamType type = null;
            if(soundStatus == ParamType.SERVICE_TOOLS_STOP_RECORD) {
                type = ParamType.SERVICE_TOOLS_START_RECORD;
            }else if(soundStatus == ParamType.SERVICE_TOOLS_START_RECORD) {
                type = ParamType.SERVICE_TOOLS_STOP_RECORD;
            }
            mListener.onFloatClick(v, type);
            return Unit.INSTANCE;
        }));

        binding.floatWinToolsScreenShotContent.setOnClickListener(new SingleClicker(1000,v->{
            mListener.onFloatClick(v, ParamType.SERVICE_TOOLS_START_Screenshot);
            return Unit.INSTANCE;
        }));

        binding.floatWinToolsExercisesContent.setOnClickListener(v->{
            mListener.onFloatClick(v, ParamType.SERVICE_TOOLS_START_Exercises);
        });
        binding.floatWinToolsCloseContent.setOnClickListener(v->{
            mListener.onFloatClick(v, ParamType.SERVICE_TOOLS_CLOSE);
        });
        binding.floatWinToolsMasterContent.setOnClickListener(v -> {
            mListener.onFloatClick(v, ParamType.SERVICE_TOOLS_TO_MASTER);
        });
    }

    public void stopRecordSound() {
        if(binding != null) {
            binding.floatWinToolsRecording.setBackgroundResource(R.mipmap.f_sound_record);
            binding.floatWinToolsRecordingTime.setText(FLOAT_WIN_SOUND);
        }
        soundStatus = ParamType.SERVICE_TOOLS_STOP_RECORD;
    }

    public void startRecordSound() {
        binding.floatWinToolsScreenRecord.setBackgroundResource(R.mipmap.f_screen_record);
        binding.floatWinToolsScreenRecordTime.setText(FLOAT_WIN_SCREEN_RECORD);

        binding.floatWinToolsRecording.setBackgroundResource(R.mipmap.f_sound_recording);
        binding.floatWinToolsRecordingTime.setVisibility(View.VISIBLE);
        soundStatus = ParamType.SERVICE_TOOLS_START_RECORD;
    }

    public void stopRecordScreen() {
        if(binding != null) {
            binding.floatWinToolsScreenRecord.setBackgroundResource(R.mipmap.f_screen_record);
            binding.floatWinToolsScreenRecordTime.setText(FLOAT_WIN_SCREEN_RECORD);
        }
        videoStatus = ParamType.SERVICE_TOOLS_STOP_RECORD;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void startRecordScreen() {
        binding.floatWinToolsRecording.setBackgroundResource(R.mipmap.f_sound_record);
        binding.floatWinToolsRecordingTime.setText(FLOAT_WIN_SOUND);

        binding.floatWinToolsScreenRecord.setBackgroundResource(R.mipmap.f_sound_recording);
        binding.floatWinToolsScreenRecordTime.setVisibility(View.VISIBLE);
        videoStatus = ParamType.SERVICE_TOOLS_START_RECORD;
    }

    // 更新悬浮窗的视图位置
    private void updateViewPosition() {
        // 此处不能直接转为整型，因为小数部分会被截掉，重复多次后就会造成偏移越来越大
        wmParams.x = Math.round(wmParams.x + mLastX - mScreenX);

        wmParams.y = Math.round(wmParams.y + mScreenY - mLastY);
        // 通过窗口管理器更新内容视图的布局参数
        if(winType == ParamType.FLOAT_WIN_TOOLS) {
            wm.updateViewLayout(mContentView, wmParams);
        }else if(winType == ParamType.FLOAT_WIN_SMALL) {
            wm.updateViewLayout(mContentSmallView, wmParams);
        }
    }

    // 显示悬浮窗
    public void show() {
        setLayout();
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

        //获取设置透明度
        int alpha = SettingsUtils.getFloatWindowAlpha();
        float percent = alpha/100.0f;
        Log.d(TAG, "show: percent=>"+percent+",alpha=>"+alpha);
        wmParams.alpha = 1.0f * percent; // 1.0为完全不透明，0.0为完全透明
        // 对齐方式为靠左且靠上，因此悬浮窗的初始位置在屏幕的左上角
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER;
        wmParams.x = 0;
        wmParams.y = 0;
        // 设置悬浮窗的宽度和高度为自适应
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // 添加自定义的窗口布局，然后屏幕上就能看到悬浮窗了
        if(winType == ParamType.FLOAT_WIN_TOOLS) {
            wm.addView(mContentView, wmParams);
        }else if(winType == ParamType.FLOAT_WIN_SMALL) {
            wm.addView(mContentSmallView, wmParams);
        }
        isShowing = true;
    }

    public void selectWin(ParamType type) {
        // 添加自定义的窗口布局，然后屏幕上就能看到悬浮窗了
        if(type == ParamType.FLOAT_WIN_TOOLS) {
            winType = ParamType.FLOAT_WIN_TOOLS;
        }else if(type == ParamType.FLOAT_WIN_SMALL) {
            winType = ParamType.FLOAT_WIN_SMALL;
        }
    }

    public void setRecordTimeUI(String timeUI) {
        this.recordTimeUI = timeUI;
    }

    // 关闭悬浮窗
    public void close() {
        if (mContentView != null && winType == ParamType.FLOAT_WIN_TOOLS &&isShow()) {
            // 移除自定义的窗口布局
            wm.removeViewImmediate(mContentView);
            isShowing = false;
        }
        if(mContentSmallView != null && winType == ParamType.FLOAT_WIN_SMALL && isShow()){
            wm.removeViewImmediate(mContentSmallView);
            isShowing = false;
        }
    }

    public void closeWin(ParamType type) {
        if(type == ParamType.FLOAT_WIN_TOOLS) {
            wm.removeViewImmediate(mContentView);
            Log.d(TAG, "closeWin: mContentView");
            isShowing = false;
        }else if(type == ParamType.FLOAT_WIN_SMALL) {
            wm.removeViewImmediate(mContentSmallView);
            isShowing = false;
        }
    }

    // 判断悬浮窗是否打开
    public boolean isShow() {
        return isShowing;
    }

    private FloatClickListener mListener; // 声明一个悬浮窗的点击监听器对象
    // 设置悬浮窗的点击监听器
    public void setOnFloatListener(FloatClickListener listener) {
        mListener = listener;
    }

    // 定义一个悬浮窗的点击监听器接口，用于触发点击行为
    public interface FloatClickListener {
        void onFloatClick(View v, ParamType paramType);
    }
}
