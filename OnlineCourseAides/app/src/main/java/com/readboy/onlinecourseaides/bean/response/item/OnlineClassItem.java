package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * @Author jll
 * @Date 2022/11/28
 */
public class OnlineClassItem implements ItemBean{
    @SerializedName("province_name")
    private String provinceName;

    @SerializedName("url")
    private String url;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "OnlineClassItem{" +
                "provinceName='" + provinceName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineClassItem that = (OnlineClassItem) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
