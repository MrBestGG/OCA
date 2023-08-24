package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Region {
    @SerializedName("province")
    private String province; // 省编号(province)[Integer]
    @SerializedName("city")
    private String city; // 市编号(city)[Integer]
    @SerializedName("district")
    private String district; // 区/县编号(district)[Integer]
    @SerializedName("school")
    private String school; // 学校编号(school)[Integer]

    @Override
    public String toString() {
        return "Region{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", school='" + school + '\'' +
                '}';
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
