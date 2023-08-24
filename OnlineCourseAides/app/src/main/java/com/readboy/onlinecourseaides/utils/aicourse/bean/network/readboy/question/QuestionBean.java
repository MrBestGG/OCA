package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.BaseUtil;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Ability;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.KeyPoint;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Page;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Section;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.TestPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教辅（53）题目集合(question_53)
 * 教辅相关
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class QuestionBean {
    
    // ============================================================== paper qst
    @SerializedName("qid")
    private String qid; // 题目编号(qid)[Integer] 列出普通题和大题、小题
    @SerializedName("no")
    private String no; // 题目序号(no)[String] 小题序号写入题干
    @SerializedName("score")
    private String score; // 题目分数(score)[Integer] 单位：0.1分
    // ==============================================================

    @SerializedName("id")
    private String id; // 编号(id)[Integer]
    @SerializedName("oid")
    private String oid; // 原始编号(oid)[Integer] 原始数据id，如猿题库id，五三id
    @SerializedName("grade")
    private Long grade; // 年级(grade)[Integer]
    @SerializedName("subject")
    private Long subject; // 科目(subject)[Integer] 高中的年级无法解析，先加上课程id
    @SerializedName("courseId")
    private Long courseId; // 课程(courseId)[Integer] 高中的年级无法解析，先加上课程id
    @SerializedName("type")
    private Long type; // 类型(type)[Integer] 指题型(面向程序)
    @SerializedName("category")
    private Long category; // 分类(category)[Integer] 指类别编码(面向用户)，详见课程集合中的课程题型
    @SerializedName("difficulty")
    private Long difficulty; // 难度级别(difficulty)[Integer] 1~10
    @SerializedName("origin")
    private String origin; // 来源(origin)[String] 主要标明所属试卷
    @SerializedName("shortOrigin")
    private String shortOrigin; // 来源简称(shortOrigin)[String]
    @SerializedName("from")
    private Long from; // 题目数据出处(from)[Integer] 用于区分内部外数据和外部.
    
    // 数据
    @SerializedName("ofrom")
    private Long ofrom; // 题目数据原始出处(ofrom)[Integer] 值定义见题目数据出处(from)[Integer]定义
    @SerializedName("keypoint")
    private List<KeyPoint> keypoint; // 所属知识点(keypoint)[ObjectArray]
    @SerializedName("testpoint")
    private List<TestPoint> testpoint;
    @SerializedName("role")
    private Long role; // 角色(role)[Integer]
    @SerializedName("relation")
    private List<String> relation; // 关联题(relation)[IntegerArray] 大题(role=1)或小题(role=2)才有：大题里面填的是小题的id 小题里面填的是小题的id
    @SerializedName("content")
    private String content; // 题干(content)[Xml]
    @SerializedName("accessory")
    private List<Accessory> accessory; // 题干附加(accessory)[ObjectArray]
    @SerializedName("correctAnswer")
    private List<String> correctAnswer; // 正确答案(correctAnswer)[StringArray] 填空或选择题答案['A', 'B', 'C']['asdasd', 'dsdsd']
    @SerializedName("answer")
    private String answer; // 显示答案(answer)[Xml] 仅用于显示，使得显示清晰美观
    @SerializedName("solution")
    private String solution; // 解析(solution)[Xml]
    @SerializedName("solutionAccessory")
    private List<SolutionAccessory> solutionAccessory; // 解析附加(solutionAccessory)[ObjectArray] 跟在解析后面的附加内容
    @SerializedName("correctAnswerAccessory")
    private CorrectAnswerAccessory correctAnswerAccessory; // 答案附加(solutionAccessory)
    @SerializedName("material")
    private Material material; // 大题材料(material)[Object]
    @SerializedName("status")
    private Long status; // 题目状态(status)[Integer]
    @SerializedName("idea")
    private String idea; // 思路启发(idea)[XmlString] 对应原题目字段【解析(solution)[Xml]】(废弃)
    @SerializedName("step")
    private String step; // 解题过程(step)[XmlString]
    @SerializedName("summary")
    private String summary; // 归纳总结(summary)[XmlString]
    @SerializedName("opinion")
    private List<Long> opinion; // 评价总结(opinion)[IntegerArray]
    @SerializedName("quality")
    private Long quality; // 资源质量(quality)[Integer]
    @SerializedName("createTime")
    private Long createTime; // 创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private Long updateTime; // 更新时间(updateTime)[Timestamp]
    @SerializedName("version")
    private Long version; // 数据版本号(version)[Integer]
    @SerializedName("recommendTime")
    private Double recommendTime; // 推荐时间(recommendTime)[Double]

    @SerializedName("section")
    private List<Section> section; // 所属章节(section)[ObjectArray] { 章节编号(id)[Integer]、 章节名称(name)[String]、章节来源(source)[Integer]、索引(index)[Integer] }
    @SerializedName("page")
    private List<Page.PageData> page; // 所属页面(page)[ObjectArray] { 书本来源(source)[Integer](书本id)、 书本页码(pageNum)[String]、 题目索引(index)[Integer] }
    @SerializedName("ability")
    private List<Ability> ability; // 能力维度(ability)[ObjectArray]

    @SerializedName("tts")
    private TTS tts;

    @JsonIgnoreProperties
    private String qesInfo;



    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Accessory implements Serializable {
        //type = 101:
        //"options" : ["①②", "②④", "①③", "①②③④"]
        //type = 102:
        //"options" : ["[p]听到敲门声，我和妈妈[u]不约而同[/u]地跑去开门。准是爸爸出差回来了！可开门一看，是查水表的王叔叔。[/p]", "[p]学习成绩的提高得有个[u]循序渐进[/u]的过程，想一口吃成个胖子，那是不切实际的。 [/p]", "[p]目前，一些厂商在广告上弄虚作假，对自己的产品[u]大肆渲染[/u]，以此误导群众，骗取不义之财，这也是一种违法行为。 [/p]", "[p]日本军国主义者所发动的侵华战争给中国人民带来了深重的灾难，可是日本文部省却[u]别具匠心[/u]地一再修改日本中小学课本，掩盖战争罪行。 [/p]"]
        //
        //注意：
        //选项不带ABCD标号
        @SerializedName("type")
        private Long type; // 类型(type)[Integer]
        @SerializedName("options")
        private List<String> options; // 选项(options)[Array]
        @SerializedName("mode")
        private Long mode; // 显示方式(mode)[Integer]
        @SerializedName("speech")
        private List<String> speech; // 语音(speech)[StringArray]
        @SerializedName("label")
        private String label; // 标签(label)[String]
        @SerializedName("translation")
        private String translation; // 参考译文(translation)[XmlString]
        @SerializedName("words")
        private List<String> words; // 核心词汇(words)[StringArray]
        @SerializedName("transWords")
        private List<String> transWords; // 单词翻译(transWords)[StringArray]
        @SerializedName("groups")
        private List<Long> groups; // 填空位置分组(groups)[IntegerArray]
        public JSONObject toJSON() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", BaseUtil.getLong(type));
                jsonObject.put("mode", BaseUtil.getLong(mode));
                jsonObject.put("label", BaseUtil.getString(label));
                jsonObject.put("translation", BaseUtil.getString(translation));
                jsonObject.put("options", BaseUtil.strListToJsonArray(options));
                jsonObject.put("speech", BaseUtil.strListToJsonArray(speech));
                jsonObject.put("words", BaseUtil.strListToJsonArray(words));
                jsonObject.put("transWords", BaseUtil.strListToJsonArray(transWords));
                jsonObject.put("groups", BaseUtil.longListToJsonArray(groups));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        public String toString() {
            return "Accessory{" +
                    "type=" + type +
                    ", options=" + options +
                    ", mode=" + mode +
                    ", speech=" + speech +
                    ", label='" + label + '\'' +
                    ", translation='" + translation + '\'' +
                    ", words=" + words +
                    ", transWords=" + transWords +
                    ", groups=" + groups +
                    '}';
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public Long getMode() {
            return mode;
        }

        public void setMode(Long mode) {
            this.mode = mode;
        }

        public List<String> getSpeech() {
            return speech;
        }

        public void setSpeech(List<String> speech) {
            this.speech = speech;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }

        public List<String> getWords() {
            return words;
        }

        public void setWords(List<String> words) {
            this.words = words;
        }

        public List<String> getTransWords() {
            return transWords;
        }

        public void setTransWords(List<String> transWords) {
            this.transWords = transWords;
        }

        public List<Long> getGroups() {
            return groups;
        }

        public void setGroups(List<Long> groups) {
            this.groups = groups;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class SolutionAccessory extends Accessory {
        // 类型(type)[Integer]
        // 标签(label)[String]
        // 参考译文(translation)[XmlString]
        // 核心词汇(words)[StringArray]
        // 单词翻译(transWords)[StringArray]
        @SerializedName("content")
        private String content; // 内容(content)[XmlString]

        @Override
        public String toString() {
            return "SolutionAccessory{" +
                    "content='" + content + '\'' +
                    '}';
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class CorrectAnswerAccessory {
        @SerializedName("equal")
        private List<List<String>> equal; // 类似答案
        @SerializedName("label")
        private List<String> label; // 答案类型 "digit"=阿拉伯数字 "chinese"=汉字

        @Override
        public String toString() {
            return "CorrectAnswerAccessory{" +
                    "equal=" + equal +
                    ", label=" + label +
                    '}';
        }

        public List<List<String>> getEqual() {
            return equal;
        }

        public void setEqual(List<List<String>> equal) {
            this.equal = equal;
        }

        public List<String> getLabel() {
            return label;
        }

        public void setLabel(List<String> label) {
            this.label = label;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Material implements Serializable {
        @SerializedName("id")
        private String id; // 编号(id)[Integer]
        @SerializedName("content")
        private String content; // 内容(content)[xml]
        @SerializedName("accessory")
        private List<Accessory> accessory; // 附加(accessory)[ObjectArray] 对应大题的【题干附加(accessory)[ObjectArray]】
        public JSONObject toJSON() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", BaseUtil.strToLong(id));
                jsonObject.put("content", BaseUtil.getString(content));
                jsonObject.put("accessory", BaseUtil.accessoryListToJsonArray(accessory));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        public String toString() {
            return "Material{" +
                    "id='" + id + '\'' +
                    ", content='" + content + '\'' +
                    ", accessory=" + accessory +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<Accessory> getAccessory() {
            return accessory;
        }

        public void setAccessory(List<Accessory> accessory) {
            this.accessory = accessory;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class TTS {
        @SerializedName("status")
        private Long status;
        @SerializedName("content")
        private String content;
        @SerializedName("option")
        private List<String> option;
        @SerializedName("solution")
        private String solution;
        @SerializedName("idea")
        private String idea;
        @SerializedName("step")
        private String step;

        @Override
        public String toString() {
            return "TTS{" +
                    "status=" + status +
                    ", content='" + content + '\'' +
                    ", option=" + option +
                    ", solution='" + solution + '\'' +
                    ", idea='" + idea + '\'' +
                    ", step='" + step + '\'' +
                    '}';
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getOption() {
            return option;
        }

        public void setOption(List<String> option) {
            this.option = option;
        }

        public String getSolution() {
            return solution;
        }

        public void setSolution(String solution) {
            this.solution = solution;
        }

        public String getIdea() {
            return idea;
        }

        public void setIdea(String idea) {
            this.idea = idea;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }
    }

    @Override
    public String toString() {
        return "QuestionBean{" +
                "qid='" + qid + '\'' +
                ", no='" + no + '\'' +
                ", score='" + score + '\'' +
                ", id='" + id + '\'' +
                ", oid='" + oid + '\'' +
                ", grade=" + grade +
                ", subject=" + subject +
                ", courseId=" + courseId +
                ", type=" + type +
                ", category=" + category +
                ", difficulty=" + difficulty +
                ", origin='" + origin + '\'' +
                ", shortOrigin='" + shortOrigin + '\'' +
                ", from=" + from +
                ", ofrom=" + ofrom +
                ", keypoint=" + keypoint +
                ", testpoint=" + testpoint +
                ", role=" + role +
                ", relation=" + relation +
                ", content='" + content + '\'' +
                ", accessory=" + accessory +
                ", correctAnswer=" + correctAnswer +
                ", answer='" + answer + '\'' +
                ", solution='" + solution + '\'' +
                ", solutionAccessory=" + solutionAccessory +
                ", correctAnswerAccessory=" + correctAnswerAccessory +
                ", material=" + material +
                ", status=" + status +
                ", idea='" + idea + '\'' +
                ", step='" + step + '\'' +
                ", summary='" + summary + '\'' +
                ", opinion=" + opinion +
                ", quality=" + quality +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", recommendTime=" + recommendTime +
                ", section=" + section +
                ", page=" + page +
                ", ability=" + ability +
                ", tts=" + tts +
                ", qesInfo='" + qesInfo + '\'' +
                '}';
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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

    public Long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Long difficulty) {
        this.difficulty = difficulty;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getShortOrigin() {
        return shortOrigin;
    }

    public void setShortOrigin(String shortOrigin) {
        this.shortOrigin = shortOrigin;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getOfrom() {
        return ofrom;
    }

    public void setOfrom(Long ofrom) {
        this.ofrom = ofrom;
    }

    public List<KeyPoint> getKeypoint() {
        return keypoint;
    }

    public void setKeypoint(List<KeyPoint> keypoint) {
        this.keypoint = keypoint;
    }

    public List<TestPoint> getTestpoint() {
        return testpoint;
    }

    public void setTestpoint(List<TestPoint> testpoint) {
        this.testpoint = testpoint;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public List<String> getRelation() {
        return relation;
    }

    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Accessory> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<Accessory> accessory) {
        this.accessory = accessory;
    }

    public List<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(List<String> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<SolutionAccessory> getSolutionAccessory() {
        return solutionAccessory;
    }

    public void setSolutionAccessory(List<SolutionAccessory> solutionAccessory) {
        this.solutionAccessory = solutionAccessory;
    }

    public CorrectAnswerAccessory getCorrectAnswerAccessory() {
        return correctAnswerAccessory;
    }

    public void setCorrectAnswerAccessory(CorrectAnswerAccessory correctAnswerAccessory) {
        this.correctAnswerAccessory = correctAnswerAccessory;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Long> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<Long> opinion) {
        this.opinion = opinion;
    }

    public Long getQuality() {
        return quality;
    }

    public void setQuality(Long quality) {
        this.quality = quality;
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

    public Double getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(Double recommendTime) {
        this.recommendTime = recommendTime;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }

    public List<Page.PageData> getPage() {
        return page;
    }

    public void setPage(List<Page.PageData> page) {
        this.page = page;
    }

    public List<Ability> getAbility() {
        return ability;
    }

    public void setAbility(List<Ability> ability) {
        this.ability = ability;
    }

    public TTS getTts() {
        return tts;
    }

    public void setTts(TTS tts) {
        this.tts = tts;
    }

    public String getQesInfo() {
        return qesInfo;
    }

    public void setQesInfo(String qesInfo) {
        this.qesInfo = qesInfo;
    }
}
