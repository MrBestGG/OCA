package com.readboy.onlinecourseaides.bean.contentFile;

import android.net.Uri;

/**
 * @Author jll
 * @Date 2022/12/5
 */
public class SoundFile {
    private final Uri uri;
    private final String name;
    private final int duration;
    private final int size;

    public SoundFile(Uri uri, String name, int duration, int size) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
    }

    @Override
    public String toString() {
        return "Video{" +
                "uri=" + uri +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }
}
