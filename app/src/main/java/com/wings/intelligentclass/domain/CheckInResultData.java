package com.wings.intelligentclass.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wing on 2017/5/7.
 */

public class CheckInResultData {
    @SerializedName("amount")
    private int amount;
    @SerializedName("check_in_list")
    private List<CheckInItem> checkInList;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<CheckInItem> getCheckInList() {
        return checkInList;
    }

    public void setCheckInList(List<CheckInItem> checkInList) {
        this.checkInList = checkInList;
    }
}
