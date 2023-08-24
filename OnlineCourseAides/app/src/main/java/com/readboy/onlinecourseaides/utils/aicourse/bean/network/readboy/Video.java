package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Video {
    @SerializedName("id")
    private String id; // 视频编号(id)[Integer/String]
    @SerializedName("iid")
    private String iid; // 视频整型编号(iid)[Integer]
    @SerializedName("name")
    private String name; // 视频名(name)[String]
    @SerializedName("fileName")
    private String fileName; // 文件名称(fileName)[String]
    @SerializedName("filePath")
    private String filePath; // 文件路径(filePath)[String]
    @SerializedName("source")
    private String source; // 来源(source)[String]
    @SerializedName("duration")
    private double duration; // 时长(duration)[Double]
    @SerializedName("thumbnail")
    private String thumbnail; // 缩略图(thumbnail)[String]
    @SerializedName("from")
    private String from; // 出处(from)[String]

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", iid='" + iid + '\'' +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", source='" + source + '\'' +
                ", duration=" + duration +
                ", thumbnail='" + thumbnail + '\'' +
                ", from='" + from + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
