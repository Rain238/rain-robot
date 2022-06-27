package qqrobot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {


    /**
     * 时间戳按指定格式转化为日期（String）
     * @param timestamp 时间戳
     * @param pattern 字符串时间
     * @return String
     */
    public static String convertTimest(Long timestamp, String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date(timestamp));
    }

    public static void main(String[] args) {

        Long timestamp = System.currentTimeMillis();

        String pattern = "yyyy-MM-dd HH:mm:ss";

        System.out.println(convertTimest(timestamp, pattern));

    }

}
