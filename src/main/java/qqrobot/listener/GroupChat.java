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

import java.io.IOException;
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
     * 效验Bot账号
     */
    private final BotRegistrar re;
    /**
     * 最高权限操控者账号，可在配置文件中修改
     */
    @Value("${master.qq}")
    private String Master;
    /**
     * 重启脚本文件路径，可在配置文件中设置
     */
    @Value("${restart.script}")
    private String restartScript;

    public GroupChat(MessageContentBuilderFactory mCBF, BotManager manager, ReviseBotMapping reviseBot, MihoyoMapping mihoyo, GsMapping gs, Send send, BotRegistrar re) {
        this.mCBF = mCBF;
        this.manager = manager;
        this.reviseBot = reviseBot;
        this.mihoyo = mihoyo;
        this.gs = gs;
        this.send = send;
        this.re = re;
    }

    /**
     * 同步监听群成员增加事件
     * 多Bot状态下不再发送重复消息
     *
     * @param gmi 监听群成员增加事件
     */
    @OnGroupMemberIncrease
    public synchronized void groupMemberIncrease(@NotNull GroupMemberIncrease gmi, @NotNull Getter getter) {
        String qqCode = gmi.getAccountInfo().getAccountCode();
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
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    send.groups(eventID, grCode, Master, String.format("欢迎%s加入~\n你是本群第%s个成员！", at.getMsg(), total));
                    send.groups(eventID, grCode, Master, "置顶公告中可能会有你想要的各种信息，记得优先查看");
                    send.groups(eventID, grCode, Master, String.format("%s你需要在接下来的「10分钟」内至少冒泡一次，否则将会被请离喔～", at.getMsg()));
                }
            }, 1500);
            //获取所有Bot
            List<Bot> bots = manager.getBots();
            for (int i = 1; i < bots.size(); i++) {
                final String botCode = bots.get(i).getBotInfo().getAccountCode();
                BotRegisterInfo r = new BotRegisterInfo(botCode, "");
                try {
                    //效验当前Bot账号
                    re.registerBot(r);
                    //新入群成员将进行Bot账号绑定
                    reviseBot.add(qqCode, botCode);
                    //将当前事件ID添加到map集合内防止多Bot状态下发送重复消息
                    increaseDecreaseMap.put(qqCode, qqCode);
                    break;
                } catch (BotVerifyException e) {
                    log.error("{}:Bot账号登陆效验失败！", botCode);
                }
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
            }
        } catch (Exception e) {
            log.warn("{}已退群...", accountName);
        }
    }

    /**
     * 监听群添加请求事件 自动同意入群
     *
     * @param groupAddRequest 群添加请求事件
     */
    @OnGroupAddRequest
    public void groupAddRequest(@NotNull GroupAddRequest groupAddRequest, @NotNull Setter setter) {
        //自动同意入群申请                        简介                     是否同意       是否黑名单    理由
        setter.setGroupAddRequestAsync(groupAddRequest.getFlag(), true, false, "");
    }


    @OnGroup
    @Filter(value = ".h1", matchType = MatchType.EQUALS, trim = true)
    public void manual(GroupMsg msg) {
        send.groups(msg, Master, "本梦的使用手册\nhttps://www.yuque.com/docs/share/b3b3c9c8-843f-457c-b1e7-eb89cfbb407e");
    }

    /**
     * 此方法用于检测所有Bot在线状态，首先要确保application.yml中登录的Bot账号全部在同一个群里面否则功能将会会出现异常
     *
     * @param msg GroupMsg
     */
    @OnGroup
    @Filter(value = "栗梦", matchType = MatchType.EQUALS, trim = true)
    public synchronized void botList(@NotNull GroupMsg msg) {
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
                bot1.getSender().SENDER.sendGroupMsg(grCode, String.format("%s\n我在", at));
            }
            map.put(msg.getId(), msg.getId());
        }
    }

    /**
     * 鸡你太美
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "鸡你太美", matchType = MatchType.EQUALS, trim = true)
    public synchronized void chickenYouAreSoBeautiful(GroupMsg msg) {
        send.groups(msg, Master, "游戏地址\nhttps://fangkuai767.github.io/EatKun/");
    }

    /**
     * 重启程序指令
     *
     * @throws Exception 非法结束异常
     */
    @OnGroup
    @OnPrivate
    @Filter(value = ".重启", matchType = MatchType.EQUALS, trim = true)
    public synchronized void manuals(MsgGet msg) throws Exception {
        String str = "才不会帮你重启系统呢！哼！";
        if (msg instanceof GroupMsg) {
            //获取当前消息发送者的权限信息
            String permissions = String.valueOf(((GroupMsg) msg).getPermission());
            //判断当前消息发送者的权限消息是否不为普通成员 OWNER=群主 ADMINISTRATOR=管理员 MEMBER=普通成员
            if (!permissions.equals("MEMBER")) {
                send.groups((GroupMsg) msg, str);
                restart();
            }
        } else if (msg instanceof PrivateMsg) {
            //获取当前QQ
            final String qqCode = msg.getAccountInfo().getAccountCode();
            if (qqCode.equals(Master)) {
                send.privates(qqCode, str);
                restart();
            }
        }
    }

    /**
     * 封装重启
     *
     * @throws IOException 非法结束异常
     */
    private void restart() throws IOException {
        //开启一个新进程
        Runtime.getRuntime().exec(String.format("CMD /C %s", restartScript));
        //退出当前进程
        System.exit(0);
    }
}
