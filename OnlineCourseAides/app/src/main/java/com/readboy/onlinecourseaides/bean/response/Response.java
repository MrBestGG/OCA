package com.readboy.onlinecourseaides.bean.response;


import com.readboy.onlinecourseaides.bean.IResponse;

public abstract class Response<T> implements IResponse {

    private int        status; // 提交数据成功返回的信息（插入，更新，删除） 1=成功, 0=失败
    private String     errmsg; // 错误信息描述
    private int        errno;  // 错误码
    private T          data;   // "data": [ 数据 ] 不一定是数组 (data为空时返回 []也有null的情况)
                               //         在AICourse接口中返回数组的接口始终返回数组，返回对象同理

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public int setErrno() {
        return 0;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    @Override
    public boolean isOk() {
        return getStatus() == 1;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", errmsg='" + errmsg + '\'' +
                ", errno=" + errno +
                ", data=" + data +
                '}';
    }
}


