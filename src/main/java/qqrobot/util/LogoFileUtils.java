package qqrobot.util;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.security.SecureRandom;

/**
 * 加载微标Logo
 */
public class LogoFileUtils {
    public String loadLogo() throws IOException {
        SecureRandom random = new SecureRandom();
        final int number = random.nextInt(18) + 1;
        InputStream is = this.getClass().getResourceAsStream(String.format("/logo/logo%s.txt", number));
        //返回读取指定资源的输入流
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            result.append(s).append(System.lineSeparator());
        }
        return result.toString();
    }

    public String tips() throws IOException {
        final Connection connect = Jsoup.connect("https://v1.hitokoto.cn/?encode=text");
        final String text = connect.timeout(10000 * 1000).ignoreContentType(true).get().select("body").text();
        return String.format("Tips：「%s」", text);
    }
}
