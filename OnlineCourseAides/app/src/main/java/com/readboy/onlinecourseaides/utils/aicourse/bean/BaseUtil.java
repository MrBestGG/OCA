package com.readboy.onlinecourseaides.utils.aicourse.bean;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.utils.Logger;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Ability;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.question.QuestionBean;
import com.readboy.provider.UserDbSearch;
import com.readboy.provider.mhc.info.BookClassInfo;
import com.readboy.provider.mhc.info.UserBaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {

    /////////////////////////////////////////////////////////////////////////////////////////////
    ///                                                                                       ///
    ///                                      基础方法                                          ///
    ///                                                                                       ///
    /////////////////////////////////////////////////////////////////////////////////////////////

    /** 获取上下文 */
    public static Context getContext() {
        return MyApplication.getInstances();
    }

//    /**得到主线程的Id*/
//    public static long getMainThreadId() {
//        return BasePureApplication.getMainThreadId();
//    }
//
//    /**得到主线程的hanlder (仅用于特殊情况) */
//    public static Handler getMainThreadHandler() {
//        return BasePureApplication.getHandler();
//    }

    /**安全的执行一个task  (仅用于特殊情况) */
//    public static void postTaskSafely(Runnable task) {
//        // 当前线程==子线程,通过消息机制,把任务交给主线程的Handler去执行
//        // 当前线程==主线程,直接执行任务
//        int curThreadId = android.os.Process.myTid();
//        long mainThreadId = getMainThreadId();
//        if (curThreadId == mainThreadId) {
//            task.run();
//        } else {
//            getMainThreadHandler().post(task);
//        }
//    }

//    /**返回ObjectMapper*/
//    public static ObjectMapper getObjectMapper() {
//        return BaseConstant.sObjMapper;
//    }

    public static String sTimeRecorderTag;
    public static long   sTimeRecorder;

    /**开始耗时记录*/
    public static void startTimeRecorder(boolean log) {
        startTimeRecorder("", log);
    }

    public static void startTimeRecorder(String tagName, boolean log) {
        if (log) {
            sTimeRecorder = System.currentTimeMillis();
            sTimeRecorderTag = tagName;
        }
    }

    /**结束耗时记录*/
//    public static void endTimeRecorder(boolean log) {
//        if (log) {
//            Logger.d("totalTime (" + sTimeRecorderTag + ") : " + (System.currentTimeMillis() - sTimeRecorder));
//            sTimeRecorder = System.currentTimeMillis();
//        }
//    }

    /**获取点击放大缩小动画集*/
    public static AnimatorSet getPressAnimatorSet(View view) {
        float value1 = 1f;
        float value2 = 0.93f;
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", value1, value2);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", value1, value2);
        ObjectAnimator restoreAnim1 = ObjectAnimator.ofFloat(view, "scaleX", value2, value1);
        ObjectAnimator restoreAnim2 = ObjectAnimator.ofFloat(view, "scaleY", value2, value1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(anim1).with(anim2);
        animatorSet.play(restoreAnim1).after(anim1).with(restoreAnim2);
        animatorSet.setDuration(200);

        return animatorSet;
    }

//    public static boolean startPlayVideos(Context context, int index, long position, VideoBean... tutorVideoBeans) {
//        boolean res = false;
//        if (null != tutorVideoBeans && tutorVideoBeans.length > 0) {
//            ArrayList<String> path = new ArrayList<>();
//            ArrayList<String> title = new ArrayList<>();
//            for (int i = 0; i < tutorVideoBeans.length; i++) {
//                VideoBean tutorVideoBean = tutorVideoBeans[i];
//                path.add(tutorVideoBean.getVid());
//                title.add(tutorVideoBean.getName());
//            }
//            res = startPlayVideos(context, index, position, path, title);
//        }
//        return res;
//    }

    /**
     * 开启阿里云视频全屏播放
     * @param context
     * @param index 播放path的第几个
     * @param position 播放的时间位置 unit=ms
     * @param path 视频vid列表
     * @param title 视频标题列表
     * @return
     */
//    public static boolean startPlayVideos(Context context, int index, long position, ArrayList<String> path, ArrayList<String> title) {
//        boolean res;
//        Intent intent = new Intent(context, PlayerActivity.class);
//        intent.putExtra("domain", BaseConstant.NAME_ALI_VIDEO_DOMAIN);
//        intent.putExtra("path", path);
//        intent.putExtra("title", title);
//        intent.putExtra("index", index);
//        intent.putExtra("position", position);
//        if (context instanceof Activity) {
//            res = true;
//            ActivityUtils.startActivity(((Activity) context), intent);
//        }
//        else {
//            res = ActivityUtils.startActivity(intent);
//        }
//        return res;
//    }

    /**加载webp*/
//    public static void loadWebp(AppCompatImageView imageView, String url) {
//        Transformation<Bitmap> transformation = new CenterInside();
//        Glide.with(imageView)
//                .load(url)
//                .optionalTransform(transformation)
//                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
//                .into(imageView);
//    }

    /**打印屏幕信息*/
//    public static void printResolution(Context context) {
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int height = dm.heightPixels;
//        int width = dm.widthPixels;
//        int sw = context.getResources().getConfiguration().smallestScreenWidthDp;
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("屏幕参数打印 : ");
//        stringBuilder.append("width : " + width);
//        stringBuilder.append("\nheight : " + height);
//        stringBuilder.append("\nDensityDpi : " + dm.densityDpi);
//        stringBuilder.append("\nDensity : " + dm.density);
//        stringBuilder.append("\nScaledDensity : " + dm.scaledDensity);
//        stringBuilder.append("\nsw : " + sw);
//        Logger.d(stringBuilder.toString());
//    }

    /**显示默认加载对话框*/
//    public static ProgressDialog showDefLoadingDialog(Context context) {
//        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.show();
//        if (progressDialog.getWindow() != null) {
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        progressDialog.setContentView(R.layout.layout_dialog_def_progress);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        return progressDialog;
//    }

    // 字符串转为MD5
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();

            for (int i = 0, v = 0; i < messageDigest.length; i++) {
                v = 0xFF & messageDigest[i];
                if (v < 16) {
                    hexString.append("0");
                }
                hexString.append(Integer.toHexString(v));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

//    // 获取资源图片的大小
//    public static int[] getResImageSize(Context context, int resId) {
//        BitmapFactory.Options dimensions = new BitmapFactory.Options();
//        dimensions.inJustDecodeBounds = true;
//        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), resId, dimensions);
//        int height = dimensions.outHeight;
//        int width =  dimensions.outWidth;
//        return new int[] { width, height };
//    }
//
//    public static boolean startRBStoreAppPage(Context context, String packageName) {
//        return gotoAppStore(context, packageName);
//    }
//
//    public static CheckImpl initForceUpdate(Activity activity){
//        ApUpdateFactory factory = new ApUpdateFactory();
//        CheckImpl mIUpudate = factory.createDefaultCheck();
//        mIUpudate.isShowDialogWhenNormalUpdate(false);
//        mIUpudate.startCheck(activity);
//        return mIUpudate;
//    }
//
//    public static void destroyForceUpdate(CheckImpl mIUpudate){
//        if(mIUpudate != null){
//            mIUpudate.releaseUpdate();
//            mIUpudate = null;
//        }
//    }
//
//    /** 改变Setting系统值 */
//    public static int editSystemValue(Context context, String keyName, String KeyVal) {
//        int result = 0;
//        String actionName = BaseConstant.ACTION_NAME_SYSTEM_SETTING;
//        String tableName = BaseConstant.TABLE_NAME_SYSTEM_SETTING;
//
//        Intent intent1 = new Intent(actionName);
//        intent1.putExtra(BaseConstant.COLUMN_NAME_SYSTEM_SETTING_TABLE_NAME, tableName);
//        intent1.putExtra(BaseConstant.COLUMN_NAME_SYSTEM_SETTING_KEY_NAME, keyName);
//        intent1.putExtra(BaseConstant.COLUMN_NAME_SYSTEM_SETTING_KEY_VALUE, KeyVal);
//
//        boolean isSupportActivity = isActionSupportActivity(context, actionName);
//        if (null == context || !(context instanceof Activity)) {
//            isSupportActivity = false;
//        }
//        if (isSupportActivity) {
//            try {
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent1);
//                result = 1;
//            } catch (Exception e) {
//                try {
//                    context.sendBroadcast(intent1);
//                    result = 2;
//                } catch (Exception ee) {
//                    ee.printStackTrace();
//                }
//            }
//        } else {
//            try {
//                context.sendBroadcast(intent1);
//                result = 2;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }

//    private static boolean isActionSupportActivity(Context context,String action){
//        final PackageManager packageManager = context.getPackageManager();
//        final Intent intent = new Intent(action);
//        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, 0);
//        if (resolveInfo.size() > 0) {
//            return true;
//        }
//        return false;
//    }
//
//    public static String getSubjectStr(int subject) {
//        if (subject == BaseConstant.SUBJECT_ID_CHINESE) { return "语文"; }
//        else if (subject == BaseConstant.SUBJECT_ID_MATH) { return "数学"; }
//        else if (subject == BaseConstant.SUBJECT_ID_ENGLISH) { return "英语"; }
//        else if (subject == BaseConstant.SUBJECT_ID_PHYSICS) { return "物理"; }
//        else if (subject == BaseConstant.SUBJECT_ID_CHEMISTRY) { return "化学"; }
//        else if (subject == BaseConstant.SUBJECT_ID_BIOLOGY) { return "生物"; }
//        else if (subject == BaseConstant.SUBJECT_ID_POLITICS) { return "政治"; }
//        else if (subject == BaseConstant.SUBJECT_ID_HISTORY) { return "历史"; }
//        else if (subject == BaseConstant.SUBJECT_ID_GEOGRAPHY) { return "地理"; }
//        else if (subject == BaseConstant.SUBJECT_ID_SCIENCE) { return "科学"; }
//        else { return "其他"; }
//    }

    public static String getGradeStr(int grade) {
        if (grade == 1) { return "一年级"; }
        if (grade == 2) { return "二年级"; }
        if (grade == 3) { return "三年级"; }
        if (grade == 4) { return "四年级"; }
        if (grade == 5) { return "五年级"; }
        if (grade == 6) { return "六年级"; }
        if (grade == 7) { return "七年级"; }
        if (grade == 8) { return "八年级"; }
        if (grade == 9) { return "九年级"; }
        if (grade == 10) { return "高一"; }
        if (grade == 11) { return "高二"; }
        if (grade == 12) { return "高三"; }
        else { return "全年级"; }
    }

    public static boolean isAppInstalled(Context context, String pkg) {
        boolean isInstalled = false;
        if (context != null && !TextUtils.isEmpty(pkg)) {
            PackageManager pm = context.getPackageManager();
            try {
                isInstalled = pm.getPackageInfo(pkg, 0) != null;
            } catch (PackageManager.NameNotFoundException e) {
            } catch (Exception e) {

            }
        }
        return isInstalled;
    }

    public static boolean gotoAppStore(Context context, String packageName) {
        try {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName("cn.dream.android.appstore",
                    "cn.dream.android.appstore.ui.activity.AppDetailActivity_");
            intent.setComponent(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pkg", packageName);
            intent.putExtra("type", "auto_download");//自动开始下载
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getAppVersionCode(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return 0;
    }

//    public static void setShadowDrawable(View view,
//                                         int shapeRadius,
//                                         int shadowColor,
//                                         int shadowRadius,
//                                         int offsetX,
//                                         int offsetY) {
//        ShadowDrawable drawable = new ShadowDrawable.Builder()
//                .setShapeRadius(shapeRadius)
//                .setShadowColor(shadowColor)
//                .setShadowRadius(shadowRadius)
//                .setOffsetX(offsetX)
//                .setOffsetY(offsetY)
//                .builder();
//        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        ViewCompat.setBackground(view, drawable);
//    }

    public static void startAnimDownUp(View view, float value, long duration) {
        if (null != view && null != view.getContext()) {
            Context context = view.getContext();
            ObjectAnimator downAnim = ObjectAnimator.ofFloat(view, "translationY", 0F,
                    value, 0F);
            ObjectAnimator upAnim = ObjectAnimator.ofFloat(view, "translationY", 0F,
                    -value, 0F);
            downAnim.setDuration(duration);
            upAnim.setDuration(duration);
            downAnim.setInterpolator(new LinearInterpolator());
            upAnim.setInterpolator(new LinearInterpolator());
            downAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        if (activity.isFinishing() || activity.isDestroyed()) {
                            upAnim.removeAllListeners();
                            return;
                        }
                    }
                    // 向下动画结束，启动向上动画
                    upAnim.start();
                }
            });
            upAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        if (activity.isFinishing() || activity.isDestroyed()) {
                            upAnim.removeAllListeners();
                            return;
                        }
                    }
                    // 向上动画结束，启动向下动画
                    downAnim.start();
                }
            });
            downAnim.start();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    ///                                                                                       ///
    ///                                      数据操作                                          ///
    ///                                                                                       ///
    /////////////////////////////////////////////////////////////////////////////////////////////

    public static int createRandomIndex(Collection collection) {
        int index = -1;
        if (null != collection && collection.size() > 0) {
            index = (int) (Math.random() * collection.size());
        }
        return index;
    }

//    public static <T> T[] shuffle(T[] arr) {
//        if (null != arr) {
//            int length = arr.length;
//            for (int i = length; i > 0; i--) {
//                int randInd = BaseConstant.sRandom.nextInt(i);
//                swap(arr, randInd, i - 1);
//            }
//        }
//        return arr;
//    }

    public static <T> void swap(T[] a, int i, int j){
        if (null != a) {
            T temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }

    public static String object2String(Parcelable stu) {
        // 1.序列化
        Parcel p = Parcel.obtain();
        stu.writeToParcel(p, 0);
        byte[] bytes = p.marshall();
        p.recycle();
        // 2.编码
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    public static JSONArray strListToJsonArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (!TextUtils.isEmpty(list.get(i))) {
                    jsonArray.put(list.get(i));
                }
            }
        }
        return jsonArray;
    }

    public static JSONArray longListToJsonArray(List<Long> list) {
        JSONArray jsonArray = new JSONArray();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                jsonArray.put(getLong(list.get(i)));
            }
        }
        return jsonArray;
    }

    public static JSONArray accessoryListToJsonArray(List<QuestionBean.Accessory> list) {
        JSONArray jsonArray = new JSONArray();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != list.get(i)) {
                    jsonArray.put(list.get(i).toJSON());
                }
            }
        }
        return jsonArray;
    }

    public static JSONArray abilityListToJsonArray(List<Ability> list) {
        JSONArray jsonArray = new JSONArray();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != list.get(i)) {
                    jsonArray.put(list.get(i).toJSON());
                }
            }
        }
        return jsonArray;
    }

//    /**使用Jackson解析jsonList*/
//    public static <T> List<T> fromJacksonList(String json, Class<T> clz) {
//        List<T> result = null;
//        if (!TextUtils.isEmpty(json)) {
//            ObjectMapper mapper = getObjectMapper();
//            try {
//                result = mapper.readValue(json,
//                        mapper.getTypeFactory().constructCollectionType(List.class, clz));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//
//    }
//
//    /**使用Jackson解析json*/
//    public static <T> T fromJackson(String json, Class<T> clz) {
//        T result = null;
//        if (!TextUtils.isEmpty(json)) {
//            ObjectMapper mapper = getObjectMapper();
//            try {
//                result = mapper.readerFor(clz).readValue(json);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    /**使用Jackson转jsonString*/
//    public static String toJson(Object obj) {
//        String result = null;
//        if (null != obj) {
//            ObjectMapper mapper = getObjectMapper();
//            try {
//                result = mapper.writeValueAsString(obj);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        } else {
//            result = "";
//        }
//        return result;
//    }
//
//    public static JSONObject toJsonObj(Object obj) {
//        String result = null;
//        ObjectMapper mapper = getObjectMapper();
//        try {
//            result = mapper.writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        try {
//            return null != result ? new JSONObject(result) : null;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static JSONArray toJsonsObj(List objs) {
//        JSONArray jsonArray = null;
//        ObjectMapper mapper = getObjectMapper();
//        if (null != objs && objs.size() > 0) {
//            jsonArray = new JSONArray();
//            for (int i = 0; i < objs.size(); i++) {
//                Object obj = objs.get(i);
//                if (null != obj) {
//                    try {
//                        String result = mapper.writeValueAsString(obj);
//                        try {
//                            JSONObject jsonObject = new JSONObject(result);
//                            jsonArray.put(jsonObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return jsonArray;
//    }

    public static String getString(String s) { return null == s ? "" : s; }

    public static long getLong(Long l) {
        return getLong(l, 0L);
    }

    public static long getLong(Long l, long def) {
        return l == null ? def : l;
    }

    public static int getInt(Integer l) {
        return getInt(l, 0);
    }

    public static int getInt(Integer l, int def) {
        return l == null ? def : l;
    }

    public static float getFloat(Float l) {
        return l == null ? 0f : l;
    }

    public static Double getDouble(Double l) {
        return l == null ? 0d : l;
    }

    public static Boolean getBoolean(Boolean b) { return b == null ? false : b; }

    public static Long strToLong(String str) {
        return strToLong(str, 0L);
    }

    public static Integer strToInteger(String str) {
        return strToInteger(str, 0);
    }

    public static Float strToFloat(String str) {
        return strToFloat(str, 0f);
    }

    public static Double strToDouble(String str) {
        return strToDouble(str, 0d);
    }

    public static Long strToLong(String str, long def) {
        Long res = def;
        if (!TextUtils.isEmpty(str)) {
            try {
                res = Long.parseLong(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Integer strToInteger(String str, int def) {
        Integer res = def;
        if (!TextUtils.isEmpty(str)) {
            try {
                res = Integer.parseInt(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Float strToFloat(String str, float def) {
        Float res = def;
        if (!TextUtils.isEmpty(str)) {
            try {
                res = Float.parseFloat(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Double strToDouble(String str, double def) {
        Double res = def;
        if (!TextUtils.isEmpty(str)) {
            try {
                res = Double.parseDouble(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Long intToLong(Integer l) {
        if (null == l) l = 0;
        return l.longValue();
    }

    public static Integer longToInteger(Long l) {
        if (null == l) l = 0l;
        return l.intValue();
    }

    public static Long doubleToLong(Double d) {
        if (null == d) d = 0d;
        return d.longValue();
    }
	
    public static Integer doubleToInteger(Double d) {
        if (null == d) d = 0d;
        return d.intValue();
    }

    public static List<Long> strToLongList(List<String> list) {
        List<Long> res = null;
        if (null != list) {
            res = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                res.add(strToLong(list.get(i)));
            }
        }
        return res;
    }

    public static List<Integer> strToIntList(List<String> list) {
        List<Integer> res = null;
        if (null != list) {
            res = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                res.add(strToInteger(list.get(i)));
            }
        }
        return res;
    }

    public static List<Integer> longToIntList(List<Long> list) {
        List<Integer> res = null;
        if (null != list) {
            res = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                res.add(longToInteger(list.get(i)));
            }
        }
        return res;
    }

    public static boolean isEmptyText(String str) {
        return TextUtils.isEmpty(str) || "null".equals(str);
    }

    public static boolean isEmptyNumText(String str) {
        if (!TextUtils.isEmpty(str)) {
            Long value = null;
            try {
                value = Long.parseLong(str);
                return value <= 0;
            } catch (Exception e) {
            }
        }
        return true;
    }


    //乘
    public static double getMultiply(double x,double y){
        BigDecimal x1 = new BigDecimal(Double.valueOf(x));
        BigDecimal y1 = new BigDecimal(Double.valueOf(y));
        return x1.multiply(y1).doubleValue();
    }

    //除
    public static double getDivide(double x,double y){
        if (y == 0) {
            return 0;
        }
        BigDecimal x1 = new BigDecimal(Double.valueOf(x));
        BigDecimal y1 = new BigDecimal(Double.valueOf(y));
        return x1.divide(y1,3,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    public static String getSerial(){
//        String serial = "";
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
//            serial = Build.SERIAL;
//        }else {
//            try {
//                serial = Build.getSerial();
//            }catch (SecurityException e){
//                e.printStackTrace();
//            }
//        }
//        if(TextUtils.isEmpty(serial)){
//            serial = "unKnowSerial";
//        }
//        Log.v("hqb", "hqb__getSerial__serial = " + serial);
//        return serial;
////        return "7d183960bf4c";
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    ///                                                                                       ///
    ///                                      家长管理                                          ///
    ///                                                                                       ///
    /////////////////////////////////////////////////////////////////////////////////////////////

    private static ArrayList<String> sBlacklist;

//    /**开启家长管理密码输入窗口*/
//    public static boolean startPMInputPwdForResult(Activity activity, int requestCode) {
//        if (null == activity || activity.isFinishing() || activity.isDestroyed()) {
//            return false;
//        }
//        try {
//            // 调用家长管理
//            Intent intent = new Intent();
//            intent.setAction(BaseConstant.ACTION_PM_INPUT_PWD);
//            activity.startActivityForResult(intent, requestCode);
//            return true;
//        } catch (ActivityNotFoundException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**处理开启家长管理密码输入窗口的结果*/
//    public static void onPMInputPwdActivityResult(int requestCode, int resultCode, Intent data, int pmRequestCode, OnPMInputPwdCallback onPMInputPwdCallback) {
//        if (requestCode == pmRequestCode) {
//            if (resultCode == BaseConstant.RESULT_CODE_PASSWORD_MANAGER_ERROR) {
//                if (null != onPMInputPwdCallback) {
//                    onPMInputPwdCallback.onPwdError();
//                }
//            }
//            else if (resultCode == BaseConstant.RESULT_CODE_PASSWORD_MANAGER_CORRECT) {
//                if (null != onPMInputPwdCallback) {
//                    onPMInputPwdCallback.onPwdCorrect();
//                }
//            }
//            else if (resultCode == BaseConstant.RESULT_CODE_PASSWORD_MANAGER_EMPTY) {
//                if (null != onPMInputPwdCallback) {
//                    onPMInputPwdCallback.onPwdEmpty();
//                }
//            }
//            else if (resultCode == BaseConstant.RESULT_CODE_PASSWORD_MANAGER_EXIST) {
//                if (null != onPMInputPwdCallback) {
//                    onPMInputPwdCallback.onPwdExistCreate();
//                }
//            }
//        }
//    }
//
//    /**应用是否被管控*/
//    public static boolean isPackageForbidden(Context context, String packageName) {
//        //Log.v("hqb", "hqb__pm__isPackageForbidden__packageName = " + packageName);
//        //filter launcher
//        if (TextUtils.isEmpty(packageName)) {
//            //Log.v("hqb", "hqb__pm__isPackageForbidden__false1");
//            return false;
//        }
//        getBlackList(context);
//        if (sBlacklist != null) {
//            //Log.v("hqb", "hqb__pm__isPackageForbidden__mBlacklist.size() = " + mBlacklist.size());
//            if (sBlacklist.contains(packageName)) {
//                //Log.v("hqb", "hqb__pm__isPackageForbidden__true2");
//                return true;
//            }
//        }
//        //Log.v("hqb", "hqb__pm__isPackageForbidden__false3");
//        return false;
//    }

    /**获取管控列表*/
//    private static void getBlackList(Context context) {
//        if (null == sBlacklist) {
//            sBlacklist = new ArrayList<>();
//        }
//        //Log.v("hqb", "hqb__pm__getBlackList");
//        ContentResolver resolver = context.getContentResolver();
//        sBlacklist.clear();
//        Cursor cursor = null;
//        try {
//            cursor = resolver.query(BaseConstant.DB_PM_LIMITED_URI, null, null, null, null);
//            if (cursor != null && cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    int col = cursor.getColumnIndex("package_name");
//                    if (col != -1) {
//                        String pkgName = cursor.getString(col);
//                        sBlacklist.add(pkgName);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//                cursor = null;
//            }
//        }
//    }

    public interface OnPMInputPwdCallback {
        void onPwdError();
        void onPwdCorrect();
        void onPwdEmpty();
        void onPwdExistCreate();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    ///                                                                                       ///
    ///                                      个人中心                                          ///
    ///                                                                                       ///
    /////////////////////////////////////////////////////////////////////////////////////////////

    public static UserBaseInfo getUserBaseInfo() {
        UserDbSearch userDbSearch = UserDbSearch.getInstance(getContext());
        UserBaseInfo userBaseInfo = null;
        try {
            userBaseInfo = null == userDbSearch ? null : userDbSearch.getUserInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBaseInfo;
    }

    /**获取用户uid*/
    public static int getUid() {
        UserBaseInfo userBaseInfo = getUserBaseInfo();
        return null != userBaseInfo ? userBaseInfo.uid : 0;
    }

    /**用户是否登录*/
    public static boolean isUserLogin() {
        return isUserLogin(getUserBaseInfo());
    }

    public static boolean isUserLogin(UserBaseInfo userBaseInfo) {
        boolean isUserLogin = false;
        if (null != userBaseInfo) {
            isUserLogin = userBaseInfo.uid > 0;
        }
        return isUserLogin;
    }

    /**用户是否登录或者登录过期*/
    public static boolean isUserLoginExpired() {
        return isUserLoginExpired(getUserBaseInfo());
    }

    public static boolean isUserLoginExpired(UserBaseInfo userBaseInfo) {
        boolean isUserLoginExpired = false;
        if (null != userBaseInfo) {

            if (userBaseInfo.uid <= 0) {
                isUserLoginExpired = true;
            }
            else if (TextUtils.isEmpty(userBaseInfo.token)) {
                isUserLoginExpired = true;
            }
            else  {
                long currentTime = Math.round(System.currentTimeMillis() / 1000d);
                if (userBaseInfo.tokenExpire >= currentTime) {
                    // 不判断userBaseInfo.tokenExpire=0的异常情况
                    isUserLoginExpired = true;
                }
            }
        }
        else {
            isUserLoginExpired = true;
        }
        return isUserLoginExpired;
    }

    /**判断uid是否合法*/
    public static boolean isUidValid(String uid) {
        boolean res = false;
        if (!TextUtils.isEmpty(uid)) {
            try {
                res = isUidValid(Long.parseLong(uid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static boolean isUidValid(long uid) {
        return uid > 0;
    }

    /**获取个人中心头像地址*/
    public static String getAvatarUrl() {
        String photoUri = "";
        UserBaseInfo userBaseInfo = getUserBaseInfo();
        if (null != userBaseInfo) {
            photoUri = userBaseInfo.photoUri;
            if (!TextUtils.isEmpty(photoUri)) {
                photoUri = photoUri + "?" + Math.random();
            }
        }
        return photoUri;
    }

//    /**获取个人中心的语文书*/
//    public static BookClassInfo getChineseBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_CHINESE);
//    }
//
//    /**获取个人中心的数学书*/
//    public static BookClassInfo getMathBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_MATH);
//    }
//
//    /**获取个人中心的英语书*/
//    public static BookClassInfo getEnglishBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_ENGLISH);
//    }
//
//    /**获取个人中心的物理书*/
//    public static BookClassInfo getPhysicsBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_PHYSICS);
//    }
//
//    /**获取个人中心的化学书*/
//    public static BookClassInfo getChemistryBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_CHEMISTRY);
//    }
//
//    /**获取个人中心的生物书*/
//    public static BookClassInfo getBiologyBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_BIOLOGY);
//    }
//
//    /**获取个人中心的政治书*/
//    public static BookClassInfo getPoliticsBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_POLITICS);
//    }
//
//    /**获取个人中心的历史书*/
//    public static BookClassInfo getHistoryBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_HISTORY);
//    }
//
//    /**获取个人中心的地理书*/
//    public static BookClassInfo getGeographyBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_GEOGRAPHY);
//    }
//
//    /**获取个人中心的科学书*/
//    public static BookClassInfo getScienceBook() {
//        return getUserBook(BaseConstant.SUBJECT_ID_SCIENCE);
//    }
//
//    public static BookClassInfo getUserBook(int subjectId) {
//        Context context = getContext();
//        BookClassInfo bookClassInfo = null;
//        UserDbSearch userDbSearch = UserDbSearch.getInstance(context);
//        if (null != userDbSearch) {
//            bookClassInfo = userDbSearch.getBookClassInfo(subjectId);
//        }
//        return bookClassInfo;
//    }

    //=======================================================================add by divhee start
    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    // 判断一个字符串是否都为数字
    public boolean isDigitEx(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    //截取数字
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // 截取非数字
    public String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
    //=======================================================================add by divhee end

    /**
     * 开启定位
     * @param activity
     * @param isReturnTwice true:1&&2  false:2 , [1]返回当前缓存  [2]开始定位，完成后返回
     * @param locationCallback
     * @return activity销毁状态返回null
     *         很久前的个人中心不存在服务也会返回null，但这种情况非常少
     */
//    public static BroadcastReceiver addLocationAction(@NonNull Activity activity, boolean isReturnTwice, @NonNull LocationCallback locationCallback) {
//        BroadcastReceiver broadcastReceiver = null;
//        if (activity.isFinishing() || activity.isDestroyed()) {
//            return broadcastReceiver;
//        }
//        Intent mIntent = new Intent();
//        mIntent.setAction(BaseConstant.ACTION_NAME_PS_LOCATION);//你定义的service的action
//        mIntent.setPackage(BaseConstant.PKG_NAME_PS);
//        mIntent.putExtra(BaseConstant.KEY_NAME_PS_LOCATION_IS_PROMPT, isReturnTwice);
//        try {
//            activity.startService(mIntent);
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(BaseConstant.ACTION_NAME_PS_LOCATION_BROADCAST);
//            activity.registerReceiver(broadcastReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    if (null != intent && null != intent.getAction()) {
//                        if (BaseConstant.ACTION_NAME_PS_LOCATION_BROADCAST.equals(intent.getAction())) {
//                            if (null != locationCallback) {
//                                locationCallback.onLocationCallback(LocationBean.parse(intent));
//                            }
//                        }
//                    }
//                }
//            }, intentFilter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return broadcastReceiver;
//    }
//
//    /**移除定位*/
//    public static void removeLocationAction(@NonNull Activity activity, BroadcastReceiver broadcastReceiver) {
//        if (null != broadcastReceiver) {
//            activity.unregisterReceiver(broadcastReceiver);
//        }
//    }
//
//    public interface LocationCallback {
//        void onLocationCallback(LocationBean locationBean);
//    }
}


