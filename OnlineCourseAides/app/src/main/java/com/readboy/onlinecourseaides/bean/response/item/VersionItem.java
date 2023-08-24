package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

/**
 * 请求data  版本 ：  人教版
 * 需要缓存
 */
public class VersionItem implements ItemBean{
    @SerializedName("edition_id")
    private String id;
    @SerializedName("edition_name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VersionItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
