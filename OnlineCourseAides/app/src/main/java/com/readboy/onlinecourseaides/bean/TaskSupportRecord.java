package com.readboy.onlinecourseaides.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 历史记录类
 */
public class TaskSupportRecord implements Serializable {

    private String time;// 日期 2022-11-05
    private String packageName;// 应用包名
    private String appName;
    private String learningTime;// 学习时长
    private int interceptMsgNumber;// 拦截信息量
    private String eyeCareTime;// 护眼时长
    private String speedTime; // 加速时长
    private List<SoundRecord> soundRecords;
    private List<ScreenRecord> screenRecords;
    private List<ScreenShotRecord> screenShotPaths;
    private ExercisesRecord exercisesRecords;//诊断结果，精准学做练习返回数据
    private boolean isFirst;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public TaskSupportRecord() {
        soundRecords = new ArrayList<>();
        screenRecords = new ArrayList<>();
        screenShotPaths = new ArrayList<>();
        exercisesRecords = new ExercisesRecord();
    }

    @Override
    public String toString() {
        return "TaskSupportRecord{" +
                "time='" + time + '\'' +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", learningTime='" + learningTime + '\'' +
                ", interceptMsgNumber=" + interceptMsgNumber +
                ", eyeCareTime='" + eyeCareTime + '\'' +
                ", speedTime='" + speedTime + '\'' +
                ", soundRecords=" + soundRecords +
                ", screenRecords=" + screenRecords +
                ", screenShotPaths=" + screenShotPaths +
                ", exercisesRecords=" + exercisesRecords +
                '}';
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(String learningTime) {
        this.learningTime = learningTime;
    }

    public int getInterceptMsgNumber() {
        return interceptMsgNumber;
    }

    public void setInterceptMsgNumber(int interceptMsgNumber) {
        this.interceptMsgNumber = interceptMsgNumber;
    }

    public String getEyeCareTime() {
        return eyeCareTime;
    }

    public void setEyeCareTime(String eyeCareTime) {
        this.eyeCareTime = eyeCareTime;
    }

    public String getSpeedTime() {
        return speedTime;
    }

    public void setSpeedTime(String speedTime) {
        this.speedTime = speedTime;
    }

    public List<SoundRecord> getSoundRecords() {
        return soundRecords;
    }

    public void setSoundRecords(List<SoundRecord> soundRecords) {
        this.soundRecords = soundRecords;
    }

    public List<ScreenRecord> getScreenRecords() {
        return screenRecords;
    }

    public void setScreenRecords(List<ScreenRecord> screenRecords) {
        this.screenRecords = screenRecords;
    }

    public List<ScreenShotRecord> getScreenShotPaths() {
        return screenShotPaths;
    }

    public void setScreenShotPaths(List<ScreenShotRecord> screenShotPaths) {
        this.screenShotPaths = screenShotPaths;
    }

    public ExercisesRecord getExercisesRecords() {
        return exercisesRecords;
    }

    public void setExercisesRecords(ExercisesRecord exercisesRecords) {
        this.exercisesRecords = exercisesRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskSupportRecord that = (TaskSupportRecord) o;
        return time.equals(that.time) && packageName.equals(that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, packageName);
    }
}
