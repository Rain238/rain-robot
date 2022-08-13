package qqrobot.module.mihoyo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * <p>
 * &#064;Author  Light rain
 * &#064;Date  2022/5/22 7:59
 */
@Slf4j
@Component
public class AutomaticCheckin extends AbstractMessage {
    private final SignInMapping signInMapping;//米游社数据表
    private final GsSignInMapping gsSignInMapping;//原神数据表

    private final static String YSCHECKCODE = "N2YyOWFmYjE0ODdlZi0wYTJkZTc2";//防止重复签到校验码
    private final static String MYSCHECKCODE = "2l4M3M7IGFjY291bnRfaWQ9MTY5";//防止重复签到校验码
    private final Send send;//发送消息
    @Value("${simbot.mihoyo.genshin.genshin-client-type}")
    private String gsType;
    @Value("${simbot.mihoyo.genshin.genshin-app-version}")
    private String gsVersion;
    @Value("${simbot.mihoyo.genshin.genshin-salt}")
    private String gsSalt;
    @Value("${simbot.mihoyo.club.miyou-club-type}")
    private String mysType;
    @Value("${simbot.mihoyo.club.miyou-club-version}")
    private String mysVersion;
    @Value("${simbot.mihoyo.club.miyou-club-salt}")
    private String mysSalt;

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
    @Scheduled(cron = "0 20 0 * * ?")//每日凌晨12:20执行
    private void remMihoyoMap() {
        mihoyoMap = new HashMap<>();
    }

    /**
     * 原神自动签到
     */
    @Scheduled(cron = "0 30 0 * * ?")//每日凌晨12:30执行
    private synchronized void gsSign() {
        if (!mihoyoMap.containsKey(YSCHECKCODE)) {
            List<GenshinSignIn> gsMihoyo = gsSignInMapping.queryAllAutomatic();//查询出要自动签到的全部记录
            for (GenshinSignIn sign : gsMihoyo) {
                log.info("原神自动签到中当前UID: {}", sign.getUid());
                String cookies = new String(Base64Util.base64decode(sign.getCookie()));//解密cookie
                //创建原神签到实现类
                GenShinSignMiHoYo miHoYo = new GenShinSignMiHoYo(cookies, sign.getUid(), gsType, gsVersion, gsSalt);
                //获取签到状态
                String code = (String) miHoYo.sign();
                if (!code.equals("旅行者,你已经签到过了")) {
                    this.gsSignMessage(sign.getQq(), miHoYo.getName(), sign.getUid(), miHoYo.hubSign(), code);
                }
            }
            mihoyoMap.put(YSCHECKCODE, YSCHECKCODE);
        }
    }

    /**
     * 米游社自动签到
     *
     * @throws Exception 异常
     */
    @Scheduled(cron = "0 30 0 * * ?")//每日凌晨12:30执行
    private synchronized void miHoYoSign() throws Exception {
        if (!mihoyoMap.containsKey(MYSCHECKCODE)) {
            List<MiHoYo> mihoyo = signInMapping.queryAllAutomatic();//查询出要自动签到的全部记录
            for (MiHoYo hoYo : mihoyo) {
                log.info("米游社自动签到中当前UID: {}", hoYo.getUid());
                String cookie = new String(Base64Util.base64decode(hoYo.getCookie()));//解密cookie
                String token = new String(Base64Util.base64decode(hoYo.getToken()));//解密token
                final String result = this.CheckinStatus(cookie, hoYo.getUid(), token, mysType, mysVersion, mysSalt);//签到校验地址
                if (result.equals("invalid request")) {
                    send.privates(hoYo.getQq(), "米游社签到失败invalid request");
                } else if (result.equals("OK")) {
                    String sign = this.sign(cookie, hoYo.getUid(), token, mysType, mysVersion, mysSalt);//执行社区签到
                    send.privates(hoYo.getQq(), String.format("执行状态: 自动\n%s", sign));//发送私聊消息提醒
                }
            }
            mihoyoMap.put(MYSCHECKCODE, MYSCHECKCODE);
        }
    }
}
