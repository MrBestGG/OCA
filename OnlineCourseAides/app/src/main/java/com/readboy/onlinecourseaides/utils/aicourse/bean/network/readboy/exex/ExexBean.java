package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.exex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.question.QuestionBean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class ExexBean {

    @SerializedName("id")
    private String id; // 编号(id)[Integer]
    @SerializedName("name")
    private String name; // 名称(name)[String] 仅用于模块划分，不决定具体内容表现形式（讲解(explain)、练习(exercise)）
    @SerializedName("module")
    private Long module; // 模块(module)[Integer]
    @SerializedName("source")
    private String source; // 出处(source)[Integer] 教辅的ID
    @SerializedName("sid")
    private String sid; // 章节编号(sid)[Integer]
    @SerializedName("level")
    private Long level; // 层次(level)[Integer]
    @SerializedName("index")
    private Integer index; // 索引(index)[Integer]
    @SerializedName("pid")
    private Integer pid; // 父目录ID(pid)[Integer] 根目录此值无意义
    @SerializedName("children")
    private List<ExexBean> children; // 子目录(children)[ObjectArray] 目录节点，null时才有内容
    @SerializedName("speech")
    private String speech; // 声音(speech)[String] 声音文件名 数据目前暂时不加
    @SerializedName("score")
    private String score; // 总分(score)[Integer] 单位：0.1分 试题类才有 已弃用，仅用于兼容旧的数据
    @SerializedName("time")
    private Integer time; // 时间(time)[Integer] 单位：分钟 试题类才有 已弃用，仅用于兼容旧的数据
    @SerializedName("content2")
    private List<Content> content2; // 内容(content2)[ObjectArray] 新标签 可以是讲解(explain)、练习(exercise)的任意组合
    @SerializedName("addition")
    private String addition; // 附加信息(addition)[Xml]
    @SerializedName("createTime")
    private Long createTime; // 创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private Long updateTime; // 更新时间(updateTime)[Timestamp]
    @SerializedName("version")
    private Integer version; // 数据版本号(version)[Integer]
    @SerializedName("type")
    private Integer type; // 类型(type)[Integer] from TutorBookBean

    @Override
    public String toString() {
        return "ExexBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", module=" + module +
                ", source='" + source + '\'' +
                ", sid='" + sid + '\'' +
                ", level=" + level +
                ", index=" + index +
                ", pid=" + pid +
                ", children=" + children +
                ", speech='" + speech + '\'' +
                ", score='" + score + '\'' +
                ", time=" + time +
                ", content2=" + content2 +
                ", addition='" + addition + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", type=" + type +
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

    public Long getModule() {
        return module;
    }

    public void setModule(Long module) {
        this.module = module;
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

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<ExexBean> getChildren() {
        return children;
    }

    public void setChildren(List<ExexBean> children) {
        this.children = children;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<Content> getContent2() {
        return content2;
    }

    public void setContent2(List<Content> content2) {
        this.content2 = content2;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Content {
        @SerializedName("name")
        private String name; // 名称(name)[String] 内容标题，可以为空 相当于一级数据目录，已弃用，仅用于兼容旧的数据
        @SerializedName("score")
        private String score; // 总分(score)[Integer] 单位：0.1分 试题类才有 已弃用，仅用于兼容旧的数据
        @SerializedName("type")
        private Integer type; // 类型(type)[Integer]
        @SerializedName("label")
        private String label; // 标签(label)[String] 内容标签，可以为空 数据目前暂时不加
        @SerializedName("explain")
        private String explain; // 讲解(explain)[Xml]
        @SerializedName("exercise")
        private List<QuestionBean> exercise; // 练习(exercise)[ObjectArray]
        @SerializedName("comment")
        private List<Comment> comment; // 注解(comment)[ObjectArray]

        @Override
        public String toString() {
            return "Content{" +
                    "name='" + name + '\'' +
                    ", score='" + score + '\'' +
                    ", type=" + type +
                    ", label='" + label + '\'' +
                    ", explain='" + explain + '\'' +
                    ", exercise=" + exercise +
                    ", comment=" + comment +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public List<QuestionBean> getExercise() {
            return exercise;
        }

        public void setExercise(List<QuestionBean> exercise) {
            this.exercise = exercise;
        }

        public List<Comment> getComment() {
            return comment;
        }

        public void setComment(List<Comment> comment) {
            this.comment = comment;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Comment {
        @SerializedName("id")
        private String id; // 注解编号(id)[String]
        @SerializedName("type")
        private String type; // 类型(type)[Integer]
        @SerializedName("content")
        private String content; // 内容(content)[Xml] 包含声音图片

        @Override
        public String toString() {
            return "Comment{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
