package com.readboy.onlinecourseaides.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.reflect.TypeToken;
import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.MainActivity;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.activity.PermissionsActivity;
import com.readboy.onlinecourseaides.bean.AppInfo;
import com.readboy.onlinecourseaides.bean.ExercisesRecord;
import com.readboy.onlinecourseaides.bean.ScreenRecord;
import com.readboy.onlinecourseaides.bean.ScreenShotRecord;
import com.readboy.onlinecourseaides.bean.SoundRecord;
import com.readboy.onlinecourseaides.bean.TaskSupportRecord;
import com.readboy.onlinecourseaides.receiver.DetectionAppReceiver;
import com.readboy.onlinecourseaides.receiver.NotifyServiceReceiver;
import com.readboy.onlinecourseaides.receiver.RecordCodeReceiver;
import com.readboy.onlinecourseaides.receiver.RecordReceiver;
import com.readboy.onlinecourseaides.utils.EyesUtils;
import com.readboy.onlinecourseaides.utils.GlobalParam;
import com.readboy.onlinecourseaides.utils.GsonManager;
import com.readboy.onlinecourseaides.utils.ParamType;
import com.readboy.onlinecourseaides.utils.SettingsUtils;
import com.readboy.onlinecourseaides.utils.VoiceAssistantUtil;
import com.readboy.onlinecourseaides.utils.cache.ACache;
import com.readboy.onlinecourseaides.window.FloatSelectWindow;
import com.readboy.onlinecourseaides.window.FloatWindow;
import com.readboy.onlinecourseaides.window.LoadWindow;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 后台的操作、悬浮窗   基础功能服务
 */
public class TaskSupportService extends Service implements Handler.Callback {

    private Context mAppContext;

    private MediaProjection mMediaProjection;
    // 音视频录制对象，暂时启用视频
    private MediaRecorder mMediaRecorder;
    //    private AudioRecorder audioRecorderMine;
    private VirtualDisplay mVirtualDisplay;
    private boolean isScreenRecording = false;
    private boolean isSoundRecording = false;
    private int mScreenDensity = 1, mWidthPixels = 720, mHeightPixels = 480;
    private boolean isAudio = false;
    private int timeCount = 0;
    // 存放资源的文件夹
    private String videoPath;
    private String picturePath;
    private String basePath;
    private String soundPath;
    // 媒体文件绝对路径
    private String myVideoPath;
    private String mySoundPath;

    private ParamType recordType;
    private String recordTimeUI = "00:00"; // 时间记录UI 显示Text

    //消息通知
    public static final int SERVICE_RUNNING = 102;
    public static final int SERVICE_SIMPLE_MSG = 103;
    public static final String SERVICE_MSG_CHANNEL = "com.readboy.onlinecourseaides";
    public static final String SERVICE_EASY_MSG_CHANNEL = "com.readboy.onlinecourseaides";
    public static final int MSG_TYPE_COUNT_DOWN = 1045;

    // 悬浮工具窗
    private FloatWindow floatWindow;
    // 加载动画
    private LoadWindow loadWindow;
    private FloatSelectWindow selectWindow;

    //窗口大小
    private ParamType winType;

    private Handler handler;
    private Handler mHandler;

    public static final int HANDLER_SERVICE_SCREEN_SHOT_FINISH = 200;
    public static final int HANDLER_SERVICE_SOUND_SCREEN_SHOT_FINISH = 201;
    public static final int HANDLER_SERVICE_SCREEN_RECORD_FINISH = 202;
    public static final int HANDLER_SERVICE_SOUND_STOP_FINISH = 203;
    public static final int HANDLER_SERVICE_SCREEN_STOP_FINISH = 204;

    public static final int HANDLER_SERVICE_SCREEN_START = 205;
    public static final int HANDLER_SERVICE_SOUND_START = 206;

    private boolean isStopSoundService = false;
    private boolean isStopScreenService = false;

    // 广播
    private DetectionAppReceiver detectionAppReceiver;
    private NotifyServiceReceiver notifyServiceReceiver;
    private RecordCodeReceiver recordCodeReceiver;
    private boolean isSoundRecordSuccess = false;

    // 记录启动过的App
    private HashMap<String, Integer> appStartList;
    //记录当前启动的应用
    private String currentAppPackageName = "";

    // 缓存
    private ACache aCache;
    //所有的网课应用名单 （更多里面的内容，需要从网络配置获取）
    private List<AppInfo> allOnlineCourseListCache;
    //所有的网课应用名单=> 用于比较判断的名单列表
    private HashMap<String, AppInfo> allOnlineCourseList;
    //当天使用的appHistoryList  记录历史
    private List<TaskSupportRecord> toDayTaskRecordList;
    //记录当前app记录
    private TaskSupportRecord currentTaskRecords;
    // 操作历史缓存  [时间,记录]  存储到本地
    private HashMap<String, List<TaskSupportRecord>> taskRecordsCacheTwo;
    //操作历史缓存 缓存索引 时间 [主要作为历史记录页面快速查找排序的索引]
    private List<String> taskRecordsCacheKeys;
    // 这里是在添加记录时记录变量，方便完成操作时，安全添加到历史记录
    private SoundRecord soundRecords;
    private ScreenRecord screenRecords;
    private ExercisesRecord exercisesRecords;
    private HashMap<String, Boolean> settingStatus;

    // 服务返回参数
    private volatile String resSoundFilePath;
    private volatile String resSoundFileName;
    private volatile String resScreenRecordFilePath;
    private volatile String resScreenRecordFileName;
    private volatile String resScreenShotsFilePath;
    private volatile String resScreenShotsFileName;
    private volatile String resSoundScreenShotsFilePath;
    private volatile String resSoundScreenShotsFileName;
    private volatile String resScreenRecordScreenShotsFilePath;
    private volatile String resScreenRecordScreenShotsFileName;

    // 记录时间
    private long startTime;
    private long endTime;
    private String dateForToday;
    // 记录是否关闭过语音助手，用于关闭后恢复
    boolean closeVoiceAssistant = false;

    private int record_type = -1;

    // 当前正在运行的录制服务
    private String currentRunningService = "";
    private boolean startHaveQuest = false;

    public TaskSupportService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        init();
//        notifyServiceRunning(getResources().getString(R.string.app_name));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        winType = ParamType.FLOAT_WIN_TOOLS;
        handler = new Handler();
        mHandler = new Handler(Looper.getMainLooper(), this);

        initData();
        initFloatWin();
        initLoadWin();
        // 改造dialog
        initSelectWin();
        Log.d(TAG, "init: receiver");
        //同步今日使用网课应用列表
        initHistoryCache();
        //注册接收器，接受app start pause 广播， 做出操作  index => [0:打开,1:关闭]
        initReceives();
        registerReceiveDetectionApp();
        registerReceiveNotifyService();
        registerRecordCodeReceiver();
    }

    private boolean isCheckMyPermission = true;

    public boolean checkMyPermission() {
        if (MyApplication.getInstances().getData() == null) {
            Intent intent1 = new Intent();
            intent1.setClass(this, PermissionsActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            intentData = MyApplication.getInstances().getData();
            Log.d(TAG, "checkMyPermission: ");
            if (intentData == null) {
                isCheckMyPermission = false;
                return false;
            }
            isCheckMyPermission = true;
            return true;
        }
        isCheckMyPermission = true;
        return true;
    }

    private boolean isFirst = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initReceives() {
        // 广播多次出现 执行一次
        detectionAppReceiver = new DetectionAppReceiver((pkgName, index) -> {
            //获取设置
            getOnlineCourseAidesSettings();

            if (index == 0) {
                getDefaultAppList();
                boolean isOnlineCourse = allOnlineCourseList.containsKey(pkgName);
                Log.d(TAG, "init: start pkgName =" + pkgName + ", isOnlineCourse=" + isOnlineCourse + ", currentAppPackageName = " + currentAppPackageName);
                if (isOnlineCourse) {
                    // 判断网课模式是否打开
                    if (settingStatus.get(SettingsUtils.KEY_AIDES_ENABLE)) {
                        updateAppInfo(pkgName);
                        doTaskStartAidesContent(pkgName);
                    }
                    // 记录相关网课APP 开始运行时间
                    startTime = System.currentTimeMillis();
                } else {
                    closeWin(ParamType.SERVICE_TOOLS_WIN, ParamType.SERVICE_LOAD_WIN);
                }
                currentAppPackageName = pkgName;
            } else if (index == 1) {
                getDefaultAppList();
                boolean isOnlineCourse = allOnlineCourseList.containsKey(pkgName);
                Log.d(TAG, "init: stop pkgName =" + pkgName + ", isOnlineCourse=" + isOnlineCourse + ", pkgName = " + currentAppPackageName);
                if (isOnlineCourse) {
                    if (pkgName.equals(currentAppPackageName)) {
                        if (settingStatus.get(SettingsUtils.KEY_AIDES_ENABLE)) {
                            // 记录学习时长  毫秒值需要转化
                            saveRecordTimeInCacheObject();
                            // 保存缓存
                            stopOnlineCourseTaskSaveCache();
                        }
                    }
                    // 关闭网课服务内容 （关闭相关设置和浮窗）
                    doTaskStopAidesContent();
                }
                // 关闭悬浮窗
                closeWin(ParamType.SERVICE_LOAD_WIN, ParamType.SERVICE_TOOLS_WIN);
            }
        });

        // 注册接收器，接收刷新数据指令
        notifyServiceReceiver = new NotifyServiceReceiver(new NotifyServiceReceiver.NotifyServiceListener() {
            @Override
            public void doTask(String recordData) {
                TaskSupportRecord record =
                        GsonManager.getInstance().fromJson(recordData, new TypeToken<TaskSupportRecord>() {
                        });
                Log.d(TAG, "notifyServiceReceiver: recordData =>" + recordData);
                for (int i = 0; i < toDayTaskRecordList.size(); i++) {
                    TaskSupportRecord record1 = toDayTaskRecordList.get(i);
                    if (record1.getPackageName().equals(record.getPackageName())) {
                        toDayTaskRecordList.set(i, record);
                        Log.d(TAG, "notifyServiceReceiver: toDayTaskRecordList =>" + toDayTaskRecordList);
                        taskRecordsCacheTwo.put(getRecordTime(), toDayTaskRecordList);
                        break;
                    }
                }
            }

            @Override
            public void refreshDefaultAppList(String appList) {
                List<AppInfo> appInfoList = GsonManager.getInstance().fromJson(appList, new TypeToken<List<AppInfo>>() {
                });
                for (AppInfo appInfo : appInfoList) {
                    allOnlineCourseList.put(appInfo.getPackageName(), appInfo);
                }
            }
        });

        // 用于接收录制服务的结果成功还是失败  特别注意广播不是顺序的
        recordCodeReceiver = new RecordCodeReceiver(intent -> {
            // 区分服务
            String serviceName = intent.getStringExtra(RecordCodeReceiver.SERVICE_NAME);
            String serviceType = intent.getStringExtra(RecordCodeReceiver.SERVICE_TYPE);

            // 共同返回参数
            String code = intent.getStringExtra(RecordCodeReceiver.RECORD_RESULT_CODE);
            String filePath = intent.getStringExtra(RecordCodeReceiver.RESULT_FILE_PATH);
            String fileName = intent.getStringExtra(RecordCodeReceiver.RESULT_FILE_NAME);

            if (serviceName != null) {
                switch (serviceName) {
                    case RecordCodeReceiver.SERVICE_NAME_SOUND: {
                        if (code.equals(RecordCodeReceiver.RECORD_CODE_SUCCESS)) {
                            if (!isShortTime()) {
                                //成功保存到历史记录
                                soundRecords.setSoundLengthTime(timeCount);
                                soundRecords.setFileName(fileName);
                                soundRecords.setSoundPath(filePath);
                                currentTaskRecords.getSoundRecords().add(soundRecords);
                                // 保存缓存
                                saveTodayRecordList();
                                stopOnlineCourseTaskSaveCache();//录音保存
                                Log.d(TAG, "stopSoundRecord: stop SERVICE_NAME_SOUND RECORD_CODE_SUCCESS");
                            } else {
                                String imgPath = null;
                                // 删除录制无用文件
                                if (soundRecords.getSoundScreenShot() != null) {
                                    imgPath = soundRecords.getSoundScreenShot().getImgPath();
                                }
                                if (imgPath != null) {
                                    delErrorFile(filePath, imgPath);
                                } else {
                                    delErrorFile(filePath);
                                }
                            }
                        } else {
                            MyApplication.getInstances().showToast("录制失败");
                            // 删除录制无用文件
                            delErrorFile(filePath);
                        }
                        isSoundRecording = false;
                        //占用停止  需要启动需要的服务
                        if (startHaveQuest) {
                            mHandler.sendEmptyMessage(HANDLER_SERVICE_SCREEN_START);
                            startHaveQuest = false;
                        }
                    }
                    break;
                    case RecordCodeReceiver.SERVICE_NAME_RECORD: {
                        String screenShotsFilePath = intent.getStringExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_PATH);
                        String screenShotsFileName = intent.getStringExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_NAME);
                        // 服务类型
                        if (serviceType.equals(RecordCodeReceiver.SERVICE_TYPE_RECORD)) {
                            if (code.equals(RecordCodeReceiver.RECORD_CODE_SUCCESS)) {
                                this.resScreenRecordScreenShotsFileName = screenShotsFileName;
                                this.resScreenRecordScreenShotsFilePath = screenShotsFilePath;
                                this.resScreenRecordFileName = fileName;
                                this.resScreenRecordFilePath = filePath;
                                if (!isShortTime()) {
                                    screenRecords.setFileName(resScreenRecordFileName);
                                    screenRecords.setScreenRecordPath(resScreenRecordFilePath);
                                    saveImgHistory(resScreenRecordScreenShotsFilePath, 1, resScreenRecordScreenShotsFileName);// 录屏保存
                                    currentTaskRecords.getScreenRecords().add(screenRecords);
                                    saveTodayRecordList();
                                    stopOnlineCourseTaskSaveCache();//录屏保存
                                } else {
                                    // 删除录制无用文件
                                    delErrorFile(filePath, screenShotsFilePath);
                                }
                            } else {
                                MyApplication.getInstances().showToast("录制失败");
                                // 删除录制无用文件
                                delErrorFile(filePath, screenShotsFilePath);
                            }
                            isScreenRecording = false;
                            //占用停止  需要启动需要的服务
                            if (startHaveQuest) {
                                mHandler.sendEmptyMessage(HANDLER_SERVICE_SOUND_START);
                                startHaveQuest = false;
                            }
                        }
                        if (serviceType.equals(RecordCodeReceiver.SERVICE_TYPE_SCREEN_SHOTS)) {
                            //SERVICE_SCREEN_SHOT_FINISH
                            record_type = intent.getIntExtra(DoRecordService.RECORD_TYPE, -1);
                            if (record_type != -1) {
                                switch (record_type) {
                                    case DoRecordService.RECORD_TYPE_SCREEN_SHOTS_SOUND: {
                                        this.resSoundScreenShotsFileName = screenShotsFileName;
                                        this.resSoundScreenShotsFilePath = screenShotsFilePath;
                                        saveImgHistory(resSoundScreenShotsFilePath, 2, resSoundScreenShotsFileName);// 录音保存
                                    }
                                    break;
                                    case DoRecordService.RECORD_TYPE_SCREEN_SHOTS: {
//                                        this.resScreenShotsFileName = screenShotsFileName;
//                                        this.resScreenShotsFilePath = screenShotsFilePath;
//                                        saveImgHistory(resScreenShotsFilePath, 0, resScreenShotsFileName);// 截图保存
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                    case RecordCodeReceiver.SERVICE_NAME_SCREENSHOTS: {
                        String screenShotsFilePath = intent.getStringExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_PATH);
                        String screenShotsFileName = intent.getStringExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_NAME);
                        if (screenShotsFilePath != null && !"".equals(screenShotsFilePath)) {
                            saveImgHistory(screenShotsFilePath, 0, screenShotsFileName);// 截图保存
                            saveTodayRecordList();
                            stopOnlineCourseTaskSaveCache();//截图保存
                        }
                    }
                    break;
                }
            }
        });
    }

    private void saveTodayRecordList() {
        if (!toDayTaskRecordList.contains(currentTaskRecords)) {
            toDayTaskRecordList.add(currentTaskRecords);
        }
    }

    public void delErrorFile(String... filePath) {
        for (String s : filePath) {
            if (s != null) {
                File file = new File(s);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public void saveRecordTimeInCacheObject() {
        String recordTime = getRecordTime();
        endTime = System.currentTimeMillis();

        String learningTime = "";
        String speedTime = "";
        String careEyesTime = "";

        learningTime = getTransformTime(currentTaskRecords.getLearningTime());
        speedTime = getTransformTime(currentTaskRecords.getSpeedTime());
        careEyesTime = getTransformTime(currentTaskRecords.getEyeCareTime());

        Log.d(TAG, "doTaskStartAidesContent: " + currentAppPackageName);
        if (settingStatus.get(SettingsUtils.KEY_AIDES_ENABLE)) {
            currentTaskRecords.setLearningTime(learningTime);
            currentTaskRecords.getExercisesRecords().setExercisesLengthTime(learningTime);
            // 开启护眼
            if (settingStatus.get(SettingsUtils.KEY_EYES_CARE_ENABLED)) {
                currentTaskRecords.setEyeCareTime(careEyesTime);
            }
            // 开启内存清理
            if (settingStatus.get(SettingsUtils.KEY_AIDES_SPEED_ENABLE)) {
                currentTaskRecords.setSpeedTime(speedTime);
            }
        }

        if (!taskRecordsCacheKeys.contains(recordTime)) {
            taskRecordsCacheKeys.add(recordTime);
        }
    }

    public String getTransformTime(String time) {
        Log.d(TAG, "getTransformTime: => " +time);
        String learningTime = "";

        if (time == null || "".equals(time)) {
            learningTime = endTime - startTime +"";
        } else {
            Long aLong = Long.valueOf(time);
            learningTime = endTime - startTime + aLong +"";
        }
        return learningTime;
    }

    public void stopOnlineCourseTaskSaveCache() {
        //网课模式未打开不计入
        if (!settingStatus.get(SettingsUtils.KEY_AIDES_ENABLE)) {
            return;
        }

        String recordTime = getRecordTime();

        if (!taskRecordsCacheKeys.contains(recordTime)) {
            taskRecordsCacheKeys.add(recordTime);
        }

        taskRecordsCacheTwo.put(recordTime, toDayTaskRecordList);

        String map = GsonManager.getInstance().toJson(taskRecordsCacheTwo,
                new TypeToken<HashMap<String, List<TaskSupportRecord>>>() {
                });

        String keys = GsonManager.getInstance().toJson(taskRecordsCacheKeys,
                new TypeToken<List<String>>() {
                });

        Log.d(TAG, "init: taskRecordsCacheTwo => " + map);
        Log.d(TAG, "init: taskRecordsCacheKeys => " + keys);

        aCache.put(GlobalParam.HISTORY_LIST_CACHE, map);
        aCache.put(GlobalParam.HISTORY_LIST_CACHE_KEYS, keys);
    }

    // 执行免打扰开启护眼清理内存的操作
    public void doTaskStartAidesContent(String pkgName) {
        Log.d(TAG, "doTaskStartAidesContent: " + currentAppPackageName);
        if (settingStatus.get(SettingsUtils.KEY_AIDES_ENABLE)) {
            if (!pkgName.equals(currentAppPackageName)) {
                showWindow(ParamType.SERVICE_LOAD_WIN);
            }
            if (settingStatus.get(SettingsUtils.KEY_FLOAT_WINDOW_ENABLED)) {
                showWindow(ParamType.SERVICE_TOOLS_WIN);
            } else {
                closeWin(ParamType.SERVICE_TOOLS_WIN);
            }

            // 开启免打扰
            if (settingStatus.get(SettingsUtils.KEY_NO_DISTURB_ENABLED)) {
                SettingsUtils.enableNoDisturb();
                AudioManager manager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                try {
                    SettingsUtils.closeNotifySound(manager);
                } catch (Exception e) {
                    Log.d(TAG, "doTaskStartAidesContent: error");
                }
            }
            // 开启护眼
            if (settingStatus.get(SettingsUtils.KEY_EYES_CARE_ENABLED)) {
                SettingsUtils.enableEyesCare();
            }
            // 开启内存清理
            if (settingStatus.get(SettingsUtils.KEY_AIDES_SPEED_ENABLE)) {
                StringBuilder builder = new StringBuilder();
                builder.append(GlobalParam.MY_APP_PACKAGENAME + ",");
                builder.append(currentAppPackageName + ",");
                builder.append("com.readboy.aicourse" + ",");
                builder.append("com.readboy.screenrecord");
                SettingsUtils.doTaskClearRam(builder.toString());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doTaskStopAidesContent() {
        // 关闭护眼
        SettingsUtils.stopEyesCare();
        // 关闭免打扰
        try {
            SettingsUtils.stopNoDisturb();
            AudioManager manager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            SettingsUtils.backNotifySound(manager);
        } catch (Exception e) {
            Log.d(TAG, "doTaskStopAidesContent: e=>" + e);
        }

        if (isSoundRecording) {
            stopRecordSound();

        }
        if (isScreenRecording) {
            stopRecordScreen();
        }
    }

    public void getOnlineCourseAidesSettings() {
        // 获取悬浮窗设置
        if (SettingsUtils.isFloatWindowEnabled()) {
            settingStatus.put(SettingsUtils.KEY_FLOAT_WINDOW_ENABLED, true);
        } else {
            settingStatus.put(SettingsUtils.KEY_FLOAT_WINDOW_ENABLED, false);
        }

        // 获取护眼模式设置
        if (SettingsUtils.isEyesCareEnable()) {
            settingStatus.put(SettingsUtils.KEY_EYES_CARE_ENABLED, true);
        } else {
            settingStatus.put(SettingsUtils.KEY_EYES_CARE_ENABLED, false);
        }

        // 获取免打扰设置
        if (SettingsUtils.isNoDisturbEnable()) {
            settingStatus.put(SettingsUtils.KEY_NO_DISTURB_ENABLED, true);
        } else {
            settingStatus.put(SettingsUtils.KEY_NO_DISTURB_ENABLED, false);
        }

        // 获取网课模式开启还是关闭
        if (SettingsUtils.isOnlineCourseAidesEnabled()) {
            settingStatus.put(SettingsUtils.KEY_AIDES_ENABLE, true);
        } else {
            settingStatus.put(SettingsUtils.KEY_AIDES_ENABLE, false);
        }
        // 获取加速设置
        if (SettingsUtils.isAccelerateEnabled()) {
            settingStatus.put(SettingsUtils.KEY_AIDES_SPEED_ENABLE, true);
        } else {
            settingStatus.put(SettingsUtils.KEY_AIDES_SPEED_ENABLE, false);
        }

        Log.d(TAG, "getOnlineCourseAidesSettings: settingsStatus" + settingStatus);
    }

    public void initHistoryCache() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateForToday = formatter.format(date);
        if (taskRecordsCacheTwo.containsKey(dateForToday)) {
            toDayTaskRecordList.clear();
            toDayTaskRecordList.addAll(Objects.requireNonNull(taskRecordsCacheTwo.get(dateForToday)));
        } else {
            toDayTaskRecordList = new ArrayList<>();
        }
    }

    //检查app是否在今日列表
    public void updateAppInfo(String pkgName) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateForToday = formatter.format(date);

        if (taskRecordsCacheTwo.containsKey(dateForToday)) {
            toDayTaskRecordList = taskRecordsCacheTwo.get(dateForToday);
        } else {
            toDayTaskRecordList = new ArrayList<>();
        }
        for (TaskSupportRecord record : toDayTaskRecordList) {
            if (pkgName.equals(record.getPackageName())) {
                currentTaskRecords = record;
                return;
            }
        }
        currentTaskRecords = new TaskSupportRecord();
        currentTaskRecords.setAppName(allOnlineCourseList.get(pkgName).getAppName());
        currentTaskRecords.setTime(dateForToday);
        currentTaskRecords.setPackageName(pkgName);
        toDayTaskRecordList.add(currentTaskRecords);
    }

    private boolean checkIsStartForAides(String packageName) {
        return appStartList.containsKey(packageName);
    }

    private void showWindow(ParamType... paramType) {
        for (ParamType type : paramType) {
            if (type == ParamType.SERVICE_LOAD_WIN) {
                if (!loadWindow.isShow()) {
                    Log.d(TAG, "showWindow: show load win");
                    loadWindow.show();
                }
            }
            if (type == ParamType.SERVICE_TOOLS_WIN) {
                if (!floatWindow.isShow()) {
                    Log.d(TAG, "showWindow: show tools win");
                    floatWindow.show();
                }
            }
        }
    }

    private void closeWin(ParamType... paramType) {
        for (ParamType type : paramType) {
            if (type == ParamType.SERVICE_LOAD_WIN) {
                if (selectWindow.isShow()) {
                    selectWindow.close();
                }
            }
            if (type == ParamType.SERVICE_TOOLS_WIN) {
                if (floatWindow.isShow()) {
                    floatWindow.close();
                }
            }
            if (type == ParamType.SERVICE_LOAD_WIN) {
                if (loadWindow.isShow()) {
                    loadWindow.close();
                }
            }
        }
    }

    // 注册缓存接收器
    public void registerReceiveNotifyService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParam.NOTIFY_SERVICE_REFRESH_CACHE);
        intentFilter.addAction(GlobalParam.NOTIFY_SERVICE_REFRESH_APPLIST);
        registerReceiver(notifyServiceReceiver, intentFilter);
    }

    public void registerRecordCodeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RecordCodeReceiver.ACTION_RECORD_RESULT);
        registerReceiver(recordCodeReceiver, intentFilter);
    }

    // 注册应用启动广播
    public void registerReceiveDetectionApp() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParam.NOTIFY_SERVICE_ONCLINECOURSE_START);
        intentFilter.addAction(GlobalParam.NOTIFY_SERVICE_ONCLINECOURSE_STOP);
        registerReceiver(detectionAppReceiver, intentFilter);
    }

    private void initData() {
        appStartList = new HashMap<>();
        mAppContext = getApplicationContext();
        // 2. 获取系统的通知管理器
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        allOnlineCourseListCache = new ArrayList<>();
        allOnlineCourseList = new HashMap<>();
        taskRecordsCacheTwo = new HashMap<>();
        toDayTaskRecordList = new ArrayList<>();
        taskRecordsCacheKeys = new ArrayList<>();
        settingStatus = new HashMap<>();
        settingStatus.put(SettingsUtils.KEY_AIDES_ENABLE, true);
        settingStatus.put(SettingsUtils.KEY_EYES_CARE_ENABLED, true);
        settingStatus.put(SettingsUtils.KEY_NO_DISTURB_ENABLED, true);
        settingStatus.put(SettingsUtils.KEY_FLOAT_WINDOW_ENABLED, true);

        //初始化缓存
        aCache = ACache.get(mAppContext);
        getAppCache();
    }

    // 获取名单
    public void getDefaultAppList() {
        allOnlineCourseList.clear();
        //获取配置好的网课app名单   （暂定: 后续通过后台api调用实现）
        String cache1 = aCache.getAsString(GlobalParam.APP_LIST_CACHE);
        List<AppInfo> appList = null;
        if (cache1 != null) {
            appList = GsonManager.getInstance().fromJson(cache1, new TypeToken<List<AppInfo>>() {
            });
            Log.d(TAG, "getAppCache: appList x11" + cache1);

            if (appList != null) {
                for (AppInfo info : appList) {
                    allOnlineCourseList.put(info.getPackageName(), info);
                }
            }
            AppInfo appInfo = new AppInfo();
            appInfo.appName = "绿色上网";
            appInfo.setPackageName(GlobalParam.GREENBROWSER_APP_PACKAGENAME);
            appInfo.setType(2);
            allOnlineCourseList.put(GlobalParam.GREENBROWSER_APP_PACKAGENAME, appInfo);
        }
    }

    // 获取缓存
    public void getAppCache() {
        String keys = aCache.getAsString(GlobalParam.HISTORY_LIST_CACHE_KEYS);
        //获取配置好的网课app名单   （暂定: 后续通过后台api调用实现）
        String cache1 = aCache.getAsString(GlobalParam.APP_LIST_CACHE);
        //获取历史记录的缓存
        String cache2 = aCache.getAsString(GlobalParam.HISTORY_LIST_CACHE);
        List<AppInfo> appList = null;
        HashMap<String, List<TaskSupportRecord>> taskSupportRecordList = null;

        if (cache1 != null) {
            appList = GsonManager.getInstance().fromJson(cache1, new TypeToken<List<AppInfo>>() {
            });
            Log.d(TAG, "getAppCache: appList" + appList);
        }
        if (cache2 != null) {
            taskSupportRecordList = GsonManager.getInstance().fromJson(cache2, new TypeToken<HashMap<String, List<TaskSupportRecord>>>() {
            });
            Log.d(TAG, "getAppCache: taskRecordsCacheTwo => " + cache2);
        }

        if (keys != null) {
            taskRecordsCacheKeys = GsonManager.getInstance().fromJson(keys, new TypeToken<List<String>>() {
            });
        }

        if (appList != null) {
            for (AppInfo info : appList) {
                allOnlineCourseList.put(info.getPackageName(), info);
            }
        }
        AppInfo appInfo = new AppInfo();
        appInfo.appName = "绿色上网";
        appInfo.setPackageName(GlobalParam.GREENBROWSER_APP_PACKAGENAME);
        appInfo.setType(2);
        allOnlineCourseList.put(GlobalParam.GREENBROWSER_APP_PACKAGENAME, appInfo);
        if (taskSupportRecordList != null) {
            taskRecordsCacheTwo.putAll(taskSupportRecordList);
        }
    }

    private Intent intentData;
    private int intentResCode = -1;

    // 每次调用服务都会执行
    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intentResCode = -1;
        intentData = null;
        if (intent != null) {
            intentResCode = intent.getIntExtra("resultCode", -1);
            intentData = MyApplication.getInstances().getData();
            if (intentData == null) {
                MyApplication.getInstances().showToast("请确认录制权限");
                Intent intent1 = new Intent();
                intent1.setClass(this, PermissionsActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                intentData = MyApplication.getInstances().getData();
            }
            mWidthPixels = intent.getIntExtra("mWidthPixels", 720);
            mHeightPixels = intent.getIntExtra("mHeightPixels", 480);
            mScreenDensity = intent.getIntExtra("mDensityDpi", 1);
            Log.d(TAG, "initData  -->  mScreenDensity: " + mScreenDensity + "  mWidthPixels: " + mWidthPixels + "  mHeightPixels: " + mHeightPixels + "  resultCode: " + intentResCode + "  data: " + intentData);
        }

        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    public void initFloatWin() {
        floatWindow = new FloatWindow(getApplicationContext());
        floatWindow.setOnFloatListener((v, p) -> {
            // 切换布局
            if (winType == ParamType.FLOAT_WIN_SMALL) {
                replaceLayout();
            }
            switch (v.getId()) {
                case R.id.float_win_tools_minimize:
                    replaceLayout();
                    break;
                case R.id.float_win_tools_screen_record_content: {
                    if (p == ParamType.SERVICE_TOOLS_STOP_RECORD) {
                        stopRecordScreen();// 停止录屏
                    } else if (p == ParamType.SERVICE_TOOLS_START_RECORD) {
                        if (isSoundRecording) {
                            startHaveQuest = true;
                            currentRunningService = "SERVICE_SCREEN";
                            stopRecordSound(); // 开始录屏 停止录音
                        } else {
                            if (checkMyPermission()) {
                                startRecordScreen(); // 开始录屏
                            } else {
                                MyApplication.getInstances().showToast("没有录屏权限！请点击确认");
                            }
                        }
                    }
                }
                break;
                case R.id.float_win_tools_screen_shot_content: {
                    // 初始化记录
                    if (checkMyPermission()) {
                        takeShotScreen(0);// 开始录音
                    } else {
                        MyApplication.getInstances().showToast("没有屏幕录制权限！请点击确认");
                    }

                    break;
                }
                case R.id.float_win_tools_sound_content: {
                    if (p == ParamType.SERVICE_TOOLS_STOP_RECORD) {
                        stopRecordSound(); // 停止录音
                    } else if (p == ParamType.SERVICE_TOOLS_START_RECORD) {
                        if (isScreenRecording) {
                            startHaveQuest = true;
                            currentRunningService = "SERVICE_SOUND";
                            stopRecordScreen();// 服务占用 停止录屏
                        } else {
                            if (checkMyPermission()) {
                                startRecordSound();// 开始录音
                            } else {
                                MyApplication.getInstances().showToast("没有录屏权限！请点击确认");
                            }
                        }
                    }
                    break;
                }
                case R.id.float_win_tools_exercises_content: {
                    selectWindow.show();
                    break;
                }
                case R.id.float_win_tools_close_content:
                    if (isSoundRecording) {
                        stopRecordSound();
                    }
                    if (isScreenRecording) {
                        stopRecordScreen();
                    }
                    // 关闭悬浮窗
                    closeWin(ParamType.SERVICE_TOOLS_WIN);
                    break;
                case R.id.float_win_tools_master_content: {   // 回到首页
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(getApplication(), MainActivity.class);
                    getApplication().startActivity(intent);
                    break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stopRecordSound() {
        if (isSoundRecording) {
            Toast.makeText(this, "已停止录音", Toast.LENGTH_SHORT).show();
            doStopSoundRecord();
            handler.postDelayed(() -> {
                if (closeVoiceAssistant) {
                    VoiceAssistantUtil.backVoiceAssistant(mAppContext);
                }
            }, 80);
            // 刷新录制UI
            floatWindow.stopRecordSound();
        }
        // AudioRecord 方式
//                        stopRecordSound();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startRecordSound() {
        if (!isSoundRecording) {
            isSoundRecording = true;
            recordType = ParamType.SERVICE_RECORD_SOUND;
            Toast.makeText(this, "开始录音，录完记着点击结束哦！", Toast.LENGTH_SHORT).show();
            // 初始化历史记录
            soundRecords = new SoundRecord();
            takeShotScreen(2);
            // 开启录音 MediaRecord
            // 录制 关闭语音助手
            closeVoiceAssistant = VoiceAssistantUtil.closeVoiceAssistant(mAppContext);
            // 必须延迟开启录音否则受语音助手影响
            doStartSoundRecord();
            floatWindow.startRecordSound();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stopRecordScreen() {
        if (isScreenRecording) {
            Toast.makeText(this, "已停止录屏", Toast.LENGTH_SHORT).show();
//            stopRecord();
            doStopRecord();
            if (closeVoiceAssistant) {
                VoiceAssistantUtil.backVoiceAssistant(mAppContext);
            }
            floatWindow.stopRecordScreen();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startRecordScreen() {
        if (!isScreenRecording) {
            isScreenRecording = true;
            Toast.makeText(this, "开始录屏，录完记着点击结束哦！", Toast.LENGTH_SHORT).show();
            // 初始化记录
            screenRecords = new ScreenRecord();
            recordType = ParamType.SERVICE_RECORD_VIDEO;
            // 截屏
            // 录屏截屏服务内部默认录屏会截图
//            takeShotScreen(1);
            // 录制 关闭语音助手
            closeVoiceAssistant = VoiceAssistantUtil.closeVoiceAssistant(mAppContext);
            floatWindow.startRecordScreen();
            doStartRecord();
        }
    }

    // 切换悬浮窗大小
    private void replaceLayout() {
        Log.d(TAG, "mini or max replace: ");
        if (winType == ParamType.FLOAT_WIN_TOOLS) {
            floatWindow.closeWin(ParamType.FLOAT_WIN_TOOLS);
            floatWindow.selectWin(ParamType.FLOAT_WIN_SMALL);
            floatWindow.setRecordTimeUI(recordTimeUI);
            floatWindow.show();
            winType = ParamType.FLOAT_WIN_SMALL;
            Log.d(TAG, "replaceLayout: small");
        } else if (winType == ParamType.FLOAT_WIN_SMALL) {
            floatWindow.closeWin(ParamType.FLOAT_WIN_SMALL);
            floatWindow.selectWin(ParamType.FLOAT_WIN_TOOLS);
            floatWindow.setRecordTimeUI(recordTimeUI);
            floatWindow.show();
            winType = ParamType.FLOAT_WIN_TOOLS;
            Log.d(TAG, "replaceLayout: tools");
        }
    }

    private void initSelectWin() {
        selectWindow = new FloatSelectWindow(mAppContext);
        selectWindow.setOnFloatListener((sectionId, bookId) -> {
            HashMap<String, String> record = selectWindow.getCurrentRecord();
            if (record.size() < 2) {
                Toast.makeText(this, "请选择完整选项", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "SelectWin: OnFloatListener" + record.toString());
            Toast.makeText(this, "选择完成准备练习", Toast.LENGTH_SHORT).show();

            // 跳转精准做练习
            Intent intent = new Intent();
            PackageManager packageManager = getPackageManager();
            String packageName = "com.readboy.aicourse";
            intent = packageManager.getLaunchIntentForPackage(packageName);

            if (packageName == null || "".equals(packageName)) {
                Toast.makeText(mAppContext, "应用未安装", Toast.LENGTH_SHORT).show();
                return;
            } else if (intent == null) {
                Toast.makeText(mAppContext, "应用未安装", Toast.LENGTH_SHORT).show();
                return;
            }
            // 保存记录
            currentTaskRecords.getExercisesRecords().setBookId(bookId);
            currentTaskRecords.getExercisesRecords().setSectionId(sectionId);

            Intent intent2 = new Intent("com.readboy.aicourse.ACTION_SECTION");
            intent2.putExtra("book_id", bookId);
            intent2.putExtra("section_id", sectionId);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(intent2);
            } catch (Exception e) {
            }

            handler.postDelayed(() -> {
                selectWindow.close();
            }, 200);
        });
    }

    public void initLoadWin() {
        loadWindow = new LoadWindow(mAppContext);
        loadWindow.setOnLoadWinListener(() -> {
            // 设置进度条动画长度和时间
            int[] ints = new int[2];
            ints[0] = 100;
            ints[1] = 3500;
            return ints;
        });
    }

    private static final String TAG = "TaskSupportService";

    // 录制视频
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doStartRecord() {
        // 清空目录
        myVideoPath = "";
        // 时间归零
        recordTimeUI = "00:00";
        timeCount = 0;
        updateRecordTimeUI();
        //最多录制xxx  记录录制时间
        mHandler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, 1000);
        // 历史记录保存
        screenRecords.setScreenRecordTime(getRecordTime2());
        screenRecords.setPackageName(currentAppPackageName);

        //开启录制服务 -------------
        Intent intent = new Intent();
        intent.setClass(mAppContext, DoRecordService.class);
        intent.putExtra(DoRecordService.RECORD_TYPE, 1);
        intent.putExtra("resultCode", intentResCode);
        intent.putExtra("data", intentData);
        intent.putExtra("mWidthPixels", mWidthPixels);
        intent.putExtra("mHeightPixels", mHeightPixels);
        intent.putExtra("mDensityDpi", mScreenDensity);
        startForegroundService(intent);
        //开启录制服务 -------------

//        // 检查文件是否存在，没有就创建
//        checkFileExits();
//
//        Log.d(TAG, "startRecord" + videoPath);
//
//        createMediaRecorder();
//
//        createVirtualDisplay(); // 在mediaRecorder.prepare() 之后调用，否则报错 failed to get surface
//
//        mMediaRecorder.start();
    }

    private void createVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("TaskSupportRecordService", mWidthPixels, mHeightPixels, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    private void createMediaRecorder() {

        // 生成文件名
        String fileName = getFileName("OCA_ScreenRecord_") + ".mp4";
        String filePath = videoPath + "/" + fileName;
        // 保存文件地址
        myVideoPath = filePath;
        Log.d("FileUtils", "startScreenRecord: " + filePath);
        screenRecords.setScreenRecordPath(filePath);
        screenRecords.setFileName(fileName);

        mMediaRecorder = new MediaRecorder();

        isAudio = true;

        //设置音频源
        int audioSourceType = MediaRecorder.AudioSource.DEFAULT;

        if (isAudio) mMediaRecorder.setAudioSource(audioSourceType);

        //设置视频源: DEFAULT,Surface,Camera
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

        //设置视频输出格式: amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        //设置视频编码格式: default, H263, H264, MPEG_4_SP
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

//        //设置音频编码格式: default，AAC，AMR_NB，AMR_WB
        if (isAudio) mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);

        //设置视频尺寸大小
        mMediaRecorder.setVideoSize(mWidthPixels, mHeightPixels);

        //设置视频编码的帧率
        mMediaRecorder.setVideoFrameRate(60);   // 30

        mMediaRecorder.setOutputFile(filePath);

        //设置视频编码的码率
        mMediaRecorder.setVideoEncodingBitRate(5 * mWidthPixels * mHeightPixels);   // mWidthPixels * mHeightPixels

        //设置视频输出路径

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "VideoSize: " + mWidthPixels + " X " + mHeightPixels + "  VideoEncodingBitRate: "
                + (5 * mWidthPixels * mHeightPixels) + "  +VideoFrameRate: " + "60");
    }

    // 停止录制
    private void doStopRecord() {
        Log.d(TAG, "stopRecord");
        //录制结束
        screenRecords.setScreenRecordLengthTime(timeCount);
        Intent intent = new Intent(RecordReceiver.STOP_SCREEN_RECORD);
        sendBroadcast(intent);
        mHandler.removeMessages(MSG_TYPE_COUNT_DOWN);
    }

    NotificationManager mManager;

    // 截屏代码 [0:单纯截图,1:录屏截图,2:录音截图]
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void takeShotScreen(int index) {
        Intent intent = new Intent();
        intent.putExtra(DoRecordService.RECORD_TYPE, index);
        intent.putExtra("resultCode", intentResCode);
        intent.putExtra("data", intentData);
        intent.putExtra("mWidthPixels", mWidthPixels);
        intent.putExtra("mHeightPixels", mHeightPixels);
        intent.putExtra("mDensityDpi", mScreenDensity);
        if (index == 0) {
            intent.setClass(mAppContext, DoScreenShotsService.class);
        } else {
            intent.setClass(mAppContext, DoRecordService.class);
        }
        startForegroundService(intent);
//        checkFileExits();
//        Log.d(TAG, "takeShotScreen: " + picturePath);
//        getBitmapImg(index);
    }

    //保存图片路径到历史记录 [0:单纯截图,1:录屏截图,2:录音截图]
    public void saveImgHistory(String imgPath, int index, String fileName) {
        switch (index) {
            case 0: {
                MyApplication.getInstances().showToast("截图已保存");
                List<ScreenShotRecord> paths = currentTaskRecords.getScreenShotPaths();
                for (ScreenShotRecord path : paths) {
                    if (path.getImgPath().equals(imgPath)) {
                        return;
                    }
                }
                ScreenShotRecord record = new ScreenShotRecord();
                record.setImgPath(imgPath);
                record.setScreenShotTime(getRecordTime2());
                record.setFileName(fileName);
                paths.add(record);
                Log.d(TAG, "saveImgHistory: paths=> " + paths);
                break;
            }
            case 1: {
                ScreenShotRecord screenShotRecord = new ScreenShotRecord();
                screenShotRecord.setFileName(fileName);
                screenShotRecord.setImgPath(imgPath);
                screenRecords.setScreenRecordScreenShot(screenShotRecord);
                break;
            }
            case 2: {
                ScreenShotRecord screenShotRecord2 = new ScreenShotRecord();
                screenShotRecord2.setFileName(fileName);
                screenShotRecord2.setImgPath(imgPath);
                soundRecords.setSoundScreenShot(screenShotRecord2);
                break;
            }

        }
    }

    // 录音代码  MediaRecorder
    @SuppressLint("WrongConstant")
    public void doStartSoundRecord() {
        String fileName = getFileName("OCA_SoundRecord_") + ".mp3";
        String filePath = soundPath + "/" + fileName;

        mySoundPath = filePath;
        Log.d("FileUtils", "startSoundRecord: " + filePath);
        // 记录到历史记录
        soundRecords.setSoundTime(getRecordTime2());
//        soundRecords.setSoundPath(filePath);
        soundRecords.setPackageName(currentAppPackageName);
//        soundRecords.setFileName(fileName);

        // 刷新纪录UI
        timeCount = 0;
        recordTimeUI = "00:00";
        updateRecordTimeUI();

        //最多录制三分钟
        mHandler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, 1000);
//        checkFileExits(); // sound
//        // 开始录音
//        /* ①Initial：实例化MediaRecorder对象 */
//        mMediaRecorder = new MediaRecorder();
//
        try {
            // 开启音频录制服务
            Intent intent = new Intent();
            intent.setClass(mAppContext, DoSoundRecordService.class);
//            intent.setAction("com.readboy.onlinecourseaides.service.SOUND_RECORD_OCA");
//            intent.setComponent(new ComponentName("com.readboy.onlinecourseaides.service","com.readboy.onlinecourseaides.service.DoSoundRecordService"));
            startService(intent);
//            /* ②setAudioSource/setVedioSource */
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// 设置麦克风
////            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.REMOTE_SUBMIX);// 设置麦克风
//            /*
//             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
//             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
//             */
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            mMediaRecorder.setAudioSamplingRate(44100);
//
//            mMediaRecorder.setOutputFile(filePath);
//
//            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            mMediaRecorder.setAudioEncodingBitRate(96000);
//
//            /* ③准备 */
//            try {
//                mMediaRecorder.prepare();
//            } catch (IOException e) {
//                Log.e(TAG, "prepare() failed");
//            }
//
//            /* ④开始 */
//            mMediaRecorder.start();
            Log.d(TAG, "startSoundRecord: start " + fileName + "=>" + filePath);
        } catch (Exception e) {
            Log.d(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doStopSoundRecord() {
        //录制结束
        mHandler.removeMessages(MSG_TYPE_COUNT_DOWN);
        try {
            Intent intent = new Intent(RecordReceiver.STOP_SOUND_RECORD);
            sendBroadcast(intent);
        } catch (RuntimeException e) {
            Log.d(TAG, "stopSoundRecord: error");
        }
    }

    public boolean isShortTime() {
        if (timeCount <= 1) {
            MyApplication.getInstances().showToast("录制时间太短了");
            return true;
        }
        return false;
    }

    public String getRecordTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public String getRecordTime2() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    private String getFileName(String pref) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = formatter.format(new Date(System.currentTimeMillis()));
        return pref + format;
    }

    /**
     * 检测文件夹不存在就创建
     */
    private boolean checkFileExits() {
        // 定义文件夹目录地址 公共目录含有自己创建的文件夹无法识别 /OnlineCourseAides  需要刷新媒体库
        basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA";
        videoPath = basePath + "/video";
        picturePath = basePath + "/img";
        soundPath = basePath + "/sound";


        Log.d(TAG, "checkFileExits: newFile : ");

        File destDir = new File(videoPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File dest2Dir = new File(picturePath);
        if (!dest2Dir.exists()) {
            dest2Dir.mkdirs();
        }
        File dest3Dir = new File(soundPath);
        if (!dest3Dir.exists()) {
            dest3Dir.mkdirs();
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case MSG_TYPE_COUNT_DOWN: {
                timeCount++;
                int minute = 0, second = 0;
                if (timeCount >= 60) {
                    minute = timeCount / 60;
                    second = timeCount % 60;
                } else {
                    second = timeCount;
                }
                String index1 = minute < 10 ? "0" + minute : "" + minute;
                String index2 = second < 10 ? "0" + second : "" + second;

                recordTimeUI = index1 + ":" + index2;
                // 更新记录时间的UI
                updateRecordTimeUI();

                Log.d(TAG, "handleMessage: time refresh" + recordTimeUI);
                mHandler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, 1000);
            }
            break;
//            case HANDLER_SERVICE_SCREEN_SHOT_FINISH:{
//
//                mHandler.removeMessages(HANDLER_SERVICE_SCREEN_SHOT_FINISH);
//            }break;
            case HANDLER_SERVICE_SOUND_SCREEN_SHOT_FINISH: {
                ScreenShotRecord record = new ScreenShotRecord();
                record.setFileName(resSoundScreenShotsFileName);
                record.setImgPath(resSoundScreenShotsFilePath);
                soundRecords.setSoundScreenShot(record);
                mHandler.removeMessages(HANDLER_SERVICE_SOUND_SCREEN_SHOT_FINISH);
            }
            break;
            case HANDLER_SERVICE_SCREEN_RECORD_FINISH: {
                mHandler.removeMessages(HANDLER_SERVICE_SCREEN_RECORD_FINISH);
            }
            break;
            case HANDLER_SERVICE_SCREEN_START: {
                startRecordScreen(); //服务占用，返回后开启
                mHandler.removeMessages(HANDLER_SERVICE_SCREEN_START);
            }
            break;
            case HANDLER_SERVICE_SOUND_START: {
                startRecordSound();//服务占用，返回后开启
                mHandler.removeMessages(HANDLER_SERVICE_SOUND_START);
            }
            break;
        }
        return true;
    }

    public void updateRecordTimeUI() {
        if (recordType == ParamType.SERVICE_RECORD_VIDEO) {
            floatWindow.updateVideoTime(recordTimeUI);
        } else if (recordType == ParamType.SERVICE_RECORD_SOUND) {
            floatWindow.updateSoundTime(recordTimeUI);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterMyReceiver();

        Log.d(TAG, "onDestroy");
        if (isScreenRecording) {
            stopRecordScreen();
        }
        if (isSoundRecording) {
            stopRecordSound();
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
//        if (mMediaProjection != null) {
//            mMediaProjection.stop();
//        }
        if (mManager != null) {
            //取消通知
            mManager.cancelAll();
        }

        // 服务结束窗口不会结束
        closeWin(ParamType.SERVICE_LOAD_WIN, ParamType.SERVICE_TOOLS_WIN);

        stopForeground(true);
    }


    //取消注册接收qi
    public void unregisterMyReceiver() {
        if (detectionAppReceiver != null) {
            unregisterReceiver(detectionAppReceiver);
        }
        if (notifyServiceReceiver != null) {
            unregisterReceiver(notifyServiceReceiver);
        }
    }
}