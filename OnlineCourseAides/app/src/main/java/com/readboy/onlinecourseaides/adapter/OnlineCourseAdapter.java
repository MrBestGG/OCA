package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.CourseInfo;

import java.util.HashMap;
import java.util.List;

public class OnlineCourseAdapter extends RecyclerView.Adapter<OnlineCourseAdapter.MyHolder> {

    private Context context;

    private CourseInfo currentPoint;
    private List<CourseInfo> mData;
    private HashMap<String, Integer> dataImg;
    private boolean isEnabledDelMode = false;
    private int delIndex;

    public OnlineCourseAdapter() {
    }

    public OnlineCourseAdapter(Context context, List<CourseInfo> mData, OnlineCourseListener listener) {
        this.context = context;
        dataImg = new HashMap<>();
        initImg();
        this.mData = mData;
        this.onlineCourseListener = listener;
    }

    private void initImg() {
        dataImg.put("广东",R.mipmap.online_course_guangdong);
        dataImg.put("广西",R.mipmap.online_course_guangxi);
        dataImg.put("浙江",R.mipmap.online_course_zhejiang);
        dataImg.put("山东",R.mipmap.online_course_shandong);
        dataImg.put("福建",R.mipmap.online_course_fujian);
        dataImg.put("海南",R.mipmap.online_course_hainan);
        dataImg.put("陕西",R.mipmap.online_course_shanxi);
        dataImg.put("河南",R.mipmap.online_course_henan);
        dataImg.put("江西",R.mipmap.online_course_jiangxi);
        dataImg.put("武汉",R.mipmap.online_course_hubei);
        dataImg.put("四川",R.mipmap.online_course_sichuan);
        dataImg.put("宁夏",R.mipmap.online_course_ningxia);
        dataImg.put("内蒙古",R.mipmap.online_course_neimenggu);
        dataImg.put("重庆",R.mipmap.online_course_chongqing);
        dataImg.put("山西",R.mipmap.online_course_shanxi2);
        dataImg.put("湖北",R.mipmap.online_course_hubei);
        dataImg.put("辽宁",R.mipmap.online_course_liaoning);
        dataImg.put("吉林",R.mipmap.online_course_jilin);
        dataImg.put("深圳",R.mipmap.online_course_shenzhen);
        dataImg.put("江苏",R.mipmap.online_course_jiangsu);
        dataImg.put("北京",R.mipmap.online_course_beijing);
        dataImg.put("黑龙江",R.mipmap.online_course_heilongjiang);
        dataImg.put("安徽",R.mipmap.online_course_anhui);
        dataImg.put("广州",R.mipmap.online_course_guangdong);
        dataImg.put("上海",R.mipmap.online_course_shanghai);
        dataImg.put("其他",R.mipmap.online_course_qita);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.online_course_item, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());

        Integer integer = dataImg.get(mData.get(position).getTitle());
        if(integer == null) {
            holder.imageView.setBackgroundResource(R.mipmap.online_course_qita);
        }else {
            holder.imageView.setBackgroundResource(integer);
        }
        holder.imageView.setOnClickListener(v->{
            setCurrentPoint(mData.get(position));
            onlineCourseListener.onCourseClick();
        });
        holder.delImg.setOnClickListener(v->{
            CourseInfo witheId = mData.get(position);
            delIndex = position;
            mData.remove(position);
            onlineCourseListener.delCourseClick(witheId);
            refreshView("onBindViewHolder");
        });
        if(isEnabledDelMode && mData.get(position).isEnabledDel()) {
            holder.delImg.setVisibility(View.VISIBLE);
        }else{
            holder.delImg.setVisibility(View.GONE);
        }
    }

    public int getDelIndex() {
        return delIndex;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imageView;
        ImageView delImg;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.online_course_title);
            imageView = itemView.findViewById(R.id.online_course_content_img);
            delImg = itemView.findViewById(R.id.class_more_del_img);
        }
    }

    private OnlineCourseListener onlineCourseListener;

    public interface OnlineCourseListener {
        void onCourseClick();
        void delCourseClick(CourseInfo index);
    }

    public CourseInfo getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(CourseInfo currentPoint) {
        this.currentPoint = currentPoint;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<CourseInfo> data) {
        this.mData = data;
        refreshView("refreshData");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItemData(CourseInfo data) {
        if(data != null) {
            mData.add(data);
        }
        refreshView("addItemData");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshView(String str) {
        Log.d("OnlineCourseAdapter", "refreshView: "+str);
        notifyDataSetChanged();
    }

    public List<CourseInfo> refreshItemData(CourseInfo data, int index) {
        if(index == 0) {
            mData.remove(index);
            mData.add(0, data);
        }else {
            mData.add(data);
        }
        refreshView("refreshItemData");
        return mData;
    }

    public void setData(List<CourseInfo> data){
        this.mData = data;
        refreshView("setData");
    }

    public void setEnabledDelMode(boolean enabledDelMode) {
        isEnabledDelMode = enabledDelMode;
        refreshView("setEnabledDelMode");
    }
}
