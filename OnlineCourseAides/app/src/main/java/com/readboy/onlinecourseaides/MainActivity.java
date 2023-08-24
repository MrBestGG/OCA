package com.readboy.onlinecourseaides;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.activity.PermissionsActivity;
import com.readboy.onlinecourseaides.activity.SettingsActivity;
import com.readboy.onlinecourseaides.bean.AppInfo;
import com.readboy.onlinecourseaides.bean.CourseInfo;
import com.readboy.onlinecourseaides.bean.OnlineClassData;
import com.readboy.onlinecourseaides.bean.response.DefaultAppResponse;
import com.readboy.onlinecourseaides.bean.response.item.AppItem;
import com.readboy.onlinecourseaides.databinding.ActivityMainBinding;
import com.readboy.onlinecourseaides.fragment.HistoryFragment;
import com.readboy.onlinecourseaides.fragment.OnlineClassFragment;
import com.readboy.onlinecourseaides.network.GreenBrowserLoader;
import com.readboy.onlinecourseaides.service.TaskSupportService;
import com.readboy.onlinecourseaides.service.record.DpiUtils;
import com.readboy.onlinecourseaides.ui.DefaultTipsDialogView;
import com.readboy.onlinecourseaides.utils.CheckAppUtils;
import com.readboy.onlinecourseaides.utils.DialogUtils;
import com.readboy.onlinecourseaides.utils.GlobalParam;
import com.readboy.onlinecourseaides.utils.NetWorkUtils;
import com.readboy.onlinecourseaides.utils.ParamType;
import com.readboy.onlinecourseaides.utils.cache.ACache;
import com.readboy.provider.UserDbSearch;
import com.readboy.provider.mhc.info.UserBaseInfo;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 *
 * 默认缓存采用 ACache
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 是否第一次使用
    private boolean isFirst = true;
    private ActivityMainBinding binding;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 100;
    // 家长管理
    public static final int REQUEST_CODE_PMG = 101;
    private int mDensityDpi, mWidthPixels, mHeightPixels;
    private ParamType selectType = ParamType.SELECT_APP_TITLE_TYPE;

    private boolean isRecordStartSettings = false;

    TextView mainAppTitle;
    TextView mainHistoryTitle;

    private MyApplication application;
    private Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        binding = ActivityMainBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= 31) {
            // 大于安卓11 需要申请获取应用列表权限
            initPermission();
        }

        // 权限检查 悬浮窗，需要弹框系统确认
        checkPermission2();

        // 录屏权限申请
        checkPermission(this);

        initData();
        initView();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main_fragment_content, OnlineClassFragment.class, null)
                .commit();

        ACache aCache = ACache.get(this);
        String isFirst1 = aCache.getAsString("IS_FIRST");
        if(isFirst1 != null) {
            isFirst = Boolean.parseBoolean(isFirst1);
        }
        if(isFirst) {
            // 麦克风扬声器检测
            DialogUtils.getDefaultTipsXPopUp(this, new DefaultTipsDialogView.DefaultTipsDialogListener() {
                @Override
                public void makeSure() {
                    checkSoundDevices();
                    aCache.put("IS_FIRST","false");
                }

                @Override
                public void cancel() {
                }
            }, "是否检测声音设备").show();
        }
    }

    private void checkSoundDevices() {
        boolean b = checkIfMicrophoneIsBusy(this);
        if(b) {
            MyApplication.getInstances().showToast("设备正常");
        }else {
            MyApplication.getInstances().showToast("设备麦克风有问题请查看！！！");
        }

    }

    @SuppressLint("MissingPermission")
    public static boolean checkIfMicrophoneIsBusy(Context ctx) {
        AudioRecord audio = null;
        boolean ready = true;
        try {
            int baseSampleRate = 44100;
            int channel = AudioFormat.CHANNEL_IN_MONO;
            int format = AudioFormat.ENCODING_PCM_16BIT;
            int buffSize = AudioRecord.getMinBufferSize(baseSampleRate, channel, format);
            audio = new AudioRecord(MediaRecorder.AudioSource.MIC, baseSampleRate, channel, format, buffSize);
            audio.startRecording();
            short buffer[] = new short[buffSize];
            int audioStatus = audio.read(buffer, 0, buffSize);
            if(audioStatus == AudioRecord.ERROR_INVALID_OPERATION || audioStatus == AudioRecord.STATE_UNINITIALIZED /* For Android 6.0 */)
                ready = false;
        }
        catch(Exception e){

            ready = false;

        }
        finally {
            try{
                audio.release();
            }
            catch(Exception e){}
        }
        return ready;
    }

    /**
     * 如果遇到闪退，黑屏，点击按钮黑屏，重点考虑权限和录音录屏截屏问题
     * 显示在其他应用上层
     */
    private void checkPermission2() {
        //悬浮窗权限 ACTION_MANAGE_OVERLAY_PERMISSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Log.d(TAG, "onActivityResult:悬浮窗权限");
                // 必须确认权限否则系统在启动服务时创建悬浮窗报错直接闪退
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }

        // 权限 获取应用使用记录
        if(!CheckAppUtils.canUsageStats(this)){
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),809);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void checkPermission(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= 29) {
            int checkPermission =
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 123);
            }
            screenRecordPermission();
        } else if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission =
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //动态申请
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }
            screenRecordPermission();
            return;
        }
        return;
    }

    public void screenRecordPermission() {
        MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_CODE);
        Log.d(TAG, "screenRecordPermission:isRecordStartSettings  ");
    }

    /**
     * 用户是否登录，这里每次调用都会访问一次数据库
     */
    public static boolean isUserLogin(Context context) {
        Log.d(TAG, "isUserLogin: ");
        UserDbSearch userDbSearch = UserDbSearch.getInstance(context);
        UserBaseInfo userBaseInfo = null == userDbSearch ? null : userDbSearch.getUserInfo();
        int uid = null != userBaseInfo ? userBaseInfo.uid : 0;
        return uid > 0;
    }

    int uId = 0;

    @SuppressLint("QueryPermissionsNeeded")
    private void initData() {
        application = MyApplication.getInstances();
        handler = new Handler();
        // 获取默认支持分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        int d = metrics.densityDpi;
        Log.d(TAG, "onCreate: w = "+w+", h = "+h+" d = "+d);

        mDensityDpi = d; // 屏幕密度（每寸像素：120/160/240/320）
        mWidthPixels = w;
        mHeightPixels = h;

        Log.d(TAG, "initData: mDensityDpi,mWidthPixels,mHeightPixels = >" + mDensityDpi + "," + mWidthPixels + "," + mHeightPixels);

        boolean userLogin = isUserLogin(getApplicationContext());
        UserBaseInfo userInfo = null;
        if(userLogin) {
            UserDbSearch userDbSearch = UserDbSearch.getInstance(this);
            userInfo = userDbSearch.getUserInfo();
            uId = userInfo.uid;
            Log.d(TAG, "getAllInfo: userInfo"+userInfo.toString());
        }

        // 检测网络状态
        if (!NetWorkUtils.checkNetWorkConnected(this)) {
            handler.postDelayed(()->{
                Toast.makeText(application,"网络异常，请检查", Toast.LENGTH_SHORT).show();
            },200);
        }

        String sn = NetWorkUtils.createCourseSign(uId+"",false);
        Log.d(TAG, "NetWorkUtils.createCourseSign: sn=>"+sn);
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        mainAppTitle = findViewById(R.id.main_title_apps);
        mainHistoryTitle = findViewById(R.id.main_title_history);
        mainAppTitle.setOnClickListener(this);
        mainHistoryTitle.setOnClickListener(this);
        binding.mainSettingsContent.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    // 跳转到绿色上网或者指定网课应用  -1  没跳转成功  0 跳转到商城   1  跳转成功
    public static final int START_APP_OPEN_STORE = 0;
    public static final int START_APP_OPEN_SUCCESS = 1;
    public static final int START_APP_OPEN_ERROR = -1;
    public int startTheAppsAndCourse(OnlineClassData classData) {
        Intent intent = null;
        String packageName = null;
        if (classData instanceof AppInfo) {
            PackageManager packageManager = getPackageManager();
            packageName = ((AppInfo) classData).getPackageName();
            intent = packageManager.getLaunchIntentForPackage(packageName);
            Log.d(TAG, "startTheAppsAndCourse: packageName = "+packageName);

            if(packageName == null || "".equals(packageName)) {
                application.showToast("应用不存在");
                return -1;
            }
            if(!checkAppInstalled(this, packageName)) {
                openAppStore(this, packageName);
                return START_APP_OPEN_STORE;
            }

            if(intent == null) {
                application.showToast("应用不存在或已隐藏");
                return START_APP_OPEN_STORE;
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
                return START_APP_OPEN_SUCCESS;
            } catch (Exception e) {
                openAppStore(this, packageName);
            }
        } else if (classData instanceof CourseInfo) {
            //启动绿色上网 打开指定网页
            CourseInfo info = (CourseInfo) classData;

            PackageManager packageManager = getPackageManager();
            packageName = GlobalParam.GREENBROWSER_APP_PACKAGENAME;
            intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent == null) {
                openAppStore(this, packageName);
                return START_APP_OPEN_STORE;
            }

            if (info.getUrl() == null || "".equals(info.getUrl())) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(intent);
                    return START_APP_OPEN_STORE;
                } catch (Exception e) {
                    startAppStore(packageName);
                }
            } else {
                // 测试
                String url1 = info.getUrl();
                Uri uri = Uri.parse(url1);
                Log.d(TAG, "startTheAppsAndCourse: url=> "+url1);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivityForResult(intent1, -1);
                    return START_APP_OPEN_SUCCESS;
                } catch (Exception e) {
                    startAppStore(packageName);
                }
            }
        }
        return START_APP_OPEN_ERROR;
    }

    private boolean checkAppInstalled(Context context,String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }


    public void startAppStore(String packageName) {
        try {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName("cn.dream.android.appstore",
                    "cn.dream.android.appstore.ui.activity.AppDetailActivity_");
            intent.setAction("android.intent.action.VIEW");
            intent.setComponent(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pkg", packageName);
            startActivity(intent);
        }catch (Exception e){
            application.showToast("未安装或已停用绿色上网");
        }
    }

    public void doPreStartApp() {
        //开启后台任务支持服务
        startTaskSupportService();
    }

    //服务未启动在启动服务
    private void startTaskSupportService() {
        boolean serviceRunning =
                CheckAppUtils.isServiceRunning(this, "com.readboy.onlinecourseaides.service.TaskSupportService");
        if (!serviceRunning) {
            Intent service = new Intent(MainActivity.this, TaskSupportService.class);
            service.putExtra("resultCode", resultCode);

            Intent intent = MyApplication.getInstances().getData();

            service.putExtra("data", intent);
            service.putExtra("mWidthPixels", mWidthPixels);
            service.putExtra("mHeightPixels", mHeightPixels);
            service.putExtra("mDensityDpi", mDensityDpi);
            startService(service);// 启动服务
            Log.d(TAG, "startTaskSupportService: startService ");
        }
    }

    @SuppressLint("ResourceAsColor")
    public void changesSelectTitle(TextView source, TextView target) {
        int paddingBottom = source.getPaddingBottom();
        int paddingTop = source.getPaddingTop();
        int paddingLeft = source.getPaddingLeft();
        int paddingRight = source.getPaddingRight();
        int paddingBottom2 = target.getPaddingBottom();
        int paddingTop2 = target.getPaddingTop();
        int paddingLeft2 = target.getPaddingLeft();
        int paddingRight2 = target.getPaddingRight();

        source.setBackgroundResource(R.drawable.main_normal_title);
        target.setBackgroundResource(R.drawable.main_select_title);

        source.setTextColor(this.getResources().getColor(R.color.blue));
        target.setTextColor(this.getResources().getColor(R.color.white));

        source.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        target.setPadding(paddingLeft2, paddingTop2, paddingRight2, paddingBottom2);
    }

    public void initPermission() {

    }

    private void refreshView(ParamType type) {
        if (type == ParamType.SELECT_APP_TITLE_TYPE) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment_content, OnlineClassFragment.class, null)
                    .commit();
        } else if (type == ParamType.SELECT_HISTORY_TITLE_TYPE) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment_content, HistoryFragment.class, null)
                    .commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_title_apps: {
                if (selectType == ParamType.SELECT_APP_TITLE_TYPE) {
                    break;
                }
                changesSelectTitle(mainHistoryTitle, mainAppTitle);
                refreshView(ParamType.SELECT_APP_TITLE_TYPE);
                selectType = ParamType.SELECT_APP_TITLE_TYPE;
            }
            break;
            case R.id.main_title_history: {
                if (selectType == ParamType.SELECT_HISTORY_TITLE_TYPE) {
                    break;
                }
                changesSelectTitle(mainAppTitle, mainHistoryTitle);
                refreshView(ParamType.SELECT_HISTORY_TITLE_TYPE);
                selectType = ParamType.SELECT_HISTORY_TITLE_TYPE;
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();

        //获取免打扰设置保存
//        boolean serviceRunning =
//                CheckAppUtils.isServiceRunning(this, "com.readboy.onlinecourseaides.service.TaskSupportService");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 跳转返回的回调
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_CODE == requestCode) {
            Log.d(TAG, " onActivityResult initData  -->  " + "  mWidthPixels: " + mWidthPixels + "  mHeightPixels: " + mHeightPixels + "  resultCode: " + resultCode + "  data: " + data);
            this.resultCode = resultCode;
            this.data = data;

            MyApplication.getInstances().setData(data);
            MyApplication.getInstances().setCode(resultCode);

            Intent service = new Intent(this, TaskSupportService.class);
            service.putExtra("resultCode", resultCode);
            service.putExtra("data", data);
            service.putExtra("mWidthPixels", mWidthPixels);
            service.putExtra("mHeightPixels", mHeightPixels);
            service.putExtra("mDensityDpi", mDensityDpi);
            startService(service);
        }
    }

    public static void openAppStore(Context context, String pkg) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("cn.dream.android.appstore",
                "cn.dream.android.appstore.ui.activity.AppDetailActivity_");
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pkg", pkg);
//        intent.putExtra("type", "auto_download");//自动开始下载
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Log.e(TAG, "ActivityNotFoundException " + exception.getLocalizedMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException" + e.getLocalizedMessage());
            Toast.makeText(context, "当前版本商城不支持跳转，请手动打开后安装", Toast.LENGTH_SHORT).show();
        }
    }

    private Intent data = new Intent();
    private int resultCode = -1;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 两次返回退出
//            exit();
            // 主界面返回压到后台
            moveTaskToBack(false);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
//            System.exit(0);
        }
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

}