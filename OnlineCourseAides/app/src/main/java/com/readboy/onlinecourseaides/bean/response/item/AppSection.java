package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AppSection implements ItemBean{

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("has_mapping")
    private Boolean hasMapping;
    @SerializedName("children")
    private List<AppSection> children;
    @SerializedName("study_status")
    private long studyStatus;
    @SerializedName("score")
    private long score;
    @SerializedName("childNode")
    private List<Object> childNode;
    @SerializedName("type")
    private int type;//1是第一个父节点 2是第二个节点 以此类推

    public void clearChildNode(){
        if(childNode!=null){
            childNode.clear();
        }
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

    public Boolean getHasMapping() {
        return hasMapping;
    }

    public void setHasMapping(Boolean hasMapping) {
        this.hasMapping = hasMapping;
    }

    public List<AppSection> getChildren() {
        return children;
    }

    public void setChildren(List<AppSection> children) {
        this.children = children;
    }

    public long getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(long studyStatus) {
        this.studyStatus = studyStatus;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public List<Object> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<Object> childNode) {
        this.childNode = childNode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AppSection{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hasMapping=" + hasMapping +
                ", children=" + children +
                ", studyStatus=" + studyStatus +
                ", score=" + score +
                ", childNode=" + childNode +
                ", type=" + type +
                '}';
    }
}
