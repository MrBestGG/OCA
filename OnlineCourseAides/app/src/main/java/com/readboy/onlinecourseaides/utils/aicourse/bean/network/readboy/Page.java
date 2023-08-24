package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Page {

    @SerializedName("createTime")
    private Long createTime; // 创建时间(createTime)[Integer]
    @SerializedName("updateTime")
    private Long updateTime; // 更新时间(updateTime)[Integer]
    @SerializedName("packageUri")
    private String packageUri; // 离线数据包地址(packageUri)[String] (废弃)
    @SerializedName("packTime")
    private String packTime; // 离线打包时间(packTime)[String] (废弃)
    @SerializedName("data")
    private List<PageData> data; // 页面章节数据(data)[ObjectArray]

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public class PageData {
        @SerializedName("id")
        private String id; // 编号(id)[String]
        @SerializedName("sid")
        private String sid; // 章节id(sid)[Integer]
        @SerializedName("type")
        private Integer type; // 类型(type)[Integer]
        @SerializedName("pagenum")
        private String pagenum; // 页码(pagenum)[String]

        //-------------------------------- 提供给QuestionBean
        @SerializedName("source")
        private String source; // 书本来源(source)[Integer]
        @SerializedName("index")
        private Long index; // 题目索引(index)[Integer]
        //--------------------------------
    }

    @Override
    public String toString() {
        return "Page{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", packageUri='" + packageUri + '\'' +
                ", packTime='" + packTime + '\'' +
                ", data=" + data +
                '}';
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getPackageUri() {
        return packageUri;
    }

    public void setPackageUri(String packageUri) {
        this.packageUri = packageUri;
    }

    public String getPackTime() {
        return packTime;
    }

    public void setPackTime(String packTime) {
        this.packTime = packTime;
    }

    public List<PageData> getData() {
        return data;
    }

    public void setData(List<PageData> data) {
        this.data = data;
    }
}
