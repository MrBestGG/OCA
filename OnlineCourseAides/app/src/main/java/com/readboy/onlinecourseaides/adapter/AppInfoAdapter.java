package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.AppInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.http.Url;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.MyHolder> {

    private Context context;

    private List<AppInfo> mData;
    private AppInfo currentAppInfo;
    private boolean isStartEditMode = false;
    private int delAppInfo;

    private boolean isEnabledAdd = false;

    public AppInfoAdapter(Context context, List<AppInfo> mData, AppInfoListener appInfoListener) {
        this.context = context;
        this.mData = mData;
        this.appInfoListener = appInfoListener;
    }

    @NonNull
    @Override
    public AppInfoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_app_item, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull AppInfoAdapter.MyHolder holder, @SuppressLint("RecyclerView") int position) {
        AppInfo appInfo = mData.get(position);
        holder.appTitle.setText(appInfo.getAppName());
//        RequestOptions options = new RequestOptions().error(R.drawable.ic_launcher_background).bitmapTransform(new RoundedCorners(30));//图片圆角为30
        CenterCrop centerCrop = new CenterCrop();
        RoundedCorners roundedCorners = new RoundedCorners(42);
        if(appInfo.appIconUrl == null || "".equals(appInfo.appIconUrl)) {
            if (appInfo.getAppIcon() == null) {
                Glide.with(context)
                        .load(context.getResources().getDrawable(R.mipmap.default_no_load))
                        .transform(centerCrop, roundedCorners)
                        .error(R.mipmap.default_no_load)
                        .placeholder(R.mipmap.default_no_load).into(holder.appIcon);
            } else {
                Glide.with(context)
                        .load(appInfo.getAppIcon())
                        .transform(centerCrop, roundedCorners)
                        .error(R.mipmap.default_no_load)
                        .placeholder(R.mipmap.default_no_load).into(holder.appIcon);
            }
        }else {
            Glide.with(context)
                    .load(appInfo.appIconUrl)
                    .transform(centerCrop, roundedCorners)
                    .error(R.mipmap.default_no_load)
                    .placeholder(R.mipmap.default_no_load).into(holder.appIcon);
        }
        if (appInfo.getType() == 1) {
            holder.appTag.setVisibility(View.INVISIBLE);
        }else if (appInfo.getType() == 2) {
            holder.appTag.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(v->{
            currentAppInfo = appInfo;
            appInfoListener.appInfoOnClick();
        });

        holder.appEdit.setOnClickListener(v->{
            delAppInfo = position;
            mData.remove(position);
            appInfoListener.delAppInfo();
            refreshView();
        });

        // 是否打开编辑模式
        if(isStartEditMode && appInfo.isEnabledDelAppInfo()) {
            holder.appEdit.setVisibility(View.VISIBLE);
        }else {
            holder.appEdit.setVisibility(View.GONE);
        }

        // 添加界面已经添加的列表
        if(isEnabledAdd && appInfo.isEnabledDelAppInfo()) {
            holder.appAdd.setVisibility(View.VISIBLE);
        }else {
            holder.appAdd.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView appTitle;

        ImageView appIcon;

        ImageView appTag;

        ImageView appEdit;

        ImageView appAdd;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            appTitle = itemView.findViewById(R.id.app_item_app_name);
            appIcon = itemView.findViewById(R.id.app_item_ic);
            appTag = itemView.findViewById(R.id.app_item_tag);
            appEdit = itemView.findViewById(R.id.app_more_del_img);
            appAdd = itemView.findViewById(R.id.app_more_del_img2);
        }
    }

    private AppInfoListener appInfoListener;

    public interface AppInfoListener {
        void appInfoOnClick();
        void delAppInfo();
    }

    public AppInfo getCurrentAppInfo() {
        return currentAppInfo;
    }

    public void setCurrentAppInfo(AppInfo currentAppInfo) {
        this.currentAppInfo = currentAppInfo;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<AppInfo> mData) {
        this.mData = mData;
        refreshView();
    }

    public List<AppInfo> refreshItemData(AppInfo data, int index) {
        if(index == 0) {
            mData.remove(index);
            mData.add(0, data);
        }else {
            mData.add(data);
        }
        refreshView();
        return mData;
    }

    public void setStartEditMode(boolean startEditMode) {
        isStartEditMode = startEditMode;
        refreshView();
    }

    public void setEnabledAdd(boolean enabledAdd) {
        isEnabledAdd = enabledAdd;
        refreshView();
    }

    public int getDelAppInfo() {
        return delAppInfo;
    }

    public void refreshView() {
        notifyDataSetChanged();
    }
}
