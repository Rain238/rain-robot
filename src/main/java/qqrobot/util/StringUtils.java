package qqrobot.util;

public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotBank(String str) {
        return !isBank(str);
    }

    public static boolean isBank(String str) {
        return str == null || "".equals(str) || str.length() == 0;
    }
}
