package com.wings.intelligentclass.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wing on 2017/5/18.
 */

public class TimeUtils {
    public static String longToDateStr(String dateStr) {
        Long aLong = Long.valueOf(dateStr);
        Date date = new Date(aLong);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(date);
    }
}
