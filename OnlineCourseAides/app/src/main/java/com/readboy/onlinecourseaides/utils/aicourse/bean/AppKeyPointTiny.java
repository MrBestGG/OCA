package com.readboy.onlinecourseaides.utils.aicourse.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AppKeyPointTiny {
    @SerializedName("keypoint_id")
    private String keypointId;
    @SerializedName("keypoint_name")
    private String keypointName;
    @SerializedName("score")
    private Long score;

    @Override
    public String toString() {
        return "AppKeyPointTiny{" +
                "keypointId='" + keypointId + '\'' +
                ", keypointName='" + keypointName + '\'' +
                ", score=" + score +
                '}';
    }

    public String getKeypointId() {
        return keypointId;
    }

    public void setKeypointId(String keypointId) {
        this.keypointId = keypointId;
    }

    public String getKeypointName() {
        return keypointName;
    }

    public void setKeypointName(String keypointName) {
        this.keypointName = keypointName;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
