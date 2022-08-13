package qqrobot.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnlySession;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.bot.*;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import net.mamoe.mirai.network.WrongPasswordException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import qqrobot.listener.mapper.ReviseBotMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 在线修改推送Bot-持续会话
 */
@Slf4j
@Component
@AllArgsConstructor
public class ReviseBot {
    /**
     * Bot管理器
     */
    private final BotManager manager;
    /**
     * 消息内容生成器工厂
     */
    private final MiraiMessageContentBuilderFactory factory;
    /**
     * Bot数据表
     */
    private final ReviseBotMapping reviseBot;


    /**
     * 通过Map储存事件ID
     * 当前事件ID不存在则发送消息后储存
     * 当前事件ID存在则发送过消息不再发送
     * 储存事件ID防止多Bot状态下发送重复消息
     */
    private final Map<String, String> map = new HashMap<>();

    /* 随便给分组取个名字啥的 */
    private static final String BOTNUMBER_GROUP = "==tell me the robot number of your choice==BOTNUMBER==";//告诉我您选择的机器人编号
    private static final String YES_GROUP = "==tell me if you modify the bot==Yes==";//告诉我你是否修改了机器人

    /**
     * 监听群聊，触发启动事件。
     */
    @OnGroup
    @Filter(value = "修改bot", matchType = MatchType.EQUALS)
    public synchronized void start(@NotNull GroupMsg m, @NotNull ListenerContext context) {
        //获取当前群号
        String grCode = m.getGroupInfo().getGroupCode();
        //获取当前QQ
        String qqCode = m.getAccountInfo().getAccountCode();
        log.info("TriggerGr: {} - ==> TriggerQQ: {} TriggerWord: {}", grCode, qqCode, m.getText());
        //创建Map储存Bot账号信息
        Map<Integer, String> map = new HashMap<>();
        //获取所有Bot
        List<Bot> bots = manager.getBots();
        //获取一个Mirai消息内容生成器
        MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();
        //构建合并消息内容
        builder.forwardMessage(forwardBuilder -> {
            for (int i = 1; i < bots.size(); i++) {
                //先清除一次再构建
                builder.clear();
                //获取bot账号
                String botQQCode = bots.get(i).getBotInfo().getAccountCode();
                //获取bot头像
                String botAvatar = bots.get(i).getBotInfo().getAccountAvatar();
                //获取bot名称
                String botName = bots.get(i).getBotInfo().getAccountNickname();
                //获取秒为单位的时间戳
                String timestamp = String.valueOf(new Date().getTime());
                int length = timestamp.length();
                int integer = Integer.parseInt(timestamp.substring(0, length - 3));
                //创建合并消息单条内容
                assert botAvatar != null;
                forwardBuilder.add(m, integer, builder.text(String.format("序号:%s\n名称:%s\n账号:%s\n", i, botName, botQQCode)).image(botAvatar).build());
                //将序号和bot账号添加到Map内
                map.put(i, botQQCode);
            }
        });
        //判断当前事件ID是否存在，多Bot状态下防止发送重复消息
        if (!this.map.containsKey(m.getId())) {
            //根据当前QQ查找已绑定的Bot账号
            String botQQ = reviseBot.QueryBotQQAccordingToQQ(qqCode);
            Bot bot;
            try {
                //指定一个Bot账号
                bot = manager.getBot(botQQ);
            } catch (WrongPasswordException w) {
                //销毁当前失效的Bot进程
                manager.destroyBot(botQQ);
                bot = manager.getDefaultBot();
            } catch (NoSuchBotException e) {
                bot = manager.getDefaultBot();
            }
            //发送合并消息内容
            bot.getSender().SENDER.sendGroupMsg(grCode, builder.build());
            log.info("修改Bot消息构建完成...");
            //得到session上下文，并断言它的确不是null
            final ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
            assert sessionContext != null;
            // 通过群号和账号拼接一个此人在此群中的唯一key
            String key = String.format("%s:%s", grCode, qqCode);
            //先清除一次再构建
            builder.clear();
            //构建at消息
            String at = builder.at(qqCode).build().getMsg();
            //根据指定的Bot账号进行发送消息
            bot.getSender().SENDER.sendGroupMsg(grCode, String.format("%s\n请输入Bot序号\n请在两分钟内做出答复", at));
            //重新分配局部变量
            Bot finalBot = bot;
            //创建回调函数 SessionCallback 实例。
            //通过 SessionCallback.builder 进行创建
            final SessionCallback<Integer> callback = SessionCallback.<Integer>builder().onResume(botNumber -> {
                finalBot.getSender().SENDER.sendGroupMsg(m, String.format("%s\n你已选择: %s\n为后续消息推送Bot", at, map.get(botNumber)));
                finalBot.getSender().SENDER.sendGroupMsg(m, String.format("%s\n是否确认修改：是/否", at));
                // 这是在回调中继续创建一个会话。
                // 这里通过 sessionContext.waiting(group, key, OnResume) 快速创建一个回调，只处理正确结果的情况，而不处理其他（出现错误、关闭事件等）
                // wait, 这里使用的是 YES_GROUP，也就是等待做出选择，但是key还是那个人对应唯一的key
                sessionContext.waiting(YES_GROUP, key, yes -> {
                    String messageBody = String.format("%s\n你已修改Bot：%s\n为后续消息推送Bot\n记得添加你的专属Bot为好友", at, map.get(botNumber));
                    //yes=true,当前QQ已绑定了其他Bot才执行修改操作
                    if (yes.equals("是") && reviseBot.queryQQ(qqCode)) {
                        finalBot.getSender().SENDER.sendGroupMsg(m, messageBody);
                        //执行修改推送Bot
                        reviseBot.searchAndModifyBotQQAccordingToQQ(qqCode, map.get(botNumber));
                    } else if (yes.equals("否")) {//放弃修改
                        finalBot.getSender().SENDER.sendGroupMsg(m, String.format("%s\n你已放弃修改", at));
                    } else if (!reviseBot.queryQQ(qqCode)) {//当前QQ未绑定任何Bot时执行
                        finalBot.getSender().SENDER.sendGroupMsg(m, messageBody);
                        //当前QQ未绑定任何Bot账号时则进行注册,插入一条新数据
                        reviseBot.add(qqCode, map.get(botNumber));
                    }
                });
            }).onError(e -> {
                //这里是第一个会话，此处通过 onError 来处理出现了异常的情况，例如超时
                if (e instanceof TimeoutException) {
                    // 超时事件是 waiting的第三个参数，可以省略，默认下，超时时间为 1分钟
                    // 这个默认的超时时间可以通过配置 simbot.core.continuousSession.defaultTimeout 进行指定。如果小于等于0，则没有超时，但是不推荐不设置超时。
                    finalBot.getSender().SENDER.sendGroupMsg(m, String.format("%s\n你已放弃修改", at));
                    log.info("{}:超时啦", qqCode);
                } else {
                    log.error("{}:出错啦", qqCode);
                }
            }).onCancel(e -> {
                //这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
                log.info("{}:关闭啦", qqCode);
                log.info("已执行消息撤回");
            }).build(); // build 构建
            //将当前事件ID添加到Map内
            this.map.put(m.getId(), m.getId());
            // 这里开始等待第一个会话。
            sessionContext.waiting(BOTNUMBER_GROUP, key, callback);
        }
    }

    /**
     * 针对上述第一个会话的监听。
     * 因为这里是监听 获取Bot序号 的事件，因此此处的 Filter为 \\d 正则。
     * 通过 @OnlySession来限制，当且仅当由 BOTNUMBER_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = BOTNUMBER_GROUP)
    @Filter(value = "\\d+", matchType = MatchType.REGEX_MATCHES)
    public void botNumber(@NotNull GroupMsg m, @NotNull ListenerContext context) {
        // 得到session上下文。
        final ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;
        final String groupCode = m.getGroupInfo().getGroupCode();
        final String qqCode = m.getAccountInfo().getAccountCode();
        // 拼接出来这个人对应的唯一key
        String key = String.format("%s:%s", groupCode, qqCode);
        final String text = m.getText();
        final Integer botNumber = Integer.parseInt(text);
        // 尝试将这个phone推送给对应的会话。
        session.push(BOTNUMBER_GROUP, key, botNumber);
        /*
            注意！如果你的会话结果逻辑比较复杂，那么你应该先判断这个key对应的会话是否存在，然后再进行推送。
            session.push 在没有发现对应的会话的情况下，会返回false。
         */
    }

    /**
     * 针对上述第二个会话的监听。
     * 因为这里是监听 来确定是否修改 的事件。
     * 通过 @OnlySession来限制，当且仅当由 YES_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = YES_GROUP)
    public void onName(@NotNull GroupMsg m, @NotNull ListenerContext context) {
        // 得到session上下文。
        final ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;
        final String groupCode = m.getGroupInfo().getGroupCode();
        final String qqCode = m.getAccountInfo().getAccountCode();
        // 拼接出来一个对应的唯一key
        String key = String.format("%s:%s", groupCode, qqCode);
        // 尝试推送结果
        session.push(YES_GROUP, key, m.getText());
    }
}

