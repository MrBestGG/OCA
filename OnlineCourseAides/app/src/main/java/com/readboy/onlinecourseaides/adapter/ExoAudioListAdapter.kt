package com.readboy.onlinecourseaides.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.util.SmartGlideImageLoader
import com.readboy.onlinecourseaides.R
import com.readboy.onlinecourseaides.bean.SoundRecord
import com.readboy.onlinecourseaides.databinding.HistorySoundRecordItemBinding
import com.readboy.onlinecourseaides.utils.DialogUtils
import com.readboy.onlinecourseaides.utils.Logger
import com.readboy.onlinecourseaides.utils.MediaPlayerHelper

class ExoAudioListAdapter(
    data: MutableList<AudioItem>,
    mediaPlayerHelper: MediaPlayerHelper? = null
) : BaseQuickAdapter<ExoAudioListAdapter.AudioItem, ExoAudioListAdapter.AudioItemViewHolder>(
    R.layout.history_sound_record_item,
    data
) {

    lateinit var listener:HistorySoundRecordListener
    private var curFocusedPos = -1
    private var mMediaPlayerHelper: MediaPlayerHelper =
        mediaPlayerHelper ?: MediaPlayerHelper().apply {
            initPlayer(true, null, null)
        }

    //这里使用exo-ui的默认播放、暂停按钮
    private val exoPlayId = com.google.android.exoplayer2.ui.R.id.exo_play
    private val exoPauseId = com.google.android.exoplayer2.ui.R.id.exo_pause

    init {

        //使用默认按钮，在viewHolder创建的时候，PlayerControlView会给按钮添加点击监听，但是监听是private的我们
        //获取不到，在这里覆盖点击监听，再在convert方法手动调用自定义PlayerControlView暴露的callOnClick模拟按钮
        //的点击，让PlayerControlView完成播放、暂停的处理
        addChildClickViewIds(exoPlayId, exoPauseId)
        setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                exoPlayId -> {
                    when {
                        //恢复播放
                        curFocusedPos == position -> {
                            notifyItemChanged(curFocusedPos, KEY_PAYLOAD_PLAY)
                        }
                        mMediaPlayerHelper.getExoPlayer() != null -> {
                            //取消当前在播放的音频item
                            if (curFocusedPos > -1 && curFocusedPos < data.size) {
                                getItem(curFocusedPos).isFocused = false
                                notifyItemChanged(curFocusedPos, KEY_PAYLOAD_REMOVE_FOCUS)
                            }
                            //播放指定的音频
                            curFocusedPos = position
                            mMediaPlayerHelper.addSingleMediaSource(getItem(position).audioUri)
                            getItem(position).isFocused = true
                            notifyItemChanged(position, KEY_PAYLOAD_PREPARE_PLAY)
                        }
                        else -> {

                        }
                    }
                }
                exoPauseId -> {
                    //暂停播放
                    if (curFocusedPos == position) {
                        notifyItemChanged(curFocusedPos, KEY_PAYLOAD_PAUSE)
                    } else {
                        Logger.w(
                            "wocao",
                            "onItemChildClick: imposable case that an unfocused pause btn was clicked"
                        )
                    }
                }
            }
        }
    }

    override fun createBaseViewHolder(view: View): AudioItemViewHolder {
        return AudioItemViewHolder((view))
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: AudioItemViewHolder, item: AudioItem) {
        Logger.d("wocao", "convert: item ${item.audioUri}")

        val controller = holder.binding.playerController
        //PlayerControlView的内部实现，没法使用viewBinding
        controller.findViewById<TextView>(R.id.custom_exo_duration).text =
            MediaPlayerHelper.longToStringTime(item.duration)
        //复用的holder要重新绑定player
        if (item.isFocused) {
            holder.binding.playerController.player = mMediaPlayerHelper.getExoPlayer()
        } else {
            holder.binding.playerController.player = null
        }
        holder.binding.historySoundSoundToText.text = "【录音转文字】"
        holder.binding.historySoundTime.text = item.soundRecord.soundTime
        var itemPosition = getItemPosition(item)
        if(itemPosition != -1) {
            holder.binding.historySoundTitle.text = "【录音${item.index}】"
        }
        holder.binding.historyImgScreenshotsEditContent.setOnClickListener {
            listener.delete(item);
        }
        holder.binding.historySoundSoundToTextEdit.setOnClickListener {
        }
        if(item.soundRecord.soundScreenShot!=null) {
            Glide.with(context)
                    .load(item.soundRecord.soundScreenShot.uri)
                    .transform(CenterCrop(), RoundedCorners(15))
                    .error(R.mipmap.default_no_load)
                    .into(holder.binding.historySoundRecordImg);
            //            holder.binding.historySoundRecordImg.setImageURI(item.soundRecord.soundScreenShot.uri)
            holder.binding.historySoundRecordImg.setOnClickListener {
                if(item.soundRecord.soundScreenShot.uri != null) {
                    DialogUtils.getNoStatusBarXPopUp(context).asImageViewer(holder.binding.historySoundRecordImg, item.soundRecord.soundScreenShot.uri, SmartGlideImageLoader())
                            .show();
                }
            }
        }
        if(item.soundRecord.soundToText!=null || "" == item.soundRecord.soundToText) {
            holder.binding.historySoundText.setText("暂无内容")
        }else {
            holder.binding.historySoundText.text = item.soundRecord.soundToText
        }
    }

    override fun convert(holder: AudioItemViewHolder, item: AudioItem, payloads: List<Any>) {
        Logger.d("wocao", "convert: item ${item.audioUri}, payloads $payloads")

        if (payloads.isEmpty())
            return
        val payload = payloads[0] as Int
        //holder.binding.playerController.apply
        holder.binding.playerController.apply {
            val id = when (payload) {
                KEY_PAYLOAD_PREPARE_PLAY -> {
                    player = mMediaPlayerHelper.getExoPlayer()
                    exoPlayId
                }
                KEY_PAYLOAD_PLAY -> {
                    exoPlayId
                }
                KEY_PAYLOAD_PAUSE -> {
                    exoPauseId
                }
                KEY_PAYLOAD_REMOVE_FOCUS -> {
                    player = null
                    exoPauseId
                }
                else -> null
            }
            id?.let { callOnClick(findViewById(it)) }
        }
    }

    fun pause() {
        mMediaPlayerHelper.pause()
    }

    fun release() {
        mMediaPlayerHelper.release()
    }

    companion object {
        const val KEY_PAYLOAD_PREPARE_PLAY = 0
        const val KEY_PAYLOAD_PLAY = 1
        const val KEY_PAYLOAD_PAUSE = 2
        const val KEY_PAYLOAD_REMOVE_FOCUS = 3
    }

    data class AudioItem(val audioUri: String, val duration: Long) {
        var index = 0
        var isFocused = false
        lateinit var soundRecord: SoundRecord
    }

    class AudioItemViewHolder(view: View) : BaseViewHolder(view) {
//        val binding = LayoutItemAudioExampleBinding.bind(view)
        val binding = HistorySoundRecordItemBinding.bind(view);
    }

    interface HistorySoundRecordListener {
        fun onItemClick(index: Int)
        fun delete(index: AudioItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(data: MutableList<AudioItem>) {
        this.data = data;
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshView() {
        notifyDataSetChanged()
    }
}
