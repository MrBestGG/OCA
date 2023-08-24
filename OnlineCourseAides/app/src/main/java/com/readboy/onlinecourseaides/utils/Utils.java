package com.readboy.onlinecourseaides.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {
    /**
     * 是否支持多录音
     * @return
     */
    public static boolean isSupportMultipleRecord()
    {
        String value = null;
        Class<?> cls = null;

        try {
            cls = Class.forName("android.os.SystemProperties");
            Method hideMethod = cls.getMethod("get", String.class);
//            Object object = cls.newInstance();
//            value = (String) hideMethod.invoke(object, "ro.readboy.ar.type");
            value = (String) hideMethod.invoke(null, "ro.readboy.multiplerecord");
        } catch (ClassNotFoundException e) {
            Log.e("isSupportMultipleRecord", "get error() ", e);
        } catch (NoSuchMethodException e) {
            Log.e("isSupportMultipleRecord", "get error() ", e);
        }
        catch (IllegalAccessException e) {
            Log.e("isSupportMultipleRecord", "get error() ", e);
        } catch (IllegalArgumentException e) {
            Log.e("isSupportMultipleRecord", "get error() ", e);
        } catch (InvocationTargetException e) {
            Log.e("isSupportMultipleRecord", "get error() ", e);
        }catch (Exception e){
            Log.e("isSupportMultipleRecord", "get error() ", e);
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(value) && value.equals("support")){
            return true;
        }
        return false;
    }

    /**
     * 录音是否被其它应用占用
     * @return
     */
    public static boolean isRecordOccupiedByOtherApp(Context context, String logTag){
        if(isSupportMultipleRecord()){
            return false;
        }
        String curPackageName = "";
        try {
//            android.app.MediaAndAudioRecordManager. mMediaAndAudioRecordManager = (MediaAndAudioRecordManager)mContext.getSystemService("media_and_audio_record");
//            mMediaAndAudioRecordManager.getCurrentRecordPackage();
            @SuppressLint("WrongConstant")
            Object manager = context.getSystemService("media_and_audio_record");
            Class mediaAndAudioRecordManager = manager.getClass();
            Method getCurrentRecordPackage = mediaAndAudioRecordManager.getMethod("getCurrentRecordPackage");
            curPackageName = (String)getCurrentRecordPackage.invoke(manager);
            Log.v("hqb", "hqb__isRecordOccupiedByOtherApp__" + logTag +"__curPackageName = " + curPackageName);
        }catch (Exception e){
            Log.v("hqb", "hqb__isRecordOccupiedByOtherApp__" + logTag + "__curPackageName__e = " + e);
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(curPackageName) && !curPackageName.equals("com.readboy.voiceassistant")){
            return true;
        }
        return false;
    }

    public static Toast mToast;
    public static void showToast(Context context, String msg){
        if(mToast == null){
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static SpannableString getHightLightTag(String content, String[] keywords, int colorRed, int colorGreen, int colorBlue) {

        SpannableString mStringBuilder = new SpannableString(content);
        for(String keyword : keywords) {
            keyword = keyword.toLowerCase();

            String lowerContent = content.toLowerCase();

            int a = lowerContent.indexOf(keyword);
            int len = content.length();

            while (a < len && a >= 0) {
                if (a == -1) {
                    break;
                }
                if (a >= 0 && a + keyword.length() <= content.length()) {
                    mStringBuilder.setSpan(
                            new ForegroundColorSpan(Color.rgb(colorRed, colorGreen, colorBlue)),
                            a, a + keyword.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                a = lowerContent.indexOf(keyword, a + keyword.length() + 1);

            }
        }

        return mStringBuilder;

    }

    public static void fileCopy(String source, String dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            File destFile = new File(dest);
            File destFileParent = destFile.getParentFile();
            if(!destFileParent.exists()){
                destFileParent.mkdirs();
            }

            input = new FileInputStream(new File(source));
            output = new FileOutputStream(new File(dest));
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
