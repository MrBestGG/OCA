package com.readboy.onlinecourseaides.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.readboy.onlinecourseaides.Application.MyApplication
import com.readboy.onlinecourseaides.receiver.RecordCodeReceiver
import com.readboy.onlinecourseaides.receiver.RecordReceiver
import com.readboy.onlinecourseaides.receiver.RecordReceiver.RecordControlListener
import com.readboy.onlinecourseaides.utils.FileUtils
import com.readboy.onlinecourseaides.utils.NotifyUtils
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class DoScreenShotsService : Service(), Handler.Callback {

    private var mHandler: Handler = Handler(Looper.getMainLooper())
    private var mContext: Context?= null

    // 录制服务
    private var mMediaProjection: MediaProjection? = null
    private var intentData: Intent? = null
    private var intentResCode = -1
    private var recordReceiver:RecordReceiver?=null

    private val screenShotsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/OCA/img"
    private var screenShotsFileName: String? = null
    private var screenShotsFilePath: String? = null

    var mWidthPixels = 720
    var mHeightPixels = 480
    var mScreenDensity = 1


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        initStart()
        var notification = NotifyUtils.notifyServiceRunning(DoSoundRecordService.SOUND_RUNNING_TITLE, this)
        startForeground(DoSoundRecordService.NOTIFY_CODE, notification)
    }

    private fun initStart() {
        // 停止服务需要的
        recordReceiver = RecordReceiver(object : RecordControlListener {
            override fun doSoundRecordTask(type: String) {}
            override fun doScreenRecordTask(type: String) {}
            override fun doScreenShotsTask(type: String) {}
        })

        // 停止服务接收器
        val intentFilter = IntentFilter()
        intentFilter.addAction(RecordReceiver.START_SCREEN_SHOTS)
        registerReceiver(recordReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intentData = null
        intentResCode = -1

        if (intent != null) {
            intentResCode = intent.getIntExtra("resultCode", -1)
            intentData = MyApplication.getInstances().data
            mWidthPixels = intent.getIntExtra("mWidthPixels", 720)
            mHeightPixels = intent.getIntExtra("mHeightPixels", 480)
            mScreenDensity = intent.getIntExtra("mDensityDpi", 1)
            Log.d(TAG, "initData  -->  mScreenDensity: $mScreenDensity  mWidthPixels: $mWidthPixels  mHeightPixels: $mHeightPixels  resultCode: $intentResCode  data: $intentData")

            mMediaProjection = (getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
                    .getMediaProjection(intentResCode, intentData!!)

            // 根据类型区分工作方式
            val record = intent.getIntExtra(DoRecordService.RECORD_TYPE, -1)
            checkFileExits()
            doTakeScreenShots(record);
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getFileName(pref: String): String? {
        @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("yyyyMMdd_HHmmss")
        val format = formatter.format(Date(System.currentTimeMillis()))
        return pref + format
    }

    /**
     * 检测文件夹不存在就创建
     */
    private fun checkFileExits(): Boolean {
        val destDir = File(screenShotsPath)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
        return true
    }

    private fun getOrientation() {
        val mConfiguration = this.resources.configuration //获取设置的配置信息

        val ori = mConfiguration.orientation //获取屏幕方向

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            val x = mHeightPixels
            mHeightPixels = mWidthPixels
            mWidthPixels = x
        }
    }

    private fun doTakeScreenShots(index: Int) {
        getBitmapImg(index)
    }

    lateinit var image: Image;
    lateinit var imageReader: ImageReader;

    @SuppressLint("WrongConstant")
    private fun getBitmapImg(index: Int) {
        val window = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        val mDisplay = window.defaultDisplay
        val metrics = DisplayMetrics()
        // use getMetrics is 2030, use getRealMetrics is 2160, the diff is NavigationBar's height
        mDisplay.getRealMetrics(metrics)
        val mDensity = metrics.densityDpi
        Log.d("WOW", "metrics.widthPixels is " + metrics.widthPixels)
        Log.d("WOW", "metrics.heightPixels is " + metrics.heightPixels)
        var mWidth = metrics.widthPixels //size.x;
        var mHeight = metrics.heightPixels //size.y;

        // 从指定资源编号的布局文件中获取内容视图对象
        val mConfiguration = this.resources.configuration //获取设置的配置信息

        val ori = mConfiguration.orientation //获取屏幕方向

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            var x = mHeight
            mHeight = mWidth
            mWidth = x
        }
        imageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2)
        val virtualDisplay = mMediaProjection!!.createVirtualDisplay(
                "ScreenShot",
                mWidth,
                mHeight,
                mDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                imageReader.surface,
                null,
                mHandler)
        val atomicId = AtomicInteger()
        imageReader.setOnImageAvailableListener({ reader: ImageReader ->
            var bitmap: Bitmap? = null
            try {
                image = reader.acquireLatestImage()
                if (image != null) {
                    // 防止保存两次
                    atomicId.incrementAndGet()
                    if (atomicId.get() == 1) {
                        bitmap = image_2_bitmap(null, Bitmap.Config.ARGB_8888)
                        // 缓冲延迟关闭 否则 imageReader queueBuffer: BufferQueue has been abandoned
                        screenShotsFileName = getFileName("OCA_ScreenShot_") + ".jpg"
                        Log.d(TAG, "getBitmap: ImageReader$atomicId")
                        saveBitmap(bitmap, screenShotsFileName!!, index)
                        image?.close()
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }finally {

            }
        }, null)
    }

    private fun saveBitmap(bitmap: Bitmap?, fileName: String, index: Int) {
        val intent = Intent(RecordCodeReceiver.ACTION_RECORD_RESULT)
        try {
            screenShotsFilePath = "$screenShotsPath/$fileName"
            // 保存记录
//            saveImgHistory(filePath, index, fileName);
            val outputStream = FileOutputStream(screenShotsFilePath)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            if (index == DoRecordService.RECORD_TYPE_SCREEN_SHOTS) {
                intent.putExtra(DoRecordService.RECORD_TYPE, 0)
            } else if (index == DoRecordService.RECORD_TYPE_RECORD_SCREEN) {
                intent.putExtra(DoRecordService.RECORD_TYPE, 1)
            } else if (index == DoRecordService.RECORD_TYPE_SCREEN_SHOTS_SOUND) {
                intent.putExtra(DoRecordService.RECORD_TYPE, 2)
            }
            intent.putExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_PATH, screenShotsFilePath)
            intent.putExtra(RecordCodeReceiver.RESULT_FILE_SCREEN_SHOTS_NAME, screenShotsFileName)
            intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_SUCCESS)

            // 保存录音截屏录屏的截图图片地址
            Log.d(TAG, "saveBitmap: filePath=>$screenShotsFilePath")
            FileUtils.updatePhotoAlbum(applicationContext, File(screenShotsFilePath), fileName) //保存图片 刷新媒体库
        } catch (e: Exception) {
            intent.putExtra(RecordCodeReceiver.RECORD_RESULT_CODE, RecordCodeReceiver.RECORD_CODE_ERROR)
        } finally {
            bitmap?.recycle()
            // 判断服务名称
            intent.putExtra(RecordCodeReceiver.SERVICE_NAME, RecordCodeReceiver.SERVICE_NAME_SCREENSHOTS)
            // 判断服务类型  一个服务的具体调用功能
//            intent.putExtra(RecordCodeReceiver.SERVICE_TYPE, RecordCodeReceiver.SERVICE_TYPE_SCREEN_SHOTS)
            sendBroadcast(intent)
            stopSelf()
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
    private fun image_2_bitmap(img: Image?, config: Bitmap.Config): Bitmap? {
        val width = image.width
        val height = image.height
        val bitmap: Bitmap
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        Log.d("WOW",
                "pixelStride:$pixelStride. rowStride:$rowStride. rowPadding$rowPadding")
        bitmap = Bitmap.createBitmap(
                width + rowPadding / pixelStride /*equals: rowStride/pixelStride */, height, config)
        bitmap.copyPixelsFromBuffer(buffer)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height)
    }

    override fun handleMessage(message: Message): Boolean {
        return false
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onDestroy() {
        super.onDestroy()
        mMediaProjection?.stop()
        imageReader?.close()
        unregisterReceiver(recordReceiver)
        stopForeground(DoRecordService.NOTIFY_CODE)
    }

    companion object {
        private const val TAG = "DoScreenShotsService"
    }
}