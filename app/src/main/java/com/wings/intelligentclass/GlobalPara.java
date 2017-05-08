package com.wings.intelligentclass;

/**
 * Created by wing on 2017/5/4.
 */

public class GlobalPara {
    private static final GlobalPara ourInstance = new GlobalPara();

    public static GlobalPara getInstance() {
        return ourInstance;
    }

    private GlobalPara() {
    }

    public String account = "-1";
    public String name = "-zjq";
    public String number = "-13477126";
}
