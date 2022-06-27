package qqrobot.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * 加载微标Logo
 */
public class LogoFileUtils {
    public String loadLogo() throws IOException {
        SecureRandom random = new SecureRandom();
        final int number = random.nextInt(18) + 1;
        File file = new File(String.format("src/main/java/qqrobot/mark/text/logo%s.txt", number));
        //使用StringBuilder可变字符串
        StringBuilder result = new StringBuilder();
        //调用FileUtils的lineIterator行迭代器进行读取
        try (LineIterator iterator = FileUtils.lineIterator(file, "UTF-8")) {
            while (iterator.hasNext()) {
                //一次读取一行
                String line = iterator.nextLine();
                //将读取的数据添加到result随后内增加一个行分隔符[System.lineSeparator()]
                result.append(line).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    public static String tips() throws IOException {
        final Connection connect = Jsoup.connect("https://v1.hitokoto.cn/?encode=text");
        final String text = connect.timeout(10000 * 1000).ignoreContentType(true).get().select("body").text();
        return String.format("Tips：「%s」", text);
    }
}
