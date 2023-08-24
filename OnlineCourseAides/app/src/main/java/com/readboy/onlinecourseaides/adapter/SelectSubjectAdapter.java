package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.databinding.FloatSelectSubjectBinding;

import java.util.List;

public class SelectSubjectAdapter extends RecyclerView.Adapter<SelectSubjectAdapter.MyHolder> {

    private int currentId = 0;

    private FloatSelectSubjectBinding binding;

    private List<String> mData;

    private Context context;

    public SelectSubjectAdapter(Context context, List<String> mData, SelectSubjectListener listener) {
        this.context = context;
        this.mData = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FloatSelectSubjectBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        return new MyHolder(view);
    }

    @SuppressLint({"RecyclerView", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String source = mData.get(position);
        holder.titleSubject.setText(source);
        if(currentId == position) {
            holder.line.setVisibility(View.VISIBLE);
            holder.titleSubject.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            holder.line.setVisibility(View.INVISIBLE);
            holder.titleSubject.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.titleSubject.setOnClickListener(v->{
            currentId = position;
            listener.onSelectSubjectClick(currentId);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView titleSubject;

        View line;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            line = binding.floatSelectPointLine;
            titleSubject = binding.floatSelectTitle;
        }
    }

    public SelectSubjectListener getListener() {
        return listener;
    }

    public void setListener(SelectSubjectListener listener) {
        this.listener = listener;
    }

    private SelectSubjectListener listener;

    public interface SelectSubjectListener {
        void onSelectSubjectClick(int index);
    }

    public String getItem(int position) {
        return mData.get(position);
    }

    public String getCurrentItem() {
        if(currentId != -1) {
            return mData.get(currentId);
        }
        return mData.get(0);
    }

    public void setmData(List<String> mData) {
        this.mData = mData;
        refresh();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        notifyDataSetChanged();
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }
}
