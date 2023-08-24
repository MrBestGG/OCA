package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/15
 */
public class OnlineClassSatesItem implements ItemBean{

    @Override
    public String getName() {
        return null;
    }

    @SerializedName("web_school_list")
    private List<OnlineClassItem> webList;

    public List<OnlineClassItem> getWebList() {
        return webList;
    }

    public void setWebList(List<OnlineClassItem> webList) {
        this.webList = webList;
    }

    @Override
    public String toString() {
        return "OnlineClassSatesItem{" +
                "webList=" + webList +
                '}';
    }
}
