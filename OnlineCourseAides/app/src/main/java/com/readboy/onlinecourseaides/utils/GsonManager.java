package com.readboy.onlinecourseaides.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @Author jll
 * @Date 2022/11/28
 */
public class GsonManager {

    public static final String TAG = "GsonManager";
    private static Gson gson;
    private static GsonManager gsonManager;

    private GsonManager() {
        gson = new Gson();
    }

    public static GsonManager getInstance() {
        if (gsonManager == null) {
            synchronized (GsonManager.class) {
                if (gsonManager == null) {
                    gsonManager = new GsonManager();
                }
            }
        }
        return gsonManager;
    }

    /**
     * 将json转化为对应的实体对象
     * new TypeToken<HashMap<String, Object>>(){}.getType()
     */
    public  <T> T fromJson(String json, TypeToken<T> type) {
        return gson.fromJson(json, type.getType());
    }

    /**
     * 将json字符串转化成实体对象
     *
     * @param json json字符串
     * @param <T>  目标对象类型
     * @return 目标对象实例
     */
    public <T> T convert(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    /**
     * toJson
     *
     * @param src 对象
     * @return String
     */
    public <T> String toJson(Object src, TypeToken<T> typeToken) {
        return gson.toJson(src, typeToken.getType());
    }
}
