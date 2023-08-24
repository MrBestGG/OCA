package com.readboy.onlinecourseaides.adapter;

import static com.readboy.onlinecourseaides.utils.Logger.d;
import static com.readboy.onlinecourseaides.utils.Logger.e;
import static com.readboy.onlinecourseaides.utils.Logger.v;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readboy.onlinecourseaides.R;
import com.readboy.onlinecourseaides.bean.SoundRecord;
import com.readboy.onlinecourseaides.bean.TaskSupportRecord;
import com.readboy.onlinecourseaides.databinding.HistoryScreenRecordItemBinding;
import com.readboy.onlinecourseaides.databinding.HistorySoundRecordItemBinding;
import com.readboy.onlinecourseaides.utils.MediaPlayerHelper;
import com.readboy.onlinecourseaides.utils.SingleClicker;

import org.w3c.dom.Text;

import java.util.List;

import kotlin.Unit;

/**
 * @Author jll
 * @Date 2022/12/5
 * 暂时废弃
 */
public class HistorySoundAdapter extends RecyclerView.Adapter<HistorySoundAdapter.MyHolder> {

    private static final String TAG = "HistorySoundAdapter";

    private MediaPlayerHelper mediaPlayerHelper;

    private int currentId = -1;
    private int lastId = -1;
    public static final int START_PLAY = 1;
    public static final int STOP_PLAY = 2;
    private int status = STOP_PLAY;

    private List<SoundRecord> data;
    private Context context;
    private HistorySoundRecordItemBinding binding;

    public HistorySoundAdapter(Context context, List<SoundRecord> mData, HistorySoundRecordListener listener) {
        this.context = context;
        this.data = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = HistorySoundRecordItemBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new MyHolder(view);
    }

    private MyHolder currentHolder;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        SoundRecord record = data.get(position);
        holder.soundRecordTime.setText(record.getSoundTime());
        holder.soundLengthText.setText(record.getSoundLengthTime()+"");

        if(currentId == position) {
            currentHolder = holder;
            Log.d(TAG, "onBindViewHolder: status" +status);
            if(status == START_PLAY) {
                holder.controlBtn.setBackgroundResource(R.drawable.sound_start);
            }else {
                holder.controlBtn.setBackgroundResource(R.drawable.sound_stop);
            }
        }else {
            holder.controlBtn.setBackgroundResource(R.drawable.sound_stop);
        }
        holder.controlBtn.setOnClickListener(new SingleClicker(500, (v) -> {
            prepareMediaPlayer(holder.seekBar, holder.soundProgressText,data.get(position).getSoundPath());
            Log.d(TAG, "onBindViewHolder: pid = "+ position +" cid = "+currentId);
            if(currentId == position) {
                if(status == STOP_PLAY) {
                    status = START_PLAY;
                }else {
                    status = STOP_PLAY;
                }
            }else {
                status = START_PLAY;
                mediaPlayerHelper.pause();
            }
            if (mediaPlayerHelper != null) {
                mediaPlayerHelper.switchPlayPause();
            }
            currentId = position;
            notifyDataSetChanged();
            return Unit.INSTANCE;
        }));

        //进度条交互处理
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if(currentHolder != null) {
                        currentHolder.soundProgressText.setText(MediaPlayerHelper.Companion.longToStringTime(progress));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayerHelper != null) {
                    mediaPlayerHelper.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayerHelper != null) {
                    d("wocao", "onStopTrackingTouch: " + seekBar.getProgress());
                    mediaPlayerHelper.seekToPosition(seekBar.getProgress());
                    mediaPlayerHelper.play();
                }
            }
        });

        holder.delBtn.setOnClickListener(v->{
            Log.d(TAG, "onBindViewHolder: del");
            listener.delete(position);
        });

        //编辑语音转文字
        holder.editBtn.setOnClickListener(v->{
        });

        if(record.getSoundScreenShot() != null) {
            holder.soundRecordImg.setImageURI(record.getSoundScreenShot().getUri());
        }
        if(record.getSoundToText() == null || "".equals(record.getSoundToText())) {
            holder.soundToText.setText("暂无内容");
        }else {
            holder.soundToText.setText(record.getSoundToText());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView delBtn;
        ImageView controlBtn;
        SeekBar seekBar;
        ImageView editBtn;
        TextView soundToText;
        TextView soundProgressText;
        TextView soundLengthText;
        TextView soundRecordTime;
        ImageView soundRecordImg;
        TextView soundToTextEdit;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            delBtn = binding.historySoundDelete;
            editBtn = binding.historySoundSoundToTextEdit;
            soundToText = binding.historySoundText;
            soundRecordTime = binding.historySoundTime;
            soundRecordImg = binding.historySoundRecordImg;
            soundToTextEdit = binding.historySoundSoundToText;
        }
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setData(List<SoundRecord> data) {
        this.data = data;
        refreshData();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData() {
        notifyDataSetChanged();
    }

    public List<SoundRecord> getData() {
        return data;
    }

    private HistorySoundRecordListener listener;

    public interface HistorySoundRecordListener {
        void onItemClick(int index);
        void delete(int index);
    }

    private void prepareMediaPlayer(SeekBar seekBar, TextView progressText, String path) {

        if (mediaPlayerHelper == null) {

            mediaPlayerHelper = new MediaPlayerHelper();
            mediaPlayerHelper.initPlayer(true, new MediaPlayerHelper.PlayerStateCallback() {
                @Override
                public void onPlaybackEnded() {
                    d(TAG, "onPlaybackEnded: ");
                    seekBar.setProgress(seekBar.getMax());
                }

                @Override
                public void onRefreshContentDuration(long duration) {
                    d(TAG, "onRefreshContentDuration: " + duration);
                    if (duration > 0){

                    }
                    seekBar.setMax((int) duration);
                }

                @Override
                public void onPlaybackTimeElapsed(long currentPosition) {
                    seekBar.setProgress((int) currentPosition);
                    progressText.setText(MediaPlayerHelper.Companion.longToStringTime(currentPosition));
                }
            }, null);
        }
        mediaPlayerHelper.addSingleMediaSource(path);
    }

    public void closeMediaSound() {
        if(mediaPlayerHelper != null) {
            mediaPlayerHelper.pause();
            mediaPlayerHelper.release();
        }
    }
}
