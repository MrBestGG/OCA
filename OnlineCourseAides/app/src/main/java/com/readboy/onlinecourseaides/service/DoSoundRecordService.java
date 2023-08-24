package com.readboy.onlinecourseaides.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.readboy.onlinecourseaides.receiver.ReceiverMsgReceiver;
import com.readboy.onlinecourseaides.receiver.RecordCodeReceiver;
import com.readboy.onlinecourseaides.receiver.RecordReceiver;
import com.readboy.onlinecourseaides.utils.FileUtils;
import com.readboy.onlinecourseaides.utils.GlobalParam;
import com.readboy.onlinecourseaides.utils.NotifyUtils;
import com.readboy.onlinecourseaides.utils.VoiceAssistantUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 录音服务
 */
public class DoSoundRecordService extends Service implements Handler.Callback{
    private static final String TAG = "DoSoundRecordService";
    public static final String SOUND_RUNNING_TITLE = "音频录制";

    public static final int NOTIFY_CODE = 10092;

    // 记录是否关闭过语音助手，用于关闭后恢复
    private boolean closeVoiceAssistant = false;
    private Context mContext;
    private String fileName;
    private String filePath;
    private String soundPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA/sound";

    private Handler mHandler;
    private MediaRecorder mMediaRecorder;
    private RecordReceiver recordReceiver;
    private Notification notification;

    private boolean isSoundRecording = false;

    public DoSoundRecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        isSoundRecording = false;
        super.onCreate();
        Log.d(TAG, "onCreate: start" +isSoundRecording);
        mContext = getApplicationContext();
        initStart();
        notification = NotifyUtils.notifyServiceRunning(SOUND_RUNNING_TITLE, this);
        startForeground(NOTIFY_CODE, notification);
    }

    private void initStart() {
        mHandler = new Handler(Looper.getMainLooper());

        recordReceiver = new RecordReceiver(new RecordReceiver.RecordControlListener() {
            @Override
            public void doSoundRecordTask(String type) {
                if(type.equals(RecordReceiver.TYPE_START)) {
//                    checkFileExits();
//                    startSoundRecord();
                }else if(type.equals(RecordReceiver.TYPE_STOP)) {
                    stopSoundRecord();
                }
            }

            @Override
            public void doScreenRecordTask(String type) {

            }

            @Override
            public void doScreenShotsTask(String type) {

            }
        });

        // 停止服务接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RecordReceiver.STOP_SOUND_RECORD);
        intentFilter.addAction(RecordReceiver.START_SOUND_RECORD);
        registerReceiver(recordReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkFileExits();
        fileName = getFileName("OCA_SoundRecord_") + ".mp3";
        filePath = soundPath + "/" + fileName;

        startSoundRecord();
        return super.onStartCommand(intent, flags, startId);
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    private void stopSoundRecord() {
        Intent intent = new Intent(RecordCodeReceiver.ACTION_RECORD_RESULT);
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();

            intent.putExtra(RecordCodeReceiver.RESULT_FILE_PATH,filePath);
            intent.putExtra(RecordCodeReceiver.RESULT_FILE_NAME,fileName);
            intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_SUCCESS);

            // 刷新媒体库
            FileUtils.updatePhotoAlbum(getApplicationContext(), new File(filePath), "");//保存音频 刷新媒体库
            //成功保存到历史记录
            Log.d(TAG, "stopSoundRecord: stop");
        } catch (RuntimeException e) {
            Log.d(TAG, "stopSoundRecord: error");
            mMediaRecorder.reset();
            mMediaRecorder.release();
            intent = new Intent(RecordCodeReceiver.ACTION_RECORD_RESULT);
            intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_ERROR);
        }finally {
            // 录音服务调用
            intent.putExtra(RecordCodeReceiver.SERVICE_NAME, RecordCodeReceiver.SERVICE_NAME_SOUND);
            intent.putExtra(RecordCodeReceiver.SERVICE_TYPE, RecordCodeReceiver.SERVICE_TYPE_SOUND);

            sendBroadcast(intent);
            isSoundRecording = false;
            stopSelf();
        }
    }

    private void startSoundRecord() {
        if(!isSoundRecording) {
            isSoundRecording = true;
            mMediaRecorder =new MediaRecorder();
            try {
                /* ②setAudioSource/setVedioSource */
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// 设置麦克风
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.REMOTE_SUBMIX);// 设置麦克风
                /*
                 * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
                 * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
                 */
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setAudioSamplingRate(44100);

                mMediaRecorder.setOutputFile(filePath);

                /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mMediaRecorder.setAudioEncodingBitRate(96000);

                /* ③准备 */
                try {
                    mMediaRecorder.prepare();
                } catch (IOException e) {
                    Log.e(TAG, "prepare() failed");
                }
                /* ④开始 */
                mMediaRecorder.start();
                Log.d(TAG, "startSoundRecord: start " + fileName + "=>" + filePath);
            } catch (IllegalStateException e) {
                Log.d(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
            }
        }
    }

    private String getFileName(String pref) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = formatter.format(new Date(System.currentTimeMillis()));
        return pref + format;
    }

    /**
     * 检测文件夹不存在就创建
     */
    private boolean checkFileExits() {
        // 定义文件夹目录地址 公共目录含有自己创建的文件夹无法识别 /OnlineCourseAides  需要刷新媒体库
        String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA";
        String screenShotsFilePath = basePath + "/img";

        Log.d(TAG, "checkFileExits: newFile : ");

        File dest1Dir = new File(screenShotsFilePath);
        if (!dest1Dir.exists()) {
            dest1Dir.mkdirs();
        }
        File dest2Dir = new File(soundPath);
        if (!dest2Dir.exists()) {
            dest2Dir.mkdirs();
        }
        return true;
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        return false;
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: onDestroy");
        if(recordReceiver != null) {
            unregisterReceiver(recordReceiver);
        }
        if(isSoundRecording) {
            stopSoundRecord();
        }
        stopForeground(NOTIFY_CODE);
    }
}