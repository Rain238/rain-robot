package qqrobot.module.mihoyo;

import lombok.AllArgsConstructor;
import qqrobot.module.mihoyo.genshin.mapper.GsSignInMapping;
import qqrobot.module.mihoyo.sign.mapper.SignInMapping;
import qqrobot.simple.Send;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@AllArgsConstructor
public abstract class AbstractMessage {
    private final GsSignInMapping gsSignMessage;//原神数据表
    private final SignInMapping signInMapping;//米游社数据表
    private final Send send;//发送消息

    /**
     * 原神签到返回结果
     *
     * @param qq      主人QQ
     * @param name    原神名称
     * @param uid     原神uid
     * @param hubSign 社区签到结果
     * @param code    结果状态
     */
    public void gsSignMessage(String qq, String name, String uid, String hubSign, String code) {
        if (code.contains("OK")) {
            send.privates(qq, String.format("名称: %s\n签到成功\n%s", name, hubSign));
        } else if (code.contains("旅行者")) {
            send.privates(qq, String.format("名称: %s\n今日你已签到了哦", name));
        } else if (code.contains("尚未登录")) {
            send.privates(qq, String.format("UID:%s\n原神Cookie已失效请重新绑定", uid));
            //将失效的Cookie账号数据清除方便重新绑定
            gsSignMessage.deleteByUid(uid);
        }
    }

    /**
     * 米游社签到并返回结果
     *
     * @param cookie 米游社Cookie
     * @param stuid  米游社通行证ID
     * @param stoken 米游社Token
     * @return String
     * @throws Exception 线程可能抛出异常
     */
    public String sign(String cookie, String stuid, String stoken) throws Exception {
        //创建线程
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        String bhs = (String) new MiHoYoSignMiHoYo(cookie, MiHoYoConfig.HubsEnum.BH3.getGame(), stuid, stoken, executor).doSign();
        if (!bhs.contains("米游社Cookie失效")) {
            new MiHoYoSignMiHoYo(cookie, MiHoYoConfig.HubsEnum.BH2.getGame(), stuid, stoken, executor).doSign();
            new MiHoYoSignMiHoYo(cookie, MiHoYoConfig.HubsEnum.DBY.getGame(), stuid, stoken, executor).doSign();
            new MiHoYoSignMiHoYo(cookie, MiHoYoConfig.HubsEnum.YS.getGame(), stuid, stoken, executor).doSign();
            new MiHoYoSignMiHoYo(cookie, MiHoYoConfig.HubsEnum.WD.getGame(), stuid, stoken, executor).doSign();
        } else {
            //将失效的Cookie账号数据进行删除方便重新绑定
            signInMapping.deleteByUid(stuid);
        }
        return bhs;
    }
}
