package com.readboy.onlinecourseaides.utils;

import static com.readboy.onlinecourseaides.utils.GlobalParam.APP_ID_GREEN;
import static com.readboy.onlinecourseaides.utils.GlobalParam.APP_SEC_COURSE;
import static com.readboy.onlinecourseaides.utils.GlobalParam.APP_SEC_GREEN;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

//import com.readboy.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class NetWorkUtils {

    /**
     * 验证URL 合法性
     */
    public static boolean isValidUrl(String urlString) {
        URI uri = null;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }

        if (uri.getHost() == null) {
            return false;
        }
        if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
            return true;
        }
        return false;
    }

    /**
     * 创建Course签名
     * 每个接口请求都必须带签名sn参数,算法规则如下:
     * sn = uid + timestamp + md5(timestamp + appsec +md5(uid))
     * uid： 10 字节，不足补 0(uid)，如 uid=2，则补 0 后为:0000000002，如果没有uid传过来就直接传10位0
     * 【特别注意，
     *  Q系列平板的账号是珠海管理的一套独立于G系列账号系统的账号系统，
     *  Q系列uid可能跟G系列的冲突，为了区分，
     *  Q系列app端统一做 uid=uid+1000000000处理以区分，
     * 默认G系列uid不会大于10亿】
     * timestamp：10 字节(timestamp),时间戳
     * md5(timestamp+appsec+md5（uid）) 长度：32 字节
     * appsec:密钥（系统分配）
     */
    public static String createCourseSign(String uid, boolean isQSerial) {
        StringBuilder uidBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(uid)) {
            uidBuilder.append(uid);
        }
        // 注意uid的位数一定要对应接口
        while (uidBuilder.length() < 10) {
            uidBuilder.insert(0, "0");
        }
        if (isQSerial) {
            uidBuilder.append("1000000000");
        }
        uid = uidBuilder.toString();

        StringBuilder signBuilder = new StringBuilder();
        String timestamp = (System.currentTimeMillis() / 1000) + "";
        String appSec = APP_SEC_COURSE;
        signBuilder.append(uid)
                .append(timestamp)
                .append(CommonUtils.getMd5(timestamp + appSec + CommonUtils.getMd5(uid)));
        return signBuilder.toString();
    }

    /**
     * 创建绿色上网接口签名
     * <p>
     * 公共鉴权参数sn（必传）:
     * sn = uid+timestamp+md5(timestamp+appSec+md5(uid))+appID
     * uid为8位，不足前面补0，比如00000002，没有uid的为8个0
     * timestamp为时间戳，10位
     */
    public static String createGreenSign(String uid, boolean isQSerial) {
        StringBuilder uidBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(uid)) {
            uidBuilder.append(uid);
        }
        // 注意uid的位数一定要对应接口
        while (uidBuilder.length() < 8) {
            uidBuilder.insert(0, "0");
        }
        if (isQSerial) {
            uidBuilder.append("1000000000");
        }
        uid = uidBuilder.toString();

        StringBuilder signBuilder = new StringBuilder();
        String timestamp = (System.currentTimeMillis() / 1000) + "";
        String appSec = APP_SEC_GREEN;
        String appId = APP_ID_GREEN;
        signBuilder.append(uid)
                .append(timestamp)
                .append(CommonUtils.getMd5(timestamp + appSec + CommonUtils.getMd5(uid)))
                .append(appId);
        return signBuilder.toString();
    }


    /**
     * 对网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean checkNetWorkConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
