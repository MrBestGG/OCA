package com.readboy.onlinecourseaides.utils.aicourse.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AppKeyPoint extends AppKeyPointTiny {

    @SerializedName("count")
    private Long count; // 考点id列表
    @SerializedName("study_status")
    private Long studyStatus; // 0-未学；1-学过
    @SerializedName("exercise_status")
    private Long exercise_status; // 练习过就会标识为1 【SectionOne章节详情的数据】
    @SerializedName("has_video")
    private boolean has_video; // true-有视频数据 false-无视频数据

    @SerializedName("correct_rate")
    private int correctRate;    //正确率

    @SerializedName("videos")
    private List<KeyPointVideoBean> videos;

    @Override
    public String toString() {
        return "AppKeyPoint{" +
                "count=" + count +
                ", studyStatus=" + studyStatus +
                ", exercise_status=" + exercise_status +
                ", has_video=" + has_video +
                ", correctRate=" + correctRate +
                ", videos=" + videos +
                '}';
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(Long studyStatus) {
        this.studyStatus = studyStatus;
    }

    public Long getExercise_status() {
        return exercise_status;
    }

    public void setExercise_status(Long exercise_status) {
        this.exercise_status = exercise_status;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public int getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(int correctRate) {
        this.correctRate = correctRate;
    }

    public List<KeyPointVideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<KeyPointVideoBean> videos) {
        this.videos = videos;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class KeyPointVideoBean {
        @SerializedName("video_id")
        private String videoId;
        @SerializedName("vid")
        private String vid;
        @SerializedName("name")
        private String name;

        @Override
        public String toString() {
            return "KeyPointVideoBean{" +
                    "videoId='" + videoId + '\'' +
                    ", vid='" + vid + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
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
    }
}
