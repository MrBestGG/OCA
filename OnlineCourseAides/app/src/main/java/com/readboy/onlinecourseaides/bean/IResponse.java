package com.readboy.onlinecourseaides.bean;

import java.io.Serializable;

public interface IResponse<T> extends Serializable {

    boolean isOk();

    T getData();

    String getErrmsg();

    int getErrno();
}
