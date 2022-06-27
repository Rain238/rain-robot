package qqrobot.module.mihoyo.genshin;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;
import qqrobot.module.mihoyo.AbstractMessage;
import qqrobot.module.mihoyo.genshin.bean.GenshinSignIn;
import qqrobot.module.mihoyo.genshin.mapper.GsSignInMapping;
import qqrobot.module.mihoyo.sign.mapper.SignInMapping;
import qqrobot.simple.Get;
import qqrobot.util.Base64Util;
import qqrobot.simple.Send;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 监听原神消息操作类
 * <p>
 * &#064;Author  Light rain
 * &#064;Date  2022/5/26 7:53
 */
@Slf4j
@Component
public class GsSignIn extends AbstractMessage {
    private final GsSignInMapping mapping;//原神数据表
    private final Send send;//发送消息
    private final Get get;//获取数据

    public GsSignIn(GsSignInMapping gsSignMessage, SignInMapping signInMapping, Send send, GsSignInMapping mapping, Send send1, Get get) {
        super(gsSignMessage, signInMapping, send);
        this.mapping = mapping;
        this.send = send1;
        this.get = get;
    }


    /**
     * 监听私聊绑定原神
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filter(value = "绑定原神", matchType = MatchType.EQUALS)
    public void privateBoundGenshinImpact(PrivateMsg privateMsg, Sender sender) {
        String qqCode = get.prCode(privateMsg);
        send.privates(qqCode, "格式为：\n原神Uid=146613006\n原神Cookie=_MHYUUID=xxx,请注意字母大小写");
    }

    /**
     * 监听私聊绑定原神格式
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filters(value = {@Filter(value = "原神Uid=", matchType = MatchType.CONTAINS, trim = true), @Filter(value = "原神Cookie=", matchType = MatchType.CONTAINS, trim = true)})
    public void privateBindUidAndCookie(PrivateMsg privateMsg, Sender sender) {
        //获取消息文本
        String msgText = get.text(privateMsg);
        //获取当前QQ账号
        String qqCode = get.prCode(privateMsg);
        try {
            //截取出Uid的值                                                                去除换行符
            String uid = msgText.split("原神Uid=")[1].split("原神Cookie=")[0].replace("\n", "");
            //查询当前uid是否存在
            if (mapping.queryRecordByUid(uid) == 0) {
                //截取出Cookie的值
                String cookie = msgText.split("原神Cookie=")[1];
                //将Cookie进行加密
                String cookies = Base64Util.base64encode(cookie.getBytes(StandardCharsets.UTF_8));
                //插入数据到数据库
                mapping.addGenshinUidAndCookie(qqCode, uid, cookies, new Date().getTime());
                send.privates(qqCode, "恭喜账号绑定成功");
            } else {
                send.privates(qqCode, "该Uid已绑定过了哦");
            }
        } catch (Exception e) {
            e.printStackTrace();
            send.privates(qqCode, "Uid或Cookie未填写正确");
        }
    }

    /**
     * 监听私聊解绑原神账号
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filter(value = "解绑原神", matchType = MatchType.CONTAINS)
    public void privateUnbind(PrivateMsg privateMsg, Sender sender) {
        //获取消息文本
        String msgText = get.text(privateMsg);
        //获取当前QQ账号
        String qqCode = get.prCode(privateMsg);
        try {
            //从消息体中截取出uid
            String uid = msgText.split("解绑原神")[1];
            //根据uid查询数据库记录并删除该记录
            if (mapping.queryRecordByUid(uid) == 1) {
                //删除指定记录
                mapping.deleteByUid(uid);
                send.privates(qqCode, "已解除绑定");
            } else {
                send.privates(qqCode, "UID未绑定");
            }
        } catch (Exception e) {
            send.privates(qqCode, "未输入正确Uid");
        }
    }


    /**
     * 监听私/群聊原神签到
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filter(value = "原神签到", matchType = MatchType.EQUALS)
    public void genshinSign(PrivateMsg privateMsg, Sender sender) {
        //获取当前QQ账号
        String qqCode = get.prCode(privateMsg);
        //查询当前QQ号绑定的原神账号是否存在没有则先绑定账号
        if (mapping.queryUidByQQ(qqCode) != 0) {
            //一个QQ可绑定多个原神UID
            List<GenshinSignIn> genshinSignIn = mapping.queryUidAndCookieByQQ(qqCode);
            for (GenshinSignIn signIn : genshinSignIn) {
                //解密cookie
                String cookies = new String(Base64Util.base64decode(signIn.getCookie()));
                //创建原神签到实现类
                GenShinSignMiHoYo genshin = new GenShinSignMiHoYo(cookies, signIn.getUid());
                //获取签到状态
                String code = (String) genshin.sign();
                this.gsSignMessage(qqCode, genshin.getName(), signIn.getUid(), genshin.hubSign(), code);
            }
        } else {
            send.privates(qqCode, "请发送\"绑定原神\"进行账号绑定");
        }
    }

    /**
     * 监听私聊开启/关闭原神自动签到
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filters(value = {@Filter(value = "开启原神自动签到", matchType = MatchType.EQUALS, trim = true), @Filter(value = "关闭原神自动签到", matchType = MatchType.EQUALS, trim = true)})
    public void groupMsgAutomaticCheckin(PrivateMsg privateMsg, Sender sender) {
        //获取消息文本
        String msgText = get.text(privateMsg);
        //获取当前QQ账号
        String qqCode = get.prCode(privateMsg);
        //根据当前QQ查询是否存在已绑定记录
        if (mapping.queryUidByQQ(qqCode) != 0) {
            if (Objects.equals(msgText, "开启原神自动签到")) {
                mapping.upDataAutomaticCheckin(qqCode, 1);
                send.privates(qqCode, "已开启原神自动签到\n签到提醒为早上7点左右");
            } else {
                mapping.upDataAutomaticCheckin(qqCode, 0);
                send.privates(qqCode, "已关闭原神自动签到");
            }
        } else {
            send.privates(qqCode, "请先绑定账号");
        }
    }
}
