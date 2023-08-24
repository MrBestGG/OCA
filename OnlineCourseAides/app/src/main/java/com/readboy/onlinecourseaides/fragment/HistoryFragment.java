package com.readboy.onlinecourseaides.fragment;

import static com.readboy.onlinecourseaides.utils.GlobalParam.HISTORY_LIST_CACHE;
import static com.readboy.onlinecourseaides.utils.GlobalParam.HISTORY_LIST_CACHE_KEYS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.adapter.ExoAudioListAdapter;
import com.readboy.onlinecourseaides.adapter.HistoryAppAndTimeAdapter;
import com.readboy.onlinecourseaides.adapter.HistoryScreenRecordAdapter;
import com.readboy.onlinecourseaides.adapter.HistoryScreenShotAdapter;
import com.readboy.onlinecourseaides.adapter.HistorySoundAdapter;
import com.readboy.onlinecourseaides.bean.ExercisesRecord;
import com.readboy.onlinecourseaides.bean.ScreenRecord;
import com.readboy.onlinecourseaides.bean.ScreenShotRecord;
import com.readboy.onlinecourseaides.bean.SoundRecord;
import com.readboy.onlinecourseaides.bean.TaskSupportRecord;
import com.readboy.onlinecourseaides.bean.response.SectionOneResponse;
import com.readboy.onlinecourseaides.bean.response.item.SectionOneItem;
import com.readboy.onlinecourseaides.databinding.FragmentHistoryBinding;
import com.readboy.onlinecourseaides.network.BookLoader;
import com.readboy.onlinecourseaides.ui.DefaultTipsDialogView;
import com.readboy.onlinecourseaides.ui.OnclickBigImgDialog;
import com.readboy.onlinecourseaides.utils.AiCourseUtils;
import com.readboy.onlinecourseaides.utils.DialogUtils;
import com.readboy.onlinecourseaides.utils.FileUtils;
import com.readboy.onlinecourseaides.utils.GlobalParam;
import com.readboy.onlinecourseaides.utils.GsonManager;
import com.readboy.onlinecourseaides.utils.MediaPlayerHelper;
import com.readboy.onlinecourseaides.utils.NetWorkUtils;
import com.readboy.onlinecourseaides.utils.cache.ACache;
import com.readboy.provider.UserDbSearch;
import com.readboy.provider.mhc.info.UserBaseInfo;
import com.readboy.wanimation.tagcloud.exp.PlanetAtlas.DataInfo.StarInfo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 历史记录碎片
 */
public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";

    private Context hContext;

    FragmentHistoryBinding binding;

    private MyApplication application;
    private Handler handler;
    //本应用缓存方式  ACache缓存工具类 来源于 https://github.com/yangfuhai/ASimpleCache/tree/master/AsimpleCacheDemo
    private ACache aCache;
    private boolean imgEdit = true;

    // 缓存记录
    private HashMap<String, List<TaskSupportRecord>> recordCaches;
    private volatile TaskSupportRecord currentRecord;
    private List<String> recordCacheKeys;
    private List<TaskSupportRecord> taskSupportRecordList;
    //当前记录index
    ArrayList<ExoAudioListAdapter.AudioItem> soundData;
    List<ScreenRecord> screenRecordData;

    private HistoryAppAndTimeAdapter appAndTimeAdapter;
    private HistoryScreenShotAdapter screenShotAdapter;
    private HistoryScreenRecordAdapter screenRecordAdapter;
    private ExoAudioListAdapter audioListAdapter;
    private AlertDialog alertDialog;
    private OnclickBigImgDialog onclickBigImgDialog;
    private ExercisesRecord exercisesRecords;

    // 获取用户id访问网络
    private String usrId;
    // 章节详情用于生成图谱
    private SectionOneItem sectionOneItem;
    //图谱数据
    protected ArrayList<StarInfo> mStarInfos = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(LayoutInflater.from(getContext()));
        init();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        application = MyApplication.getInstances();
        handler = new Handler();
        initCache(hContext);
        initData();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        initUsrId();
        soundData = new ArrayList<>();
        screenRecordData = new ArrayList<>();
        recordCaches = new HashMap<>();
        taskSupportRecordList = new ArrayList<>();
        recordCacheKeys = new ArrayList<>();
        currentRecord = new TaskSupportRecord();
//        loadCacheFromACache();
        loadCacheFromACache2();
        if (taskSupportRecordList.size() > 0) {
            loadCurrentRecord(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {
        onclickBigImgDialog = new OnclickBigImgDialog(getContext());
        // 首次进来无内容UI
        refreshContentUI();
        // 没有截图隐藏内容
        initNoScreenShotsContent();
        //没有内容隐藏笔记标题和图标
        refreshViewNoteTitleAndImg();
        // 初始化左边app目录
        initAppAndTimeAdapter();
        //这里的适配器参数是引用传递，所以不用setData
        //初始化截图适配器
        initScreenShotAdapter();
        //初始化录音列表适配器
        initAudioListAdapter();
        //初始化视频播放适配器
        initScreenRecordAdapter();
        // 精准学跳转
        refreshAiCourseContent();
        // 更新报告顶部
        updateCurrentRecordInfo();
    }

    private void refreshContentUI() {
        if (currentRecord != null) {
            if ("".equals(currentRecord.getAppName()) || currentRecord.getAppName() == null) {
                binding.historyNoContent.setVisibility(View.VISIBLE);
                binding.historyReportBase.setVisibility(View.INVISIBLE);
                binding.historyRecordsTitleImg.setVisibility(View.INVISIBLE);
                binding.historyStudyReportTitle.setVisibility(View.INVISIBLE);
                binding.historyContentScrollView.setBackgroundResource(R.color.normal_100);
            } else {
                binding.historyNoContent.setVisibility(View.INVISIBLE);
                binding.historyReportBase.setVisibility(View.VISIBLE);
                binding.historyRecordsTitleImg.setVisibility(View.VISIBLE);
                binding.historyStudyReportTitle.setVisibility(View.VISIBLE);
                binding.historyContentScrollView.setBackgroundResource(R.drawable.hirstory_default);
            }
        }
    }

    public void refreshViewNoteTitleAndImg() {
        if (isLoadInfo()) {
            binding.historyRecordsTitle.setVisibility(View.VISIBLE);
            binding.historyRecordsTitleImg2.setVisibility(View.VISIBLE);
        } else {
            binding.historyRecordsTitle.setVisibility(View.INVISIBLE);
            binding.historyRecordsTitleImg2.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshAiCourseContent() {
        if (currentRecord != null) {
            String sectionId = currentRecord.getExercisesRecords().getSectionId();
            if (sectionId != null && !"".equals(sectionId)) {
                binding.historyAiTestContent.setVisibility(View.VISIBLE);
                binding.historyTvTestTitle2.setOnClickListener(v -> {
                    if (currentRecord == null) {
//                        loadCacheFromACache();
                        loadCacheFromACache2();
                        if (taskSupportRecordList.size() > 0) {
                            loadCurrentRecord(0);
                        }
                    }
                    exercisesRecords = currentRecord.getExercisesRecords();
                    if (exercisesRecords.getBookId() != null && exercisesRecords.getSectionId() != null) {
                        String bookId = exercisesRecords.getBookId();
                        String sectionId1 = exercisesRecords.getSectionId();
                        // 跳转精准做练习
                        gotoAiCourseTest(bookId, sectionId1);
                    }
                });
                getNoteMapDateFromNet();
            } else {
                binding.historyAiTestContent.setVisibility(View.GONE);
            }
        }
    }

    private void gotoAiCourseTest(String bookId, String sectionId) {
        Intent intent = null;
        PackageManager packageManager = hContext.getPackageManager();
        String packageName = "com.readboy.aicourse";
        intent = packageManager.getLaunchIntentForPackage(packageName);

        if (packageName == null || "".equals(packageName)) {
            Toast.makeText(hContext, "应用未安装", Toast.LENGTH_SHORT).show();
            return;
        } else if (intent == null) {
            Toast.makeText(hContext, "应用未安装", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent2 = new Intent("com.readboy.aicourse.ACTION_SECTION");
        intent2.putExtra("book_id", bookId);
        intent2.putExtra("section_id", sectionId);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent2);
        } catch (Exception e) {
        }
    }

    public void reverseScreenRecordData() {
        screenRecordData.clear();
        int index3 = 0;
        for (int i = currentRecord.getScreenRecords().size() - 1; i >= 0; i--) {
            ScreenRecord record = currentRecord.getScreenRecords().get(i);
            record.index = i + 1;
            screenRecordData.add(currentRecord.getScreenRecords().get(i));
            index3++;
        }
//        Log.d(TAG, "initScreenRecordAdapter: 倒叙处理"+screenRecordData);
    }

    private void initScreenRecordAdapter() {
        initScreenRecordUri();
//        reverseScreenRecordData();//倒叙处理
        screenRecordAdapter = new HistoryScreenRecordAdapter(getContext(), screenRecordData, new HistoryScreenRecordAdapter.HistoryScreenRecordListener() {
            @Override
            public void onItemClick(int index) {
            }

            @Override
            public void doChangeImgSize(int index) {
                application.showToast("裁切大小");
                handler.postDelayed(() -> {
                    initScreenRecordUri();
                    screenRecordAdapter.refreshView();
                }, 200);
            }

            @Override
            public void delRecord(ScreenRecord record) {
                int index = 0;

                for (int i = 0; i < currentRecord.getScreenRecords().size(); i++) {
                    if (currentRecord.getScreenRecords().get(i).equals(record)) {
                        index = i;
                        break;
                    }
                }

                // 删除弹框
                DialogUtils.getDefaultTipsXPopUp(getContext(), new DefaultTipsDialogView.DefaultTipsDialogListener() {
                    @Override
                    public void makeSure() {
                        handler.post(() -> {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                if (record.getScreenRecordScreenShot() != null) {
                                    FileUtils.delFile(record.getScreenRecordScreenShot().getUri() + "", getContext());
                                }
                                FileUtils.delFile(record.getUri() + "", getContext());
                            } else {
                                if (record.getScreenRecordScreenShot() != null) {
                                    FileUtils.delFile(record.getScreenRecordScreenShot().getImgPath() + "", getContext());
                                }
                                FileUtils.delFile(record.getScreenRecordPath(), getContext());
                            }
                            screenRecordData.remove(record);
                            currentRecord.getScreenRecords().remove(record);
                            screenRecordAdapter.refreshView();
                            // 没有截图隐藏内容
                            initNoScreenShotsContent();
                            //没有内容隐藏笔记标题和图标
                            refreshViewNoteTitleAndImg();
                            updateRecordCache();
                        });
                    }

                    @Override
                    public void cancel() {
                    }
                }, "确认是否删除").show();
            }
        });
        LinearLayoutManager screenMgr = new LinearLayoutManager(getContext());
        binding.historyScreenRecords.setLayoutManager(screenMgr);
//        binding.historyScreenRecords.setNestedScrollingEnabled(true); // 禁止滑动  无效
        binding.historyScreenRecords.setAdapter(screenRecordAdapter);
    }

    public void reverseSoundData() {
        soundData.clear();

        String[] paths = new String[currentRecord.getSoundRecords().size()];
        // 按时间倒序排列
        int index = 0;
        for (int i = currentRecord.getSoundRecords().size() - 1; i >= 0; i--) {
            paths[index++] = currentRecord.getSoundRecords().get(i).getSoundPath() + "";
        }

        Long[] durations = MediaPlayerHelper.getMediaFileDuration(paths);

        int index2 = 0;
        for (int i = currentRecord.getSoundRecords().size() - 1; i >= 0; i--) {
            ExoAudioListAdapter.AudioItem audioItem = new ExoAudioListAdapter.AudioItem(paths[index2], durations[index2]);
            audioItem.soundRecord = currentRecord.getSoundRecords().get(i);
            audioItem.setIndex(i + 1);
            soundData.add(audioItem);
            index2++;
        }
//        Log.d(TAG, "initAudioListAdapter: 倒叙"+soundData);
    }

    private void initAudioListAdapter() {
        initSoundUri();
//        reverseSoundData();// 倒序
        audioListAdapter = new ExoAudioListAdapter(soundData, null);
        audioListAdapter.setListener(new ExoAudioListAdapter.HistorySoundRecordListener() {
            @Override
            public void onItemClick(int index) {
            }

            @Override
            public void delete(ExoAudioListAdapter.AudioItem index) {
                // 删除弹框
                DialogUtils.getDefaultTipsXPopUp(getContext(), new DefaultTipsDialogView.DefaultTipsDialogListener() {
                    @Override
                    public void makeSure() {
                        SoundRecord record = index.getSoundRecord();
                        handler.post(() -> {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                FileUtils.delFile(record.getUri() + "", getContext());
                                if (record.getSoundScreenShot() != null) {
                                    FileUtils.delFile(record.getSoundScreenShot().getUri() + "", getContext());
                                }
                            } else {
                                FileUtils.delFile(record.getSoundPath(), getContext());
                                if (record.getSoundScreenShot() != null) {
                                    FileUtils.delFile(record.getSoundScreenShot().getImgPath(), getContext());
                                }
                            }
                            Log.d(TAG, "HistorySoundRecordListener del: index=" + index);
                            currentRecord.getSoundRecords().remove(record);
                            soundData.remove(index);
                            Log.d(TAG, "HistorySoundRecordListener del: soundData=>" + soundData);
                            audioListAdapter.setDataList(soundData);
                            // 没有截图隐藏内容
                            initNoScreenShotsContent();
                            //没有内容隐藏笔记标题和图标
                            refreshViewNoteTitleAndImg();
                            // 实际文件删除
                            updateRecordCache();
                        });
                    }

                    @Override
                    public void cancel() {
                    }
                }, "确认是否删除").show();
            }
        });
        LinearLayoutManager soundMgr = new LinearLayoutManager(getContext());
        binding.historySoundRecords.setLayoutManager(soundMgr);
//        binding.historySoundRecords.setNestedScrollingEnabled(true);
        binding.historySoundRecords.setAdapter(audioListAdapter);
    }

    private void initScreenShotAdapter() {
        initImgUri();
        screenShotAdapter = new HistoryScreenShotAdapter(getContext(), currentRecord.getScreenShotPaths(), new HistoryScreenShotAdapter.HistoryScreenShotListener() {
            @Override
            public void onItemClick(int index) {
                // 删除弹框
                DialogUtils.getDefaultTipsXPopUp(getContext(), new DefaultTipsDialogView.DefaultTipsDialogListener() {
                    @Override
                    public void makeSure() {
                        handler.post(() -> {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                FileUtils.delFile(currentRecord.getScreenShotPaths().get(index).getUri() + "", getContext());
                            } else {
                                FileUtils.delFile(currentRecord.getScreenShotPaths().get(index).getImgPath(), getContext());
                            }
                            currentRecord.getScreenShotPaths().remove(index);
                            screenShotAdapter.refreshView();
                            refreshViewNoteTitleAndImg();
                            initNoScreenShotsContent();
                            updateRecordCache();
                        });
                    }

                    @Override
                    public void cancel() {

                    }
                }, "确认是否删除").show();
            }

            @Override
            public void selectedImg(int index) {
            }
        });
        GridLayoutManager screenshotMgr = new GridLayoutManager(getContext(), 4);
        // 设置间距，实际上是padding
        binding.historyImgScreenshots.setLayoutManager(screenshotMgr);
        binding.historyImgScreenshots.setAdapter(screenShotAdapter);

        binding.historyImgScreenshotsContentLayout.setOnClickListener(v -> {
            screenShotAdapter.setEdit(imgEdit);
            imgEdit = !imgEdit;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAppAndTimeAdapter() {
        appAndTimeAdapter = new HistoryAppAndTimeAdapter(getContext(), taskSupportRecordList, index -> {
            // 刷新学习报告数据
            currentRecord = index;
            Log.d(TAG, "initView: currentRecord=>" + currentRecord);
            for (int i = 0; i < taskSupportRecordList.size(); i++) {
                if (taskSupportRecordList.get(i).getPackageName().equals(index.getPackageName())
                        && taskSupportRecordList.get(i).getTime().equals(index.getTime())) {
                    loadCurrentRecord(i);
                    break;
                }
            }
            refreshContentUI();
            refreshViewNoteTitleAndImg();
            initNoScreenShotsContent();
            updateCurrentRecordInfo();
            handler.post(this::refreshSoundRecord);
            handler.post(this::refreshScreenRecords);
            handler.post(this::refreshScreenShotRecords);
            handler.post(this::refreshAiCourseContent);

        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.historyTimeAndAppName.setLayoutManager(manager);
        binding.historyTimeAndAppName.setAdapter(appAndTimeAdapter);
    }

    public boolean isLoadInfo() {
        if (currentRecord.getScreenRecords().size() == 0
                && currentRecord.getScreenShotPaths().size() == 0
                && currentRecord.getSoundRecords().size() == 0
                && (currentRecord.getExercisesRecords().getSectionId() == null
                || "".equals(currentRecord.getExercisesRecords().getSectionId()))) {
            return false;
        }
        return true;
    }

    private void refreshScreenShotRecords() {
        initImgUri();
        if (currentRecord.getScreenShotPaths().size() == 0) {
            binding.historyImgScreenshotsTitle.setVisibility(View.GONE);
            binding.historyImgScreenshotsEdit.setVisibility(View.GONE);
            binding.historyScreenshotsTime.setVisibility(View.GONE);
        } else {
            binding.historyImgScreenshotsTitle.setVisibility(View.VISIBLE);
            binding.historyImgScreenshotsEdit.setVisibility(View.VISIBLE);
            binding.historyScreenshotsTime.setVisibility(View.VISIBLE);
        }
        screenShotAdapter.setData(currentRecord.getScreenShotPaths());
    }

    private void refreshScreenRecords() {
        initScreenRecordUri();
        screenRecordAdapter.setData(screenRecordData);
    }

    private void refreshSoundRecord() {
        initSoundUri();
        audioListAdapter.setDataList(soundData);
    }

    private void initNoScreenShotsContent() {
        if (currentRecord.getScreenShotPaths().size() == 0) {
            binding.historyScreenshotsTime.setVisibility(View.GONE);
            binding.historyImgScreenshotsTitle.setVisibility(View.GONE);
            binding.historyImgScreenshotsContentLayout.setVisibility(View.GONE);
        } else {
            binding.historyScreenshotsTime.setVisibility(View.VISIBLE);
            binding.historyImgScreenshotsTitle.setVisibility(View.VISIBLE);
            binding.historyImgScreenshotsContentLayout.setVisibility(View.VISIBLE);
        }
    }

    public void updateRecordCache() {
        //通知服务更新存储记录
        String json = GsonManager.getInstance().toJson(currentRecord, new TypeToken<TaskSupportRecord>() {
        });
        Intent intent = new Intent(GlobalParam.NOTIFY_SERVICE_REFRESH_CACHE);
        intent.putExtra(GlobalParam.NOTIFY_SERVICE_REFRESH_DATA, json);
        getActivity().sendBroadcast(intent);
        // 保存到缓存
        saveCache();
    }

    private void initImgUri() {
        if (currentRecord.getScreenShotPaths().size() != 0) {
            binding.historyScreenshotsTime.setText(currentRecord.getScreenShotPaths().get(0).getScreenShotTime());

            ScreenShotRecord path1 = currentRecord.getScreenShotPaths().get(0);
            Uri uri1 = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    uri1 = loadFileFromMedia(path1.getFileName());
                } else {
                    uri1 = loadFileFromMedia(path1.getImgPath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "initImgUri: uri => " + uri1 + ", fileName " + path1.getFileName());
            if (uri1 != null) {
                path1.setUri(uri1);
            } else {
                // 没有URI就删除
                Log.d(TAG, "initImgUri: ScreenShotRecord no uri remove it");
                currentRecord.getScreenShotPaths().remove(path1);
            }

        }
        //rX  加载文件
        Observable.create(subscriber -> {
            for (ScreenShotRecord path : currentRecord.getScreenShotPaths()) {
                Uri uri = null;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = loadFileFromMedia(path.getFileName());
                    } else {
                        uri = loadFileFromMedia(path.getImgPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "initImgUri: rx uri => " + uri + ", fileName " + path.getFileName());
                if (uri != null) {
                    path.setUri(uri);
                } else {
                    // 没有URI就删除
                    Log.d(TAG, "initImgUri: ScreenShotRecord no uri remove it");
                    currentRecord.getScreenShotPaths().remove(path);
                }
            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        screenShotAdapter.refreshView();
                        Log.d(TAG, "onCompleted: ScreenShotRecord异步加载完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onCompleted: ScreenShotRecord异步加载完成");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onCompleted: ScreenShotRecord异步加载完成");
                    }
                });
    }

    private void initSoundUri() {
        if(currentRecord.getSoundRecords().size() != 0) {
            SoundRecord record = currentRecord.getSoundRecords().get(0);
            Uri uri = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    uri = loadFileFromMedia(record.getFileName());
                } else {
                    uri = loadFileFromMedia(record.getSoundPath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            Log.d(TAG, "initSoundUri: uri => " + uri + ", fileName " + path.getFileName());
            if (uri == null) {
                currentRecord.getSoundRecords().remove(record);
                Log.d(TAG, "initSoundUri: SoundRecord no uri remove it => " + record);
            } else {
                record.setUri(uri);
            }

            Uri uri2 = null;
            ScreenShotRecord screenShot = record.getSoundScreenShot();
            if (screenShot != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri2 = loadFileFromMedia(screenShot.getFileName());
                    } else {
                        uri2 = loadFileFromMedia(screenShot.getImgPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                record.getSoundScreenShot().setUri(uri2);
                Log.d(TAG, "initSoundUri: uri2 => " + uri2 + ", fileName " + screenShot.getFileName());
            }
        }
//        for (SoundRecord path : currentRecord.getSoundRecords()) {
//            Uri uri = null;
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    uri = loadFileFromMedia(path.getFileName());
//                } else {
//                    uri = loadFileFromMedia(path.getSoundPath());
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
////            Log.d(TAG, "initSoundUri: uri => " + uri + ", fileName " + path.getFileName());
//            if (uri == null) {
//                currentRecord.getSoundRecords().remove(path);
//                Log.d(TAG, "initSoundUri: SoundRecord no uri remove it => " + path);
//                continue;
//            } else {
//                path.setUri(uri);
//            }
//
//            Uri uri2 = null;
//            ScreenShotRecord screenShot = path.getSoundScreenShot();
//            if (screenShot == null) continue;
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    uri2 = loadFileFromMedia(screenShot.getFileName());
//                } else {
//                    uri2 = loadFileFromMedia(screenShot.getImgPath());
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            path.getSoundScreenShot().setUri(uri2);
//            Log.d(TAG, "initSoundUri: uri2 => " + uri2 + ", fileName " + screenShot.getFileName());
//        }


        Observable.create(subscriber -> {
            for (SoundRecord path : currentRecord.getSoundRecords()) {
                Uri uri = null;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = loadFileFromMedia(path.getFileName());
                    } else {
                        uri = loadFileFromMedia(path.getSoundPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//            Log.d(TAG, "initSoundUri: uri => " + uri + ", fileName " + path.getFileName());
                if (uri == null) {
                    currentRecord.getSoundRecords().remove(path);
                    Log.d(TAG, "initSoundUri: SoundRecord no uri remove it => " + path);
                    continue;
                } else {
                    path.setUri(uri);
                }

                Uri uri2 = null;
                ScreenShotRecord screenShot = path.getSoundScreenShot();
                if (screenShot == null) continue;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri2 = loadFileFromMedia(screenShot.getFileName());
                    } else {
                        uri2 = loadFileFromMedia(screenShot.getImgPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                path.getSoundScreenShot().setUri(uri2);
                Log.d(TAG, "initSoundUri: uri2 => " + uri2 + ", fileName " + screenShot.getFileName());
            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        reverseSoundData();
                        audioListAdapter.refreshView();
                        Log.d(TAG, "onCompleted: SoundRecord异步加载完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onCompleted: SoundRecord异步加载失败");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onCompleted: SoundRecord异步加载完成");
                    }
                });
    }

    private void initScreenRecordUri() {
        if(currentRecord.getScreenRecords().size() != 0) {
            ScreenRecord screenRecord = currentRecord.getScreenRecords().get(0);
            Uri uri = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    uri = loadFileFromMedia(screenRecord.getFileName());
                } else {
                    uri = loadFileFromMedia(screenRecord.getScreenRecordPath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (uri == null) {
                currentRecord.getScreenRecords().remove(screenRecord);
                Log.d(TAG, "initScreenRecordUri: ScreenRecord no uri remove it =>" + screenRecord);
            } else {
                screenRecord.setUri(uri);
            }

            Uri uri2 = null;
            ScreenShotRecord screenShot = screenRecord.getScreenRecordScreenShot();
            if (screenShot != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri2 = loadFileFromMedia(screenShot.getFileName());
                    } else {
                        uri2 = loadFileFromMedia(screenShot.getImgPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                screenRecord.getScreenRecordScreenShot().setUri(uri2);
                Log.d(TAG, "initScreenRecordUri: uri2 => " + uri2);
            }
        }

        // 没有截图隐藏内容
        initNoScreenShotsContent();
        //没有内容隐藏笔记标题和图标
        refreshViewNoteTitleAndImg();

//        for (ScreenRecord path : currentRecord.getScreenRecords()) {
//            Uri uri = null;
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    uri = loadFileFromMedia(path.getFileName());
//                } else {
//                    uri = loadFileFromMedia(path.getScreenRecordPath());
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
////            Log.d(TAG, "initScreenRecordUri: uri => " + uri + ", fileName " + path.getFileName());
//            if (uri == null) {
//                currentRecord.getScreenRecords().remove(path);
//                Log.d(TAG, "initScreenRecordUri: ScreenRecord no uri remove it =>" + path);
//                continue;
//            } else {
//                path.setUri(uri);
//            }
//
//            Uri uri2 = null;
//            ScreenShotRecord screenShot = path.getScreenRecordScreenShot();
//            if (screenShot == null) continue;
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    uri2 = loadFileFromMedia(screenShot.getFileName());
//                } else {
//                    uri2 = loadFileFromMedia(screenShot.getImgPath());
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            path.getScreenRecordScreenShot().setUri(uri2);
//            Log.d(TAG, "initScreenRecordUri: uri2 => " + uri2);
//        }

        Observable.create(subscriber -> {
            for (ScreenRecord path : currentRecord.getScreenRecords()) {
                Uri uri = null;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = loadFileFromMedia(path.getFileName());
                    } else {
                        uri = loadFileFromMedia(path.getScreenRecordPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//            Log.d(TAG, "initScreenRecordUri: uri => " + uri + ", fileName " + path.getFileName());
                if (uri == null) {
                    currentRecord.getScreenRecords().remove(path);
                    Log.d(TAG, "initScreenRecordUri: ScreenRecord no uri remove it =>" + path);
                    continue;
                } else {
                    path.setUri(uri);
                }

                Uri uri2 = null;
                ScreenShotRecord screenShot = path.getScreenRecordScreenShot();
                if (screenShot == null) continue;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri2 = loadFileFromMedia(screenShot.getFileName());
                    } else {
                        uri2 = loadFileFromMedia(screenShot.getImgPath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                path.getScreenRecordScreenShot().setUri(uri2);
                Log.d(TAG, "initScreenRecordUri: uri2 => " + uri2);
            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        reverseScreenRecordData();
                        screenRecordAdapter.refreshView();
                        //加载 没有截图隐藏内容
                        initNoScreenShotsContent();
                        //没有内容隐藏笔记标题和图标
                        refreshViewNoteTitleAndImg();
                        Log.d(TAG, "onCompleted: ScreenRecord异步加载完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onCompleted: ScreenRecord异步加载失败");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onCompleted: ScreenRecord异步加载完成");
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentRecordInfo() {
        String learningTime = currentRecord.getLearningTime();
        String speedTime = currentRecord.getSpeedTime();
        String eyeCareTime = currentRecord.getEyeCareTime();

        String time = doTransformTime(learningTime);
        String time1 = doTransformTime(speedTime);
        String time2 = doTransformTime(eyeCareTime);

        // 学习时长
        binding.historyStudyTime.setText(time);
        // 加速时长
        binding.historySpeedTime.setText(time1);
//        if (currentRecord.getInterceptMsgNumber() == 0) {
//            binding.historyInterceptMsgNumber.setText("0条");
//        } else {
//            binding.historyInterceptMsgNumber.setText(currentRecord.getInterceptMsgNumber() + "条");
//        }
        // 护眼时长
        binding.historyPreventEyeTime.setText(time2);
        binding.historyStudyReportTitle.setText("学习报告");
    }

    public String doTransformTime(String learningTime) {
        Log.d(TAG, "updateCurrentRecordInfo: learningTime=> " + learningTime);
        if (learningTime == null) {
            learningTime = "0";
        }
        Long aLong = Long.valueOf(learningTime);

        long seconds = aLong / 1000;
        long totaldata = seconds; //计算秒值
// 计算时分秒
//        long day = totaldata / 3600 / 24;
        long hours = totaldata / 3600 / 24; // 小时
        long minutes = totaldata % 3600 / 60; // 分钟

        String time = "";

        if (minutes <= 0) {
            time = "0分钟";
        } else {
            if (hours > 0) {
                time = hours + "小时" + minutes + "分钟";
            } else {
                time = minutes + "分钟";
            }
        }
        return time;
    }

    /*
    /**
     * 转换图谱数据
     * @param mapping 图谱节点
     * @param keypoints   知识点信息
     * @param testPoints  考点信息
     * @param isGraph true：非精准学图谱
     * @return
    public void getStarInfos(AppMapping mapping,
                             List<AppKeyPoint> keypoints,
                             List<AppTestPoint> testPoints,
                             boolean isGraph)
     */
    private void getNoteMapDateFromNet() {
        BookLoader loader = new BookLoader();
        String sn2 = NetWorkUtils.createCourseSign(usrId + "", false);
        String sectionId = "556154";
        if (exercisesRecords != null) {
            String sectionId1 = exercisesRecords.getSectionId();
            if (sectionId1 != null && !"".equals(sectionId1)) {
                sectionId = exercisesRecords.getSectionId();
            } else {
                Log.d(TAG, "getNoteMapDateFromNet: 暂无内容");
                return;
            }
            Log.d(TAG, "getNoteMapDateFromNet: sectionId1=> " + sectionId1);
        }
        Log.d(TAG, "getNoteMapDateFromNet: sn2=> " + sn2);
        loader.getSectionOne(sn2, sectionId).subscribe(new Subscriber<SectionOneResponse>() {
            @Override
            public void onCompleted() {
                mStarInfos.clear();
                // AI精准学加载失败
                if(sectionOneItem == null) {
                    binding.historyAiTestContent.setVisibility(View.GONE);
                    currentRecord.getExercisesRecords().setSectionId("");
                    refreshViewNoteTitleAndImg();
                    return;
                }
                if(sectionOneItem.getMapping() == null || sectionOneItem.getKeypoint() == null || sectionOneItem.getTestpoint() == null) {
                    binding.historyAiTestContent.setVisibility(View.GONE);
                    currentRecord.getExercisesRecords().setSectionId("");
                    refreshViewNoteTitleAndImg();
                    return;
                }

                List<StarInfo> starInfos = AiCourseUtils.getStarInfos(
                        sectionOneItem.getMapping(),
                        sectionOneItem.getKeypoint(),
                        sectionOneItem.getTestpoint(),
                        true
                );
                if (starInfos.size() != 0) {
                    mStarInfos.addAll(starInfos);
                    handler.post(() -> {
                        binding.littleStarview.setVisibility(View.VISIBLE);
                        binding.littleStarview.setBackgroundColor(Color.TRANSPARENT);
                        binding.littleStarview.setPointIsAllOpen(false);
                        binding.littleStarview.setMapInitCenter(true);
                        binding.littleStarview.setStarViewPadding(0, 0);
                        binding.littleStarview.setVisibility(View.VISIBLE);
                        binding.littleStarview.setScreenScale(0.5f);
                        binding.littleStarview.setDataInSimpleViewWidhTagetCopyInfos(mStarInfos, 1.0f, -1, -1);
                    });
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getNoteMapDateFromNet onError: e=> " + e);
            }

            @Override
            public void onNext(SectionOneResponse sectionOneResponse) {
                Log.d(TAG, "getNoteMapDateFromNet onNext: data=> " + sectionOneResponse.getData());
                sectionOneItem = sectionOneResponse.getData();
            }
        });
    }

    //初始化usrId
    private void initUsrId() {
        usrId = "0";
        //【未登录不处理？】
        boolean userLogin = isUserLogin(getContext());
        UserBaseInfo userInfo = null;
        if (userLogin) {
            UserDbSearch userDbSearch = UserDbSearch.getInstance(getContext());
            userInfo = userDbSearch.getUserInfo();
            usrId = userInfo.uid + "";
        } else {
            application.showToast("请登录账号");
        }
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

    public void showMakeSureDialog(DialogUtils.DialogListener listener) {
        alertDialog = DialogUtils.getAlertDialog(getActivity(), "提示", "确认是否删除", listener);
        alertDialog.show();
    }

    private void loadCurrentRecord(int index) {
        if (taskSupportRecordList.size() == 0) return;
        currentRecord = taskSupportRecordList.get(index);
        if (currentRecord.getLearningTime() == null || "".equals(currentRecord.getLearningTime())) {
            currentRecord.setLearningTime("0");
        }
        if (currentRecord.getEyeCareTime() == null || "".equals(currentRecord.getEyeCareTime())) {
            currentRecord.setEyeCareTime("0");
        }
        if (currentRecord.getSpeedTime() == null || "".equals(currentRecord.getSpeedTime())) {
            currentRecord.setSpeedTime("0");
        }
        exercisesRecords = currentRecord.getExercisesRecords();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadCacheFromACache2() {
        String recordCache = aCache.getAsString(HISTORY_LIST_CACHE);

        String recordKeys = aCache.getAsString(HISTORY_LIST_CACHE_KEYS);

        Log.d(TAG, "loadCacheFromACache: recordCache => " + recordCache);
        Log.d(TAG, "loadCacheFromACache: recordKeys => " + recordKeys);

        HashMap<String, List<TaskSupportRecord>> map = null;
        List<String> keys = null;
        if (recordCache != null) {
            map = GsonManager.getInstance().fromJson(recordCache,
                    new TypeToken<HashMap<String, List<TaskSupportRecord>>>() {
                    });
        }

        if (recordKeys != null) {
            keys = GsonManager.getInstance().fromJson(recordKeys,
                    new TypeToken<List<String>>() {
                    });
        }

        if (map != null) {
            recordCaches.clear();
            recordCaches.putAll(map);
        }

        if (keys != null) {
            recordCacheKeys.clear();
            recordCacheKeys.addAll(keys);
        }
        Collections.reverse(recordCacheKeys);
        Log.d(TAG, "loadCacheFromACache2: recordCacheKeys = " + recordCacheKeys);
        doTaskChangeCacheToList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadCacheFromACache() {
        String recordCache = aCache.getAsString(HISTORY_LIST_CACHE);
        String recordKeys = aCache.getAsString(HISTORY_LIST_CACHE_KEYS);

        Log.d(TAG, "loadCacheFromACache: recordCache => " + recordCache);
        Log.d(TAG, "loadCacheFromACache: recordKeys => " + recordKeys);

        HashMap<String, List<TaskSupportRecord>> map = null;
        List<String> keys = null;
        if (recordCache != null) {
            map = GsonManager.getInstance().fromJson(recordCache,
                    new TypeToken<HashMap<String, List<TaskSupportRecord>>>() {
                    });
        }
        if (recordKeys != null) {
            keys = GsonManager.getInstance().fromJson(recordKeys, new TypeToken<List<String>>() {
            });
        }
        if (map != null) {
            recordCaches.clear();
            recordCaches.putAll(map);
        }
        if (keys != null) {
            recordCacheKeys.clear();
            recordCacheKeys.addAll(keys);
        }
        doTaskChangeCacheToList();
    }

    //将缓存处理成列表recordCacheKeys.size()
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void doTaskChangeCacheToList() {
        taskSupportRecordList.clear();
        for (int i = 0; i < recordCacheKeys.size(); i++) {
            if (recordCaches.containsKey(recordCacheKeys.get(i))) {
                Log.d(TAG, "doTaskChangeCacheToList: time = " + recordCacheKeys.get(i));
                List<TaskSupportRecord> records = recordCaches.get(recordCacheKeys.get(i));
                if (records != null) {
                    if (records.size() == 0) {
                        continue;
                    }
                    this.taskSupportRecordList.addAll(Objects.requireNonNull(records));
                }
            }
        }
    }

    private void initCache(Context context) {
        if (hContext != null) {
            aCache = ACache.get(context);
            return;
        }
        aCache = ACache.get(getActivity());
    }

    // 默认生命周期一定执行
    @Override
    public void onPause() {
        super.onPause();
        // 关闭删除截图状态
        screenShotAdapter.setEdit(false);
        audioListAdapter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveCache();
    }

    public void saveCache() {
        //通知服务更新存储记录
        List<TaskSupportRecord> records = recordCaches.get(currentRecord.getTime());
        if (records == null) return;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPackageName().equals(currentRecord.getPackageName())) {
                records.set(i, currentRecord);
                recordCaches.put(currentRecord.getTime(), records);
                String s = GsonManager.getInstance().toJson(recordCaches,
                        new TypeToken<HashMap<String, List<TaskSupportRecord>>>() {
                        });
                aCache.put(HISTORY_LIST_CACHE, s);
                return;
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.hContext = context;
    }

    // 默认生命周期 【1:activity被杀死,2:commit 新的fragment,3:不一定一定执行（会被系统意外杀死）结束方法写在onPause】
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onclickBigImgDialog != null) {
            onclickBigImgDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        getNoteMapDateFromNet();
    }

    /**
     * targetSdk >= 29 传入文件名称
     * targetSdk < 29 传入文件路径
     *
     * @param fileNameOrPath
     */
    public Uri loadFileFromMedia(String fileNameOrPath) throws FileNotFoundException {
        return FileUtils.loadMediaFile(fileNameOrPath, hContext);
    }

    public static void main(String[] args) {
        long time = 1000L;
        String s = milliToHms(time);
        System.out.println(s);
    }

    public static String milliToHms(long time) {
        long s = 0;
        long m = 0;
        long h = 0;
        long sec = TimeUnit.MILLISECONDS.toSeconds(time);
        if (sec >= 60) {
            s = sec % 60;
            m = sec / 60;
            h = m / 60;
            m = m % 60;
        } else {
            s = sec;
        }
        String second = s > 10 ? s + "" : "0" + s;
        String min = m > 0 ? (m > 10 ? m + "" : "0" + m) + ":" : "";
        String hour = h > 0 ? (h > 10 ? h + "" : "0" + h) + ":" : "";
        System.out.println("sec= " + sec + "，h=" + h + ",m = " + m + ",s=" + s);
        String hms = hour + min + second;
        return hms;
    }
}
