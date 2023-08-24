package com.readboy.onlinecourseaides.utils.aicourse.bean;

public class VideoBean {


    /**
     * vid : 1bfd2adc73c24d0893965bb58a20f5b1
     * duration : 1294.7
     * id : 883668110
     * version : 2566
     * fileSize : 205235782
     * name : 语文小学3上__第1课·大青树下的小学
     * videoUri : /download/mp4rjb1/语文小学3上大青树下的小学_E9CB.mp4
     * thumbnailUrl : https://contres.readboy.com/video_resource/msfdb/thumbnail/b8/71/59/b871598dd1ccada0e1ba67f29dac1335.jpg
     */

    private String vid;
    private double duration;
    private String id;
    private String version;
    private String fileSize;
    private String name;
    private String videoUri;
    private String thumbnailUrl;

    protected String requestId;

    @Override
    public String toString() {
        return "VideoBean{" +
                "vid='" + vid + '\'' +
                ", duration=" + duration +
                ", id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", name='" + name + '\'' +
                ", videoUri='" + videoUri + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
