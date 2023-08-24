package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Chinese {
    @SerializedName("data")
    private List<String> data;  // 汉字(data)[StringArry]
    @SerializedName("type")
    private Long type;  // 类型(type)[Integer]
    @SerializedName("label")
    private String label;  // 标签(label)[String]

    @Override
    public String toString() {
        return "Chinese{" +
                "data=" + data +
                ", type=" + type +
                ", label='" + label + '\'' +
                '}';
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
