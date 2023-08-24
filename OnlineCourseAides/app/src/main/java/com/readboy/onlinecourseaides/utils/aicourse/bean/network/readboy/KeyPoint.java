package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识点集合(tiku.keypoint)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class KeyPoint {

    @SerializedName("id")
    private String id; // 知识点编号(id)[Integer]
    @SerializedName("name")
    private String name; // 知识点名称(name)[String]

    @SerializedName("source")
    private String source; // 出处(source)[Integer]
    @SerializedName("courseId")
    private String courseId; // 课程(courseId)[Integer]
    @SerializedName("children")
    private List<String> children; // 子知识点编号(children)[IntegerArray]
    @SerializedName("level")
    private Long level; // 层次(level)[Integer]
    @SerializedName("more")
    private String more; // ？？？(more)[String]
    @SerializedName("vId")
    private String vId; // 视频ID？？？(vId)[String] (旧字段，即将废弃)
    @SerializedName("video")
    private String video; // 视频(video)[String]
    @SerializedName("video2")
    private List<String> video2; // 视频(video2)[IntegerArray]
    @SerializedName("status")
    private Long status; // 状态(status)[Integer] (目前只在根节点引入此状态)
    @SerializedName("createTime")
    private Long createTime; // 创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private Long updateTime; // 更新时间(updateTime)[Timestamp]
    @SerializedName("version")
    private Long version; // 数据版本号(version)[Integer]
    @SerializedName("interimStatus")
    private Long interimStatus; // 临时状态(interimStatus)[Integer]
    @SerializedName("description")
    private String description; // 描述(description)[String]

    @Override
    public String toString() {
        return "KeyPoint{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", courseId='" + courseId + '\'' +
                ", children=" + children +
                ", level=" + level +
                ", more='" + more + '\'' +
                ", vId='" + vId + '\'' +
                ", video='" + video + '\'' +
                ", video2=" + video2 +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", interimStatus=" + interimStatus +
                ", description='" + description + '\'' +
                '}';
    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<String> getVideo2() {
        return video2;
    }

    public void setVideo2(List<String> video2) {
        this.video2 = video2;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getInterimStatus() {
        return interimStatus;
    }

    public void setInterimStatus(Long interimStatus) {
        this.interimStatus = interimStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
