package com.readboy.onlinecourseaides.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.readboy.onlinecourseaides.bean.response.item.GradeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mhc on 2017/12/6.
 */

public class GradeBean implements Parcelable , Serializable {

    public static String[] GRADE_NAME = {
            "一年级上",
            "一年级下",
            "二年级上",
            "二年级下",
            "三年级上",
            "三年级下",
            "四年级上",
            "四年级下",
            "五年级上",
            "五年级下",
            "六年级上",
            "六年级下",
            "七年级上",
            "七年级下",
            "八年级上",
            "八年级下",
            "九年级上",
            "九年级下",
            "必修1全一册",
            "必修2全一册"
    };

    private static List<GradeItem> gradeSource = new ArrayList<>();

    public static List<GradeItem> getGradeSource() {
        return gradeSource;
    }

    public static void setGradeSource(List<GradeItem> source) {
        gradeSource = source;
    }

    public static GradeItem getGradeSourceId(String source) {
        for (GradeItem item : gradeSource) {
            if(source.equals(item.getName())) {
                return item;
            }
        }
        return null;
    }

    public static int getGradeSourceSemester(String source) {
        for (GradeItem item : gradeSource) {
            if(source.equals(item.getName())) {
                return item.getSemester();
            }
        }
        return -1;
    }

    public static final int PRIMARY_SCHOOL_GRADE_COUNT = 6;
    public static final int JUNIOR_SCHOOL_GRADE_COUNT = 3;
    public static final int SENIOR_SCHOOL_GRADE_COUNT = 3;
    private static final int STAGE_PRIMARY_SCHOOL = 1;
    private static final int STAGE_JUNIOR_SCHOOL = 2;
    public static final int STAGE_SENIOR_SCHOOL = 3;
    private int mId;
    private String mName;

    public static int getId(String mName) {
        for (int i = 0; i < GRADE_NAME.length; i++) {
            if (mName.equals(GRADE_NAME[i])) {
                return i;
            }
        }
        return 1;
    }

    public static String getGradeName(int index) {
        if(index <= 0)
            return GRADE_NAME[0];
        else
            return GRADE_NAME[index];
    }

    public static void setGradeName(String[] gradeName) {
        GRADE_NAME = gradeName;
    }

    public GradeBean() {
    }

    public GradeBean(int gradeId) {
        setId(gradeId);
    }

    /**
     *
     * @return 年级id (从1开始，1代表一年级，6代表六年级)
     */
    public int getId() {
        return mId;
    }

    /**
     *
     * @param mId 年级id(从1开始，1代表一年级，6代表六年级（小学），7代表六年级(初中)，8代表七年级)
     */
    public void setId(int mId) {
        if(mId > GRADE_NAME.length || mId < 1)
        {
            mId = 1;
        }
        this.mId = mId;
    }

    public String getName() {
        return GRADE_NAME[mId - 1];
    }

    public void setName(String mName) {
        this.mName = mName;
    }


    public static int getStageFromGradeIndex(int gradeIndex)
    {
        if(gradeIndex < PRIMARY_SCHOOL_GRADE_COUNT)
        {
            return STAGE_PRIMARY_SCHOOL;
        }
        else if(gradeIndex < PRIMARY_SCHOOL_GRADE_COUNT+JUNIOR_SCHOOL_GRADE_COUNT)
        {
            return STAGE_JUNIOR_SCHOOL;
        }
        else
        {
            return STAGE_SENIOR_SCHOOL;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
    }

    protected GradeBean(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
    }

    public static final Creator<GradeBean> CREATOR = new Creator<GradeBean>() {
        @Override
        public GradeBean createFromParcel(Parcel source) {
            return new GradeBean(source);
        }

        @Override
        public GradeBean[] newArray(int size) {
            return new GradeBean[size];
        }
    };
}
