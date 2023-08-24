package com.readboy.onlinecourseaides.utils;

import android.util.Log;

import com.readboy.onlinecourseaides.Application.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {
    private static final String TAG = Logger.class.getSimpleName();

    public static final String PREFIX = "Logger => ";
    public static boolean isEnableLog = false;

    public static String logFilePath = "";
    private static ExecutorService sWriteThread;
    private static final ConcurrentLinkedQueue<LogInfo> sLogInfoQueue = new ConcurrentLinkedQueue<>();

    /**
     * @param isEnableLog <code>ture-</code>开启内部打印，<code>false-</code>关闭内部打印
     */
    public static void setIsEnableLog(boolean isEnableLog) {
        Logger.isEnableLog = isEnableLog;
    }

    public static String generatePrefix() {
        return generatePrefix(Thread.currentThread().getName());
    }

    public static String generatePrefix(String threadName) {
        return PREFIX + "[" + threadName + "] ";
    }

    public static void v(String tag, String msg) {
        v(tag, msg, isEnableLog);
    }

    public static void mv(String tag, String msg) {
        v(tag, msg, true);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, isEnableLog);
    }

    public static void md(String tag, String msg) {
        d(tag, msg, true);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, isEnableLog);
    }

    public static void mi(String tag, String msg) {
        i(tag, msg, true);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, isEnableLog);
    }

    public static void mw(String tag, String msg) {
        w(tag, msg, true);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        w(tag, msg, throwable, isEnableLog);
    }

    public static void mw(String tag, String msg, Throwable throwable) {
        w(tag, msg, throwable, true);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, isEnableLog);
    }

    public static void me(String tag, String msg) {
        e(tag, msg, true);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        e(tag, msg, throwable, isEnableLog);
    }

    public static void me(String tag, String msg, Throwable throwable) {
        e(tag, msg, throwable, true);
    }

    private static void v(String tag, String msg, boolean isEnableLog) {
        if (isEnableLog) Log.v(tag, generatePrefix() + msg);
    }

    private static void d(String tag, String msg, boolean isEnableLog) {
        if (isEnableLog) Log.d(tag, generatePrefix() + msg);
    }

    private static void i(String tag, String msg, boolean isEnableLog) {
        if (isEnableLog) Log.i(tag, generatePrefix() + msg);
    }

    private static void w(String tag, String msg, boolean isEnableLog) {
        if (isEnableLog) Log.w(tag, generatePrefix() + msg);
    }

    private static void w(String tag, String msg, Throwable throwable, boolean isEnableLog) {
        if (isEnableLog) Log.w(tag, generatePrefix() + msg, throwable);
    }

    private static void e(String tag, String msg, boolean isEnableLog) {
        if (isEnableLog) Log.e(tag, generatePrefix() + msg);
    }

    private static void e(String tag, String msg, Throwable throwable, boolean isEnableLog) {
        if (isEnableLog) Log.e(tag, generatePrefix() + msg, throwable);
    }

    public static synchronized void logTrace(LogInfo logInfo) {
//        if (isEnableLog) {
        sLogInfoQueue.add(logInfo);
        if (sWriteThread == null) {
            sWriteThread = Executors.newSingleThreadExecutor();
        }
        sWriteThread.execute(() -> {
            while (!sLogInfoQueue.isEmpty()) {//队列不空时
                try {
                    recordStringLog(sLogInfoQueue.poll());
                } catch (Exception e) {
                    me(TAG, "recordStringLog: ", e);
                }
            }
        });
//        }
    }

    /**
     * 打开日志文件并写入日志
     **/
    public static void recordStringLog(LogInfo logInfo) {// 新建或打开日志文件
        Date now = new Date();
        logFilePath = MyApplication.getInstances().getExternalFilesDir("log").getAbsolutePath()
                + File.separator + new SimpleDateFormat("yy-MM-dd", Locale.CHINA).format(now)
                + ".txt";
        File file = new File(logFilePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e(TAG, "行为日志：在 \"" + logFilePath + "\" 创建文件失败！ " + e.getMessage());
                return;
            }
        }
        try {
            StringBuilder builder = new StringBuilder(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(now));
            builder.append('\n').append(generatePrefix(logInfo.threadName)).append(logInfo.msg).append("\nStackTrace:\n");
            for (StackTraceElement t : logInfo.trace) {
                builder.append("\t").append(t.getClassName()).append(": ").append(t.getMethodName())
                        .append(", line:").append(t.getLineNumber()).append('\n');
            }

            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(builder.toString());
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
            i(TAG, "recordStringLog: 行为日志写入成功==>\n" + builder);
        } catch (IOException e) {
            e(TAG, "recordStringLog: " + e.getMessage());
        }

//————————————————
//        版权声明：本文为CSDN博主「NN955」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/NN955/article/details/27841941
    }

    public static class LogInfo {
        private final String threadName;
        private final String msg;
        private final StackTraceElement[] trace;

        public LogInfo(String threadName, String msg, StackTraceElement[] trace) {
            this.threadName = threadName;
            this.msg = msg;
            this.trace = trace;
        }
    }
}
