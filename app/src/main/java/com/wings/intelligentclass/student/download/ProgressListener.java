package com.wings.intelligentclass.student.download;

/**
 * Created by wing on 2017/5/18.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
