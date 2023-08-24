package com.readboy.onlinecourseaides.utils.aicourse.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy.Mapping;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AppMapping extends Mapping {

    @SerializedName("mapping_id")
    private String mapping_id;
    @SerializedName("section_id")
    private String section_id;

    @Override
    public String toString() {
        return "AppMapping{" +
                "mapping_id='" + mapping_id + '\'' +
                ", section_id='" + section_id + '\'' +
                '}';
    }

    public String getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(String mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}
