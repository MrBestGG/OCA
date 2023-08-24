package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考点(testpoint)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class TestPoint {

    @SerializedName("id")
    private String id; // 序号(id)[Integer]
    @SerializedName("name")
    private String name; // 名称(name)[String]
    @SerializedName("description")
    private String description; // 描述(description)[String]
    @SerializedName("source")
    private String source; // 知识点(source)[Integer]
    @SerializedName("explain")
    private List<Explain> explain; // 讲解(explain)[ObjectArray]
    @SerializedName("errorProne")
    private List<ErrorProne> errorProne; // 易错易混(errorProne)[ObjectArray]
    @SerializedName("status")
    private Long status; // 状态(status)[Integer]
    @SerializedName("createTime")
    private Long createTime; //创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private Long updateTime; //更新时间(updateTime)[Timestamp]
    @SerializedName("version")
    private Long version; //数据版本号(version)[Integer]
    @SerializedName("districtStat")
    private List<DistrictStat> districtStat; //地区统计(districtStat)[ObjectArray]
    @SerializedName("defaultDistrictStat")
    private List<DefaultDistrictStat> defaultDistrictStat; //默认地区统计(defaultDistrictStat)[Object]

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Explain {
        @SerializedName("example")
        private String example; // 例题(example)[Integer] (同步题库题目ID，和题目编辑界面关联考点无关)
        @SerializedName("video")
        private String video; // 视频(video)[Integer] (考点视频ID,考点视频库video.video_testpoint)

        @Override
        public String toString() {
            return "Explain{" +
                    "example='" + example + '\'' +
                    ", video='" + video + '\'' +
                    '}';
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class ErrorProne {
        @SerializedName("qid")
        private String qid; // 例题(qid)[Integer] (同步题库题目ID，和题目编辑界面关联考点无关)
        @SerializedName("description")
        private String description; // 描述(description)[String]

        @Override
        public String toString() {
            return "ErrorProne{" +
                    "qid='" + qid + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class DistrictStat {
        @SerializedName("district")
        private String district; // 地区id(district)[Integer]
        @SerializedName("scorePercent")
        private Double scorePercent; // 分值(scorePercent)[Double] (百分数*100，如50%计为50)
        @SerializedName("frequency")
        private Long frequency; // 频次(frequency)[Integer]

        @Override
        public String toString() {
            return "DistrictStat{" +
                    "district='" + district + '\'' +
                    ", scorePercent=" + scorePercent +
                    ", frequency=" + frequency +
                    '}';
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Double getScorePercent() {
            return scorePercent;
        }

        public void setScorePercent(Double scorePercent) {
            this.scorePercent = scorePercent;
        }

        public Long getFrequency() {
            return frequency;
        }

        public void setFrequency(Long frequency) {
            this.frequency = frequency;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class DefaultDistrictStat {
        @SerializedName("scorePercent")
        private Double scorePercent; // 分值(scorePercent)[Double] (百分数*100，如50%计为50)
        @SerializedName("frequency")
        private Long frequency; // 频次(frequency)[Integer]

        @Override
        public String toString() {
            return "DefaultDistrictStat{" +
                    "scorePercent=" + scorePercent +
                    ", frequency=" + frequency +
                    '}';
        }

        public Double getScorePercent() {
            return scorePercent;
        }

        public void setScorePercent(Double scorePercent) {
            this.scorePercent = scorePercent;
        }

        public Long getFrequency() {
            return frequency;
        }

        public void setFrequency(Long frequency) {
            this.frequency = frequency;
        }
    }


    @Override
    public String toString() {
        return "TestPoint{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", source='" + source + '\'' +
                ", explain=" + explain +
                ", errorProne=" + errorProne +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", districtStat=" + districtStat +
                ", defaultDistrictStat=" + defaultDistrictStat +
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Explain> getExplain() {
        return explain;
    }

    public void setExplain(List<Explain> explain) {
        this.explain = explain;
    }

    public List<ErrorProne> getErrorProne() {
        return errorProne;
    }

    public void setErrorProne(List<ErrorProne> errorProne) {
        this.errorProne = errorProne;
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

    public List<DistrictStat> getDistrictStat() {
        return districtStat;
    }

    public void setDistrictStat(List<DistrictStat> districtStat) {
        this.districtStat = districtStat;
    }

    public List<DefaultDistrictStat> getDefaultDistrictStat() {
        return defaultDistrictStat;
    }

    public void setDefaultDistrictStat(List<DefaultDistrictStat> defaultDistrictStat) {
        this.defaultDistrictStat = defaultDistrictStat;
    }
}
