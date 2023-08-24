package com.readboy.onlinecourseaides.utils.aicourse.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class SimilarBean {

    @SerializedName("name")
    private String name;
    @SerializedName("question")
    private List<SimilarQuestion> question;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class SimilarQuestion {
        @SerializedName("question_id")
        private String questionId;
        @SerializedName("level")
        private Long level;
        public boolean isHaveVideo() {
            return false;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public Long getLevel() {
            return level;
        }

        public void setLevel(Long level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return "SimilarQuestion{" +
                    "questionId='" + questionId + '\'' +
                    ", level=" + level +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SimilarBean{" +
                "name='" + name + '\'' +
                ", question=" + question +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimilarQuestion> getQuestion() {
        return question;
    }

    public void setQuestion(List<SimilarQuestion> question) {
        this.question = question;
    }
}
