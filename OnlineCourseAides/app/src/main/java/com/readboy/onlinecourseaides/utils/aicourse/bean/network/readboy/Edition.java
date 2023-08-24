package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Edition {
    @SerializedName("id")
    private String id; // 版本编号(id)[Integer]
    @SerializedName("name")
    private String name; // 版本名(name)[String]

    @Override
    public String toString() {
        return "Edition{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
}
