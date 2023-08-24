package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.paper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Region;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.question.QuestionBean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷集合(paper)
 * 试卷题目集合(question_paper)
 * 试卷相关
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class PaperBean {
    
    @SerializedName("id")
    private String id; // 编号(id)[Integer]
    @SerializedName("name")
    private String name; // 名称(name)[String]
    @SerializedName("grade")
    private Integer grade; // 年级(grade)[Integer]
    @SerializedName("subject")
    private Integer subject; // 科目(subject)[Integer]
    @SerializedName("courseId")
    private String courseId; // 课程(courseId)[Integer]
    @SerializedName("type")
    private Integer type; // 类型(type)[Integer]
    @SerializedName("year")
    private Integer year; // 年份(year)[Integer]
    @SerializedName("region")
    private Region region; // 地区(region)[ObjectArray]
    @SerializedName("source")
    private String source; // 试卷出处(source)[Integer]  来自具体书本，目前仅五三试卷（from=1）有此字段
    @SerializedName("sid")
    private String sid; // 章节编号(sid)[Integer]  来自具体书本章节，目前仅五三试卷（from=1）有此字段
    @SerializedName("from")
    private Integer from; // 试卷数据出处(from)[Integer]
    @SerializedName("addition")
    private String addition; // 附加信息(addition)[Xml] 考试公式提示、说明等信息
    @SerializedName("time")
    private Integer time; // 考试时间(time)[Integer] 单位：分钟
    @SerializedName("score")
    private String score; // 考试总分(score)[Integer] 单位：0.1分
    @SerializedName("status")
    private Integer status; // 状态(status)[Integer]
    @SerializedName("children")
    private List<Content> children; // 内容(children)[ObjectArray]
    @SerializedName("createTime")
    private Long createTime; // 创建时间(createTime)[Integer]
    @SerializedName("updateTime")
    private Long updateTime; // 更新时间(updateTime)[Integer]
    @SerializedName("version")
    private Integer version; // 数据版本号(version)[Integer]

    @Override
    public String toString() {
        return "PaperBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", subject=" + subject +
                ", courseId='" + courseId + '\'' +
                ", type=" + type +
                ", year=" + year +
                ", region=" + region +
                ", source='" + source + '\'' +
                ", sid='" + sid + '\'' +
                ", from=" + from +
                ", addition='" + addition + '\'' +
                ", time=" + time +
                ", score='" + score + '\'' +
                ", status=" + status +
                ", children=" + children +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Content> getChildren() {
        return children;
    }

    public void setChildren(List<Content> children) {
        this.children = children;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Content {
        @SerializedName("id")
        private String id; // 编号(id)[Integer]
        @SerializedName("name")
        private String name; // 名称(name)[String]
        @SerializedName("type")
        private Integer type; // 类型(type)[Integer]
        @SerializedName("score")
        private String score; // 分数(score)[Integer] 单位：0.1分
        @SerializedName("addition")
        private String addition; // 附加信息(addition)[Xml] 仅用于"听力"的示例短文及声音，只存在于最后一级目录
        @SerializedName("qst")
        private List<QuestionBean> qst; // 试题(qst)[ObjectArray] 本级children不存在时有效
        @SerializedName("children")
        private List<Content> children; // 子目录(children)[ObjectArray] 本级qst不存在时有效，结构与上一级sect相同

        @Override
        public String toString() {
            return "Content{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    ", score='" + score + '\'' +
                    ", addition='" + addition + '\'' +
                    ", qst=" + qst +
                    ", children=" + children +
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

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getAddition() {
            return addition;
        }

        public void setAddition(String addition) {
            this.addition = addition;
        }

        public List<QuestionBean> getQst() {
            return qst;
        }

        public void setQst(List<QuestionBean> qst) {
            this.qst = qst;
        }

        public List<Content> getChildren() {
            return children;
        }

        public void setChildren(List<Content> children) {
            this.children = children;
        }
    }
}
