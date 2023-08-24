package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class GradeItem implements ItemBean{
    @SerializedName("grade")
    private int grade;
    @SerializedName("semester")
    private int semester;
    @SerializedName("name")
    private String name;

    @Override
    public String toString() {
        return "GradeItem{" +
                "grade=" + grade +
                ", semester=" + semester +
                ", name='" + name + '\'' +
                '}';
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeItem)) return false;
        GradeItem gradeItem = (GradeItem) o;
        return name.equals(gradeItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
