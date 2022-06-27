package qqrobot.module.mihoyo;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import java.security.SecureRandom;
import java.util.*;

/**
 * 请求米忽悠网址必要的请求头/Cookie/DS算法
 *
 * &#064;Author  Light rain
 * &#064;Date  2022/5/20 12:08
 */
public abstract class MiHoYoAbstractSign implements Sign {
    //构造器注入
    public final String cookie;
    //构造器注入
    public final String uid;
    //服务器id
    public String region;
    //类型
    private String clientType;
    //版本号
    private String appVersion;
    //校验码
    private String salt;

    public MiHoYoAbstractSign(String cookie, String uid) {
        this.cookie = cookie;
        this.uid = uid;
    }

    public MiHoYoAbstractSign(String cookie) {
        this.cookie = cookie;
        this.uid = "";
    }

    //空参数构造器cookie/uid必须初始化
    public MiHoYoAbstractSign() {
        this.cookie = "";
        this.uid = "";
    }

    /**
     * 重写接口请求头getHeaders方法
     *
     * @return Header[]
     */
    @Override
    public Header[] getHeaders() {
        return new HeaderBuilder.Builder().add("x-rpc-device_id", UUID.randomUUID().toString().replace("-", "").toUpperCase()).add("Content-Type", "application/json;charset=UTF-8").add("x-rpc-client_type", getClientType()).add("x-rpc-app_version", getAppVersion()).add("DS", getDS()).addAll(getBasicHeaders()).build();
    }

    /**
     * 请求头基本参数
     *
     * @return Header[]
     */
    protected Header[] getBasicHeaders() {
        return new HeaderBuilder.Builder().add("Cookie", cookie).add("User-Agent", MiHoYoConfig.USER_AGENT).add("Referer", MiHoYoConfig.REFERER_URL).add("Accept-Encoding", "gzip, deflate, br").add("x-rpc-channel", "appstore").add("accept-language", "zh-CN,zh;q=0.9,ja-JP;q=0.8,ja;q=0.7,en-US;q=0.6,en;q=0.5").add("accept-encoding", "gzip, deflate").add("accept-encoding", "gzip, deflate").add("x-requested-with", "com.mihoyo.hyperion").add("Host", "api-takumi.mihoyo.com").build();
    }

    /**
     * 原神签到DS算法
     *
     * @return String
     */
    protected String getDS() {
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = getRandomStr();
        return createDS(getSalt(), i, r);
    }

    /**
     * 获取随机字符串用于DS算法中
     *
     * @return String
     */
    protected String getRandomStr() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            String CONSTANTS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            int number = random.nextInt(CONSTANTS.length());
            char charAt = CONSTANTS.charAt(number);
            sb.append(charAt);
        }
        return sb.toString();
    }

    /**
     * 创建DS算法
     *
     * @param n salt
     * @param i t
     * @param r r
     * @return String
     */
    private String createDS(String n, String i, String r) {
        String c = DigestUtils.md5Hex("salt=" + n + "&t=" + i + "&r=" + r);
        return String.format("%s,%s,%s", i, r, c);
    }


//<editor-fold defaultstate="collapsed" desc="delombok">
//</editor-fold>
    /**
     * 建造者模式，用于创建header
     */
    public static class HeaderBuilder {

        public static class Builder {
            private final Map<String, String> header = new HashMap<>();

            public Builder add(String name, String value) {
                this.header.put(name, value);
                return this;
            }

            public Builder addAll(Header[] headers) {
                for (Header h : headers) {
                    this.header.put(h.getName(), h.getValue());
                }
                return this;
            }

            public Header[] build() {
                List<Header> list = new ArrayList<>();
                for (String key : this.header.keySet()) {
                    list.add(new BasicHeader(key, this.header.get(key)));
                }
                return list.toArray(new Header[0]);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public String getCookie() {
        return this.cookie;
    }

    @SuppressWarnings("all")
    public String getUid() {
        return this.uid;
    }

    @SuppressWarnings("all")
    public String getRegion() {
        return this.region;
    }

    @SuppressWarnings("all")
    public String getClientType() {
        return this.clientType;
    }

    @SuppressWarnings("all")
    public String getAppVersion() {
        return this.appVersion;
    }

    @SuppressWarnings("all")
    public String getSalt() {
        return this.salt;
    }

    @SuppressWarnings("all")
    public void setRegion(final String region) {
        this.region = region;
    }

    @SuppressWarnings("all")
    public void setClientType(final String clientType) {
        this.clientType = clientType;
    }

    @SuppressWarnings("all")
    public void setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
    }

    @SuppressWarnings("all")
    public void setSalt(final String salt) {
        this.salt = salt;
    }
    //</editor-fold>
}
