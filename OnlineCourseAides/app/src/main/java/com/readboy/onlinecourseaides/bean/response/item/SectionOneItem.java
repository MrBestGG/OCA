package com.readboy.onlinecourseaides.bean.response.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.AppKeyPoint;
import com.readboy.onlinecourseaides.utils.aicourse.bean.AppMapping;
import com.readboy.onlinecourseaides.utils.aicourse.bean.AppTestPoint;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class SectionOneItem {

    @SerializedName("id")
    private String id;
    @SerializedName("section_id")
    private String sectionId;
    @SerializedName("book_id")
    private String bookId;
    @SerializedName("name")
    private String name;
    @SerializedName("level")
    private Long level;
    @SerializedName("my_test")
    private SectionTestBean myTest;
    @SerializedName("video")
    private List<SectionVideoBean> video;

    // ---------------------------------- SectionOneNewBean在levelInfo里面的变量
    @SerializedName("mapping")
    private AppMapping mapping;
    @SerializedName("keypoint")
    private List<AppKeyPoint> keypoint;
    @SerializedName("testpoint")
    private List<AppTestPoint> testpoint;
    @SerializedName("score")
    private Long score;
    // ----------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class SectionTestBean extends MyTestBean {
        @SerializedName("detail")
        private MyTestOnePreviewBean detail;
        @SerializedName("keypoint")
        private List<AppKeyPoint> keypoint;
        @SerializedName("testpoint")
        private List<AppTestPoint> testpoint;

        public MyTestOnePreviewBean getDetail() {
            return detail;
        }

        public void setDetail(MyTestOnePreviewBean detail) {
            this.detail = detail;
        }

        public List<AppKeyPoint> getKeypoint() {
            return keypoint;
        }

        public void setKeypoint(List<AppKeyPoint> keypoint) {
            this.keypoint = keypoint;
        }

        public List<AppTestPoint> getTestpoint() {
            return testpoint;
        }

        public void setTestpoint(List<AppTestPoint> testpoint) {
            this.testpoint = testpoint;
        }

        @Override
        public String toString() {
            return "SectionTestBean{" +
                    "detail=" + detail +
                    ", keypoint=" + keypoint +
                    ", testpoint=" + testpoint +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class MyTestBean {
        @SerializedName("id")
        private String id;
        @SerializedName("uid")
        private String uid;
        @SerializedName("section_id")
        private String sectionId;
        @SerializedName("test_duration")
        private Long testDuration;
        @SerializedName("score")
        private Integer score;
        @SerializedName("count")
        private Integer count;
        @SerializedName("correct")
        private Integer correct;
        @SerializedName("is_invalid")
        private Boolean isInvalid;
        @SerializedName("start_at")
        private Long startAt;
        @SerializedName("create_at")
        private String create_at;
        @SerializedName("keypoint_level")
        private String keypoint_level;
        @SerializedName("testpoint_level")
        private String testpoint_level;

        @Override
        public String toString() {
            return "MyTestBean{" +
                    "id='" + id + '\'' +
                    ", uid='" + uid + '\'' +
                    ", sectionId='" + sectionId + '\'' +
                    ", testDuration=" + testDuration +
                    ", score=" + score +
                    ", count=" + count +
                    ", correct=" + correct +
                    ", isInvalid=" + isInvalid +
                    ", startAt=" + startAt +
                    ", create_at='" + create_at + '\'' +
                    ", keypoint_level='" + keypoint_level + '\'' +
                    ", testpoint_level='" + testpoint_level + '\'' +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSectionId() {
            return sectionId;
        }

        public void setSectionId(String sectionId) {
            this.sectionId = sectionId;
        }

        public Long getTestDuration() {
            return testDuration;
        }

        public void setTestDuration(Long testDuration) {
            this.testDuration = testDuration;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getCorrect() {
            return correct;
        }

        public void setCorrect(Integer correct) {
            this.correct = correct;
        }

        public Boolean getInvalid() {
            return isInvalid;
        }

        public void setInvalid(Boolean invalid) {
            isInvalid = invalid;
        }

        public Long getStartAt() {
            return startAt;
        }

        public void setStartAt(Long startAt) {
            this.startAt = startAt;
        }

        public String getCreate_at() {
            return create_at;
        }

        public void setCreate_at(String create_at) {
            this.create_at = create_at;
        }

        public String getKeypoint_level() {
            return keypoint_level;
        }

        public void setKeypoint_level(String keypoint_level) {
            this.keypoint_level = keypoint_level;
        }

        public String getTestpoint_level() {
            return testpoint_level;
        }

        public void setTestpoint_level(String testpoint_level) {
            this.testpoint_level = testpoint_level;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public class MyTestOnePreviewBean extends MyTestOnePreviewTinyBean {
        @SerializedName("id")
        private String id;
        @SerializedName("uid")
        private String uid;
        @SerializedName("record_id")
        private String recordId;
        @SerializedName("type")
        private Long type;

        @Override
        public String toString() {
            return "MyTestOnePreviewBean{" +
                    "id='" + id + '\'' +
                    ", uid='" + uid + '\'' +
                    ", recordId='" + recordId + '\'' +
                    ", type=" + type +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public class MyTestOnePreviewTinyBean {

        @SerializedName("question_id")
        private String questionId;
        @SerializedName("score")
        private Long score;
        @SerializedName("start_at")
        private Long startAt;
        @SerializedName("duration")
        private Long duration;
        @SerializedName("is_pass")
        private Boolean isPass;
        @SerializedName("answer")
        private String answer;
        @SerializedName("is_correct")
        private Boolean isCorrect;

        private int level_app; // [1,3] ， 由于有补题逻辑，未必和服务器一致
        private int level_server; // [1,5] ,   服务器返回的等级
        private boolean is_similar; //是否为相似题

        private String multiQesRootId;

        @Override
        public String toString() {
            return "MyTestOnePreviewTinyBean{" +
                    "questionId='" + questionId + '\'' +
                    ", score=" + score +
                    ", startAt=" + startAt +
                    ", duration=" + duration +
                    ", isPass=" + isPass +
                    ", answer='" + answer + '\'' +
                    ", isCorrect=" + isCorrect +
                    ", level_app=" + level_app +
                    ", level_server=" + level_server +
                    ", is_similar=" + is_similar +
                    ", multiQesRootId='" + multiQesRootId + '\'' +
                    '}';
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public Long getScore() {
            return score;
        }

        public void setScore(Long score) {
            this.score = score;
        }

        public Long getStartAt() {
            return startAt;
        }

        public void setStartAt(Long startAt) {
            this.startAt = startAt;
        }

        public Long getDuration() {
            return duration;
        }

        public void setDuration(Long duration) {
            this.duration = duration;
        }

        public Boolean getPass() {
            return isPass;
        }

        public void setPass(Boolean pass) {
            isPass = pass;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public Boolean getCorrect() {
            return isCorrect;
        }

        public void setCorrect(Boolean correct) {
            isCorrect = correct;
        }

        public int getLevel_app() {
            return level_app;
        }

        public void setLevel_app(int level_app) {
            this.level_app = level_app;
        }

        public int getLevel_server() {
            return level_server;
        }

        public void setLevel_server(int level_server) {
            this.level_server = level_server;
        }

        public boolean isIs_similar() {
            return is_similar;
        }

        public void setIs_similar(boolean is_similar) {
            this.is_similar = is_similar;
        }

        public String getMultiQesRootId() {
            return multiQesRootId;
        }

        public void setMultiQesRootId(String multiQesRootId) {
            this.multiQesRootId = multiQesRootId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class SectionVideoBean {
        @SerializedName("video_id")
        private String videoId;
        @SerializedName("section_id")
        private String sectionId;
        @SerializedName("vid")
        private String vid;
        @SerializedName("name")
        private String name;
        @SerializedName("file_path")
        private String filePath;
        @SerializedName("file_name")
        private String fileName;
        @SerializedName("file_size")
        private Long fileSize;
        @SerializedName("duration")
        private Double duration;

        @Override
        public String toString() {
            return "SectionVideoBean{" +
                    "videoId='" + videoId + '\'' +
                    ", sectionId='" + sectionId + '\'' +
                    ", vid='" + vid + '\'' +
                    ", name='" + name + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", fileSize=" + fileSize +
                    ", duration=" + duration +
                    '}';
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getSectionId() {
            return sectionId;
        }

        public void setSectionId(String sectionId) {
            this.sectionId = sectionId;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }

        public Double getDuration() {
            return duration;
        }

        public void setDuration(Double duration) {
            this.duration = duration;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public SectionTestBean getMyTest() {
        return myTest;
    }

    public void setMyTest(SectionTestBean myTest) {
        this.myTest = myTest;
    }

    public List<SectionVideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<SectionVideoBean> video) {
        this.video = video;
    }

    public AppMapping getMapping() {
        return mapping;
    }

    public void setMapping(AppMapping mapping) {
        this.mapping = mapping;
    }

    public List<AppKeyPoint> getKeypoint() {
        return keypoint;
    }

    public void setKeypoint(List<AppKeyPoint> keypoint) {
        this.keypoint = keypoint;
    }

    public List<AppTestPoint> getTestpoint() {
        return testpoint;
    }

    public void setTestpoint(List<AppTestPoint> testpoint) {
        this.testpoint = testpoint;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SectionOneItem{" +
                "id='" + id + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", myTest=" + myTest +
                ", video=" + video +
                ", mapping=" + mapping +
                ", keypoint=" + keypoint +
                ", testpoint=" + testpoint +
                ", score=" + score +
                '}';
    }
}
