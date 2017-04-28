package com.wings.intelligentclass.domain;

/**
 * Created by wing on 2017/4/28.
 */

public class Result {
    public int code;
    public String message;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
