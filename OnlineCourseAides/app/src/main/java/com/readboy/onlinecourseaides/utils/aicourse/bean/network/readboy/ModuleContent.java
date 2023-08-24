package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class ModuleContent {
    @SerializedName("module")
    private Long module; // 模块(module)[Integer]
    @SerializedName("id")
    private String id; // 模块目录编号(id)[Integer]
    @SerializedName("name")
    private String name; // 模块目录名称(name)[String]
    @SerializedName("type")
    private String type; //模块目录类型(type)[Integer]
    @SerializedName("children")
    private List<ModuleContent> children; //模块目录子目录(children)[ObjectArray]

    @Override
    public String toString() {
        return "ModuleContent{" +
                "module=" + module +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", children=" + children +
                '}';
    }

    public Long getModule() {
        return module;
    }

    public void setModule(Long module) {
        this.module = module;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ModuleContent> getChildren() {
        return children;
    }

    public void setChildren(List<ModuleContent> children) {
        this.children = children;
    }
}
