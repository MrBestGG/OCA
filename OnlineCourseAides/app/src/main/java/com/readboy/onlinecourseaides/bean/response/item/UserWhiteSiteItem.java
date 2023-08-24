package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

/**
 * @Author jll
 * @Date 2022/11/28
 *
 *   "id": 195588,
 *   "uid": 3753857,
 *   "name": "cn.bing.com",
 *   "url": "https://cn.bing.com/",
 *   "is_common": 0,
 *   "collect": 0
 */
public class UserWhiteSiteItem implements ItemBean{
    @SerializedName("id")
    private String id;
    @SerializedName("uid")
    private String uId;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("is_common")
    private int isCommon;
    @SerializedName("collect")
    private int collect;
    @SerializedName("source")
    private int source;

    @Override
    public String toString() {
        return "UserWhiteSiteItem{" +
                "id='" + id + '\'' +
                ", uId='" + uId + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", isCommon=" + isCommon +
                ", collect=" + collect +
                ", source=" + source +
                '}';
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(int isCommon) {
        this.isCommon = isCommon;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }
}
