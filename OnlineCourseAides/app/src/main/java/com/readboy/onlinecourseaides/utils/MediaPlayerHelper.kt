package com.readboy.onlinecourseaides.utils

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.readboy.onlinecourseaides.Application.MyApplication
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

class MediaPlayerHelper {
    private var mediaPlayer: MediaPlayer? = null
    private var exoPlayer: ExoPlayer? = null
    private var playerHandler: PlayerHandler? = null
    private var callback: PlayerStateCallback? = null

    fun initPlayer(
        isUserExoPlayer: Boolean = true,
        callback: PlayerStateCallback?,
        uri: String? = null
    ): MediaPlayerHelper {
        this.callback = callback
        if (isUserExoPlayer) {
            if (exoPlayer == null) {
                val ctx = MyApplication.getInstances()
                val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(ctx)

                val mediaSourceFactory: MediaSource.Factory =
                    DefaultMediaSourceFactory(mediaDataSourceFactory)
                exoPlayer = ExoPlayer.Builder(ctx)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .build().apply {
                        repeatMode = Player.REPEAT_MODE_OFF
                        playWhenReady = false
                        addListener(object : Player.Listener {
                            override fun onPlaybackStateChanged(playbackState: Int) {
                                //通知使用者处理音视频播放状态变化
                                Logger.d(
                                    TAG,
                                    "onPlaybackStateChanged: ${getPlayerStateStr(playbackState)}"
                                )
                                when (playbackState) {
                                    Player.STATE_READY -> {
                                        Logger.d(
                                            TAG,
                                            "onPlaybackStateChanged: duration ${exoPlayer?.duration}, content duration ${exoPlayer?.contentDuration}"
                                        )
                                    }
                                    Player.STATE_ENDED -> {
//                                        playWhenReady = false
                                        playerHandler?.removeMessages(PlayerHandler.MSG_TIMER_PLAYING)
                                        this@MediaPlayerHelper.callback?.onPlaybackEnded()
                                    }

                                }
                            }

                        })
                    }
            } else {
                // TODO: 2022/12/6 do nothing by now
            }
            if (playerHandler == null) {
                playerHandler = PlayerHandler(null, exoPlayer, callback)
            } else {
                playerHandler!!.init(null, exoPlayer, callback)
            }
            if (uri != null) {
                addSingleMediaSource(uri)
            }
        } else {

        }

        return this
    }

    /**
     * 清空旧的播放列表，添加新的媒体源，并重置进度条
     */
    fun addSingleMediaSource(uri: String): MediaPlayerHelper {
        if (exoPlayer != null) {
            val mediaDataSourceFactory = DefaultDataSource.Factory(MyApplication.getInstances())

            val mediaSource: MediaSource =
                ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

            if (playerHandler != null) {
                mediaSource.addEventListener(playerHandler!!, object : MediaSourceEventListener {
                    override fun onLoadStarted(
                        windowIndex: Int,
                        mediaPeriodId: MediaSource.MediaPeriodId?,
                        loadEventInfo: LoadEventInfo,
                        mediaLoadData: MediaLoadData
                    ) {
                        super.onLoadStarted(
                            windowIndex,
                            mediaPeriodId,
                            loadEventInfo,
                            mediaLoadData
                        )
                        Logger.d(
                            TAG,
                            "onLoadStarted: event info $loadEventInfo, load data $mediaLoadData, " +
                                    "duration ${exoPlayer?.duration}, content duration ${exoPlayer?.contentDuration}"
                        )
                    }

                    override fun onLoadCompleted(
                        windowIndex: Int,
                        mediaPeriodId: MediaSource.MediaPeriodId?,
                        loadEventInfo: LoadEventInfo,
                        mediaLoadData: MediaLoadData
                    ) {
                        super.onLoadCompleted(
                            windowIndex,
                            mediaPeriodId,
                            loadEventInfo,
                            mediaLoadData
                        )
                        Logger.d(
                            TAG,
                            "onLoadCompleted: event info $loadEventInfo, load data $mediaLoadData, " +
                                    "duration ${exoPlayer?.duration}, content duration ${exoPlayer?.contentDuration}"
                        )
                        callback?.onRefreshContentDuration(exoPlayer?.contentDuration ?: 0)
                    }

                    override fun onLoadCanceled(
                        windowIndex: Int,
                        mediaPeriodId: MediaSource.MediaPeriodId?,
                        loadEventInfo: LoadEventInfo,
                        mediaLoadData: MediaLoadData
                    ) {
                        super.onLoadCanceled(
                            windowIndex,
                            mediaPeriodId,
                            loadEventInfo,
                            mediaLoadData
                        )
                    }

                    override fun onLoadError(
                        windowIndex: Int,
                        mediaPeriodId: MediaSource.MediaPeriodId?,
                        loadEventInfo: LoadEventInfo,
                        mediaLoadData: MediaLoadData,
                        error: IOException,
                        wasCanceled: Boolean
                    ) {
                        super.onLoadError(
                            windowIndex,
                            mediaPeriodId,
                            loadEventInfo,
                            mediaLoadData,
                            error,
                            wasCanceled
                        )
                    }
                })
            }

            exoPlayer!!.apply {
                setMediaSource(mediaSource)
                prepare()
            }
        }

        return this
    }

    fun getPlaybackState() = exoPlayer?.playbackState ?: Int.MIN_VALUE

    fun getExoPlayer(): Player? = exoPlayer

    fun switchPlayPause() {
        val play = exoPlayer?.playWhenReady ?: false
        if (play) {
            pause()
        } else {
            play()
        }
    }

    fun getPlayStatus(): Boolean {
        return exoPlayer?.playWhenReady ?: false;
    }

    fun play() {
        exoPlayer?.apply {
            Logger.d(
                TAG,
                "play: duration ${exoPlayer?.duration}, content duration ${exoPlayer?.contentDuration}"
            )
            // TODO: 2022/12/6 很怪 其他状态回调都不能保证duration能获取到
            callback?.onRefreshContentDuration(exoPlayer?.contentDuration ?: 0)
            playWhenReady = true
            if (currentPosition >= duration) {
                seekToDefaultPosition()
            }
            playerHandler?.sendEmptyMessage(PlayerHandler.MSG_TIMER_PLAYING)
        }
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
        playerHandler?.removeMessages(PlayerHandler.MSG_TIMER_PLAYING)
    }

    @Deprecated(
        message = "尽量不要用这个，似乎对MediaSource的构建有要求，不然没效果，用PlaybackControllerView控制",
        level = DeprecationLevel.WARNING
    )
    fun seekToPosition(positionMs: Long) {
        try {
//            exoPlayer?.seekTo(0, positionMs)
            exoPlayer?.seekTo(positionMs)
//            exoPlayer?.setMediaItem(exoPlayer?.getMediaItemAt(0)!!, false)

//            exoPlayer?.prepare()
//            exoPlayer?.playWhenReady = true
//            playerHandler?.sendEmptyMessage(PlayerHandler.MSG_TIMER_PLAYING)
        } catch (e: Exception) {
            Logger.me(TAG, "seekToPosition: failed", e)
        }
    }

    fun release() {
        callback = null
        playerHandler?.removeMessages(PlayerHandler.MSG_TIMER_PLAYING)

        exoPlayer?.apply {
            playWhenReady = false
            release()
        }
        exoPlayer = null
        mediaPlayer?.apply {
            reset()
            release()
        }
        mediaPlayer = null
    }

    private fun getPlayerStateStr(state: Int) = when (state) {
        Player.STATE_IDLE -> "STATE_IDLE"
        Player.STATE_BUFFERING -> "STATE_BUFFERING"
        Player.STATE_READY -> "STATE_READY"
        Player.STATE_ENDED -> "STATE_ENDED"
        else -> "STATE_UNKNOWN"
    }

    interface PlayerStateCallback {
        fun onRefreshContentDuration(duration: Long) {}

        fun onPlaybackTimeElapsed(currentPosition: Long) {}

        fun onPlaybackEnded() {}

    }

    companion object {
        private const val TAG = "MediaPlayerHelper"

        @JvmStatic
        fun longToStringTime(timeLong: Long): String {
            var seconds = (timeLong + 500) / 1000 //+500是为了跟PlayerControlView的显示方式一致
            val res = StringBuilder()
            val h = seconds / 3600
            if (h in 1..9) res.append(0)
            if (h > 0) res.append(h).append(":")
            seconds %= 3600

            val m = seconds / 60
            if (m < 10) res.append(0)
            res.append(m).append(":")
            seconds %= 60

            if (seconds < 10) res.append(0)
            res.append(seconds)
            return res.toString()
        }

        /**
         * 背课文搞过来的读取音频文件总时长的方法，目前支持.wav, .mp3，时长默认返回0
         */
        @JvmStatic
        fun getMediaFileDuration(uris: Array<String>): Array<Long> {
            val durations = Array<Long>(uris.size) { 0 }
            if (uris.isEmpty())
                return durations
            val a = SystemClock.elapsedRealtime()
            val retriever = MediaMetadataRetriever()
            for (i in uris.indices) {
                val uri = uris[i]
                val f = File(uri)
                if (uri.endsWith(".wav")) {
                    //用于wav格式
                    if (f.exists()) {
                        Logger.d(TAG, "getMediaDuration: have wav: $uri")
                        durations[i] = WavMergeUtil.getWavLength(uri)
                    } else {

                    }
                } else if (uri.endsWith(".mp3")) {
                    //不限格式，速度大致比上面的慢50%
                    if (f.exists()) {
                        Logger.d(TAG, "getMergedRecordDuration: have mp3: $uri")
                        try {
                            retriever.setDataSource(uri)
                            durations[i] =
                                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
                                    .toLong()
                        } catch (e: Exception) {
                            Logger.mw(TAG, "getMediaDuration: failed", e)
                        }
                    } else {

                    }
                }
            }
            retriever.release()
            Logger.d(TAG, "getMediaDuration: finished in ${SystemClock.elapsedRealtime() - a} ms")

            return durations
        }
    }
}

class PlayerHandler(
    private var mediaPlayer: MediaPlayer?,
    private var exoPlayer: ExoPlayer?,
    callback: MediaPlayerHelper.PlayerStateCallback?,
    private var delayMillis: Long = 300L,
    looper: Looper = Looper.getMainLooper()
) : Handler(looper) {
    private var callbackRef = WeakReference(callback)

    companion object {
        private const val TAG = "MediaPlayerHelper"
        const val MSG_TIMER_PLAYING = 0
    }

    fun init(
        mediaPlayer: MediaPlayer?,
        exoPlayer: ExoPlayer?,
        callback: MediaPlayerHelper.PlayerStateCallback?
    ) {

        removeMessages(MSG_TIMER_PLAYING)
        this.mediaPlayer = mediaPlayer
        this.exoPlayer = exoPlayer
        this.callbackRef = WeakReference(callback)
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            //更新进度条
            MSG_TIMER_PLAYING -> {
                val curPos =
                    mediaPlayer?.currentPosition?.toLong() ?: exoPlayer?.currentPosition ?: 0L
                Logger.d(TAG, "handleMessage: cur pos $curPos")

                callbackRef.get()?.onPlaybackTimeElapsed(curPos)
                sendEmptyMessageDelayed(MSG_TIMER_PLAYING, delayMillis)
            }
        }
    }
}