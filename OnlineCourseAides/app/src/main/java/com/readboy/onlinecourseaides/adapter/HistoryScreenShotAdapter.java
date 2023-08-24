package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.util.SmartGlideImageLoader;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.ScreenShotRecord;
import com.readboy.onlinecourseaides.databinding.HirstoryImgshotItemBinding;
import com.readboy.onlinecourseaides.utils.DialogUtils;

import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/5
 */
public class HistoryScreenShotAdapter extends RecyclerView.Adapter<HistoryScreenShotAdapter.MyHolder> {

    private static final String TAG = "ScreenShotAdapter";

    private boolean isEdit = false;

    private List<ScreenShotRecord> data;
    private int currentId = 0;
    private Context context;
    private HirstoryImgshotItemBinding binding;

    public HistoryScreenShotAdapter(Context context, List<ScreenShotRecord> mData, HistoryScreenShotListener listener) {
        this.context = context;
        this.data = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = HirstoryImgshotItemBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new MyHolder(view);
    }

    int count = 0;
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if(isEdit) {
            holder.imageEdit.setVisibility(View.VISIBLE);
        }else {
            holder.imageEdit.setVisibility(View.INVISIBLE);
        }
        if(position < data.size()){
            CenterCrop centerCrop = new CenterCrop();
            RoundedCorners roundedCorners = new RoundedCorners(15);
//        holder.image.setImageURI(data.get(position).getUri());
            Glide.with(context)
                    .load(data.get(position).getUri())
                    .transform(centerCrop, roundedCorners)
                    .error(R.mipmap.default_no_load)
                    .into(holder.image);
        }

        holder.imageEdit.setOnClickListener(v->{
            listener.onItemClick(position);
        });

        holder.image.setOnClickListener(v->{
            DialogUtils.getNoStatusBarXPopUp(context)
                    .asImageViewer((ImageView) v, data.get(position).getUri(), new SmartGlideImageLoader())
                    .show();
            listener.selectedImg(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView imageEdit;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = binding.historyRecordShotImg;
            imageEdit = binding.historyRecordShotImgEdit;
        }
    }

    public int getCurrentId() {
        return currentId;
    }

    public List<ScreenShotRecord> getData() {
        return data;
    }

    public void setData(List<ScreenShotRecord> data) {
        this.data = data;
        refreshView();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshView() {
        notifyDataSetChanged();
    }

    private HistoryScreenShotListener listener;

    public interface HistoryScreenShotListener {
        void onItemClick(int index);
        void selectedImg(int index);
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        refreshView();
    }

    public void removeItem(int index) {
        data.remove(index);
        refreshView();
    }
}
