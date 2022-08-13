package qqrobot.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.*;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.bot.*;
import love.forte.simbot.filter.MatchType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qqrobot.listener.mapper.GsMapping;
import qqrobot.listener.mapper.MihoyoMapping;
import qqrobot.listener.mapper.ReviseBotMapping;
import qqrobot.simple.Send;

import java.security.SecureRandom;
import java.util.*;

/**
 * 群聊事件监听类
 * 所有需要被管理的类都需要标注 {@link Beans} 注解。
 * <p>
 * &#064;Author  RainRain
 * &#064;Data  2022/5/26 15:38
 */
@Slf4j
//@AllArgsConstructor
@Component
public class GroupChat {
    /**
     * 消息内容生成器工厂
     */
    private final MessageContentBuilderFactory mCBF;

    /**
     * Bot管理器
     */
    private final BotManager manager;
    /**
     * 通过Map储存当前事件ID
     * 防止Bot发送重复消息
     */
    private final Map<String, String> map = new HashMap<>();
    /**
     * 通过Map储存当前进群者的QQ账号
     * 当前进群者的QQ账号不存在则发送消息后储存
     * 当前进群者的QQ账号存在则发送过消息不再发送
     * 储存进群者的QQ账号防止多Bot状态下发送重复消息
     * <p>
     * 当进群者进群后退群会将此集合内删掉进群者的QQ账号
     * 重新进群后会和新人一样触发效果
     * 避免一个人重复进群只有第一次触发效果
     */
    private final Map<String, String> increaseDecreaseMap = new HashMap<>();
    /**
     * Bot数据表
     */
    private final ReviseBotMapping reviseBot;
    /**
     * 米哈游数据表
     */
    private final MihoyoMapping mihoyo;
    /**
     * 原神数据表
     */
    private final GsMapping gs;
    /**
     * 发送消息
     */
    private final Send send;

    /**
     * 最高权限操控者账号，可在配置文件中修改
     */
    @Value("${master.qq}")
    private String Master;

    private final Map<String, StringBuilder> qqVerificationCode = new HashMap<>();

    public GroupChat(MessageContentBuilderFactory mCBF, BotManager manager, ReviseBotMapping reviseBot, MihoyoMapping mihoyo, GsMapping gs, Send send) {
        this.mCBF = mCBF;
        this.manager = manager;
        this.reviseBot = reviseBot;
        this.mihoyo = mihoyo;
        this.gs = gs;
        this.send = send;
    }

    /**
     * 同步监听群成员增加事件
     * 多Bot状态下不再发送重复消息
     *
     * @param gmi 监听群成员增加事件
     */
    @OnGroupMemberIncrease
    public synchronized void groupMemberIncrease(@NotNull GroupMemberIncrease gmi, @NotNull Getter getter, Setter setter) {
        log.info("GroupNumber: {} <==-<== NewIncreaseQQ: {} NewIncreaseName: {}", gmi.getGroupInfo().getGroupCode(), gmi.getAccountInfo().getAccountCode(), gmi.getAccountInfo().getAccountNickname());
        //获取进群者QQ账号
        String qqCode = gmi.getAccountInfo().getAccountCode();
        //获取本群群号
        String grCode = gmi.getGroupInfo().getGroupCode();
        //获取消息内容生成器
        MessageContentBuilder message = mCBF.getMessageContentBuilder();
        //at当前进群的成员
        MessageContent at = message.at(qqCode).build();
        //获取当前群的人数
        int total = getter.getGroupInfo(grCode).getTotal();
        //自定义事件id
        String eventID = String.format("MGMInc-%s", qqCode);
        //map集合内没有当前事件ID时进入,防止多Bot状态下发送重复消息
        if (!increaseDecreaseMap.containsKey(qqCode)) {
            //定义取值范围 生成随机的4位数字验证码
            String str = "0123456789";
            //容量为4
            StringBuilder stb = new StringBuilder(4);
            for (int i = 0; i < 4; i++)
                stb.append(str.charAt(new SecureRandom().nextInt(str.length())));//遍历4次，拿到某个字符并且拼接
            Timer timer = new Timer();
            //延迟1.5s后执行 如不延迟有概率发送失败，加群者还没进入就已经执行到了此方法自然@会找不到群成员
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    send.groups(eventID, grCode, Master, String.format("欢迎%s加入~\n你是本群第%s个成员！", at.getMsg(), total));
                    send.groups(eventID, grCode, Master, "置顶公告中可能会有你想要的各种信息，记得优先查看");
                    send.groups(eventID, grCode, Master, String.format("%s你需要在「5分钟」内发送验证码%s，否则将会被请离喔～", at.getMsg(), stb));
                }
            }, 1500);
            //将进群者账号和当前生成的随机验证码存入Map集合内
            qqVerificationCode.put(qqCode, stb);
            //获取一个默认bot账号
            String accountCode = manager.getDefaultBot().getSender().getBotInfo().getAccountCode();
            //新入群成员将进行Bot账号绑定
            reviseBot.add(qqCode, accountCode);
            //将当前事件ID添加到map集合内防止多Bot状态下发送重复消息
            increaseDecreaseMap.put(qqCode, qqCode);
            //延迟10分钟后执行 如果qqVerificationCode集合内还有该账号记录则未完成验证将其踢除
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (qqVerificationCode.containsKey(qqCode)) {
                        setter.setGroupMemberKick(grCode, qqCode, "未在规定时间内完成验证~", false);
                    }
                }
            }, 5 * 60 * 1000);
        }
    }

    /**
     * 获取验证码
     * @param msg GroupMsg 群聊消息
     */
    @OnGroup
    @Filter(value = "^[0-9]{4}", matchType = MatchType.REGEX_MATCHES)
    public void getVerificationCode(GroupMsg msg) {
        //获取当前QQ账号
        String qqCode = msg.getAccountInfo().getAccountCode();
        //根据当前QQ账号查找qqVerificationCode集合内是否存在记录
        if (qqVerificationCode.containsKey(qqCode)) {
            //获取消息体
            String text = msg.getText();
            //获取当前集合内的验证码
            StringBuilder stb = qqVerificationCode.get(qqCode);
            //判断消息体是否与验证码一致，一致则为验证成功
            if (text.equals(stb.toString())) {
                MessageContent at = mCBF.getMessageContentBuilder().at(qqCode).build();
                qqVerificationCode.remove(qqCode);
                send.groups(msg, String.format("%s已通过验证", at.getMsg()));
            }
        }
    }

    /**
     * 同步监听群成员减少事件
     * 多Bot状态下不再发送重复消息
     *
     * @param gbr 监听群成员减少事件
     */
    @OnGroupMemberReduce
    public synchronized void getAccountInfo(@NotNull GroupMemberReduce gbr) {
        log.info("ExitReminder: {}已从{}群中离开...", gbr.getAccountInfo().getAccountCode(), gbr.getGroupInfo().getGroupCode());
        String grCode = gbr.getGroupInfo().getGroupCode();
        String qqCode = gbr.getAccountInfo().getAccountCode();
        String accountName = gbr.getAccountInfo().getAccountNickname();
        //自定义事件
        String eventID = String.format("MGMInc-%s", qqCode);
        //防止多Bot状态下发送重复消息，如果不try...catch第二个Bot线程执行时将会找不到key后抛出异常
        try {
            if (increaseDecreaseMap.containsKey(qqCode) || reviseBot.queryQQ(qqCode)) {
                send.groups(eventID, grCode, Master, String.format("%s离开了我们...", accountName));
                //删除绑定的原神账号
                gs.deleteByQQ(qqCode);
                //删除绑定的米游社账号
                mihoyo.deleteByQQ(qqCode);
                //删除Bot表内的注册信息
                reviseBot.deleteAccordingToQQ(qqCode);
                //将该账号在Map内删除
                increaseDecreaseMap.remove(qqCode);

                qqVerificationCode.remove(qqCode);
            }
        } catch (Exception e) {
            log.warn("{}已退群...", accountName);
        }
    }

    /**
     * 监听群添加请求事件 自动同意入群
     *
     * @param request 群添加请求事件
     */
    @OnGroupAddRequest
    public void groupAddRequest(@NotNull GroupAddRequest request, @NotNull Setter setter) {
        log.info("EnterGroupReminder: {}请求进入{}群,入群理由: {}", request.getAccountInfo().getAccountCode(), request.getGroupInfo().getGroupCode(), request.getFlag());
        //自动同意入群申请                        简介                     是否同意       是否黑名单    理由
        setter.setGroupAddRequestAsync(request.getFlag(), true, false, "");
    }


    /**
     * 此方法用于检测所有Bot在线状态，首先要确保application.yml中登录的Bot账号全部在同一个群里面否则功能将会会出现异常
     *
     * @param msg GroupMsg
     */
    @OnGroup
    @Filter(value = "栗梦", matchType = MatchType.EQUALS, trim = true)
    public synchronized void botList(@NotNull GroupMsg msg) {
        log.info("TriggerQQ: {} TriggerWord: {}", msg.getAccountInfo().getAccountCode(), msg.getText());
        if (!map.containsKey(msg.getId())) {
            String qqCode = msg.getAccountInfo().getAccountCode();
            String grCode = msg.getGroupInfo().getGroupCode();
            //获取一个消息内容生成器
            MessageContentBuilder builder = mCBF.getMessageContentBuilder();
            //构建at消息
            String at = builder.at(qqCode).build().getMsg();
            //获取所有Bot账号
            List<Bot> bots = manager.getBots();
            for (Bot bot : bots) {
                //获取bot账号
                String botQQCode = bot.getBotInfo().getAccountCode();
                //指定一个Bot账号进行发送消息
                Bot bot1 = manager.getBot(botQQCode);
                bot1.getSender().SENDER.sendGroupMsg(grCode, String.format("%s\n干嘛啦", at));
            }
            map.put(msg.getId(), msg.getId());
        }
    }
}
