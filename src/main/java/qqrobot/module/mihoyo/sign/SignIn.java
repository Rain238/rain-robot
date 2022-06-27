package qqrobot.module.mihoyo.sign;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;
import qqrobot.module.mihoyo.AbstractMessage;
import qqrobot.module.mihoyo.genshin.mapper.GsSignInMapping;
import qqrobot.module.mihoyo.sign.bean.MiHoYo;
import qqrobot.module.mihoyo.sign.mapper.SignInMapping;
import qqrobot.simple.Get;
import qqrobot.util.Base64Util;
import qqrobot.util.GetstokenUtils;
import qqrobot.simple.Send;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * 监听米游社社区签到
 *
 * &#064;Author  Light rain
 * &#064;Date  2022/5/26 7:58
 */
@Slf4j
@Component
public class SignIn extends AbstractMessage {
    private final MessageContentBuilderFactory messageContentBuilderFactory;//获取消息内容生成器工厂
    private final SignInMapping mapping;//米游社数据表
    private final Send send;//发送消息
    private final Get get;//获取数据


    public SignIn(GsSignInMapping gsSignMessage, SignInMapping signInMapping, Send send, MessageContentBuilderFactory messageContentBuilderFactory, SignInMapping mapping, Send send1, Get get) {
        super(gsSignMessage, signInMapping, send);
        this.messageContentBuilderFactory = messageContentBuilderFactory;
        this.mapping = mapping;
        this.send = send1;
        this.get = get;
    }


    /**
     * 监听私/群聊米游社签到
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filter(value = "米游社签到", matchType = MatchType.EQUALS, trim = true)
    public void signIn(PrivateMsg privateMsg, Sender sender) throws Exception {
        String qqCode = get.prCode(privateMsg);
        MessageContentBuilder message = messageContentBuilderFactory.getMessageContentBuilder();
        //根据当前QQ查询是否已绑定米游社
        if (mapping.queryUidByQQ(qqCode) == 1) {
            send.privates(qqCode, "正在执行签到请稍后...");
            //根据当前QQ查询出cookie/通行证id/stoken
            MiHoYo miHoYo = mapping.queryUidAndCookieOrTokenByQQ(qqCode);
            //解密cookie
            String cookie = new String(Base64Util.base64decode(miHoYo.getCookie()));
            //解密token
            String token = new String(Base64Util.base64decode(miHoYo.getToken()));
            //执行签到并返回数据
            String sign = this.sign(cookie, miHoYo.getUid(), token);
            send.privates(qqCode, sign);
        } else {
            send.privates(qqCode, "请先绑定米游社");
        }
    }

    /**
     * 监听私聊绑定米游社
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filter(value = "绑定米游社", matchType = MatchType.EQUALS, trim = true)
    public void privateMsgBindMiyouClub(PrivateMsg privateMsg, Sender sender) {
        String qqCode = get.prCode(privateMsg);
        //根据该QQ查询是否已绑定米游社
        if (mapping.queryUidByQQ(qqCode) != 1) {
            send.privates(qqCode, "格式为：\n通行证ID=你的通行证ID\n米游社Cookie=你的cookie,请注意字母大小写");
        } else {
            //根据当前QQ查询出cookie/通行证id/stoken
            MiHoYo miHoYo = mapping.queryUidAndCookieOrTokenByQQ(qqCode);
            //提示已经绑定过了米游社
            send.privates(qqCode, String.format("已绑定通行证ID:%s", miHoYo.getUid()));
        }
    }

    /**
     * 监听私聊绑定米游社格式
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filters(value = {@Filter(value = "通行证ID=", matchType = MatchType.CONTAINS, trim = true), @Filter(value = "米游社Cookie=", matchType = MatchType.CONTAINS, trim = true)})
    public void s(PrivateMsg privateMsg, Sender sender) {
        String qqCode = get.prCode(privateMsg);
        String msgText = get.text(privateMsg);
        try {
            //通行证id                                                                     去除换行符
            String uid = msgText.split("通行证ID=")[1].split("米游社Cookie=")[0].replace("\n", "");
            //cookie
            String cookie = msgText.split("Cookie=")[1];
            //根据cookie获取token
            String token = GetstokenUtils.doGen(cookie);
            if (token.equals("login_ticket已失效,请重新登录获取")) {
                send.privates(qqCode, String.format("%s\n请重新登陆此网站获取\nhttps://user.mihoyo.com/#/login/captcha", token));
            } else if (mapping.queryUidByQQ(qqCode) != 1) {
                //将cookie进行加密
                String cookies = Base64Util.base64encode(cookie.getBytes(StandardCharsets.UTF_8));
                //将tokens进行加密
                String tokens = Base64Util.base64encode(token.getBytes(StandardCharsets.UTF_8));
                //将数据插入到数据库
                mapping.addUidAndCookieOrToken(qqCode, uid, cookies, tokens, new Date().getTime());
                send.privates(qqCode, "恭喜成功绑定通行证");
                send.privates(qqCode, "开启米游社自动签到\n可每天自动执行任务哦\n早上7点左右发送通知");
            } else {
                //根据当前QQ查询出cookie/通行证id/stoken
                MiHoYo miHoYo = mapping.queryUidAndCookieOrTokenByQQ(qqCode);
                //提示已经绑定过了米游社
                send.privates(qqCode, String.format("已绑定通行证ID:%s", miHoYo.getUid()));
            }
        } catch (Exception e) {
            send.privates(qqCode, "通行证ID或Cookie未填写正确");
        }
    }

    /**
     * 监听私聊开启/关闭米游社自动签到
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filters(value = {@Filter(value = "开启米游社自动签到", matchType = MatchType.EQUALS, trim = true), @Filter(value = "关闭米游社自动签到", matchType = MatchType.EQUALS, trim = true)})
    public void privateMsgAutomaticCheckin(PrivateMsg privateMsg, Sender sender) {
        String qqCode = get.prCode(privateMsg);
        String msgText = get.text(privateMsg);
        if (mapping.queryUidByQQ(qqCode) == 1) {
            if (Objects.equals(msgText, "开启米游社自动签到")) {
                //开启当前QQ账号绑定的米游社通行证为自动签到
                mapping.upDataAutomaticCheckin(qqCode, 1);
                send.privates(qqCode, "已开启米游社自动签到\n自动签到提醒为早上7点左右");
            } else {
                //关闭自动签到当前QQ账号绑定的米游社通行证
                mapping.upDataAutomaticCheckin(qqCode, 0);
                send.privates(qqCode, "已关闭米游社自动签到");
            }
        } else {
            send.privates(qqCode, "请先绑定米游社哦");
        }
    }

    /**
     * 监听私聊解绑米游社
     *
     * @param privateMsg 私聊消息
     * @param sender     发送消息
     */
    @OnPrivate
    @Filter(value = "解绑米游社", matchType = MatchType.EQUALS, trim = true)
    public void privateMsgUnbind(PrivateMsg privateMsg, Sender sender) {
        String qqCode = get.prCode(privateMsg);
        if (mapping.deleteByQQ(qqCode)) {
            send.privates(qqCode, "已解绑通行证ID");
        } else {
            send.privates(qqCode, "尚未绑定通行证");
        }
    }
}
