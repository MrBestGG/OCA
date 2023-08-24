package com.readboy.onlinecourseaides.bean.response.item;

import com.google.gson.annotations.SerializedName;

/**
 * 书本列表
 */
public class AppBookItem implements ItemBean {

    @SerializedName("id")
    private String id;
    @SerializedName("book_id")
    private String bookId;
    @SerializedName("book_name")
    private String bookName;
    @SerializedName("book_status")
    private Long bookStatus;
    @SerializedName("cover")
    private String cover;
    @SerializedName("semester")
    private Long semester;
    @SerializedName("edition_id")
    private String editionId;
    @SerializedName("edition_name")
    private String editionName;
    @SerializedName("press_id")
    private String pressId;
    @SerializedName("press_name")
    private String pressName;
    @SerializedName("grade")
    private Long grade;
    @SerializedName("subject")
    private Long subject;

    @Override
    public String toString() {
        return "AppBookBean{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookStatus=" + bookStatus +
                ", cover='" + cover + '\'' +
                ", semester=" + semester +
                ", editionId='" + editionId + '\'' +
                ", editionName='" + editionName + '\'' +
                ", pressId='" + pressId + '\'' +
                ", pressName='" + pressName + '\'' +
                ", grade=" + grade +
                ", subject=" + subject +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(Long bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Long getSemester() {
        return semester;
    }

    public void setSemester(Long semester) {
        this.semester = semester;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public String getPressId() {
        return pressId;
    }

    public void setPressId(String pressId) {
        this.pressId = pressId;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    @Override
    public String getName() {
        return null;
    }
}
