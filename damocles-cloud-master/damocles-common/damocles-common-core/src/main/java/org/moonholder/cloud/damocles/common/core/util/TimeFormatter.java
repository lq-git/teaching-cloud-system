package org.moonholder.cloud.damocles.common.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author moonholder
 * @Description //TODO 时间格式化器
 * @Date 13:02 2020/12/30
 */
public class TimeFormatter {
    private static long day = 0;
    private static long hour = 0;
    private static long minute = 0;
    private static long second = 0;

    public static String format(long totalSeconds) {
        resetDate();
        if (totalSeconds > 0) {
            second = totalSeconds;
            if (second >= 60) {
                minute = second / 60;
                second = second % 60;
                if (minute >= 60) {
                    hour = minute / 60;
                    minute = minute % 60;
                    if (hour > 24) {
                        day = hour / 24;
                        hour = hour % 24;
                    }
                }
            }
        }
        return String.format("%d天%d小时%d分%d秒", day, hour, minute, second);
    }

    private static void resetDate() {
        day = 0;
        hour = 0;
        minute = 0;
        second = 0;
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
