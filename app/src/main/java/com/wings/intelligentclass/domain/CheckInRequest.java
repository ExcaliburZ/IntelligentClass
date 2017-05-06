package com.wings.intelligentclass.domain;

import java.util.List;

/**
 * Created by wing on 2017/5/7.
 */

public class CheckInRequest {
    private List<String> accountList;
    private String classID;

    public CheckInRequest(List<String> accountList, String classID) {
        this.accountList = accountList;
        this.classID = classID;
    }

    public List<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
