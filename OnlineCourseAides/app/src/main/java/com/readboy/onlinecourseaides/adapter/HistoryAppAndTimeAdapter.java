package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.AppAndTimeItem;
import com.readboy.onlinecourseaides.bean.TaskSupportRecord;
import com.readboy.onlinecourseaides.databinding.HistoryAppAndTimeItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/5
 */
public class HistoryAppAndTimeAdapter extends RecyclerView.Adapter<HistoryAppAndTimeAdapter.MyHolder> {
    private static final String TAG = "HistoryAppTime";

    private List<TaskSupportRecord> data;
    private int currentId = 1;
    private Context context;
    private HistoryAppAndTimeItemBinding binding;

    public HistoryAppAndTimeAdapter(Context context, List<TaskSupportRecord> mData, HistoryAppAndTimeListener listener) {
        this.context = context;
        data = new ArrayList<>();
        initData(mData);
        this.listener = listener;
    }

    private void initData(List<TaskSupportRecord> data) {
        boolean isFirst = true;
        String lastTime = "-1";
        ArrayList<TaskSupportRecord> records = new ArrayList<>();

        for (TaskSupportRecord record : data) {
            String time = record.getTime();
            if(time != null) {
                if(!lastTime.equals(time)) {
                    TaskSupportRecord record1 = new TaskSupportRecord();
                    record1.setTime(time);
                    record1.setFirst(true);
                    records.add(record1);
                }
                record.setFirst(false);
                records.add(record);
                lastTime = time;
            }
        }
        this.data.clear();
        this.data.addAll(records);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = HistoryAppAndTimeItemBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        return new MyHolder(view);
    }

    @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        if(data.get(position).isFirst()) {
            holder.appName.setText(data.get(position).getTime());
            holder.timeImg.setBackgroundResource(R.mipmap.time_record_img);
            holder.appName.setTextColor(context.getResources().getColor(R.color.blue));
            holder.appNameContent.setBackgroundResource(R.color.normal_100);
        }else {
            holder.appName.setText(data.get(position).getAppName());
            if(currentId == position) {
                holder.appNameContent.setBackgroundResource(R.drawable.history_select);
                holder.timeImg.setBackgroundResource(R.color.blue);
                holder.appName.setTextColor(context.getResources().getColor(R.color.white));
            }else {
                holder.appNameContent.setBackgroundResource(R.color.normal_100);
                holder.appName.setTextColor(context.getResources().getColor(R.color.black));
                holder.timeImg.setBackgroundResource(R.color.normal_100);
            }
        }

        holder.appNameContent.setOnClickListener(v->{
            if(!data.get(position).isFirst()) {
                Log.d(TAG, "setOnClickListener: position = "+position+", current="+currentId+", name = "+data.get(position));
                currentId = position;
                listener.onItemClick(data.get(currentId));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView timeImg;
        View appNameContent;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            appName = binding.historyItemAppName;
            timeImg = binding.historyItemAppTimeImg;
            appNameContent = binding.historyItemAppTimeContent;
        }
    }

    public int getCurrentId() {
        return currentId;
    }

    public List<TaskSupportRecord> getData() {
        return data;
    }

    public String getAppName(int index) {
        return data.get(index).getAppName();
    }

    private HistoryAppAndTimeListener listener;

    public interface HistoryAppAndTimeListener {
        void onItemClick(TaskSupportRecord index);
    }
}
