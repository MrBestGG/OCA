package com.readboy.onlinecourseaides.bean;

import java.io.Serializable;

/**
 * @Author jll
 * @Date 2022/11/30
 */
public class ExercisesRecord implements Serializable {
    private String name;
    private String bookId;
    private String sectionId;
    private String ExercisesLengthTime;

    @Override
    public String toString() {
        return "ExercisesRecord{" +
                "name='" + name + '\'' +
                ", bookId='" + bookId + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", ExercisesLengthTime=" + ExercisesLengthTime +
                '}';
    }

    public String getExercisesLengthTime() {
        return ExercisesLengthTime;
    }

    public void setExercisesLengthTime(String exercisesLengthTime) {
        ExercisesLengthTime = exercisesLengthTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
