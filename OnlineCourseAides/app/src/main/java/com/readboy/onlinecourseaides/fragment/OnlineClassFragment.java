package com.readboy.onlinecourseaides.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.MainActivity;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.activity.RequestActivity;
import com.readboy.onlinecourseaides.adapter.AppInfoAdapter;
import com.readboy.onlinecourseaides.adapter.OnlineCourseAdapter;
import com.readboy.onlinecourseaides.adapter.SpacesAppItemDecoration;
import com.readboy.onlinecourseaides.adapter.SpacesItemsDecoration;
import com.readboy.onlinecourseaides.bean.AppInfo;
import com.readboy.onlinecourseaides.bean.CourseInfo;
import com.readboy.onlinecourseaides.bean.ScreenShotRecord;
import com.readboy.onlinecourseaides.bean.SoundRecord;
import com.readboy.onlinecourseaides.bean.response.DeafultResponse;
import com.readboy.onlinecourseaides.bean.response.DefaultAppResponse;
import com.readboy.onlinecourseaides.bean.response.OnlineClassResponse;
import com.readboy.onlinecourseaides.bean.response.UserWhiteSiteResponse;
import com.readboy.onlinecourseaides.bean.response.item.AppItem;
import com.readboy.onlinecourseaides.bean.response.item.OnlineClassItem;
import com.readboy.onlinecourseaides.bean.response.item.OnlineClassSatesItem;
import com.readboy.onlinecourseaides.bean.response.item.UserWhiteSiteItem;
import com.readboy.onlinecourseaides.databinding.FragmentMainBinding;
import com.readboy.onlinecourseaides.network.GreenBrowserLoader;
import com.readboy.onlinecourseaides.receiver.ReceiverMsgReceiver;
import com.readboy.onlinecourseaides.ui.AddOnlineCourseView;
import com.readboy.onlinecourseaides.ui.AppInfoDialog;
import com.readboy.onlinecourseaides.ui.AppMoreDialog;
import com.readboy.onlinecourseaides.ui.ClassMoreDialog;
import com.readboy.onlinecourseaides.ui.DefaultTipsDialogView;
import com.readboy.onlinecourseaides.utils.GlobalParam;
import com.readboy.onlinecourseaides.utils.GsonManager;
import com.readboy.onlinecourseaides.utils.NetWorkUtils;
import com.readboy.onlinecourseaides.utils.ParamType;
import com.readboy.onlinecourseaides.utils.ParentManagerUtils;
import com.readboy.onlinecourseaides.utils.cache.ACache;
import com.readboy.provider.UserDbSearch;
import com.readboy.provider.mhc.info.UserBaseInfo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 主页面，网课应用碎片
 */
public class OnlineClassFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "OnlineClassFragment";
    // 添加类型是网课助手 type = 1
    private static final String ADD_TYPE_OCA = "1";

    FragmentMainBinding binding;

    private Context context;

    private View rootView;

    public static final int DEFAULT_APP_NUMBER = 6;
    public static final int DEFAULT_COURSE_NUMBER = 6;

    private ParamType selectType = ParamType.SELECT_APP_TITLE_TYPE;

    private AppMoreDialog appMoreDialog;
    private ClassMoreDialog classMoreDialog2;
    private AppInfoDialog addAppInfoDialog;

    //全部应用信息
    private List<AppInfo> appInfoList;
    // packageName:app   缓存加载的所有APP 用于获取图标和最新安装版本等
    private HashMap<String, AppInfo> appInfoAllCache;
    //首页默认应用列表
    private volatile LinkedList<AppInfo> appDefaultList;
    //首页默认网课云
    private volatile LinkedList<CourseInfo> courseDefaultList;

    // 目前更多就是配置的全部应用
    // 更多里面的内容 APP
    private volatile List<AppInfo> moreAppDialogData;
    // 更多里面的内容 CLASS
    private volatile List<CourseInfo> moreClassDialogData;

    private AppInfoAdapter appInfoAdapter;
    private OnlineCourseAdapter onlineCourseAdapter;

    private MyApplication application;
    private Handler handler;
    //缓存工具类 来源于 https://github.com/yangfuhai/ASimpleCache/tree/master/AsimpleCacheDemo
    private ACache aCache;
    private ReceiverMsgReceiver msgReceiver;

    // 个人中心信息
    private String usrId;
    private UserBaseInfo userBaseInfo;
    private boolean isFirstEnabled = true;

    public OnlineClassFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setContentView();
        initData();
        initView();
        Log.d(TAG, "onCreateView: start");
        return rootView;
    }

    private void registerMyReceiver() {
        msgReceiver = new ReceiverMsgReceiver((code, type) -> {
            if (RequestActivity.CODE_SUCCESS.equals(code) && ReceiverMsgReceiver.PMG_MSG_TYPE_PMG_CLASS_DEL.equals(type)) {
                classMoreDialog2.setEnableStartEdit(true);
                classMoreDialog2.refreshEditStatus();
            }
            if (RequestActivity.CODE_SUCCESS.equals(code) && ReceiverMsgReceiver.PMG_MSG_TYPE_PMG_CLASS_ADD.equals(type)) {
                if (isSendAddClass) {
                    openDialogAddClass();
                }
                isSendAddClass = false;
                isEnabledAddClass = true;
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReceiverMsgReceiver.PMG_MSG);
        requireActivity().registerReceiver(msgReceiver, intentFilter);
    }

    private void initView() {
        binding.mainTitleRightMore.setOnClickListener(this);
        binding.mainTitleRightMoreApp.setOnClickListener(this);

        if (appDefaultList.size() == 0) {
            binding.mainNoContentTitle.setVisibility(View.VISIBLE);
            binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
        } else {
            binding.mainNoContentTitle.setVisibility(View.INVISIBLE);
            binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
        }
        addAppInfoDialog = new AppInfoDialog(getActivity(), () -> {
            new XPopup.Builder(getContext())
                    .asCustom(new DefaultTipsDialogView(context, new DefaultTipsDialogView.DefaultTipsDialogListener() {
                        @Override
                        public void makeSure() {
                            handler.post(() -> {
                                AppInfo data = addAppInfoDialog.getSelectData();
                                // 设置此应用的名单可以删除
                                data.setEnabledDelAppInfo(true);
                                if (appDefaultList.contains(data) || moreAppDialogData.contains(data)) {
                                    application.showToast("该应用已经存在");
                                } else {
                                    moreAppDialogData.add(data);
                                    appMoreDialog.refreshView();
                                    application.showToast("添加名单成功");
                                    doSaveListCache();
                                    String json = GsonManager.getInstance().toJson(moreAppDialogData, new TypeToken<List<AppInfo>>() {
                                    });
                                    Intent intent = new Intent(GlobalParam.NOTIFY_SERVICE_REFRESH_APPLIST);
                                    intent.putExtra(GlobalParam.NOTIFY_SERVICE_REFRESH_DATA_APP_LIST, json);
                                    getActivity().sendBroadcast(intent);
                                }
                                //操作完成关闭弹框
                                addAppInfoDialog.dismiss();
                            });
                        }

                        @Override
                        public void cancel() {
                        }
                    }, "确认加入名单")).show();
        });
        appMoreDialog = new AppMoreDialog(getActivity(), moreAppDialogData, onSelectDialogListener, ParamType.TYPE_APP_DIALOG);
        classMoreDialog2 = new ClassMoreDialog(getActivity(), moreClassDialogData, selectClassDialogListener);
        appInfoAdapter = new AppInfoAdapter(getActivity(), appDefaultList, appListener);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.mainRecyclerViewApps.setLayoutManager(manager);
        // 设置间距，实际上是padding
        binding.mainRecyclerViewApps.addItemDecoration(new SpacesAppItemDecoration(52));
        binding.mainRecyclerViewApps.setAdapter(appInfoAdapter);

        onlineCourseAdapter = new OnlineCourseAdapter(getActivity(), courseDefaultList, onlineCourseListener);
        GridLayoutManager manager2 = new GridLayoutManager(getContext(), 3);
        int spanCount = 3; // 3 columns
        int spacing = 45; // 50px
        // 设置间距，实际上是padding
        binding.mainRecyclerViewClass.setLayoutManager(manager2);
        binding.mainRecyclerViewClass.addItemDecoration(new SpacesItemsDecoration(spanCount, spacing, false));
        binding.mainRecyclerViewClass.setAdapter(onlineCourseAdapter);
    }


    AppInfoAdapter.AppInfoListener appListener = new AppInfoAdapter.AppInfoListener() {
        @Override
        public void appInfoOnClick() {
            MainActivity activity = (MainActivity) requireActivity();
            activity.doPreStartApp();
            // 跳转到绿色上网或者指定网课应用
            activity.startTheAppsAndCourse(appInfoAdapter.getCurrentAppInfo());
        }

        @Override
        public void delAppInfo() {

        }
    };

    OnlineCourseAdapter.OnlineCourseListener onlineCourseListener = new OnlineCourseAdapter.OnlineCourseListener() {
        @Override
        public void onCourseClick() {
            MainActivity activity = (MainActivity) requireActivity();
            activity.doPreStartApp();
            // 跳转到绿色上网或者指定网课应用
            activity.startTheAppsAndCourse(onlineCourseAdapter.getCurrentPoint());
        }

        @Override
        public void delCourseClick(CourseInfo index) {
        }
    };

    private boolean isEnabledAddClass = false;
    private boolean isSendAddClass = true;
    ClassMoreDialog.SelectClassDialogListener selectClassDialogListener = new ClassMoreDialog.SelectClassDialogListener() {
        @Override
        public void gotoMoreClick() {
            classMoreDialog2.dismiss();
            //直接弹窗设置
            if (initUsrId()) {
                // 家长管控输密码 ParentManagerUtils.popupPMPasswordInput(getActivity(), 1)
                if (isSendAddClass) {
                    Intent intent = new Intent();
                    intent.putExtra(RequestActivity.TYPE, ReceiverMsgReceiver.PMG_MSG_TYPE_PMG_CLASS_ADD);
                    intent.setClass(context, RequestActivity.class);
                    context.startActivity(intent);
                } else {
                    //打开添加弹窗
                    openDialogAddClass(); //gotoMoreClick
                }
            }
        }

        @Override
        public void itemOnClick() {
            CourseInfo courseInfo = classMoreDialog2.getCurrentPoint();
            if (!courseDefaultList.contains(courseInfo)) {
                //刷新主页面
                courseDefaultList.removeLast();
                courseDefaultList.addFirst(courseInfo);
                onlineCourseAdapter.refreshData(courseDefaultList);
            }
            classMoreDialog2.dismiss();
        }

        // 适配器内部已经删除这里不需要remove
        @Override
        public void delItem(CourseInfo index) {
            if (index != null) {
                String witheId = index.getWitheId();
                if (witheId != null) {
                    if (index.getSource() == 1) {
                        if (initUsrId()) {
                            String sn2 = NetWorkUtils.createGreenSign(usrId, false);
                            Log.d(TAG, "delItem: del  sn2 " + sn2);
                            GreenBrowserLoader loader = new GreenBrowserLoader();
                            loader.delUserWithe(sn2, witheId, usrId).subscribe(new Subscriber<DeafultResponse>() {
                                @Override
                                public void onCompleted() {
                                    // 删除网课云  如果是首页删除需要处理
//                        Log.d(TAG, "delUserWithe: classDefault = " + courseDefaultList);
                                    for (int i = 0; i < courseDefaultList.size(); i++) {
                                        CourseInfo info = courseDefaultList.get(i);
                                        if (!moreClassDialogData.contains(info)) {
                                            courseDefaultList.remove(i);
                                            for (CourseInfo datum : moreClassDialogData) {
                                                if (!courseDefaultList.contains(datum)) {
                                                    courseDefaultList.add(i, datum);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    classMoreDialog2.refreshView();
                                    onlineCourseAdapter.refreshView("del more item");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d(TAG, "delUserWithe onError: e=>" + e);
                                }

                                @Override
                                public void onNext(DeafultResponse deafultResponse) {
                                    Log.d(TAG, "delUserWithe onNext: " + deafultResponse + " index = " + index + ", usrid = " + usrId);
                                }
                            });
                        }
                    } else {
                        // 删除网课云  如果是首页删除需要处理
//                        Log.d(TAG, "delUserWithe: classDefault = " + courseDefaultList);
                        for (int j = 0; j < courseDefaultList.size(); j++) {
                            CourseInfo info2 = courseDefaultList.get(j);
                            if (witheId.equals(info2.getWitheId())) {
                                courseDefaultList.remove(j);
                                for (CourseInfo datum : moreClassDialogData) {
                                    if (!courseDefaultList.contains(datum)) {
                                        courseDefaultList.add(j, datum);
                                        break;
                                    }
                                }
                            }
                        }
                        classMoreDialog2.refreshView();
                        onlineCourseAdapter.refreshView("del more item");
                    }
                }
            }


        }

        @Override
        public boolean editItem() {
            return ParentManagerUtils.popupPMPasswordInput(getActivity(), MainActivity.REQUEST_CODE_PMG);
        }
    };

    AppMoreDialog.OnSelectDialogListener onSelectDialogListener = new AppMoreDialog.OnSelectDialogListener() {
        @Override
        public void selectOnClick(ParamType paramType) {
            // 添加应用界面
            if (paramType == ParamType.TYPE_APP_DIALOG) {
                Log.d(TAG, "more: AppMoreDialog click");
                // 检测网络状态
                if (!NetWorkUtils.checkNetWorkConnected(getContext())) {
                    Toast.makeText(application,"网络异常，请检查", Toast.LENGTH_SHORT).show();
                }else {
                    appMoreDialog.dismiss();
                    getAllInstallAppList();
                    addAppInfoDialog.show();
                }
            }

            if (paramType == ParamType.APP_MORE_DEL_APP) {
                Log.d(TAG, "selectOnClick: appDefault = " + appDefaultList);
                for (int i = 0; i < appDefaultList.size(); i++) {
                    AppInfo appInfo = appDefaultList.get(i);
                    if (!appInfo.isEnabledDelAppInfo()) {
                        continue;
                    }
                    if (!moreAppDialogData.contains(appInfo)) {
                        Log.d(TAG, "selectOnClick: del app => " + appInfo);
                        appDefaultList.remove(i);
                        for (AppInfo datum : moreAppDialogData) {
                            if (!appDefaultList.contains(datum)) {
                                appDefaultList.add(i, datum);
                                Log.d(TAG, "selectOnClick: datum =>" + datum);
                                break;
                            }
                        }
                    }
                }
                String json = GsonManager.getInstance().toJson(moreAppDialogData, new TypeToken<List<AppInfo>>() {
                });
                Log.d(TAG, "selectOnClick: json = > "+ json);
                Intent intent = new Intent(GlobalParam.NOTIFY_SERVICE_REFRESH_APPLIST);
                intent.putExtra(GlobalParam.NOTIFY_SERVICE_REFRESH_DATA_APP_LIST, json);
                getActivity().sendBroadcast(intent);
                appInfoAdapter.refreshView();
            }
        }

        // 只有跳转成功才能执行
        @Override
        public void itemOnClick(ParamType paramType) {
            if (paramType == ParamType.TYPE_APP_DIALOG) {
                AppInfo appInfo = (AppInfo) appMoreDialog.getCurrentPoint();
                if (!appDefaultList.contains(appInfo)) {
                    //刷新主页面
                    appDefaultList.removeLast();
                    appDefaultList.addFirst(appInfo);
                    appInfoAdapter.refreshData(appDefaultList);
                }
                Log.d(TAG, "itemOnClick:appDefaultList=> " + appDefaultList);
                appMoreDialog.dismiss();
            }
        }
    };

    public void getAllInstallAppList() {
        Observable.create(subscriber -> {
            // 获取AllInstallAppList
            getAppInfoAllInstalled();

            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        for (AppInfo appInfo : appInfoList) {
                            if (moreAppDialogData.contains(appInfo)) {
                                appInfo.setEnabledDelAppInfo(true);
                            }
                        }
                        if(addAppInfoDialog != null) {
                            addAppInfoDialog.setData(appInfoList);
                        }
                        Log.d(TAG, "onCompleted: ScreenShotRecord异步加载完成");
                        getIcon();
                        appInfoAdapter.refreshView();
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

    // 默认是用户的白名单
    public void refreshMoreAppDialogData(CourseInfo info) {
        CourseInfo info1 = allWitheUserMap.get(info.getUrl());
        info1.setTitle(info.getTitle());
        info1.setEnabledDel(true);
        boolean isAdd = false;
        for (CourseInfo datum : moreClassDialogData) {
            if (datum.getUrl().equals(info1.getUrl())) {
                MyApplication.getInstances().showToast("已存在相同网址");
                isAdd = true;
                break;
            }
        }
        if (!isAdd) {
            Log.d(TAG, "makeSure:allWitheUserMap.containsKey add success ");
            moreClassDialogData.add(info1);
            classMoreDialog2.refreshView();
            application.showToast("添加成功");
        }
    }

    //添加弹窗 添加网课云 添加白名单网址
    public void openDialogAddClass() {
        getUserWhiteListFromNet();// 添加前加载
        Log.d(TAG, "openDialogAddClass: ");
        String sn2 = NetWorkUtils.createGreenSign(usrId, false);
        GreenBrowserLoader loader = new GreenBrowserLoader();
        AddOnlineCourseView courseView = new AddOnlineCourseView(context, new AddOnlineCourseView.AddOnlineCourseDialogListener() {
            @Override
            public void makeSure(ParamType type, CourseInfo info) {
                if (type == ParamType.RESULT_DO_SUCCESS) {
                    // 判断是否被用户已经添加到绿色上网
                    if (allWitheUserMap.containsKey(info.getUrl())) {
                        refreshMoreAppDialogData(info);
                    } else {
                        info.setEnabledDel(true);
                        Log.d(TAG, "makeSure: sn2 = " + sn2);
                        Log.d(TAG, "makeSure:userid = " + usrId + ",name = " + info.getTitle() + ", url = " + info.getUrl() + " ,SOURCE = " + ADD_TYPE_OCA);
                        loader.addUserWithe(sn2, usrId, info.getTitle(), info.getUrl(), ADD_TYPE_OCA).subscribe(new Subscriber<DeafultResponse>() {
                            @Override
                            public void onCompleted() {
                                isSendAddClass = true;
                                isEnabledAddClass = false;
                                doSaveListCache();
                                getUserWhiteListFromNet(); // 添加后刷新
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: e => " + e);
                                application.showToast("添加失败网络异常");
                            }

                            @Override
                            public void onNext(DeafultResponse s) {
                                Log.d(TAG, "addDefaultAppList onNext: s = " + s.getData());
                                application.showToast("添加成功");
                            }
                        });
                    }
                }
            }

            @Override
            public void cancel() {
                isSendAddClass = true;
                isEnabledAddClass = false;
            }
        }) {
            @Override
            public void dismiss() {
                super.dismiss();
                isSendAddClass = true;
                isEnabledAddClass = false;
            }
        };
        new XPopup.Builder(context)
                .asCustom(courseView).show();
    }

    private void initData() {
        application = MyApplication.getInstances();
        handler = new Handler();
        initCache();
        initUsrId();
        // 后期可配置到网络
        appInfoList = new ArrayList<>();
        moreAppDialogData = new ArrayList<>();
        moreClassDialogData = new ArrayList<>();
        appDefaultList = new LinkedList<>();
        courseDefaultList = new LinkedList<>();
        appInfoAllCache = new HashMap<>();
        getAllInstallAppList();
        // 读取ACache的内容  读取缓存
        readDefaultCache();
        // 从网络获取信息
        getInfoFromNet();
    }

    //初始化usrId
    private boolean initUsrId() {
        usrId = "0";
        //【未登录不处理？】
        boolean userLogin = isUserLogin(getContext());
        UserBaseInfo userInfo = null;
        if (userLogin) {
            UserDbSearch userDbSearch = UserDbSearch.getInstance(getContext());
            userInfo = userDbSearch.getUserInfo();
            usrId = userInfo.uid + "";
            userBaseInfo = userInfo;
            return true;
        } else {
            application.showToast("请登录账号");
            return false;
        }
    }

    public void initCache() {
        //缓存文件放置在/data/data/app-package-name/cache/路径下，缓存的目录默认为ACache
        aCache = ACache.get(getContext());
    }

    // 获取绿色上网数据
    public static final String MAIN_NET_LOAD_APP = "MAIN_NET_LOAD_APP";
    public static final String MAIN_NET_LOAD_CLASS = "MAIN_NET_LOAD_APP";

    public void getInfoFromNet() {
        String cache1 = aCache.getAsString(MAIN_NET_LOAD_APP);
        String cache2 = aCache.getAsString(MAIN_NET_LOAD_CLASS);

        boolean isError = true;
        boolean isError2 = true;
        if (cache1 != null) {
            isError = Boolean.parseBoolean(cache1);
        }
        if (cache2 != null) {
            isError2 = Boolean.parseBoolean(cache2);
        }
        Log.d(TAG, "getInfoFromNet: isError = " + isError);
        if (appDefaultList.size() == 0 || moreAppDialogData.size() == 0 || isError) {
            getDefaultAppInfoFromNet();// 初次加载
        } else {
            getDefaultAppInfoFromNet2();
        }
        Log.d(TAG, "getInfoFromNet: isError2 = " + isError2);
        if (moreClassDialogData.size() == 0 || isError2 || courseDefaultList.size() == 0) {
            getCourseInfoFromGreenFromNet();// 初次加载
        } else {
            getCourseInfoFromGreenFromNet2();
        }

        // 默认白名单点击加载
        getUserWhiteListFromNet();// 初次加载
    }

    private void initFirstInfo() {
        String provStr = "广东";
        if (userBaseInfo != null) {
            provStr = userBaseInfo.provStr;
        }
        Log.d(TAG, "initFirstInfo: provStr = " + provStr);
        if (provStr == null) return;
        if (!provStr.contains(courseDefaultList.getFirst().getTitle())) {
            for (int i = 1; i < courseDefaultList.size(); i++) {
                if (provStr.contains(courseDefaultList.getFirst().getTitle())) {
                    CourseInfo info = courseDefaultList.get(0);
                    CourseInfo info1 = courseDefaultList.get(i);
                    courseDefaultList.removeFirst();
                    courseDefaultList.addFirst(info1);
                    courseDefaultList.remove(i);
                    courseDefaultList.add(i, info);
                    Log.d(TAG, "initFirstInfo: provStr.contains(courseDefaultList" + courseDefaultList.getFirst());
                    return;
                }
            }
            for (CourseInfo datum : moreClassDialogData) {
                if (provStr.contains(datum.getTitle()) && !courseDefaultList.contains(datum)) {
                    courseDefaultList.removeLast();
                    courseDefaultList.addFirst(datum);
                    Log.d(TAG, "initFirstInfo: provStr.contains(moreClassDialogData" + courseDefaultList.getFirst());
                    return;
                }
            }
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

    // url:info
    private HashMap<String, CourseInfo> allWitheUserMap;

    // 获取OCA添加不在缓存的
    public void getUserWhiteListFromNet() {
        allWitheUserMap = new HashMap<>();
        GreenBrowserLoader loader = new GreenBrowserLoader();
        if (initUsrId()) {
            String sn2 = NetWorkUtils.createGreenSign(usrId, false);
            loader.getUserWhiteList(sn2, usrId).subscribe(new Subscriber<UserWhiteSiteResponse>() {
                @Override
                public void onCompleted() {
                    classMoreDialog2.refreshView();
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: e" + e);
                }

                @Override
                public void onNext(UserWhiteSiteResponse userWhiteSiteResponse) {
                    if (userWhiteSiteResponse.getData() != null) {
                        List<UserWhiteSiteItem> data = userWhiteSiteResponse.getData();
                        for (UserWhiteSiteItem item : data) {
                            if (item.getIsCommon() == 1) continue;
                            if (item.getIsCommon() == 0) {
                                CourseInfo info = new CourseInfo();
                                info.setTitle(item.getName());
                                info.setUrl(item.getUrl());
                                info.setEnabledDel(true);
                                info.setWitheId(item.getId());
                                info.setCollect(item.getCollect());
                                info.setSource(item.getSource());

//                                Log.d(TAG, "getUserWhiteList: onNext=> user " + item);
                                boolean isAdd = true;
                                for (int i = 0; i < moreClassDialogData.size(); i++) {
                                    CourseInfo info1 = moreClassDialogData.get(i);
                                    if (info1.getUrl().equals(info.getUrl())) {
                                        // 加载id用于删除
                                        info1.setWitheId(info.getWitheId());
                                        isAdd = false;
                                        break;
                                    }
                                }
                                // 获取OCA添加且不在缓存的
                                if (item.getSource() == 1 && isAdd) {
                                    moreClassDialogData.add(info);
                                }
                                // 所有自己添加的白名单
                                allWitheUserMap.put(info.getUrl(), info);
                            }
                        }
                    }
                }
            });
        }
    }

    // 从网络获取网课云信息列表、 APP默认列表
    private void getCourseInfoFromGreenFromNet() {
        // 获取请求密钥
//        String sn2 = NetWorkUtils.createGreenSign(usrId, false);
        GreenBrowserLoader loader = new GreenBrowserLoader();
        String sn2 = "";

        loader.getOnlineCourseUrlList(sn2).subscribe(new Subscriber<OnlineClassResponse>() {
            @Override
            public void onCompleted() {
                initFirstInfo();
                Log.d(TAG, "onCompleted: initFirstInfo courseDefaultList " + courseDefaultList);
                onlineCourseAdapter.refreshView("main getCourseInfoFromGreenFromNet");
                classMoreDialog2.refreshView();
                aCache.put(MAIN_NET_LOAD_CLASS, "false");
            }

            @Override
            public void onError(Throwable e) {
                String asString = aCache.getAsString(MAIN_NET_LOAD_CLASS);
                if(!"false".equals(asString)) {
                    aCache.put(MAIN_NET_LOAD_APP, "true");
                }
                Log.d(TAG, "getOnlineCourseUrlList error: " + e);
                Log.d(TAG, "getOnlineCourseUrlList error  => " + aCache.getAsString(MAIN_NET_LOAD_CLASS));
                courseDefaultList.clear();
                moreClassDialogData.clear();
                noNetWorkInitCourseInfo();
            }

            @Override
            public void onNext(OnlineClassResponse onlineClassResponse) {
                Log.d(TAG, "getOnlineCourseUrlList onNext: onlineClassResponse=> " + onlineClassResponse);
                if (onlineClassResponse != null) {
                    moreClassDialogData.clear();
                    courseDefaultList.clear();
                    OnlineClassSatesItem data = onlineClassResponse.getData();
                    Log.d(TAG, "getOnlineCourseUrlList onNext: OnlineClassItem=> " + data);
                    for (int i = 0; i < data.getWebList().size(); i++) {
                        CourseInfo info =
                                new CourseInfo(data.getWebList().get(i).getProvinceName(), data.getWebList().get(i).getUrl());
                        info.setEnabledDel(false);
                        if (!moreClassDialogData.contains(info)) {
                            if (i < DEFAULT_COURSE_NUMBER) {
                                courseDefaultList.add(info);
                            }
                            moreClassDialogData.add(info);
                        }
                    }
                }
            }
        });
    }

    // 刷新网站网址
    private void getCourseInfoFromGreenFromNet2() {
        // 获取请求密钥
//        String sn2 = NetWorkUtils.createGreenSign(usrId, false);
        GreenBrowserLoader loader = new GreenBrowserLoader();
        String sn2 = "";

        loader.getOnlineCourseUrlList(sn2).subscribe(new Subscriber<OnlineClassResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getOnlineCourseUrlList error: " + e);
            }

            @Override
            public void onNext(OnlineClassResponse onlineClassResponse) {
                Log.d(TAG, "getOnlineCourseUrlList onNext: onlineClassResponse=> " + onlineClassResponse);
                if (onlineClassResponse != null) {
                    OnlineClassSatesItem data = onlineClassResponse.getData();
                    List<OnlineClassItem> list = data.getWebList();
                    updateCourseList(list);
                }
            }
        });
    }

    private void updateCourseList(List<OnlineClassItem> list) {
        Log.d(TAG, "updateCourseList: list => "+list);
        // 转化成Map提高速度 ， 避免嵌套循环
        HashMap<String, OnlineClassItem> map = new HashMap<>();
        HashMap<String, CourseInfo> courseMap = new HashMap<>();
        HashMap<String, CourseInfo> moreMap = new HashMap<>();
        HashMap<String, OnlineClassItem> nameMap = new HashMap<>();
        for (OnlineClassItem item : list) {
            map.put(item.getUrl(), item);
            nameMap.put(item.getName(), item);
        }
        for (CourseInfo courseInfo : courseDefaultList) {
            courseMap.put(courseInfo.getUrl(), courseInfo);
        }
        for (CourseInfo datum : moreClassDialogData) {
            moreMap.put(datum.getUrl(), datum);
        }

        for (OnlineClassItem item : list) {
            if(!moreMap.containsKey(item.getUrl())) {
                CourseInfo info1 = new CourseInfo();
                info1.setUrl(item.getUrl());
                info1.setTitle(item.getProvinceName());
                info1.setEnabledDel(false);
                moreClassDialogData.add(info1);
                Log.d(TAG, "updateCourseList: new class data =>"+item);
            }

            // url 需要刷新 或者 url不存在需要更新
            for (int i = 0; i < courseDefaultList.size(); i++) {
                CourseInfo info = courseDefaultList.get(i);
                if(info.isEnabledDel()) {
                    continue;
                }
                //根据名字 URL更新
                if (info.getTitle().equals(item.getName())) {
                    if (!map.containsKey(info.getUrl())) {
                        info.setUrl(nameMap.get(info.getTitle()).getUrl());
                        continue;
                    }
                }
                //没找到url,也没有名字，直接更新一个到列表
                if (!map.containsKey(info.getUrl())) {
                    courseDefaultList.remove(i);
                    for (CourseInfo datum : moreClassDialogData) {
                        if(!courseMap.containsKey(datum.getUrl())) {
                            courseDefaultList.add(datum);
                        }
                    }
                    break;
                }
            }

            // 全部列表需要更新
            for (int i = 0; i < moreClassDialogData.size(); i++) {
                CourseInfo moreInfo = moreClassDialogData.get(i);
                //根据名字 URL更新
                if(moreInfo.isEnabledDel()) {
                    continue;
                }
                if (moreInfo.getTitle().equals(item.getName())) {
                    if (!map.containsKey(moreInfo.getUrl())) {
                        moreInfo.setUrl(nameMap.get(moreInfo.getTitle()).getUrl());
                        break;
                    }
                }
                //没找到url,也没有名字
                if (!map.containsKey(moreInfo.getUrl())) {
                    moreClassDialogData.remove(i);
                    break;
                }
            }
        }
        onlineCourseAdapter.refreshView("main getCourseInfoFromGreenFromNet");
        classMoreDialog2.refreshView();
    }


    private void noNetWorkInitCourseInfo() {
        CourseInfo info1 = new CourseInfo();
        CourseInfo info2 = new CourseInfo();
        CourseInfo info3 = new CourseInfo();
        CourseInfo info4 = new CourseInfo();
        CourseInfo info5 = new CourseInfo();
        CourseInfo info6 = new CourseInfo();
        CourseInfo info7 = new CourseInfo();
        CourseInfo info8 = new CourseInfo();
        CourseInfo info9 = new CourseInfo();

        info1.setTitle("广东");
        info1.setUrl("http://zy.gdedu.gov.cn");
        info1.setEnabledDel(false);

        info2.setTitle("上海");
        info2.setUrl("https://www.sh.smartedu.cn");
        info2.setEnabledDel(false);

        info3.setTitle("浙江");
        info3.setUrl("http://yun.zjer.cn");
        info3.setEnabledDel(false);

        info4.setTitle("河南");
        info4.setUrl("https://www.hner.cn");
        info4.setEnabledDel(false);

        info5.setTitle("重庆");
        info5.setUrl("https://basic.cq.smartedu.cn");
        info5.setEnabledDel(false);

        info6.setTitle("江苏");
        info6.setUrl("https://mskzkt.jse.edu.cn/cloudCourse/index/pc");
        info6.setEnabledDel(false);

        info7.setTitle("深圳");
        info7.setUrl("https://zy.szedu.cn");
        info7.setEnabledDel(false);

        info8.setTitle("北京");
        info8.setUrl("https://basic.beijing.smartedu.cn/index");
        info8.setEnabledDel(false);

        info9.setTitle("山东");
        info9.setUrl("http://www.sdsjyy.cn");
        info9.setEnabledDel(false);

        courseDefaultList.add(info1);
        courseDefaultList.add(info2);
        courseDefaultList.add(info3);
        courseDefaultList.add(info4);
        courseDefaultList.add(info5);
        courseDefaultList.add(info6);

        moreClassDialogData.add(info1);
        moreClassDialogData.add(info2);
        moreClassDialogData.add(info3);
        moreClassDialogData.add(info4);
        moreClassDialogData.add(info5);
        moreClassDialogData.add(info6);
        moreClassDialogData.add(info7);
        moreClassDialogData.add(info8);
        moreClassDialogData.add(info9);
    }

    public void getDefaultAppInfoFromNet() {
        // 获取请求密钥
//        String sn2 = NetWorkUtils.createGreenSign(usrId, false);
        String sn2 = "";
        GreenBrowserLoader loader = new GreenBrowserLoader();
        loader.getDefaultAppList(sn2).subscribe(new Subscriber<DefaultAppResponse>() {
            @Override
            public void onCompleted() {
                handler.postDelayed(() -> {
                    Log.d(TAG, "onCompleted: refreshData appDefaultList=>" + moreAppDialogData);
                    appInfoAdapter.refreshData(appDefaultList);
                    appMoreDialog.refreshAppData(moreAppDialogData);

                    if (appDefaultList.size() == 0) {
                        binding.mainNoContentTitle.setVisibility(View.VISIBLE);
                        binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
                    } else {
                        binding.mainNoContentTitle.setVisibility(View.INVISIBLE);
                        binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
                    }
                    aCache.put(MAIN_NET_LOAD_APP, "false");
                }, 100);

            }

            @Override
            public void onError(Throwable e) {
                String asString = aCache.getAsString(MAIN_NET_LOAD_APP);
                if(!"false".equals(asString)) {
                    aCache.put(MAIN_NET_LOAD_APP, "true");
                }
                Log.d(TAG, "onNext: defaultAppResponse error=> " + e);
                noNetWorkInitDefaultAppInfo();
                appMoreDialog.refreshAppData(moreAppDialogData);

                if (appDefaultList.size() == 0) {
                    binding.mainNoContentTitle.setVisibility(View.VISIBLE);
                    binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
                } else {
                    binding.mainNoContentTitle.setVisibility(View.INVISIBLE);
                    binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNext(DefaultAppResponse defaultAppResponse) {
                appDefaultList.clear();
                moreAppDialogData.clear();
                Log.d(TAG, "onNext: defaultAppResponse => " + defaultAppResponse.getData());
                List<AppItem> webList = defaultAppResponse.getData().getWebList();
                for (int i = 0; i < webList.size(); i++) {
                    AppItem appItem = webList.get(i);
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppName(appItem.getAppName());
                    appInfo.setPackageName(appItem.getPackageName());
                    appInfo.appIconUrl = appItem.getAppIcon();
                    // 判断应用是否是第三方
                    if (isThirdPartyApps(appInfo.getPackageName())) {
                        appInfo.setType(1);
                    } else {
                        appInfo.setType(2);
                    }
                    if (GlobalParam.MY_APP_PACKAGENAME.equals(appInfo.getPackageName())) {
                        continue;
                    }

                    if (i < 6) {
                        appDefaultList.add(appInfo);
                    }
                    moreAppDialogData.add(appInfo);
                }
                if (appDefaultList.size() == 0) {
                    binding.mainNoContentTitle.setVisibility(View.VISIBLE);
                    binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
                } else {
                    binding.mainNoContentTitle.setVisibility(View.INVISIBLE);
                    binding.mainRecyclerViewApps.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getDefaultAppInfoFromNet2() {
        // 获取请求密钥
//        String sn2 = NetWorkUtils.createGreenSign(usrId, false);
        String sn2 = "";
        GreenBrowserLoader loader = new GreenBrowserLoader();
        loader.getDefaultAppList(sn2).subscribe(new Subscriber<DefaultAppResponse>() {
            @Override
            public void onCompleted() {
                appMoreDialog.refreshAppData(moreAppDialogData);
                appInfoAdapter.refreshView();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onNext: getDefaultAppInfoFromNet2 error=> " + e);
            }

            @Override
            public void onNext(DefaultAppResponse defaultAppResponse) {
                List<AppItem> webList = defaultAppResponse.getData().getWebList();
                HashMap<String, AppInfo> moreApp = new HashMap<>();
                HashMap<String, AppInfo> defApp = new HashMap<>();
                HashMap<String, AppItem> list = new HashMap<>();

                for (AppItem item : webList) {
                    list.put(item.getPackageName(), item);
                }

                for (AppInfo info : appDefaultList) {
                    defApp.put(info.getPackageName(), info);
                }
                // 目前只对更多添加新应用
                for (AppInfo datum : moreAppDialogData) {
                    moreApp.put(datum.getPackageName(), datum);
                }
                for (AppItem appItem : webList) {
                    // 不存在添加
                    if(!moreApp.containsKey(appItem.getPackageName())) {
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAppName(appItem.getAppName());
                        appInfo.setPackageName(appItem.getPackageName());
                        appInfo.appIconUrl = appItem.getAppIcon();
                        // 判断应用是否是第三方
                        if (isThirdPartyApps(appInfo.getPackageName())) {
                            appInfo.setType(1);
                        } else {
                            appInfo.setType(2);
                        }
                        if (GlobalParam.MY_APP_PACKAGENAME.equals(appInfo.getPackageName())) {
                            continue;
                        }
                        moreAppDialogData.add(appInfo);
                        Log.d(TAG, "onNext: getDefaultAppInfoFromNet2 " + appInfo);
                    }
                }
//                // 后台移除并且没安装删除
//                for (AppInfo datum : moreAppDialogData) {
//                    if(!list.containsKey(datum.getPackageName()) && !appInfoAllCache.containsKey(datum.getPackageName())) {
//                        moreAppDialogData.remove(datum);
//                    }
//                }
//
//                // 后台移除并且没安装删除
//                for (int i = 0; i < appDefaultList.size(); i++) {
//                    AppInfo appInfo = appDefaultList.get(i);
//                    if(appInfo.isEnabledDelAppInfo()) {
//                        continue;
//                    }
//                    if(!list.containsKey(appInfo.getPackageName()) && !appInfoAllCache.containsKey(appInfo.getPackageName())) {
//                        appDefaultList.remove(i);
//                        for (AppInfo dialogDatum : moreAppDialogData) {
//                            if(!defApp.containsKey(dialogDatum)) {
//                                appDefaultList.add(i,dialogDatum);
//                                break;
//                            }
//                        }
//                    }
//                }
            }
        });
    }

    private void noNetWorkInitDefaultAppInfo() {
        binding.mainNoContentTitle.setVisibility(View.VISIBLE);
        binding.mainRecyclerViewApps.setVisibility(View.INVISIBLE);
    }

    // 加载系统应用
    public boolean checkDefaultAPP(String pkgName) {
        List<String> appNames = new ArrayList<>();
        appNames.add("com.readboy.learningplanner");
        appNames.add("com.readboy.aicourse");
        appNames.add("com.readboy.eden.microclass");
        appNames.add("com.readboy.train");

        if("com.readboy.moralslaw".equals(pkgName)) {
            return false;
        }

        if (appNames.contains(pkgName)) {
            return true;
        }
        return false;
    }

    // 获取所有安装的应用 已经安装的应用不包含系统应用，其他被需要的应用添加排除项目
    @SuppressLint("QueryPermissionsNeeded")
    private void getAppInfoAllInstalled() {
        long start = System.currentTimeMillis();
        appInfoList.clear();
        PackageManager manager = requireActivity().getPackageManager();
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        List<PackageInfo> packages = manager.getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            Intent intent = manager.getLaunchIntentForPackage(packageInfo.packageName);
//            Log.d(TAG, "getAppInfoAllInstalled: app no launch name = "+packageInfo.applicationInfo.loadLabel(manager).toString() +",pkgName = " +packageInfo.packageName);

            // 排除没有启动图标的应用
            if (intent == null) {
//                Log.d(TAG, "getAppInfoAllInstalled: app no launch name = "+packageInfo.applicationInfo.loadLabel(manager).toString() +",pkgName = " +packageInfo.packageName);
                continue;
            }
            if("com.readboy.moralslaw".equals(packageInfo.packageName)) {
                continue;
            }
            String appName = packageInfo.applicationInfo.loadLabel(manager).toString();
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || checkDefaultAPP(packageInfo.packageName)) { //排除系统应用(设置，时钟)
                // AppInfo 自定义类，包含应用信息
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(appName);//获取应用名称
                appInfo.setPackageName(packageInfo.packageName); //获取应用包名，可用于卸载和启动应用
                appInfo.setVersionName(packageInfo.versionName);//获取应用版本名
                appInfo.setVersionCode(packageInfo.versionCode);//获取应用版本号
                appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(manager));//获取应用图标
                // 判断应用是否是第三方
                if (isThirdPartyApps(appInfo.getPackageName())) {
                    appInfo.setType(1);
                } else {
                    appInfo.setType(2);
                }
                if (GlobalParam.MY_APP_PACKAGENAME.equals(appInfo.getPackageName())) {
                    continue;
                }
                appInfoList.add(appInfo);
                appInfoAllCache.put(appInfo.getPackageName(), appInfo);
            }
        }
//        String toJson = GsonManager.getInstance().toJson(appInfoList, new TypeToken<List<AppInfo>>() {
//        });
//        Log.d(TAG, "getAppInfoAllInstalled: allAppList = " + toJson);
    }

    public static boolean isThirdPartyApps(String packageName) {
        if (packageName != null &&
                (packageName.startsWith("com.readboy.")
                        || packageName.startsWith("cn.readboy.")
                        || packageName.startsWith("com.dream.")
                        || packageName.startsWith("cn.dream.")
                        || packageName.startsWith("android.dream.")
                        || packageName.startsWith("com.android.")
                        || packageName.equals("org.codeaurora.snapcam")
                        || packageName.equals("com.mediatek.camera")
                        || packageName.equals("com.android.dialer")
                        || packageName.equals("org.codeaurora.dialer"))) {
            return true;
        }
        return false;
    }

    /**
     * 读取缓存
     * aCache.put(GlobalParam.APP_DEFAULT_LIST_CACHE, appDefaultJson);
     * aCache.put(GlobalParam.COURSE_DEFAULT_LIST_CACHE, courseDefaultJson);
     * // 全部添加名单
     * aCache.put(GlobalParam.COURSE_LIST_CACHE, courseMoreJson);
     * aCache.put(GlobalParam.APP_LIST_CACHE, appMoreJson);
     */
    public void readDefaultCache() {
        List<AppInfo> defaultAppList = null;
        List<AppInfo> appList = null;
        List<CourseInfo> appDefaultCourse = null;
        List<CourseInfo> appCourse = null;
        // 读取缓存
        if (aCache != null) {
            // 测试ACache
            String cache1 = aCache.getAsString(GlobalParam.APP_DEFAULT_LIST_CACHE);
//            Log.d(TAG, "readDefaultCache: cache1 =>" + cache1);
            String cache2 = aCache.getAsString(GlobalParam.APP_LIST_CACHE);
            Log.d(TAG, "readDefaultCache: cache2 =>" + cache2);
            String cache3 = aCache.getAsString(GlobalParam.COURSE_DEFAULT_LIST_CACHE);
//            Log.d(TAG, "readDefaultCache: cache3 =>" + cache3);
            String cache4 = aCache.getAsString(GlobalParam.COURSE_LIST_CACHE);
//            Log.d(TAG, "readDefaultCache: cache4 =>" + cache4);
            if (cache1 != null) {
                defaultAppList = GsonManager.getInstance().fromJson(cache1, new TypeToken<List<AppInfo>>() {
                });
            }
            if (cache2 != null) {
                appList = GsonManager.getInstance().fromJson(cache2, new TypeToken<List<AppInfo>>() {
                });
            }
            if (cache3 != null) {
                appDefaultCourse = GsonManager.getInstance().fromJson(cache3, new TypeToken<List<CourseInfo>>() {
                });
            }
            if (cache4 != null) {
                appCourse = GsonManager.getInstance().fromJson(cache4, new TypeToken<List<CourseInfo>>() {
                });
            }
        }

        if (defaultAppList != null) {
            appDefaultList.clear();
            appDefaultList.addAll(defaultAppList);
        }
        if (appDefaultCourse != null) {
            courseDefaultList.clear();
            courseDefaultList.addAll(appDefaultCourse);
        }
        if (appList != null) {
            moreAppDialogData.clear();
            moreAppDialogData.addAll(appList);
        }
        if (appCourse != null) {
            moreClassDialogData.clear();
            moreClassDialogData.addAll(appCourse);
        }
        getIcon();
    }

    //获取图标
    public void getIcon() {
        for (int i = 0; i < appDefaultList.size(); i++) {
            AppInfo appInfo = appDefaultList.get(i);
            if (appInfoAllCache.containsKey(appInfo.getPackageName())) {
                AppInfo info = appInfoAllCache.get(appInfo.getPackageName());
                if (info.getVersionCode() > appInfo.getVersionCode()) {
                    appInfo.setAppName(info.appName);//获取应用名称
                    appInfo.setVersionName(info.versionName);//获取应用版本名
                    appInfo.setVersionCode(info.versionCode);//获取应用版本号
                    appInfo.setAppIcon(info.appIcon);//获取应用图标
                    // 判断应用是否是第三方
                    if (isThirdPartyApps(appInfo.getPackageName())) {
                        appInfo.setType(1);
                    } else {
                        appInfo.setType(2);
                    }
                }
                appInfo.setAppIcon(info.appIcon);//获取应用图标
            }
        }

        for (int i = 0; i < moreAppDialogData.size(); i++) {
            AppInfo appInfo = moreAppDialogData.get(i);
            if (appInfoAllCache.containsKey(appInfo.getPackageName())) {
                AppInfo info = appInfoAllCache.get(appInfo.getPackageName());
                if (info.getVersionCode() > appInfo.getVersionCode()) {
                    appInfo.setAppName(info.appName);//获取应用名称
                    appInfo.setVersionName(info.versionName);//获取应用版本名
                    appInfo.setVersionCode(info.versionCode);//获取应用版本号
                    appInfo.setAppIcon(info.appIcon);//获取应用图标
                    // 判断应用是否是第三方
                    if (isThirdPartyApps(appInfo.getPackageName())) {
                        appInfo.setType(1);
                    } else {
                        appInfo.setType(2);
                    }
                }
                appInfo.setAppIcon(info.appIcon);//获取应用图标
            }
        }
    }

    public void setContentView() {
        binding = FragmentMainBinding.inflate(LayoutInflater.from(getContext()));
        rootView = binding.getRoot();
    }

    int classDialogCount = 0;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_title_right_more: {
//                classMoreDialog.show();
                classMoreDialog2.show();
            }
            break;
            case R.id.main_title_right_more_app: {
                appMoreDialog.show();
            }
            break;
        }
    }

    /**
     * 每次程序结束缓存列表
     * gson 解析出现GC 大概率是序列化问题，查看属性和类有没有不能序列化的
     **/
    public void doSaveListCache() {
        String appDefaultJson = GsonManager.getInstance().toJson(appDefaultList, new TypeToken<List<AppInfo>>() {
        });
        String courseDefaultJson = GsonManager.getInstance().toJson(courseDefaultList, new TypeToken<List<CourseInfo>>() {
        });
        String appMoreJson = GsonManager.getInstance().toJson(moreAppDialogData, new TypeToken<List<AppInfo>>() {
        });
        String courseMoreJson = GsonManager.getInstance().toJson(moreClassDialogData, new TypeToken<List<CourseInfo>>() {
        });
        Log.d(TAG, "doSaveListCache: json " + appMoreJson);
        if (aCache != null) {
            // 首页默认
            aCache.put(GlobalParam.APP_DEFAULT_LIST_CACHE, appDefaultJson);
            aCache.put(GlobalParam.COURSE_DEFAULT_LIST_CACHE, courseDefaultJson, 3 * ACache.TIME_DAY);
            // 全部添加名单
            aCache.put(GlobalParam.COURSE_LIST_CACHE, courseMoreJson);
            aCache.put(GlobalParam.APP_LIST_CACHE, appMoreJson);
        }
    }

    // 默认生命周期一定执行
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        // 重新加载,读取缓存
        doSaveListCache();
        if (classMoreDialog2 != null) {
            if (classMoreDialog2.isShowing()) {
                classMoreDialog2.backStatus();
            }
        }
        if (appMoreDialog != null) {
            appMoreDialog.setStartDelMode(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (msgReceiver != null) {
            requireActivity().unregisterReceiver(msgReceiver);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerMyReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        readDefaultCache();
        // 从网络获取信息
        getInfoFromNet();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // 默认生命周期 【1:activity被杀死,2:commit 新的fragment,3:不一定一定执行（会被系统意外杀死）结束方法写在onPause】
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDialog();
    }

    public void closeDialog() {
        if (appMoreDialog != null) {
            appMoreDialog.dismiss();
        } else if (addAppInfoDialog != null) {
            addAppInfoDialog.dismiss();
        } else if (classMoreDialog2 != null) {
            classMoreDialog2.dismiss();
        }
    }
}
