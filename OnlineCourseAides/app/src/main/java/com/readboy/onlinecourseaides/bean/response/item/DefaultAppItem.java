package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/19
 */
public class DefaultAppItem implements ItemBean{

    @SerializedName("web_class_app")
    private List<AppItem> webList;

    public List<AppItem> getWebList() {
        return webList;
    }

    public void setWebList(List<AppItem> webList) {
        this.webList = webList;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String toString() {
        return "DefaultAppItem{" +
                "webList=" + webList +
                '}';
    }
}
