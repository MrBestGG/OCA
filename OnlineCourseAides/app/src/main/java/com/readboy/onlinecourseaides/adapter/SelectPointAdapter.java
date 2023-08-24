package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.PointBean;
import com.readboy.onlinecourseaides.databinding.FloatSelectCommonBinding;
import com.readboy.onlinecourseaides.databinding.FloatSelectPointBinding;

import java.util.EventListener;
import java.util.List;

public class SelectPointAdapter extends RecyclerView.Adapter<SelectPointAdapter.MyHolder> {
//    FloatSelectCommonBinding binding;

    FloatSelectPointBinding binding;

    private int currentId = -1;
    private List<PointBean> mData;
    private Context context;

    public SelectPointAdapter(Context context, List<PointBean> mData, SelectPointListener listener) {
        this.context = context;
        this.mData = mData;
        initData();
        this.listener = listener;
    }

    private void initData() {
        for (int i = 0; i < mData.size(); i++) {
//            Log.d(TAG, "refreshSectionList: child"+mData.get(i).getName());
            int index = i;
            if(mData.get(i).getChildren() != null) {
                for (int j = 0; j < mData.get(i).getChildren().size(); j++) {
//                    Log.d(TAG, "refreshSectionList: child"+mData.get(i).getChildren().get(j).getName());
                    mData.add(++index,mData.get(i).getChildren().get(j));
                }
            }
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FloatSelectPointBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        return new MyHolder(view);
    }

    @Override
    @SuppressLint({"RecyclerView", "NotifyDataSetChanged", "SetTextI18n"})
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        PointBean pointBean = mData.get(position);
        String source = pointBean.getName();

        if(pointBean.getLevelIndex() == PointBean.LEVEL_CHAPTER || pointBean.getLevelIndex() == PointBean.LEVEL_NOCHILDE) {
            holder.title.setTextSize(22);
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
            holder.title.setText(source);
        }else if(pointBean.getLevelIndex() == PointBean.LEVEL_SECTION){
            holder.title.setTextSize(20);
            holder.title.setTypeface(Typeface.DEFAULT);
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
            holder.title.setText("  "+source);
        }

        if(currentId == position) {
            holder.content.setBackgroundResource(R.drawable.history_select);
            holder.title.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.content.setBackgroundResource(R.color.white);
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.title.setOnClickListener(v -> {
            if (pointBean.getLevelIndex() == PointBean.LEVEL_SECTION ||pointBean.getLevelIndex() == PointBean.LEVEL_NOCHILDE) {
                currentId = position;
                listener.onSelectSubjectClick(currentId);
                notifyDataSetChanged();
            }
            Log.d(TAG, "onBindViewHolder: setOnClickListener  point");
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView title;
        View content;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.floatSelectPoint;
            content = binding.floatSelectPointContent;
        }
    }

    private SelectPointListener listener;

    public interface SelectPointListener {
        void onSelectSubjectClick(int index);
    }

    public PointBean getItem(int position) {
        return mData.get(position);
    }

    public PointBean getCurrentItem() {
        if(currentId != -1) {
            return mData.get(currentId);
        }
        return mData.get(0);
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public void setmData(List<PointBean> mData) {
        this.mData = mData;
        refresh();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        for (int i = 0; i < mData.size(); i++) {
            int index = i;
            if(mData.get(i).getChildren() != null) {
                for (int j = 0; j < mData.get(i).getChildren().size(); j++) {
                    mData.add(++index,mData.get(i).getChildren().get(j));
                }
            }
        }
        notifyDataSetChanged();
    }

    private static final String TAG = "SelectPointAdapter";
}
