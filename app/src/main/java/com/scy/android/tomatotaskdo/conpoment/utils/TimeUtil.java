package com.scy.android.tomatotaskdo.conpoment.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 沈程阳
 * created by scy on 2019/1/26 10:48
 * 邮箱：1797484636@qq.com
 */
public class TimeUtil {

    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;



        Long minute = (ms) / mi;
        Long second = (ms - minute * mi) / ss;
        Long milliSecond = ms - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(minute > 0 && minute < 10) { sb.append("0" + minute + ":");
        } if (minute >= 10) {sb.append(minute + ":");
        } if (minute <= 0) {sb.append("00:");}
        if(second > 0 && second < 10) { sb.append("0" + second);
        } if (second >= 10) {sb.append(second);}
        if (second <= 0) {sb.append("00");}
        return sb.toString();
    }

    public static String formatMinuteTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Long minute = (ms) / mi;

        return minute.toString();
    }

    public static String formatFocusTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String formatTaskTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

}
