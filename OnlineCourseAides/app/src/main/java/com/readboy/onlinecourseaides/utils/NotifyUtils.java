package com.readboy.onlinecourseaides.utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.readboy.onlinecourseaides.R;

public class NotifyUtils {

    //消息通知
    public static final int SERVICE_RUNNING = 102;
    public static final int SERVICE_SIMPLE_MSG = 103;
    public static final String SERVICE_MSG_CHANNEL = "com.readboy.onlinecourseaides";
    public static final String SERVICE_EASY_MSG_CHANNEL = "com.readboy.onlinecourseaides";
    public static final String NOTIFY_TITLE = "网课助手";
    public static final int MSG_TYPE_COUNT_DOWN = 1045;

    // 常驻后台服务通知
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Notification notifyServiceRunning(String data, Context context) {
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.online_course_aides_icon) // 设置状态栏内的小图标
//                .setContentTitle(data)
                .setContentText(data + "运行中") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        //----------------  新增代码 --------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            NotificationChannel notificationChannel = new NotificationChannel(SERVICE_MSG_CHANNEL,
                    "onlineCourseAides", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.setShowBadge(false);//是否显示角标
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(SERVICE_MSG_CHANNEL);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        return notification;
//        Notification notification = getNotification(data);
//        // 4. 发送通知  前台服务 注意不能多次调用
//        startForeground(SERVICE_RUNNING, notification);
    }
}
