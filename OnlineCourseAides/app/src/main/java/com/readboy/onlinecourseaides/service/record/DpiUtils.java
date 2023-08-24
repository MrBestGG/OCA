package com.readboy.onlinecourseaides.service.record;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class DpiUtils {
    private static final String TAG = "DpiUtils";

    private final int[][] validResolutions = {
            // CEA Resolutions  480p
            {640, 480},
            {720, 480},
            {800, 480},
            {854, 480},
            {864, 480},
            {848, 480},
            // 720p
            {1280, 720},
            // 1080p
            {1920, 1080},
            // VESA Resolutions
            {800, 600},
            {1024, 768},
            {1152, 864},
            {1280, 768},
            {1280, 800},
            {1360, 768},
            {1366, 768},
            {1600, 1200},
            {1920, 1200},
    };

    private int DevicesType = 0;
    public static final int DEVICES_TYPE_Qualcomm = 0;
    public static final int DEVICES_TYPE_MTK = 1;
    public static final String CPU_NAME_QUALCOMM = "QCOM";
    public static final String CPU_NAME_MTK = "MTK";

    public static final String DEVICES_DPI_PARAMS_MTK = "/vendor/etc/media_profiles_V1_0.xml";
    public static final String DEVICES_DPI_PARAMS_QUALCOMM = "/system/etc/media_profiles.xml ";

    public static int getDevicesType() {
        String type = SystemPropertiesInvoke.getStringByKey("ro.readboy.platform", "");
        if (type != null) {
            Log.d(TAG, "getDevicesType: " + type);
            if (type.equals(CPU_NAME_QUALCOMM)) {
                return DEVICES_TYPE_Qualcomm;
            } else if (type.equals(CPU_NAME_MTK)) {
                return DEVICES_TYPE_MTK;
            }
        }
        return -1;
    }

//    /**
//     * 判断是否符合屏幕分辨率
//     * @param w
//     * @param h
//     * @param d
//     * @param isNomal
//     * @return
//     */
//    public boolean isFormatSystemDpi(int w,int h, int d, boolean isLandscape) {
//        int devicesType = getDevicesType();
//        switch (devicesType) {
//            case DEVICES_TYPE_Qualcomm:
//                if(h <= 1080 && w <= 1920 && isLandscape) {
//                    return true;
//                }else if (h <= 1920 && w <= 1080 && !isLandscape){
//                    return true;
//                }
//                return false;
//                break;
//            case DEVICES_TYPE_MTK:
//                if(h <= 1080 && w <= 1920 && isLandscape) {
//                    return true;
//                }else if (h <= 1920 && w <= 1080 && !isLandscape){
//                    return true;
//                }
//                return false;
//                break;
//        }
//
//        return false;
//    }

    public static int[][] getDpiWithAndHeight() {
        int devicesType = getDevicesType();
        switch (devicesType) {
            case DEVICES_TYPE_Qualcomm:
                try {
                    File mediaProfiles = new File(DEVICES_DPI_PARAMS_QUALCOMM);
                    FileInputStream fin = new FileInputStream(mediaProfiles);
                    byte[] bytes = new byte[(int) mediaProfiles.length()];
                    // xml to json
                    XmlToJson xmlToJson = new XmlToJson.Builder(new String(bytes)).build();
                    Log.d(TAG, "getDpiWithAndHeight: xml to json =>" + xmlToJson.toString());
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "getDpiWithAndHeight: read media_profiles.xml error");
                    e.printStackTrace();
                }
                break;
            case DEVICES_TYPE_MTK:
                try {
                    File mediaProfiles = new File(DEVICES_DPI_PARAMS_MTK);
                    FileInputStream fin = new FileInputStream(mediaProfiles);
                    byte[] bytes = new byte[(int) mediaProfiles.length()];
                    // xml to json
                    XmlToJson xmlToJson = new XmlToJson.Builder(new String(bytes)).build();
                    Log.d(TAG, "getDpiWithAndHeight: xml to json =>" + xmlToJson.toString());
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "getDpiWithAndHeight: read media_profiles.xml error");
                    e.printStackTrace();
                }
                break;
        }

        return null;
    }
}
