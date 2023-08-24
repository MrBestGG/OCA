package com.readboy.onlinecourseaides.bean;

import com.readboy.onlinecourseaides.bean.response.item.VersionItem;

import java.io.Serializable;
import java.util.List;

public class BookVersionCache implements Serializable {
    private int versionId;
    private int grade;
    private int semester;
    private int subject;
    private List<VersionItem> versionList;

    @Override
    public String toString() {
        return "BookVersionCache{" +
                "versionId=" + versionId +
                ", grade=" + grade +
                ", semester=" + semester +
                ", subject=" + subject +
                ", versionList=" + versionList +
                '}';
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public List<VersionItem> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<VersionItem> versionList) {
        this.versionList = versionList;
    }
}
