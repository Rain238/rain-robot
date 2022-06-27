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
//        String 小谷米游社cookie = "_MHYUUID=ca1ddb9c-488e-4984-b38b-6378325a663b; UM_distinctid=17fc11f07da840-09b4f66c86f88e-56171d51-1bcab9-17fc11f07db1042; mi18nLang=zh-cn; _ga_9TTX3TE5YL=GS1.1.1650532728.2.0.1650532728.0; _ga_KJ6J9V9VZQ=GS1.1.1652829317.1.0.1652829322.0; _ga=GA1.2.1455285799.1648212500; CNZZDATA1274689524=126619928-1653035823-%7C1653041162; login_uid=155683391; login_ticket=6JyzYrZYb1jgwgWQHcYV8Z3Agxr0Lu7fz4KE5RH3";
        String s = "X01IWVVVSUQ9Y2ExZGRiOWMtNDg4ZS00OTg0LWIzOGItNjM3ODMyNWE2NjNiOyBVTV9kaXN0aW5jdGlkPTE3ZmMxMWYwN2RhODQwLTA5YjRmNjZjODZmODhlLTU2MTcxZDUxLTFiY2FiOS0xN2ZjMTFmMDdkYjEwNDI7IG1pMThuTGFuZz16aC1jbjsgX2dhXzlUVFgzVEU1WUw9R1MxLjEuMTY1MDUzMjcyOC4yLjAuMTY1MDUzMjcyOC4wOyBfZ2FfS0o2SjlWOVZaUT1HUzEuMS4xNjUyODI5MzE3LjEuMC4xNjUyODI5MzIyLjA7IF9nYT1HQTEuMi4xNDU1Mjg1Nzk5LjE2NDgyMTI1MDA7IF9naWQ9R0ExLjIuNTI4NzcwNDUwLjE2NTMwMzA0OTI7IGx0b2tlbj1kdU1VUlFQZzBObVZGRGhVbVRXQ1I2UFFldnJBNERQbFZiOUhrR0QzOyBsdHVpZD03NDg1MDIwNDsgY29va2llX3Rva2VuPVdKWkliTHdINE5tbUtQblFMU2NMVmNmeGFVVDlMUGgxN2JaVjF2OWQ7IGFjY291bnRfaWQ9NzQ4NTAyMDQ7IENOWlpEQVRBMTI3NDY4OTUyND0xMjY2MTk5MjgtMTY1MzAzNTgyMy0lN0MxNjUzMDk4ODE3OyBsb2dpbl91aWQ9NzQ4NTAyMDQ7IGxvZ2luX3RpY2tldD02RVBrZzJBQ09GTTZFOFR1aE9aaHg0OHBtbnl1djA4MzR4V0J1R3Nw";
        byte[] bytes = base64decode(s);
        String s2 = new String(bytes);
        System.out.println("base64解密："+s2);
        String s1 = base64encode(s.getBytes(StandardCharsets.UTF_8));
        System.out.println("base64加密："+s1);
//        byte[] bytes = base64decode(s);
//        String s2 = new String(bytes);
//        System.out.println("base64解密："+s2);
    }
}
