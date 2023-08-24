package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.databinding.FloatSelectCommonBinding;

import java.util.List;

public class SelectCommonAdapter extends RecyclerView.Adapter<SelectCommonAdapter.MyHolder> {

    FloatSelectCommonBinding binding;

    private int currentId = -1;
    private List<String> mData;
    private Context context;

    public SelectCommonAdapter(Context context, List<String> mData, SelectCommonListener listener) {
        this.context = context;
        this.mData = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FloatSelectCommonBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        return new MyHolder(view);
    }

    @Override
    @SuppressLint({"RecyclerView", "NotifyDataSetChanged"})
    public void onBindViewHolder(@NonNull MyHolder holder,  int position) {
        String source = mData.get(position);
        holder.title.setText(source);
        if(currentId == position) {
            Log.d(TAG, "setOnClickListener: change color position = "+position+", current="+currentId+", name = "+mData.get(position)+"this=>"+this);
            holder.title.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.title.setOnClickListener(v->{
            Log.d(TAG, "setOnClickListener: position = "+position+", current="+currentId+", name = "+mData.get(position)+"this=>" +this);
            currentId = position;
            listener.onSelectSubjectClick(currentId);
            notifyDataSetChanged();
        });
    }

    private static final String TAG = "SelectCommonAdapter";
    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.floatSelectPoint;
        }
    }

    private SelectCommonListener listener;

    public interface SelectCommonListener {
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

    public List<String> getmData() {
        return mData;
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
