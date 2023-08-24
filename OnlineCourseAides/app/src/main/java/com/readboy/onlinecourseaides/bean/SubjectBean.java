package com.readboy.onlinecourseaides.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by mhc on 2017/12/6.
 */

public class SubjectBean implements Parcelable , Serializable {
    public static String[] SUBJECT_NAME = {
            "语文", "数学", "英语",
            "物理", "化学", "生物",
            "道德与法治", "历史", "地理"
    };

    public static int getId(String mName) {
        for (int i = 0; i < SUBJECT_NAME.length; i++) {
            if(SUBJECT_NAME[i].equals(mName)){
                return i;
            }
        }
        return 0;
    }

    public static String getSubjectName(int index) {
        if(index < 0 || index > SUBJECT_NAME.length) {
            return SUBJECT_NAME[0];
        }else {
            return SUBJECT_NAME[index];
        }
    }

    public static void setSubjectName(String[] subjectName) {
        SUBJECT_NAME = subjectName;
    }

    private int mId;
    private String mName;

    public SubjectBean() {
    }

    public SubjectBean(int mId) {
        setId(mId);
    }

    /**
     * @return 科目id(服务器科目没有0 ， 从1开始 ， 1代表语文 ， 语数外物化生政史地)
     */
    public int getId() {
        return mId;
    }

    /**
     * @param mId 科目id(从1开始，1代表语文，语数外物化生政史地)
     */
    public void setId(int mId) {
        if (mId < 0 || mId >= SUBJECT_NAME.length) {
            mId = 0;
        }
        this.mId = mId;
    }

    public String getName() {
        return SUBJECT_NAME[mId];
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

    protected SubjectBean(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
    }

    public static final Creator<SubjectBean> CREATOR = new Creator<SubjectBean>() {
        @Override
        public SubjectBean createFromParcel(Parcel source) {
            return new SubjectBean(source);
        }

        @Override
        public SubjectBean[] newArray(int size) {
            return new SubjectBean[size];
        }
    };
}
