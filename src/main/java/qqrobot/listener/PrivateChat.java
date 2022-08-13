package qqrobot.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.FriendAddRequest;
import love.forte.simbot.api.message.events.FriendIncrease;
import love.forte.simbot.api.message.events.FriendReduce;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.filter.MatchType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qqrobot.listener.mapper.GsMapping;
import qqrobot.listener.mapper.MihoyoMapping;
import qqrobot.listener.mapper.ReviseBotMapping;
import qqrobot.simple.Send;

import java.util.List;

/**
 * 私聊事件监听类
 * 所有需要被管理的类都需要标注 {@link Beans} 注解。
 * <p>
 * &#064;Author  RainRain
 * &#064;Data  2022/5/26 15:38
 */
@Slf4j
@Component
//@AllArgsConstructor
public class PrivateChat {
    /**
     * 原神数据表
     */
    private final GsMapping gsMapping;
    /**
     * 米游社数据表
     */
    private final MihoyoMapping mihoyoMapping;
    /**
     * Bot管理器
     */
    private final BotManager manager;
    /**
     * 消息内容生成器工厂
     */
    private final MessageContentBuilderFactory factory;
    /**
     * Bot数据表
     */
    private final ReviseBotMapping reviseBot;
    /**
     * 发送消息
     */
    private final Send send;
    /**
     * 最高权限操控者账号，可在配置文件中修改
     */
    @Value("${master.qq}")
    private String Master;

    public PrivateChat(GsMapping gsMapping, MihoyoMapping mihoyoMapping, BotManager manager, MessageContentBuilderFactory factory, ReviseBotMapping reviseBot, Send send) {
        this.gsMapping = gsMapping;
        this.mihoyoMapping = mihoyoMapping;
        this.manager = manager;
        this.factory = factory;
        this.reviseBot = reviseBot;
        this.send = send;
    }

    /**
     * 监听好友请求事件
     * Bot自动同意加为好友
     *
     * @param friend 监听好友请求事件
     * @param setter 设置自动同意好友
     */
    @OnFriendAddRequest
    public void friend(@NotNull FriendAddRequest friend, @NotNull Setter setter) {
        log.info("新增好友申请: {} 附带消息: {}", friend.getAccountInfo().getAccountCode(), friend.getFlag());
        setter.setFriendAddRequestAsync(friend.getFlag(), friend.getAccountInfo().getAccountNickname(), true, false);
    }

    /**
     * 监听好用增加事件
     *
     * @param fi     监听好用增加事件
     * @param getter 获取当前bot账号
     */
    @OnFriendIncrease
    public void friendIncrease(FriendIncrease fi, @NotNull Getter getter) {
        //获取当前botQQ
        String botQQ = getter.getBotInfo().getAccountCode();
        //获取当前好友QQ
        String friendQQ = fi.getAccountInfo().getAccountCode();
        log.info("Remind: {} - ==> 已经成为 {} 好友", friendQQ, botQQ);
        //查询对方账号是否注册,注册过将不再执行注册,以免抛出异常
        if (!reviseBot.queryQQ(friendQQ)) {
            //将对方账号和当前Bot账号进行注册
            reviseBot.add(friendQQ, botQQ);
        }
//        send.privates(friendQQ, botQQ, String.format("Hello主人，我是 %s 你的专属消息推送助手，如你不喜欢我可在总群发送“修改bot”，选择你的专属推送助手", getter.getBotInfo().getBotName()));
        send.privates(friendQQ, botQQ, String.format("Hello主人，我是 %s 你的专属消息推送助手", getter.getBotInfo().getBotName()));
        send.privates(friendQQ, botQQ, "第一次使用不会怎么办？");
        send.privates(friendQQ, botQQ, "亲爱的主人，看这里\nhttps://www.yuque.com/docs/share/daff7200-3d76-48e8-9e75-29b6f4034d4e?# 《樱野栗梦使用手册》");
        send.privates(friendQQ, botQQ, String.format("主人如有其他问题可联系给予我生命的主人酱：%s", Master));
    }

    /**
     * 监听好友减少事件
     *
     * @param friendReduce 监听好友减少事件
     */
    @OnFriendReduce
    public void friendReduce(@NotNull FriendReduce friendReduce) {
        //获取当前Bot账号
        String currentBotCode = friendReduce.getBotInfo().getAccountCode();
        //获取被删好友的QQ账号
        String qqCode = friendReduce.getAccountInfo().getAccountCode();
        log.info("DeleteRemind: {} - ==> 已将 {} Bot删除...", qqCode, currentBotCode);
        //根据QQ查询Bot账号
        String botCode = reviseBot.QueryBotQQAccordingToQQ(qqCode);
        //判断删除的是否为已绑的Bot账号,如果是则清空全部数据,如果不是则不执行任何操作
        //避免删除其他Bot账号时清空所有已绑定数据
        if (currentBotCode.equals(botCode)) {
            //删除绑定的原神账号
            gsMapping.deleteByQQ(qqCode);
            //删除绑定的米游社账号
            mihoyoMapping.deleteByQQ(qqCode);
            //删除Bot表内的注册信息
            reviseBot.deleteAccordingToQQ(qqCode);
        }
    }

    /**
     * 检测所有Bot在线状态
     *
     * @param msg 私聊消息
     */
    @OnPrivate
    @Filter(value = "bot列表", matchType = MatchType.EQUALS, trim = true)
    public void botList(@NotNull PrivateMsg msg) {
        log.info("TriggerQQ: {} TriggerWord: {}", msg.getAccountInfo().getAccountCode(), msg.getText());
        String qqCode = msg.getAccountInfo().getAccountCode();
        //获取所有Bot
        List<Bot> bots = manager.getBots();
        for (Bot bot : bots) {
            //获取bot账号
            String botQQCode = bot.getBotInfo().getAccountCode();
            //获取bot头像
            String botAvatar = bot.getBotInfo().getAccountAvatar();
            //获取bot名称
            String botName = bot.getBotInfo().getAccountNickname();
            //获取消息工厂
            MessageContentBuilder message = factory.getMessageContentBuilder();
            assert botAvatar != null;
            send.privates(qqCode, message.text(String.format("名称:%s\n账号:%s\n", botName, botQQCode)).image(botAvatar).build());
        }
    }

    //    @OnPrivate
//    public void a(@NotNull PrivateMsg msg) {
//        final String originalData = msg.getOriginalData();
//        System.out.println(originalData);
//        MessageContent msgContent = msg.getMsgContent();
//        // 打印消息主体
//        System.out.println(msgContent);
//        // 打印消息主体中的所有图片的链接（如果有的话）
//        List<Neko> imageCats = msgContent.getCats("image");
//        System.out.println("img counts: " + imageCats.size());
//        for (Neko image : imageCats) {
//            System.out.println("Img url: " + image.get("url"));
//        }
//    }

}
