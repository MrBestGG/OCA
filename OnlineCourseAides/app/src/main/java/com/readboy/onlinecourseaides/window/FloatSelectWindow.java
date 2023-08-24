package com.readboy.onlinecourseaides.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.adapter.SelectCommonAdapter;
import com.readboy.onlinecourseaides.adapter.SelectPointAdapter;
import com.readboy.onlinecourseaides.adapter.SelectSubjectAdapter;
import com.readboy.onlinecourseaides.adapter.SpacesItemDecoration;
import com.readboy.onlinecourseaides.bean.AppInfo;
import com.readboy.onlinecourseaides.bean.BookVersionCache;
import com.readboy.onlinecourseaides.bean.GradeBean;
import com.readboy.onlinecourseaides.bean.PointBean;
import com.readboy.onlinecourseaides.bean.SubjectBean;
import com.readboy.onlinecourseaides.bean.response.BookOneResponse;
import com.readboy.onlinecourseaides.bean.response.BookResponse;
import com.readboy.onlinecourseaides.bean.response.GradeResponse;
import com.readboy.onlinecourseaides.bean.response.SubjectResponse;
import com.readboy.onlinecourseaides.bean.response.VersionResponse;
import com.readboy.onlinecourseaides.bean.response.item.AppBookItem;
import com.readboy.onlinecourseaides.bean.response.item.AppSection;
import com.readboy.onlinecourseaides.bean.response.item.BookOneItem;
import com.readboy.onlinecourseaides.bean.response.item.GradeItem;
import com.readboy.onlinecourseaides.bean.response.item.SubjectItem;
import com.readboy.onlinecourseaides.bean.response.item.VersionItem;
import com.readboy.onlinecourseaides.databinding.FloatToExercise2Binding;
import com.readboy.onlinecourseaides.databinding.FloatToExerciseBinding;
import com.readboy.onlinecourseaides.network.BookLoader;
import com.readboy.onlinecourseaides.utils.GsonManager;
import com.readboy.onlinecourseaides.utils.NetWorkUtils;
import com.readboy.provider.UserDbSearch;
import com.readboy.provider.mhc.info.UserBaseInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

public class FloatSelectWindow extends View {

    private FloatToExerciseBinding binding;
    private FloatToExercise2Binding binding2;

    private final static String TAG = "FloatWindow";
    public static final String SUBJECT = "subject";
    public static final String GRADE = "grade";
    public static final String BOOK = "book"; // 书籍版本
    public static final String POINT = "point";
    public static final String SEMESTER = "semester";

    private final Context mContext; // 声明一个上下文对象
    private final WindowManager wm; // 声明一个窗口管理器对象
    private static WindowManager.LayoutParams wmParams;
    public View mContentView; // 声明一个内容视图对象
    private boolean isShowing = false; // 是否正在显示

    private String uId; // 用户ID

    private int currentGrade = -1;
    // 界面显示数据
    private List<String> grades;
    private List<String> subjects;
    private List<String> books; // 书籍版本
    private List<PointBean> pointx; // 章节目录
    // 选择记录
    private HashMap<String, String> currentRecord;

    // 网络请求返回数据
    private List<GradeItem> gradeItems;
    private List<SubjectItem> subjectItems;
    private List<VersionItem> versionItems;
    // 书本详情
    private BookOneItem bookOneItems;
    // 书本信息 获取bookId 默认通过条件获取一个
    private List<AppBookItem> appBookItems;

    // 信息缓存  由于数据量小仅仅内存缓存，减少网络请求数量
    private List<AppBookItem> bookCacheOne;
    int bookCacheNumbers = 0; // 记录获取书本ID 和获取书本详情 的对应关系
    private List<HashMap<AppBookItem,BookOneItem>> bookCacheTwo; //书本对应的书本详情
    private List<BookVersionCache> bookVersionCacheList; // 版本缓存
    private List<HashMap<String,List<GradeItem>>> gradeItemCacheList; // 版本缓存

    private SelectSubjectAdapter selectSubjectAdapter;
    private SelectCommonAdapter selectGradeAdapter;
    private SelectCommonAdapter selectBookAdapter;
    private SelectPointAdapter selectPointAdapter;

    private Handler handler;
    private MyApplication application;
    // 网络加载类
    private BookLoader bookLoader;

    public FloatSelectWindow(Context context) {
        super(context);
        // 从系统服务中获取窗口管理器，后续将通过该管理器添加悬浮窗
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        mContext = context;
        initData();
    }

    private void initData() {
        grades = new ArrayList<>();
        subjects = new ArrayList<>();
        books = new ArrayList<>();
        List<String> points = new ArrayList<>();
        currentRecord = new HashMap<>();

        bookOneItems = new BookOneItem();
        gradeItems = new ArrayList<>();
        versionItems = new ArrayList<>();
        subjectItems = new ArrayList<>();
        appBookItems = new ArrayList<>();

        bookCacheOne = new ArrayList<>();
        bookCacheTwo = new ArrayList<>();
        bookVersionCacheList = new ArrayList<>();
        gradeItemCacheList = new ArrayList<>();

        handler = new Handler();
        application = MyApplication.getInstances();
        bookLoader = new BookLoader();

        // 初始化记录
        currentRecord.put(SUBJECT,SubjectBean.getSubjectName(0));
        currentRecord.put(GRADE,GradeBean.getGradeName(0));
        currentRecord.put(SEMESTER, "1");
        ArrayList<GradeItem> gradeList = new ArrayList<>();
        GradeItem item = new GradeItem();
        item.setGrade(1);
        item.setName(GradeBean.getGradeName(0));
        item.setSemester(1);
        gradeList.add(item);
        GradeBean.setGradeSource(gradeList);

        getAllInfo();
    }

    public void getAllInfo(){
        //【未登录不处理？】
        boolean userLogin = isUserLogin(getContext());
        UserBaseInfo userInfo = null;
        if(userLogin) {
            UserDbSearch userDbSearch = UserDbSearch.getInstance(getContext());
            userInfo = userDbSearch.getUserInfo();
            currentGrade = userInfo.gradeInt; // 默认从零开始对应一年级
            uId = userInfo.uid+"";
            Log.d(TAG, "getAllInfo: userInfo"+userInfo.toString());
        }else {
            application.showToast("请登录账号");
        }

        // 检测网络状态
        if (!NetWorkUtils.checkNetWorkConnected(getContext())) {
            handler.postDelayed(() -> {
                Toast.makeText(application, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
            }, 200);
        }

        subjects.addAll(Arrays.asList(SubjectBean.SUBJECT_NAME));
        grades.addAll(Arrays.asList(GradeBean.GRADE_NAME));

        pointx = new ArrayList<>();
        ArrayList<PointBean> pointChildren = new ArrayList<>();
        pointChildren.add(new PointBean(1,1+"","暂无内容",null));
        pointx.add(new PointBean(0,1+"","暂无内容",pointChildren));

        books.add("人教版");
        books.add("苏教版");
        books.add("鲁教版");
        books.add("浙教版");

        // 获取请求密钥
        String sn = NetWorkUtils.createCourseSign(uId+"",false);
        //读取本地缓存
        //测试网络请求
//        getAllInfoFromNetWork(sn);
        Log.d(TAG, "getAllInfo: currentGrade =>"+currentGrade);
    }

    private void getAllInfoFromNetWork(String sn) {
        getSubjectListForWork(sn);//【初次加载】
        getGradeListFromNetWork(sn);//【初次加载】
    }

    public void getSubjectListForWork(String sn) {
        bookLoader.getSubjectList(sn).subscribe(new Subscriber<SubjectResponse>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "getSubjectList => onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getSubjectList onError =>"+e.toString());
            }

            @Override
            public void onNext(SubjectResponse subjectResponse) {
                Log.d(TAG, "getSubjectList onNext: book =>"+subjectResponse);
                subjectItems = subjectResponse.getData();
                if(subjectItems == null) {
                    String[] name = SubjectBean.SUBJECT_NAME;
                    for (String s : name) {
                        subjects.add(s);
                    }
                    selectSubjectAdapter.setmData(subjects);
                    return;
                }
                refreshSubjectList();
            }
        });
    }

    // 根据课本id获取课本章节
    public void getSectionListForWork(String sn) {
        AppBookItem item;
        if(appBookItems.size() != 0) {
            item = appBookItems.get(0);
        }else {
            pointx.clear();
            pointx.add(new PointBean(0,1+"","暂无内容",null));
            selectPointAdapter.setmData(pointx);
            return;
        }
        bookLoader.getSectionList(sn,item.getBookId()).subscribe(new Subscriber<BookOneResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getSectionList onError =>"+e.toString());
            }

            @Override
            public void onNext(BookOneResponse subjectResponse) {
                Log.d(TAG, "getSectionList onNext: book =>"+subjectResponse.getData());
                bookOneItems = subjectResponse.getData();
                // 缓存结果
                if(bookOneItems != null){
                    if(bookOneItems.getChildren() != null) {
                        bookCacheTwo.get(bookCacheNumbers).put(item,bookOneItems);
                        bookCacheNumbers++;
                    }
                }else {
                    pointx.clear();
                    pointx.add(new PointBean(0,1+"","暂无内容",null));
                    selectPointAdapter.setmData(pointx);
                    Log.d(TAG, "onNext: pointx=> " + pointx);
                    return;
                }
                refreshSectionList(); // 获取课本章节
            }
        });
    }

    //根据学科获取教材版本 subjectId gradeId
    public void getVersionListFormNetWork(String sn) {
        String s = currentRecord.get(SUBJECT);
        int subjectId = SubjectBean.getId(s) + 1;
        Log.d(TAG, "getGradeListFromNetWork:currentRecord sid=  "+s);
        for (String s1 : SubjectBean.SUBJECT_NAME) {
            Log.d(TAG, "getGradeListFromNetWork: SubjectBean = "+s1);
        }

        String grade = currentRecord.get(GRADE);
        GradeItem gradeItem = GradeBean.getGradeSourceId(grade);
        int gradeId = 1;
        if(gradeItem != null) {
            gradeId = gradeItem.getGrade();
        }
        int semester = GradeBean.getGradeSourceSemester(grade);

        Log.d(TAG, "getVersionListFormNetWork: grade = "+ grade + "gradeId = "+gradeId);
        Log.d(TAG, "getVersionListFormNetWork: subject = "+subjectId);
        Log.d(TAG, "getVersionListFormNetWork: semester = "+semester);
        Log.d(TAG, "getVersionListFormNetWork: sn = "+sn);

        int finalSemester = semester;
        int finalGradeId = gradeId;

        // 读取版本缓存
        if(bookVersionCacheList.size() != 0) {
            for (BookVersionCache cache : bookVersionCacheList) {
                int grade1 = cache.getGrade();
                int subject1 = cache.getSubject();
                if(grade1 == gradeId && subject1 == subjectId) {
                    versionItems = cache.getVersionList();
                    // 刷新教材版本列表
                    refreshVersionList(); //缓存刷新

                    Log.d(TAG, "getVersionListFormNetWork: load version cache =>{gid "+grade1+",sid "+subject1+"}");
                    if(versionItems.size() == 0){
                        return;
                    }
                    currentRecord.put(BOOK,versionItems.get(0).getName());
                    // 获取book
                    getBookListFormNetWork(sn, finalGradeId +"",""+subjectId, finalSemester +"",versionItems.get(0).getId());//【代码标记 => 读取版本缓存 】
                    return;
                }
            }
        }

        // 网络请求
        bookLoader.getVersionList(sn,subjectId, gradeId+"").subscribe(new Subscriber<VersionResponse>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "getVersionListFormNetWork: => onCompleted");
                // 暂时加载第一个，点击切换
                if(versionItems == null) return;
                if(versionItems.size() == 0){
                    return;
                }
                currentRecord.put(BOOK,versionItems.get(0).getName());
                //sn, grade,subject,semester,editionId
                Log.d(TAG, "getVersionListFormNetWork: onCompleted gradeId ="+ finalGradeId
                        + ",subjectId=" + subjectId+ ",version =" + versionItems.get(0)
                        + ",vid =" + versionItems.get(0).getId()+", semester = "+ finalSemester);
                // 获取book
                getBookListFormNetWork(sn, finalGradeId +"",""+subjectId, finalSemester +"",versionItems.get(0).getId());//【代码标记 => 获取版本 】
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getBookList onError =>"+e);
                handler.postDelayed(()->{
                    Toast.makeText(application,"请求失败", Toast.LENGTH_SHORT).show();
                },1000);
            }

            @Override
            public void onNext(VersionResponse versionResponse) {
                Log.d(TAG, "getVersionListFormNetWork: onNext =>"+versionResponse);
                versionItems = versionResponse.getData();

                if(versionItems == null) {
                    books.clear();
                    books.add("暂无内容...");
                    selectBookAdapter.setmData(books);
                    application.showToast("网络异常，请检查");
                    return;
                }
                // 刷新教材版本列表
                refreshVersionList();

                //缓存本次Version
                BookVersionCache cache = new BookVersionCache();
                cache.setGrade(finalGradeId);
                cache.setSubject(subjectId);
                cache.setVersionList(versionItems);
                bookVersionCacheList.add(cache);
            }
        });
    }

    // 根据学科年级教材版本获取课本id
    public void getBookListFormNetWork(String sn,String grade,String subject,String semester, String editionId) {
        //查看缓存结果 加载上次的结果
        if(bookCacheTwo.size() != 0) {
            for (AppBookItem item : bookCacheOne) {
                for (HashMap<AppBookItem, BookOneItem> map : bookCacheTwo) {
                    BookOneItem book = map.get(item);
                    if(book == null) continue;
                    String gid =  String.valueOf(book.getGrade());
                    String sid =  String.valueOf(book.getSubject());
                    String smid =  String.valueOf(book.getSemester());
                    String eid =  String.valueOf(book.getEditionId());
                    if(gid.equals(grade)&&sid.equals(subject)&&smid.equals(semester)&&eid.equals(editionId)){
                        Log.d(TAG, "getBookListFormNetWork: load book Cache => {bookId"+book.getBookId()+",bookName"+book.getBookName()+",versionName"+book.getEditionName());
                        bookOneItems = book;
                        refreshSectionList();// 读取缓存
                        return;
                    }
                }
            }
        }

        bookLoader.getBookList(sn, grade,subject,semester,editionId).subscribe(new Subscriber<BookResponse>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "getBookList: => onCompleted");
                getSectionListForWork(sn); // 【代码查找标记 =>获取课本ID 】
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getBookList onError =>"+e);
                handler.postDelayed(()->{
                    Toast.makeText(application,"请求失败", Toast.LENGTH_SHORT).show();
                },1000);
            }

            @Override
            public void onNext(BookResponse bookBean) {
                Log.d(TAG, "getBookList: onNext =>"+bookBean);
                if(bookBean.getData().size() == 0) {
                    Toast.makeText(application,"找不到相关的内容", Toast.LENGTH_SHORT).show();
                    appBookItems.clear();
                    return;
                }
                appBookItems = bookBean.getData();
                // 书本缓存
                bookCacheOne.add(appBookItems.get(0));
                HashMap<AppBookItem, BookOneItem> map = new HashMap<>();
                map.put(appBookItems.get(0),null);
                bookCacheTwo.add(map);


            }
        });
    }

    // 获取年级，高中是必修
    public void getGradeListFromNetWork(String sn) {
        String s = currentRecord.get(SUBJECT);
        int subjectId = SubjectBean.getId(s) + 1;
        Log.d(TAG, "getGradeListFromNetWork:currentRecord sid=  "+s);
        Log.d(TAG, "getGradeListFromNetWork: SubjectBean = "+SubjectBean.SUBJECT_NAME);
        // 查找缓存
        if(gradeItemCacheList.size() != 0) {
            for (HashMap<String, List<GradeItem>> map : gradeItemCacheList) {
                List<GradeItem> items = map.get(subjectId + "");
                if(items != null) {
                    gradeItems = items;
                    refreshGradeList();
                    currentRecord.put(GRADE,grades.get(0));
                    Log.d(TAG, "getVersionListFormNetWork: load grade cache =>{sid="+subjectId+"}");
                    // 获取请求密钥
                    getVersionListFormNetWork(sn);//【代码标记=> 年级缓存】
                    return;
                }
            }
        }

        bookLoader.getGradeList(sn, subjectId).subscribe(new Subscriber<GradeResponse>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "getGradeList => onCompleted 学科id"+ subjectId);
                // 获取请求密钥
                getVersionListFormNetWork(sn);//【代码标记=> 获取年级】
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getGradeList onError =>"+e.toString());
            }

            @Override
            public void onNext(GradeResponse gradeResponse) {
                Log.d(TAG, "getGradeList onNext: book =>"+gradeResponse);
                gradeItems = gradeResponse.getData();
                if(gradeItems == null) {
                    String[] gradeNames = GradeBean.GRADE_NAME;
                    for (String name : gradeNames) {
                        grades.add(name);
                    }
                    selectGradeAdapter.setmData(grades);
                    return;
                }
                refreshGradeList();
                currentRecord.put(GRADE,grades.get(0));

                // 缓存科目和年级对应关系
                HashMap<String, List<GradeItem>> map = new HashMap<>();
                map.put(subjectId+"", gradeItems);
                gradeItemCacheList.add(map);
            }
        });
    }

    public void refreshSubjectList() {
        subjects.clear();
        for (SubjectItem item : subjectItems) {
            subjects.add(item.getName());
        }

        String[] str = new String[subjects.size()];
        subjects.toArray(str);
        SubjectBean.setSubjectName(str);

        selectSubjectAdapter.setmData(subjects);
    }

    public void refreshSectionList() {
        pointx.clear();
        List<AppSection> children = bookOneItems.getChildren();
//        for (AppSection child : children) {
//
//            if(!child.getHasMapping())
//                continue;
//            ArrayList<PointBean> pointBeans = new ArrayList<>();
//            if(child.getChildren() != null) {
////                Log.d(TAG, "refreshSectionList: child"+child.getName());
//                for (AppSection section : child.getChildren()) {
//                    if(!section.getHasMapping())
//                        continue;
////                    Log.d(TAG, "refreshSectionList: section"+section.getName());
//                    pointBeans.add(new PointBean(PointBean.LEVEL_SECTION,section.getId(),section.getName(),null));
//
//                }
//                pointx.add(new PointBean(PointBean.LEVEL_CHAPTER,child.getId(),child.getName(),pointBeans));
//            }else{
//                if(child.getHasMapping()) {
//                    pointx.add(new PointBean(PointBean.LEVEL_NOCHILDE,child.getId(),child.getName(),null));
//                }
//            }
//        }
//        selectPointAdapter.setmData(pointx);
        Log.d(TAG, "refreshSectionList: children= "+children);
        if(children != null) {
            for (AppSection child : children) {
                if(child == null ||!child.getHasMapping()) continue;

                ArrayList<PointBean> pointList = new ArrayList<>();
                boolean isChild = false;
                if(child.getChildren() != null) {
                    doTransformPointData(child.getChildren(), pointList);
                    isChild = true;
                }else {
                    pointx.add(new PointBean(PointBean.LEVEL_NOCHILDE,child.getId(),child.getName(),null));
                }
                if(isChild) {
                    pointx.add(new PointBean(PointBean.LEVEL_CHAPTER,child.getId(),child.getName(),pointList));
                }
            }
        }
//        String appDefaultJson = GsonManager.getInstance().toJson(pointx, new TypeToken<List<PointBean>>() {
//        });
//        Log.d(TAG, "refreshSectionList: pointx = "+appDefaultJson);
        selectPointAdapter.setmData(pointx);
    }

    public void doTransformPointData(List<AppSection> data, List<PointBean> pointBeans) {
        if(data != null || pointBeans != null) {
            for (AppSection child : data) {
                if(child == null) continue;
                if(!child.getHasMapping()) continue;

                List<PointBean> pointList = new ArrayList<>();
                boolean isChild = false;
                if(child.getChildren() != null) {
                    doTransformPointData(child.getChildren(), pointList);
                    isChild = true;
                }else {
                    pointBeans.add(new PointBean(PointBean.LEVEL_SECTION,child.getId(),child.getName(),null));
                }
                if(isChild) {
                    pointBeans.add(new PointBean(PointBean.LEVEL_CHAPTER,child.getId(),child.getName(),pointList));
                }
            }
        }
    }

    public void refreshGradeList() {
        grades.clear();
        for (GradeItem item : gradeItems) {
            grades.add(item.getName());
        }
        GradeBean.setGradeSource(gradeItems);
        selectGradeAdapter.setCurrentId(0);
        selectGradeAdapter.setmData(grades);
    }

    public void refreshVersionList() {
        books.clear();
        for (VersionItem item : versionItems) {
            books.add(item.getName());
        }
        String[] str = new String[books.size()];
        books.toArray(str);
        selectBookAdapter.setCurrentId(0);
        selectBookAdapter.setmData(books);
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

    private boolean ORIENTATION_LANDSCAPE = true;

    // 设置悬浮窗的内容布局
    public void setLayout() {
        // 从指定资源编号的布局文件中获取内容视图对象
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            binding = FloatToExerciseBinding.inflate(LayoutInflater.from(mContext));
            mContentView = binding.getRoot();
            ORIENTATION_LANDSCAPE = true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            binding2 = FloatToExercise2Binding.inflate(LayoutInflater.from(mContext));
            mContentView = binding2.getRoot();
            ORIENTATION_LANDSCAPE = true;
        }
        initView();
    }

    private int isSubject = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initView() {
        selectSubjectAdapter = new SelectSubjectAdapter(mContext,subjects,index->{
            String item = selectSubjectAdapter.getItem(index);
            // 保存科目记录
            currentRecord.put(SUBJECT, item);
            Log.d(TAG, "initView: selectSubjectAdapter =>"+item);
            // 获取请求密钥
            String sn = NetWorkUtils.createCourseSign(uId+"",false);
            isSubject = 1;
            getGradeListFromNetWork(sn);//【代码标记=> 科目选择适配器】
        });
        selectGradeAdapter = new SelectCommonAdapter(mContext, grades,index->{
            String item = selectGradeAdapter.getItem(index);
            //保存年级记录
            currentRecord.put(GRADE, item);
            Log.d(TAG, "initView: selectGradeAdapter =>"+item);
            // 获取请求密钥
            String sn = NetWorkUtils.createCourseSign(uId+"",false);
            getVersionListFormNetWork(sn);//【代码标记=> 年级选择适配器】
        });
        // 注意这个是版本选择适配器
        selectBookAdapter = new SelectCommonAdapter(mContext, books, index->{
            String item = selectBookAdapter.getItem(index);
            String edition_id = null;

            for (VersionItem versionItem : versionItems) {
                if(versionItem.getName().equals(item)){
                    edition_id = versionItem.getId();
                    break;
                }
            }

            // 保存书本版本ID
            currentRecord.put(BOOK, edition_id);
            Log.d(TAG, "initView: selectBookAdapter =>"+item);
            // 获取请求密钥
            String sn = NetWorkUtils.createCourseSign(uId+"",false);
            String s = currentRecord.get(SUBJECT);
            int subjectId = SubjectBean.getId(s) + 1;
            String grade = currentRecord.get(GRADE);
            GradeItem gradeItem = GradeBean.getGradeSourceId(grade);
            int gradeId = gradeItem.getGrade();
            int semester = gradeItem.getSemester();

            //sn, grade,subject,semester,editionId
            Log.d(TAG, "selectBookAdapter: ItemOnClick => gradeId ="
                    + gradeId + ",subjectId=" + subjectId+ ",vid =" + edition_id+", semester = "+semester);
            getBookListFormNetWork(sn,gradeId+"","" +subjectId,semester+"",edition_id); //【代码标记 => 版本适配器 】
        });
        selectPointAdapter = new SelectPointAdapter(mContext, pointx, index->{
            PointBean item = selectPointAdapter.getItem(index);
            // 保存章节id记录
            currentRecord.put(POINT, ""+item.getId());
            Log.d(TAG, "initView: item =>"+item.toString());
            //保存book id
            currentRecord.put("book_id", bookOneItems.getBookId());

            mListener.onFloatClick(item.getId(), bookOneItems.getBookId());
        });

        //是否横屏
//        binding.floatExercisesClass.setAdapter(selectSubjectAdapter);
//        binding.floatExercisesGrade.setAdapter(selectGradeAdapter);
//        binding.floatExercisesBook.setAdapter(selectBookAdapter);
//        binding.floatExercisesPoint.setAdapter(selectPointAdapter);

        RecyclerView floatExercisesClass =(RecyclerView) mContentView.findViewById(R.id.float_exercises_class);
        RecyclerView floatExercisesGrade =(RecyclerView) mContentView.findViewById(R.id.float_exercises_grade);
        RecyclerView floatExercisesBook =(RecyclerView) mContentView.findViewById(R.id.float_exercises_book);
        RecyclerView floatExercisesPoint =(RecyclerView) mContentView.findViewById(R.id.float_exercises_point);
        floatExercisesClass.setAdapter(selectSubjectAdapter);
        floatExercisesGrade.setAdapter(selectGradeAdapter);
        floatExercisesBook.setAdapter(selectBookAdapter);
        floatExercisesPoint.setAdapter(selectPointAdapter);

        // 必须一个ListView一个管理器
        LinearLayoutManager subjectManager = new LinearLayoutManager(mContext);
        subjectManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager commonManager1 = new LinearLayoutManager(mContext);
        LinearLayoutManager commonManager2 = new LinearLayoutManager(mContext);
        LinearLayoutManager commonManager3 = new LinearLayoutManager(mContext);
        commonManager1.setOrientation(LinearLayoutManager.VERTICAL);
        commonManager2.setOrientation(LinearLayoutManager.VERTICAL);
        commonManager3.setOrientation(LinearLayoutManager.VERTICAL);

        floatExercisesClass.setLayoutManager(subjectManager);
        floatExercisesGrade.setLayoutManager(commonManager1);
        floatExercisesBook.setLayoutManager(commonManager2);
        floatExercisesPoint.setLayoutManager(commonManager3);
        mContentView.findViewById(R.id.float_select_do_content).setOnClickListener(v->{
//            show();
        });
        //FloatWin 实现类似Dialog 点击外部消失效果
        mContentView.findViewById(R.id.float_select_content).setOnClickListener(v->{
            if(isShow()){
                close();
            }
        });

        if(currentGrade != -1) {
            selectGradeAdapter.setCurrentId(currentGrade);
        }
    }

    private boolean isInViewArea(View view, float x, float y) {
        Log.d(TAG,"点击的坐标  x " + x + "y " + y);
        Rect r = new Rect();
        view.getLocalVisibleRect(r);
        if (x > r.left && x < r.right && y > r.top && y < r.bottom) {
            return true;
        }
        return false;
    }

    public void changeMode() {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            ORIENTATION_LANDSCAPE = true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            ORIENTATION_LANDSCAPE = false;
        }
        setLayout();
    }

    // 显示悬浮窗
    public void show() {
        changeMode();

        currentRecord.put(SUBJECT,SubjectBean.getSubjectName(0));
        currentRecord.put(GRADE,GradeBean.getGradeName(0));
        currentRecord.put(SEMESTER, "1");
        selectSubjectAdapter.setCurrentId(0);
        selectSubjectAdapter.refresh();
        // 获取请求密钥
        String sn = NetWorkUtils.createCourseSign(uId+"",false);
        //读取本地缓存
        //测试网络请求
        getAllInfoFromNetWork(sn);

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
        wmParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wmParams.x = 0;
        wmParams.y = 0;
        // 设置悬浮窗的宽度和高度为自适应
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        // 添加自定义的窗口布局，然后屏幕上就能看到悬浮窗了
        // 避免重复添加
        if(!isShow()) {
            wm.addView(mContentView, wmParams);
        }
        isShowing = true;
    }

    // 关闭悬浮窗
    public void close() {
        currentRecord = new HashMap<>();
        if (mContentView != null && isShowing) {
            // 移除自定义的窗口布局
            wm.removeView(mContentView);
            isShowing = false;
        }
    }

    // 判断悬浮窗是否打开
    public boolean isShow() {
        return isShowing;
    }

    private FloatSelectClickListener mListener; // 声明一个悬浮窗的点击监听器对象
    // 设置悬浮窗的点击监听器
    public void setOnFloatListener(FloatSelectClickListener listener) {
        mListener = listener;
    }

    // 定义一个悬浮窗的点击监听器接口，用于触发点击行为
    public interface FloatSelectClickListener {
        void onFloatClick(String sectionId,String bookId);
    }

    public HashMap<String, String> getCurrentRecord() {
        return currentRecord;
    }
}
