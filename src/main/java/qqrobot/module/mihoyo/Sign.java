package qqrobot.module.mihoyo;

import org.apache.http.Header;

/**
 * &#064;Author  Light rain
 * &#064;Date  2022/5/20 12:08
 */
public interface Sign {
    //签到
    Object sign();

    //请求头
    Header[] getHeaders();
}
