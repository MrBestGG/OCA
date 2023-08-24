package com.readboy.onlinecourseaides.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhc on 2017/12/7.
 */

public class PointBean implements Serializable {
    public final static int LEVEL_CHAPTER = 0;
    public final static int LEVEL_NOCHILDE = 2;
    public final static int LEVEL_SECTION = LEVEL_CHAPTER+1;
    public final static int LEVEL_POINT = LEVEL_SECTION+1;
    private int mLevelIndex = LEVEL_CHAPTER;
    private String mId;
    private String mName;
    private List<PointBean> mChildren = new ArrayList<>();
    private int mIndex = 0;
    private boolean mIsLearned = false;
    private int mMasterStarNumber = 0;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public int getLevelIndex() {
        return mLevelIndex;
    }

    public void setLevelIndex(int mLevelIndex) {
        this.mLevelIndex = mLevelIndex;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }


    public List<PointBean> getChildren() {
        return mChildren;
    }

    public void setChildren(ArrayList<PointBean> mChildren) {
        this.mChildren = mChildren;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public boolean isLearned() {
        return mIsLearned;
    }

    public void setLearned(boolean mLearned) {
        mIsLearned = mLearned;
    }

    public int getMasterStarNumber() {
        return mMasterStarNumber;
    }

    public void setMasterStarNumber(int mMasterStarNumber) {
        this.mMasterStarNumber = mMasterStarNumber;
    }

    public void setInfo(JSONObject jobj)
    {
        if(jobj == null)
        {
            return;
        }
        int id = jobj.optInt("id", 0);
        String name = jobj.optString("name", "");
        if(jobj.has("children"))
        {
            JSONArray children = jobj.optJSONArray("children");
            for(int i = 0; i < children.length(); i++)
            {
                PointBean pointBean = new PointBean();
                pointBean.setLevelIndex(mLevelIndex+1);
                pointBean.setIndex(i);
                pointBean.setInfo(children.optJSONObject(i));
                getChildren().add(pointBean);

            }
        }
        setId(id+"");
        setName(name);
    }

    public PointBean() {
    }

    public PointBean(int mLevelIndex, String mId, String mName, List<PointBean> mChildren) {
        this.mLevelIndex = mLevelIndex;
        this.mId = mId;
        this.mName = mName;
        this.mChildren = mChildren;
    }

    @Override
    public String toString() {
        return "PointBean{" +
                "mLevelIndex=" + mLevelIndex +
                ", mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mChildren=" + mChildren +
                ", mIndex=" + mIndex +
                ", mIsLearned=" + mIsLearned +
                ", mMasterStarNumber=" + mMasterStarNumber +
                '}';
    }
}
