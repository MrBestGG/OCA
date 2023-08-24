package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.exex.ExexBean;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.paper.PaperBean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 目录集合
 * (教材：tiku.section、
 * 教辅：tiku.tutor_sec、
 * 自适应、智能评测：evaluation.section、
 * 电子书包：eschoolbag.section)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Section {

    @SerializedName("id")
    private String id;  // 目录编号(id)[Integer]
    @SerializedName("oid")
    private String oid; // 原始编号(oid)[Integer] (原始数据id，如猿题库id)
    @SerializedName("name")
    private String name; // 目录名称(name)[String]
    @SerializedName("type")
    private Long type; // 目录类型(type)[Integer]
    @SerializedName("courseId")
    private String courseId; // 课程编号(courseId)[Integer]
    @SerializedName("source")
    private String source; // 目录出处(source)[Integer] (书本的ID)
    @SerializedName("level")
    private Long level; // 层次(level)[Integer]
    @SerializedName("relation")
    private List<Relation> relation; // 关联的目录(relation)[ObjectArray]
    @SerializedName("enable")
    private Long enable; // 是否启用(enable)[Integer] 默认为启用 (废弃)
    @SerializedName("speech")
    private String speech;  // 声音(speech)[String] 小学全解目录存在声音
    @SerializedName("count")
    private Long count; // 题目数(count)[Integer] (废弃)
    @SerializedName("status")
    private Long status; // 状态(status)[Integer]
    @SerializedName("createTime")
    private Long createTime; // 创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private Long updateTime; // 更新时间(updateTime)[Timestamp]
    @SerializedName("version")
    private Long version; // 数据版本号(version)[Integer]
    @SerializedName("chapterOrder")
    private Long chapterOrder; // 排序号(chapterOrder)[Integer]

    @SerializedName("mapping")
    private List<String> mapping; // 知识点图谱(mapping)[IntegerArray]
    @SerializedName("dialogure")
    private List<Long> dialogure; // 短文(dialogure)[IntegerArray] (仅存在于最后一级目录)
    @SerializedName("sentence")
    private List<Long> sentence; // 句子(sentence)[IntegerArray] (仅存在于最后一级目录)
    @SerializedName("vocabulary")
    private List<Long> vocabulary; // 单词词汇(vocabulary)[IntegerArray] (仅存在于第一级目录)
    @SerializedName("dub")
    private List<Long> dub; // 配音(dub)[IntegerArray] (仅存在于第一级目录)

    //-------------------------------- 提供给QuestionBean
    @SerializedName("index")
    private Long index; // 索引(index)[Integer] ？？？？？？？？？？ 用于章节题目排序
    //--------------------------------

    @SerializedName("children")
    private List<Section> children; // 子目录(children)[IntegerArray]

    // ============================================================== 子目录children为空时才有下面的资源
    @SerializedName("word")
    private List<String> word;  // 单词(word)[StringArray]
    @SerializedName("pinyin")
    private List<String> pinyin;  // 拼音(pinyin)[StringArray]
    @SerializedName("hanzi")
    private List<String> hanzi;  // 汉字(hanzi)[StringArray] (废弃)
    @SerializedName("chinese")
    private List<Chinese> chinese;  // 汉字(chinese)[ObjectArray]
    @SerializedName("terms")
    private List<String> terms;  // 词语(terms)[StringArray]
    @SerializedName("game")
    private List<Long> game;  // 游戏(game)[IntegerArray]
    @SerializedName("paper")
    private List<PaperBean> paper;  // 试卷(paper)[ObjectArray]
    @SerializedName("keypoint")
    private List<KeyPoint> keypoint; // 知识点(keypoint)[ObjectArray] (仅id，name)

    @SerializedName("word2")
    private List<Explain> word2; // 单词2(word2)[StringArray] (与section_word表关联)
    @SerializedName("explain2")
    private List<Explain> explain2; // 全解(explain2)[ObjectArray]
    @SerializedName("exercise2")
    private List<Exercise> exercise2; // 全练(exercise2)[ObjectArray]
    @SerializedName("video2")
    private List<String> video2; // 视频(video2)[IntegerArray]

    @SerializedName("exex")
    private List<ExexBean> exex; // 全解全练(exex)[ObjectArray] (旧字段，即将废弃, 仅id，type，name，moduel, children)
    @SerializedName("explain")
    private List<Explain> explain; // 全解(explain)[ObjectArray] (旧字段，即将废弃, 仅id，type，name，moduel, children)
    @SerializedName("exercise")
    private List<Exercise> exercise; // 全练(exercise)[ObjectArray] (旧字段，即将废弃, 仅id，type，name，moduel, children)
    @SerializedName("video")
    private List<Video> video; // 视频(video)[ObjectArray] (旧字段，即将废弃)
    // ==============================================================

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Relation {
        @SerializedName("type")
        private Long type; // 书本类型(type)[Integer]
        @SerializedName("category")
        private Long category; // 书本分类(category)[Integer]
        @SerializedName("module")
        private Long module; // 书本模块(module)[[Integer]
        @SerializedName("id")
        private String id; // 目录编号(id)[Integer]

        @Override
        public String toString() {
            return "Relation{" +
                    "type=" + type +
                    ", category=" + category +
                    ", module=" + module +
                    ", id='" + id + '\'' +
                    '}';
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public Long getCategory() {
            return category;
        }

        public void setCategory(Long category) {
            this.category = category;
        }

        public Long getModule() {
            return module;
        }

        public void setModule(Long module) {
            this.module = module;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return "Section{" +
                "id='" + id + '\'' +
                ", oid='" + oid + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", courseId='" + courseId + '\'' +
                ", source='" + source + '\'' +
                ", level=" + level +
                ", relation=" + relation +
                ", enable=" + enable +
                ", speech='" + speech + '\'' +
                ", count=" + count +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", chapterOrder=" + chapterOrder +
                ", mapping=" + mapping +
                ", dialogure=" + dialogure +
                ", sentence=" + sentence +
                ", vocabulary=" + vocabulary +
                ", dub=" + dub +
                ", index=" + index +
                ", children=" + children +
                ", word=" + word +
                ", pinyin=" + pinyin +
                ", hanzi=" + hanzi +
                ", chinese=" + chinese +
                ", terms=" + terms +
                ", game=" + game +
                ", paper=" + paper +
                ", keypoint=" + keypoint +
                ", word2=" + word2 +
                ", explain2=" + explain2 +
                ", exercise2=" + exercise2 +
                ", video2=" + video2 +
                ", exex=" + exex +
                ", explain=" + explain +
                ", exercise=" + exercise +
                ", video=" + video +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public List<Relation> getRelation() {
        return relation;
    }

    public void setRelation(List<Relation> relation) {
        this.relation = relation;
    }

    public Long getEnable() {
        return enable;
    }

    public void setEnable(Long enable) {
        this.enable = enable;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

    public Long getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(Long chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public List<String> getMapping() {
        return mapping;
    }

    public void setMapping(List<String> mapping) {
        this.mapping = mapping;
    }

    public List<Long> getDialogure() {
        return dialogure;
    }

    public void setDialogure(List<Long> dialogure) {
        this.dialogure = dialogure;
    }

    public List<Long> getSentence() {
        return sentence;
    }

    public void setSentence(List<Long> sentence) {
        this.sentence = sentence;
    }

    public List<Long> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<Long> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public List<Long> getDub() {
        return dub;
    }

    public void setDub(List<Long> dub) {
        this.dub = dub;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<Section> getChildren() {
        return children;
    }

    public void setChildren(List<Section> children) {
        this.children = children;
    }

    public List<String> getWord() {
        return word;
    }

    public void setWord(List<String> word) {
        this.word = word;
    }

    public List<String> getPinyin() {
        return pinyin;
    }

    public void setPinyin(List<String> pinyin) {
        this.pinyin = pinyin;
    }

    public List<String> getHanzi() {
        return hanzi;
    }

    public void setHanzi(List<String> hanzi) {
        this.hanzi = hanzi;
    }

    public List<Chinese> getChinese() {
        return chinese;
    }

    public void setChinese(List<Chinese> chinese) {
        this.chinese = chinese;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public List<Long> getGame() {
        return game;
    }

    public void setGame(List<Long> game) {
        this.game = game;
    }

    public List<PaperBean> getPaper() {
        return paper;
    }

    public void setPaper(List<PaperBean> paper) {
        this.paper = paper;
    }

    public List<KeyPoint> getKeypoint() {
        return keypoint;
    }

    public void setKeypoint(List<KeyPoint> keypoint) {
        this.keypoint = keypoint;
    }

    public List<Explain> getWord2() {
        return word2;
    }

    public void setWord2(List<Explain> word2) {
        this.word2 = word2;
    }

    public List<Explain> getExplain2() {
        return explain2;
    }

    public void setExplain2(List<Explain> explain2) {
        this.explain2 = explain2;
    }

    public List<Exercise> getExercise2() {
        return exercise2;
    }

    public void setExercise2(List<Exercise> exercise2) {
        this.exercise2 = exercise2;
    }

    public List<String> getVideo2() {
        return video2;
    }

    public void setVideo2(List<String> video2) {
        this.video2 = video2;
    }

    public List<ExexBean> getExex() {
        return exex;
    }

    public void setExex(List<ExexBean> exex) {
        this.exex = exex;
    }

    public List<Explain> getExplain() {
        return explain;
    }

    public void setExplain(List<Explain> explain) {
        this.explain = explain;
    }

    public List<Exercise> getExercise() {
        return exercise;
    }

    public void setExercise(List<Exercise> exercise) {
        this.exercise = exercise;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }
}
