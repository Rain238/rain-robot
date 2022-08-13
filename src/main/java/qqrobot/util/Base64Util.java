package qqrobot.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
    // base64加密
    public static String base64encode(byte[] input){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(input);
    }

    // base64解密
    public static byte[] base64decode(String code) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(code);
    }
    public static void main(String[] args) throws Exception {
        String s = "";
        byte[] bytes = base64decode(s);
        String s2 = new String(bytes);
        System.out.println("base64解密："+s2);
        String s1 = base64encode(s.getBytes(StandardCharsets.UTF_8));
        System.out.println("base64加密："+s1);
    }
}
