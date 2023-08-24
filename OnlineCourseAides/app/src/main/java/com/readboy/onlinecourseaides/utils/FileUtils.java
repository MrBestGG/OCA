package com.readboy.onlinecourseaides.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * @Author jll
 * @Date 2022/11/29
 */
public class FileUtils {

    /**
     * 兼容android 10 及以下
     * 更新媒体库
     *
     * @param mContext
     * @param file
     */
    public static void updatePhotoAlbum(Context mContext, File file, String name) {
        if(file == null) return;
        if(file.exists()) {
            String mimeType = getMimeType(file);
            Log.d("FileUtils", "updatePhotoAlbum: =>"+mimeType);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            }else {
            }
            MediaScannerConnection.scanFile(mContext.getApplicationContext(), new String[]{file.getAbsolutePath()}, new String[]{mimeType}, (path, uri) -> {
            });
        }
    }

    public static String getMimeType(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getName());
        return type;
    }

    public static Uri toUri(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + ".fileprovider", new File(filePath));
        }
        return Uri.fromFile(new File(filePath));
    }

    // 获取存储在媒体的资源文件
    @SuppressLint("Range")
    public static Uri loadMediaFile(String fileName, Context context) {
        if(fileName == null) {
            Log.d(TAG, "loadMediaFile: error Uri loadMediaFile(String fileName, Context context): fileName==null");
            return null;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ContentResolver cr = context.getContentResolver();
            try (Cursor c = queryByFileName(cr, fileName)) {
                Uri uri = null;
                if (c.moveToFirst()) {
                    int idColumn = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                    long id = c.getLong(idColumn);
                    uri = ContentUris.withAppendedId(
                            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id);
                }
                return uri;
            }
        } else {
            Uri uri = toUri(context, fileName);
            return uri;
        }
    }

    private static Cursor queryByFileName(ContentResolver cr, String name) {
        return queryFiles(cr, MediaStore.Files.FileColumns.DISPLAY_NAME + " = ?", new String[]{name});
    }

    private static Cursor queryFiles(ContentResolver cr, String select, String[] selectionArgs) {
        return cr.query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                null, select, selectionArgs, null);
    }


    public static void  deleteMedieFile(String path,Context context){
        File file = new File(path);
        if (file.isFile()) {
            String filePath = file.getPath();
            if(filePath.endsWith(".mp4")){
                int res = context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath+"\"",
                        null);
                if (res>0){
                    file.delete();
                }else{
                    Log.e(TAG, "删除文件失败");
                }
            }else if (filePath.endsWith(".jpg")||filePath.endsWith(".png")||filePath.endsWith(".bmp")){
                int res = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath+"\"",
                        null);
                if (res>0){
                    file.delete();
                }else{
                    Log.e(TAG, "删除文件失败");
                }
            }else{
                file.delete();
            }
            //删除多媒体数据库中的数据
            return;
        }
    }

    /**
     * 安卓10是Uri
     * 安卓10以下是路径
     * @param pathOrUri
     */
    public static boolean delFile(String pathOrUri,Context context) {
        if(pathOrUri == null) {
            Log.d(TAG, "delFile: del file error: delFile(String pathOrUri,Context context):pathOrUri == null");
            return false;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ContentResolver mContentResolver = context.getContentResolver();
            int count = 0;
            try {
                count = mContentResolver.delete(Uri.parse(pathOrUri), null, null);
            } catch (Exception e) {
                Log.d(TAG, "delFile: error: mContentResolver.delete(Uri.parse(pathOrUri), null, null);");
                e.printStackTrace();
            }
            return (count == 1);
        }else {
            File file = new File(pathOrUri);
            if(file.exists()) {
                file.delete();
                Log.d(TAG, "delFile: del img");
                return true;
            }else {
                return false;
            }
        }
    }

    public static void saveBitmapAsPng(Bitmap bmp,File f,FileUtilsListener listener) {
        try {
            if(f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            listener.finishTask();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBasePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/OCA";
    }

    private static final String TAG = "FileUtils";

    public interface FileUtilsListener {
        void finishTask();
    }
}
