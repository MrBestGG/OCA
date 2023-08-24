package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Exercise {
    @SerializedName("id")
    private String id; // 教辅目录编号(id)[Integer]
    @SerializedName("name")
    private String name; // 教辅目录名称(name)[String]
    @SerializedName("children")
    private List<Exercise> children; // 教辅目录子目录(children)[ObjectArray]
    @SerializedName("content")
    private List<ModuleContent> content; // 模块目录内容(content)[ObjectArray]

    @Override
    public String toString() {
        return "Exercise{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                ", content=" + content +
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

    public List<Exercise> getChildren() {
        return children;
    }

    public void setChildren(List<Exercise> children) {
        this.children = children;
    }

    public List<ModuleContent> getContent() {
        return content;
    }

    public void setContent(List<ModuleContent> content) {
        this.content = content;
    }
}
