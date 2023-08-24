package com.readboy.onlinecourseaides.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * @return 客户端版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            String pkName = context.getPackageName();
            versionCode = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
        } catch (Exception e) {
            versionCode = 200319001;
        }
        return versionCode;
    }

    public static String getModel() {
        String model;
        try {
            model = Build.MODEL;
        } catch (Exception e) {
            model = "Readboy_AI";
            e.printStackTrace();
        }
        return model;
    }

    /**
     * @return 获取序列号
     */
    public static String getSerial() {
        String serial = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            serial = Build.SERIAL;
        } else {
            try {
                serial = Build.getSerial();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(serial)) {
            serial = "unKnowSerial";
        }
//        serial = serial + "ddd";
        Log.v("hqb", "hqb__Utils__getSerial__serial = " + serial);
        return serial;
    }

    public static String getMd5(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.reset();
            digest.update(code.getBytes());
            byte[] hashValue = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashValue) {
                if ((b & 0xFF) < 0x10) {
                    sb.append("0");
                }

                sb.append(Integer.toHexString(b & 0xFF));
            }

            return sb.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析返回的数据
     *
     * @param response
     * @return
     */
    public static String getResponseBody(Response response) {

        Charset UTF8 = Charset.forName("UTF-8");
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        return buffer.clone().readString(charset);
    }
}
