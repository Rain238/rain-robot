package qqrobot.module.mihoyo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import qqrobot.module.mihoyo.genshin.GenShinSignMiHoYo;
import qqrobot.module.mihoyo.genshin.bean.GenshinSignIn;
import qqrobot.module.mihoyo.genshin.mapper.GsSignInMapping;
import qqrobot.module.mihoyo.sign.bean.MiHoYo;
import qqrobot.module.mihoyo.sign.mapper.SignInMapping;
import qqrobot.util.Base64Util;
import qqrobot.simple.Send;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动签到任务类
 *
 * &#064;Author  Light rain
 * &#064;Date  2022/5/22 7:59
 */
@Component
public class AutomaticCheckin extends AbstractMessage {
    private final SignInMapping signInMapping;//米游社数据表
    private final GsSignInMapping gsSignInMapping;//原神数据表
    private final Send send;//发送消息
    //防止重复签到Map,每日重置一次
    Map<String, String> mihoyoMap = new HashMap<>();

    public AutomaticCheckin(GsSignInMapping gsSignMessage, SignInMapping signInMapping, Send send, SignInMapping signInMapping1, GsSignInMapping gsSignInMapping, Send send1) {
        super(gsSignMessage, signInMapping, send);
        this.signInMapping = signInMapping1;
        this.gsSignInMapping = gsSignInMapping;
        this.send = send1;
    }

    /**
     * 每日重置Map，来保证每日正常签到
     */
    @Scheduled(cron = "0 0 6 * * ?")
    private void remMihoyoMap() {
        mihoyoMap = new HashMap<>();
    }

    /**
     * 米游社自动签到
     *
     * @throws Exception 异常
     */
    @Scheduled(cron = "0 3 8 * * ?")
    private synchronized void miHoYoSign() throws Exception {
        //防止重复签到校验码
        String checkCode = "2l4M3M7IGFjY291bnRfaWQ9MTY5";
        if (!mihoyoMap.containsKey(checkCode)) {
            //查询出要自动签到的全部记录
            List<MiHoYo> mihoyo = signInMapping.queryAllAutomatic();
            for (MiHoYo hoYo : mihoyo) {
                //解密cookie
                String cookie = new String(Base64Util.base64decode(hoYo.getCookie()));
                //解密token
                String token = new String(Base64Util.base64decode(hoYo.getToken()));
                //执行签到并返回数据
                //执行社区签到
                String sign = this.sign(cookie, hoYo.getUid(), token);
                //发送私聊消息提醒
                send.privates(hoYo.getQq(), String.format("执行状态: 自动\n%s", sign));
            }
            mihoyoMap.put(checkCode, checkCode);
        }
    }

    /**
     * 原神自动签到
     */
    @Scheduled(cron = "0 3 8 * * ?")
    private synchronized void gsSign() {
        //防止重复签到校验码
        String checkCode = "N2YyOWFmYjE0ODdlZi0wYTJkZTc2";
        if (!mihoyoMap.containsKey(checkCode)) {
            //查询出要自动签到的全部记录
            List<GenshinSignIn> gsMihoyo = gsSignInMapping.queryAllAutomatic();
            for (GenshinSignIn sign : gsMihoyo) {
                //解密cookie
                String cookies = new String(Base64Util.base64decode(sign.getCookie()));
                //创建原神签到实现类
                GenShinSignMiHoYo genShinSignMiHoYo = new GenShinSignMiHoYo(cookies, sign.getUid());
                //获取签到状态
                String code = (String) genShinSignMiHoYo.sign();
                this.gsSignMessage(sign.getQq(), genShinSignMiHoYo.getName(), sign.getUid(), genShinSignMiHoYo.hubSign(), code);
            }
            mihoyoMap.put(checkCode, checkCode);
        }
    }
}
