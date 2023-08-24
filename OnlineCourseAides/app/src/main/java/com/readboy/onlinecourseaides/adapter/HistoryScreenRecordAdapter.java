package com.readboy.onlinecourseaides.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.lxj.xpopup.util.SmartGlideImageLoader;
import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.activity.VideoPlayerActivity;
import com.readboy.onlinecourseaides.bean.ScreenRecord;
import com.readboy.onlinecourseaides.databinding.HistoryScreenRecordItemBinding;
import com.readboy.onlinecourseaides.ui.OnclickBigImgDialog;
import com.readboy.onlinecourseaides.utils.DialogUtils;
import com.readboy.onlinecourseaides.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jll
 * @Date 2022/12/5
 */
public class HistoryScreenRecordAdapter extends RecyclerView.Adapter<HistoryScreenRecordAdapter.MyHolder> {

    private static final String TAG = "ScreenRecordAdapter";

    private List<ScreenRecord> data;
    private int currentId = 0;
    private Context context;
    private HistoryScreenRecordItemBinding binding;

    private MediaController mMediaController;

    public HistoryScreenRecordAdapter(Context context, List<ScreenRecord> mData, HistoryScreenRecordListener listener) {
        this.context = context;
        this.data = mData;
        this.listener = listener;
        mMediaController = new MediaController(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(HistoryScreenRecordItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    private boolean isPlayer = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CenterCrop centerCrop = new CenterCrop();
        RoundedCorners roundedCorners = new RoundedCorners(15);
        holder.binding.historySoundTitle.setText("【录屏"+data.get(position).index+"】");
        if (data.get(position).getScreenRecordScreenShot() != null) {
            Glide.with(context)
                    .load(data.get(position).getScreenRecordScreenShot().getUri())
                    .transform(centerCrop, roundedCorners)
                    .error(R.mipmap.default_no_load)
                    .into(holder.binding.historyScreenRecordImg);
//            holder.binding.historyScreenRecordImg.setImageURI(data.get(position).getScreenRecordScreenShot().getUri());
            holder.binding.historyScreenRecordImg.setOnClickListener(v -> {
                if(data.get(position).getScreenRecordScreenShot().getUri() != null) {
                    DialogUtils.getNoStatusBarXPopUp(context)
                            .asImageViewer((ImageView) v, data.get(position).getScreenRecordScreenShot().getUri(), new SmartGlideImageLoader())
                            .show();
                }
            });
        }

        // 加载第一帧到ImageView
        Glide.with(context)
                .load(data.get(position).getUri())
                .transform(centerCrop, roundedCorners)
                .error(R.mipmap.default_no_load)
                .into(holder.binding.historyScreenRecordVideo);
        holder.binding.historyScreenRecordVideo.setOnClickListener(v -> {
            listener.onItemClick(position);
            Uri uri = data.get(position).getUri();
            Log.d(TAG, "onBindViewHolder: video uri=> " + uri);

            Intent intent = new Intent(context, VideoPlayerActivity.class);
            ArrayList<String> path = new ArrayList<>();
            path.add(uri.toString());
            ArrayList<String> title = new ArrayList<>();
            title.add(data.get(position).getFileName() + "历史记录");
            intent.putExtra(VideoPlayerActivity.VIDEO_PATH, path);
            intent.putExtra(VideoPlayerActivity.VIDEO_TITLE, title);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
        });
        holder.binding.historyScreenRecordCai.setOnClickListener(v -> {
            ScreenRecord record = data.get(position);
            OnclickBigImgDialog dialog = new OnclickBigImgDialog(context);
            dialog.setImgUri(record.getScreenRecordScreenShot().getUri());
            dialog.setListener((uri, bit) -> {
                String imgPath = FileUtils.getBasePath() + "/img/";
                String fileName = "cai_" + record.getScreenRecordScreenShot().getFileName();
                String path = imgPath + fileName;
                File file = new File(path);
                FileUtils.saveBitmapAsPng(bit, file, () -> {
                    Uri uri1 = null;
                    Log.d(TAG, "historyScreenRecordCai: path=>" + path);
                    FileUtils.updatePhotoAlbum(context, new File(path), "");//刷新媒体库
                    record.getScreenRecordScreenShot().setFileName(fileName);
                    record.getScreenRecordScreenShot().setImgPath(path);
                    listener.doChangeImgSize(position);
                });
            });
            dialog.show();
        });
        holder.binding.historyImgScreenshotsEditContent.setOnClickListener(v -> {
            listener.delRecord(data.get(position));
        });
        holder.binding.historyScreenTime.setText(data.get(position).getScreenRecordTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        HistoryScreenRecordItemBinding binding;

        public MyHolder(HistoryScreenRecordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public int getCurrentId() {
        return currentId;
    }

    public List<ScreenRecord> getData() {
        return data;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ScreenRecord> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshView() {
        notifyDataSetChanged();
    }

    private HistoryScreenRecordListener listener;

    public interface HistoryScreenRecordListener {
        void onItemClick(int index);

        void doChangeImgSize(int index);

        void delRecord(ScreenRecord index);
    }
}
