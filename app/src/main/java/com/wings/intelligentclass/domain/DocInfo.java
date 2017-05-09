package com.wings.intelligentclass.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wing on 2017/5/9.
 */

public class DocInfo {
    private String id;
    private String name;
    @SerializedName("upload_time")
    private String uploadTime;
    private String size;
    @SerializedName("class_id")
    private String classID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
