package com.readboy.onlinecourseaides.utils.aicourse.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class QuestionPreviewBean {
    @SerializedName("question_id")
    private String questionId;
    @SerializedName("difficulty")
    private Long difficulty;
    @SerializedName("members")
    private Long members;
    @SerializedName("correct_members")
    private Long correctMembers;
    @SerializedName("testpoint_count")
    private Long testPointCount;

    @Override
    public String toString() {
        return "QuestionPreviewBean{" +
                "questionId='" + questionId + '\'' +
                ", difficulty=" + difficulty +
                ", members=" + members +
                ", correctMembers=" + correctMembers +
                ", testPointCount=" + testPointCount +
                '}';
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Long difficulty) {
        this.difficulty = difficulty;
    }

    public Long getMembers() {
        return members;
    }

    public void setMembers(Long members) {
        this.members = members;
    }

    public Long getCorrectMembers() {
        return correctMembers;
    }

    public void setCorrectMembers(Long correctMembers) {
        this.correctMembers = correctMembers;
    }

    public Long getTestPointCount() {
        return testPointCount;
    }

    public void setTestPointCount(Long testPointCount) {
        this.testPointCount = testPointCount;
    }

    public boolean isHaveVideo() {
        return false;
    }
}
