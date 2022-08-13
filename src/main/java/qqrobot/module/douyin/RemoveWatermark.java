package qqrobot.module.douyin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import qqrobot.simple.Send;
import qqrobot.util.HttpUtil;

import java.util.Objects;

/**
 * 抖音视频去水印
 * 支持PC分享/手机分享链接
 * <p>
 * &#064;Author  Light rain
 * &#064;Date  2022/7/03 4:02
 */
@Slf4j
@Component
@AllArgsConstructor
public class RemoveWatermark {
    /**
     * 发送消息
     */
    private final Send send;
    /**
     * 消息内容生成器工厂
     */
    private final MiraiMessageContentBuilderFactory factory;
    /**
     * 去水印接口
     */
    private static final String biglee = "https://biglee.pro/douyin/api.php";
    /**
     * 转换短链接接口
     */
    private static final String ock = "https://ock.cn/api/short?longurl=%s";

    @OnGroup//监听群聊
    @OnPrivate//监听私聊
    @Filter(value = "https://v.douyin.com/", matchType = MatchType.CONTAINS, trim = true)
    public void removeWatermark(MsgGet msg, Sender s) {
        if (msg instanceof GroupMsg)
            log.info("TriggerGr: {} - ==> TriggerQQ: {} TriggerWord: {}", ((GroupMsg) msg).getGroupInfo().getGroupCode(), msg.getAccountInfo().getAccountCode(), msg.getText());
        else if (msg instanceof PrivateMsg)
            log.info("TriggerQQ: {} TriggerWord: {}", msg.getAccountInfo().getAccountCode(), msg.getText());
        //截取出抖音Https链接
        final String ur = String.format("https%s", Objects.requireNonNull(msg.getText()).split("https")[1]);//字符串截取
        final String url = ur.substring(0, ur.lastIndexOf("/")) + "/";//从后面斜杠开始再截取一遍，用于截取Pc端抖音链接
        final String bigleeResult = HttpUtil.sendPost(biglee, url);//拿到链接后进行去水印操作
        JSONObject bigleeJson = JSONObject.fromObject(bigleeResult);//获取去除水印的Json对象
        final Object bigleeCode = bigleeJson.get("code");//状态码
        final Object bigleeMsg = bigleeJson.get("msg");//消息
        final Object bigleeData = bigleeJson.get("data");//抖音无水印视频链接
        final String ockResult = HttpUtil.sendGet(String.format(ock, bigleeData.toString()));//将无水印链接转换为短链接
        JSONObject shortLinkJson = JSONObject.fromObject(ockResult);//获取短链接Json对象
        final Object ockData = shortLinkJson.get("data");//获取链接数组
        final JSONObject cokJson = JSONObject.fromObject(ockData);//将链接数组转换成Json对象
        final Object shortLink = cokJson.get("short");//获取短链接
        final Object ockCode = shortLinkJson.get("code");//短链接状态码
        final String qqCode = msg.getAccountInfo().getAccountCode();//获取当前QQ账号
        MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();//获取一个Mirai消息内容生成器
        String at = builder.at(qqCode).build().getMsg();//构建at消息
        final boolean code = bigleeCode.toString().equals("200") && ockCode.toString().equals("1000");//成功状态码
        final boolean groups = msg instanceof GroupMsg;//判断群聊消息
        final boolean privates = msg instanceof PrivateMsg;//判断私聊消息
        final String str = String.format("%s\n%s", bigleeMsg, shortLink);//消息内容
        if (code && groups) send.groups(((GroupMsg) msg), at + "\n" + str);
        else if (code && privates) send.privates(qqCode, str);
        else if (!code && groups) send.groups((GroupMsg) msg, at + "\n获取失败！");
        else if (!code && privates) send.privates(qqCode, "获取失败！");
    }
}
