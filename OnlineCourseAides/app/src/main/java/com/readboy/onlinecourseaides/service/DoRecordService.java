package com.readboy.onlinecourseaides.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.readboy.onlinecourseaides.Application.MyApplication;
import com.readboy.onlinecourseaides.activity.PermissionsActivity;
import com.readboy.onlinecourseaides.databinding.FloatToExercise2Binding;
import com.readboy.onlinecourseaides.databinding.FloatToExerciseBinding;
import com.readboy.onlinecourseaides.receiver.RecordCodeReceiver;
import com.readboy.onlinecourseaides.receiver.RecordReceiver;
import com.readboy.onlinecourseaides.service.record.DpiUtils;
import com.readboy.onlinecourseaides.utils.FileUtils;
import com.readboy.onlinecourseaides.utils.NotifyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 录屏和截图服务
 */
public class DoRecordService extends Service implements Handler.Callback {
    private static final String TAG = "DoRecordService";

    public static final String SCREEN_RUNNING_TITLE = "屏幕捕获录制服务";

    // 区分此服务的工作方式
    public static final String RECORD_TYPE = "RECORD_TYPE";
    public static final int RECORD_TYPE_RECORD_SCREEN = 1;
    public static final int RECORD_TYPE_SCREEN_SHOTS = 0;
    public static final int RECORD_TYPE_SCREEN_SHOTS_SOUND = 2;


    public static final int NOTIFY_CODE = 10092;

    private Context mContext;
    private String fileName;
    private String filePath;
    private String screenShotsFileName;
    private String screenShotsFilePath;

    private String videoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA/video";
    private String screenShotsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA/img";

    private Handler mHandler;
    private RecordReceiver recordReceiver;
    private Notification notification;
    // 录制服务
    private MediaProjection mMediaProjection;
    private MediaRecorder mMediaRecorder;
    private VirtualDisplay mVirtualDisplay;
    private Intent intentData = null;
    private int intentResCode = -1;

    int mWidthPixels = 720;
    int mHeightPixels = 480;
    int mScreenDensity = 1;

    int mWidthPixels2 = 1920;
    int mHeightPixels2 = 1080;
    int mScreenDensity2 = 1;

    private volatile boolean isRecording = false;

    public DoRecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        isRecording = false;
        super.onCreate();
        Log.d(TAG, "onCreate: start" + isRecording);
        mContext = getApplicationContext();
        initStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initStart() {
        mHandler = new Handler(Looper.getMainLooper());

        recordReceiver = new RecordReceiver(new RecordReceiver.RecordControlListener() {
            @Override
            public void doSoundRecordTask(String type) {

            }

            @Override
            public void doScreenRecordTask(String type) {
                if (type.equals(RecordReceiver.TYPE_STOP)) {
                    stopRecord();
                }
            }

            @Override
            public void doScreenShotsTask(String type) {

            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RecordReceiver.START_SCREEN_RECORD);
        intentFilter.addAction(RecordReceiver.STOP_SCREEN_RECORD);
        intentFilter.addAction(RecordReceiver.START_SCREEN_SHOTS);
        registerReceiver(recordReceiver, intentFilter);
        notification = NotifyUtils.notifyServiceRunning(SCREEN_RUNNING_TITLE, this);
        startForeground(NOTIFY_CODE, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intentData = null;
        intentResCode = -1;

        if (intent != null) {
            intentResCode = intent.getIntExtra("resultCode", -1);
            intentData = MyApplication.getInstances().getData();

            DpiUtils.getDpiWithAndHeight();
            // 获取默认支持分辨率
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            int w = metrics.widthPixels;
            int h = metrics.heightPixels;
            int d = metrics.densityDpi;
            mWidthPixels = intent.getIntExtra("mWidthPixels", 720);
            mHeightPixels = intent.getIntExtra("mHeightPixels", 480);

            if(DpiUtils.getDevicesType() == DpiUtils.DEVICES_TYPE_MTK){
                double x = mWidthPixels*1.00/mHeightPixels*1080.00;
                Log.d(TAG, "onStartCommand: x = "+x);
                double floor = Math.floor(x);
                Log.d(TAG, "onStartCommand: f = "+floor);
                mWidthPixels = (int) floor;
                mHeightPixels = 1080;
            }

            Log.d(TAG, "onCreate: w = "+w+", h = "+h+" d = "+d);
            Log.d(TAG, "onCreate  -->  mScreenDensity: " + mScreenDensity + "  mWidthPixels: " + mWidthPixels + "  mHeightPixels: " + mHeightPixels + "  resultCode: " + intentResCode + "  data: " + intentData);

            mScreenDensity = intent.getIntExtra("mDensityDpi", 1);
            Log.d(TAG, "onCreate  -->  mScreenDensity: " + mScreenDensity + "  mWidthPixels: " + mWidthPixels + "  mHeightPixels: " + mHeightPixels + "  resultCode: " + intentResCode + "  data: " + intentData);

            mMediaProjection = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE))
                    .getMediaProjection(intentResCode, intentData);

            checkFileExits();
            // 根据类型区分工作方式
            int record = intent.getIntExtra(RECORD_TYPE, -1);
            if (RECORD_TYPE_RECORD_SCREEN == record) {
                notification = NotifyUtils.notifyServiceRunning(SCREEN_RUNNING_TITLE, this);
                takeScreenShots(1);
                startRecord();
                startForeground(NOTIFY_CODE, notification);
            } else if (RECORD_TYPE_SCREEN_SHOTS == record) {
                notification = NotifyUtils.notifyServiceRunning(SCREEN_RUNNING_TITLE, this);
                takeScreenShots(0);
                startForeground(NOTIFY_CODE, notification);
            } else if (RECORD_TYPE_SCREEN_SHOTS_SOUND == record) {
                notification = NotifyUtils.notifyServiceRunning(DoSoundRecordService.SOUND_RUNNING_TITLE, this);
                takeScreenShots(2);
                startForeground(DoSoundRecordService.NOTIFY_CODE, notification);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void takeScreenShots(int index) {
        getBitmapImg(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startRecord() {
        isRecording = true;
        fileName = getFileName("OCA_SoundRecord_") + ".mp4";
        filePath = videoPath + "/" + fileName;

        createMediaRecorder();
        createVirtualDisplay(); // 在mediaRecorder.prepare() 之后调用，否则报错 failed to get surface
        mMediaRecorder.start();
    }

    // 停止录制
    private void stopRecord() {
        if(isRecording) {

            Intent intent = new Intent(RecordCodeReceiver.ACTION_RECORD_RESULT);
            screenShotsFilePath = screenShotsPath + "/" + fileName;


            Log.d(TAG, "stopRecord");
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mVirtualDisplay.release();

                // 录制视频的文件地址
                intent.putExtra(RecordCodeReceiver.RESULT_FILE_PATH, filePath);
                intent.putExtra(RecordCodeReceiver.RESULT_FILE_NAME, fileName);
                // 截图保存的路径
                intent.putExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_PATH, screenShotsFilePath);
                intent.putExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_NAME, screenShotsFileName);

                // 返回值
                intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_SUCCESS);

                // 刷新媒体库
                FileUtils.updatePhotoAlbum(getApplicationContext(), new File(filePath), "");//保存视频 刷新媒体库
                // 正常关闭才可以添加到历史记录
                Log.d(TAG, "stopRecord: stop");
            } catch (RuntimeException e) {
                mMediaRecorder.reset();
                mMediaRecorder.release();

                intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_ERROR);
                Log.d(TAG, "stopRecord: error");
            } finally {
                // 判断是那个服务被调用了
                intent.putExtra(RecordCodeReceiver.SERVICE_NAME, RecordCodeReceiver.SERVICE_NAME_RECORD);
                intent.putExtra(RecordCodeReceiver.SERVICE_TYPE, RecordCodeReceiver.SERVICE_TYPE_RECORD);

                sendBroadcast(intent);
                stopSelf();
                //录制结束
                isRecording = false;
            }
        }
    }

    private void createVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("TaskSupportRecordService", mWidthPixels, mHeightPixels, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
    }

    private boolean isAudio = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    private void createMediaRecorder() {

        mMediaRecorder = new MediaRecorder();

        isAudio = true;

        //设置音频源
        int audioSourceType = MediaRecorder.AudioSource.DEFAULT;

        if (isAudio) mMediaRecorder.setAudioSource(audioSourceType);

        //设置视频源: DEFAULT,Surface,Camera
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

        //设置视频输出格式: amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        //设置视频编码格式: default, H263, H264, MPEG_4_SP
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

//        //设置音频编码格式: default，AAC，AMR_NB，AMR_WB
        if (isAudio) mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);

        //设置视频尺寸大小
        mMediaRecorder.setVideoSize(mWidthPixels, mHeightPixels);

        //设置视频编码的帧率
        mMediaRecorder.setVideoFrameRate(60);   // 30

        //设置视频输出路径
        mMediaRecorder.setOutputFile(filePath);

        //设置视频编码的码率
        mMediaRecorder.setVideoEncodingBitRate(5 * mWidthPixels * mHeightPixels);   // mWidthPixels * mHeightPixels

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "VideoSize: " + mWidthPixels + " X " + mHeightPixels + "  VideoEncodingBitRate: "
                + (5 * mWidthPixels * mHeightPixels) + "  +VideoFrameRate: " + "60");
    }

    private Image image;

    private ImageReader imageReader;

    @SuppressLint("WrongConstant")
    public void getBitmapImg(int index) {
        WindowManager window = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = window.getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        // use getMetrics is 2030, use getRealMetrics is 2160, the diff is NavigationBar's height
        mDisplay.getRealMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("WOW", "metrics.widthPixels is " + metrics.widthPixels);
        Log.d("WOW", "metrics.heightPixels is " + metrics.heightPixels);
        int mWidth = metrics.widthPixels;//size.x;
        int mHeight = metrics.heightPixels;//size.y;

        // 从指定资源编号的布局文件中获取内容视图对象
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            int x = mHeight;
            mHeight = mWidth;
            mWidth = x;
        }

        imageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        VirtualDisplay virtualDisplay = mMediaProjection.createVirtualDisplay(
                "ScreenShot1",
                mWidth,
                mHeight,
                mDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                imageReader.getSurface(),
                null,
                mHandler);
        AtomicInteger atomicId = new AtomicInteger();
        imageReader.setOnImageAvailableListener(reader -> {
            image = null;
            Bitmap bitmap = null;
            try {
                image = reader.acquireLatestImage();
                if (image != null) {
                    Log.d(TAG, "getBitmap: ImageReader" + atomicId);
                    // 防止保存两次
                    atomicId.incrementAndGet();
                    if (atomicId.get() == 1) {
                        bitmap = image_2_bitmap(image, Bitmap.Config.ARGB_8888);
                        // 缓冲延迟关闭 否则 imageReader queueBuffer: BufferQueue has been abandoned
                        screenShotsFileName = getFileName("OCA_ScreenShot_") + ".jpg";
                        Log.d(TAG, "getBitmap: ImageReader" + atomicId);
                        saveBitmap(bitmap, screenShotsFileName, index);
                    }
                }
            }finally {
                if (null != image) {
                    image.close(); // close it when used and
                }
            }
        }, null);
    }

    private void saveBitmap(Bitmap bitmap, String fileName, int index) {
        Intent intent = new Intent(RecordCodeReceiver.ACTION_RECORD_RESULT);
        boolean isStop = false;
        try {
            screenShotsFilePath = screenShotsPath + "/" + fileName;
            // 保存记录
//            saveImgHistory(filePath, index, fileName);
            FileOutputStream outputStream = new FileOutputStream(screenShotsFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            imageReader.close();
            if (index == RECORD_TYPE_SCREEN_SHOTS) {
                intent.putExtra(RECORD_TYPE, 0);
                isStop = true;
            } else if (index == RECORD_TYPE_RECORD_SCREEN) {
                intent.putExtra(RECORD_TYPE, 1);
            } else if (index == RECORD_TYPE_SCREEN_SHOTS_SOUND) {
                intent.putExtra(RECORD_TYPE, 2);
                isStop = true;
            }
            intent.putExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_PATH, screenShotsFilePath);
            intent.putExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_NAME, screenShotsFileName);
            intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_SUCCESS);

            // 保存录音截屏录屏的截图图片地址
            Log.d(TAG, "saveBitmap: filePath=>" + screenShotsFilePath);
            FileUtils.updatePhotoAlbum(getApplicationContext(), new File(screenShotsFilePath), fileName);//保存图片 刷新媒体库
        } catch (Exception e) {
            intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_ERROR);
        } finally {
            if (null != bitmap) {
                bitmap.recycle();
            }
            // 判断服务名称
            intent.putExtra(RecordCodeReceiver.SERVICE_NAME, RecordCodeReceiver.SERVICE_NAME_RECORD);
            // 判断服务类型  一个服务的具体调用功能
            intent.putExtra(RecordCodeReceiver.SERVICE_TYPE, RecordCodeReceiver.SERVICE_TYPE_SCREEN_SHOTS);
            sendBroadcast(intent);
            if (isStop) {
                stopSelf();
            }
        }
    }

    /**
     * 这个方法可以转换，但是得到的图片右边多了一列，比如上面方法得到1080x2160，这个方法得到1088x2160
     * 所以要对得到的Bitmap裁剪一下
     *
     * @param image
     * @param config
     * @return
     */
    private Bitmap image_2_bitmap(Image image, Bitmap.Config config) {

        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap bitmap;

        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Log.d("WOW",
                "pixelStride:" + pixelStride + ". rowStride:" + rowStride + ". rowPadding" + rowPadding);

        bitmap = Bitmap.createBitmap(
                width + rowPadding / pixelStride/*equals: rowStride/pixelStride */
                , height, config);
        bitmap.copyPixelsFromBuffer(buffer);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }


    private String getFileName(String pref) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = formatter.format(new Date(System.currentTimeMillis()));
        return pref + format;
    }

    /**
     * 检测文件夹不存在就创建
     */
    /**
     * 检测文件夹不存在就创建
     */
    private boolean checkFileExits() {
        // 定义文件夹目录地址 公共目录含有自己创建的文件夹无法识别 /OnlineCourseAides  需要刷新媒体库
        String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA";
        videoPath = basePath + "/video";
        screenShotsFilePath = basePath + "/img";

        Log.d(TAG, "checkFileExits: newFile : ");

        File destDir = new File(videoPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File dest2Dir = new File(screenShotsFilePath);
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
        if (recordReceiver != null) {
            unregisterReceiver(recordReceiver);
        }
        if (isRecording) {
            stopRecord();
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
        }
        stopForeground(NOTIFY_CODE);
    }
}