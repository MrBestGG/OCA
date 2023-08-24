package com.readboy.onlinecourseaides.utils.aicourse.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AppTestPoint extends AppTestPointTiny {
    // 这里没有获取question，只是结构体定义了所以返回了，数据格式见章节题目，应该都为NULL
    @SerializedName("question")
    private List<QuestionPreviewBean> question;
    @SerializedName("study_status")
    private Long studyStatus; // 0-未学；1-学过
    @SerializedName("exercise_status")
    private Long exercise_status; // 练习过就会标识为1 【SectionOne章节详情的数据】
    @SerializedName("has_question")
    private Boolean has_question;

    @SerializedName("correct_rate")
    private int correctRate;    //正确率

    @SerializedName("similar")
    private List<SimilarBean> similar;

    public AppTestPoint() {
    }

    @JsonIgnoreProperties
    public AppTestPoint(String name) {
        this.setTestpointName(name);
    }

    @Override
    public String toString() {
        return "AppTestPoint{" +
                "question=" + question +
                ", studyStatus=" + studyStatus +
                ", exercise_status=" + exercise_status +
                ", has_question=" + has_question +
                ", correctRate=" + correctRate +
                ", similar=" + similar +
                '}';
    }

    public List<QuestionPreviewBean> getQuestion() {
        return question;
    }

    public void setQuestion(List<QuestionPreviewBean> question) {
        this.question = question;
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

    public Boolean getHas_question() {
        return has_question;
    }

    public void setHas_question(Boolean has_question) {
        this.has_question = has_question;
    }

    public int getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(int correctRate) {
        this.correctRate = correctRate;
    }

    public List<SimilarBean> getSimilar() {
        return similar;
    }

    public void setSimilar(List<SimilarBean> similar) {
        this.similar = similar;
    }
}
