package qqrobot.listener;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qqrobot.simple.Send;

/**
 * 监听群/私聊消息
 * <p>
 * 高级指令如:.关机 .重启需要最高权限者才可以操作
 * CMD中运行项目发送.重启后没有打开新窗口如发送版本
 * 有消息回复不用担心，代表已经重启了，手动关闭请打开
 * 任务管理器找到进程结束掉即可.
 */
@Component
public class GroupPrivateChat {
    /**
     * 消息内容生成器工厂
     */
    private final MessageContentBuilderFactory mCBF;
    /**
     * 发送消息
     */
    private final Send send;
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

    public GroupPrivateChat(MessageContentBuilderFactory mCBF, Send send) {
        this.mCBF = mCBF;
        this.send = send;
    }

    /**
     * 关闭程序指令
     */
    @OnGroup
    @OnPrivate
    @Filter(value = ".关机", matchType = MatchType.EQUALS, trim = true)
    public synchronized void shutdown(MsgGet msg) {
        String str = "说谁丑呢,不跟你玩了,哼！";
        //获取当前触发者的QQ账号
        final String qqCode = msg.getAccountInfo().getAccountCode();
        //获取消息内容生成器
        MessageContentBuilder message = mCBF.getMessageContentBuilder();
        //at当前进群的成员
        MessageContent at = message.at(qqCode).build();
        //判断当前QQ是否为最高权限者的QQ
        if (qqCode.equals(Master)) {
            if (msg instanceof GroupMsg) {
                send.groups((GroupMsg) msg, String.format("%s\n%s", at.getMsg(), str));
                System.exit(0);
            } else if (msg instanceof PrivateMsg) {
                send.privates((PrivateMsg) msg, str);
                System.exit(0);
            }
        } else {
            String str2 = "就凭你也想关掉我,没门";
            if (msg instanceof GroupMsg) {
                send.groups((GroupMsg) msg, String.format("%s\n%s", at.getMsg(), str2));
            } else if (msg instanceof PrivateMsg) {
                send.privates((PrivateMsg) msg, str2);
            }
        }
    }

    /**
     * 重启程序指令
     *
     * @throws Exception 非法结束异常
     */
    @OnGroup
    @OnPrivate
    @Filter(value = ".重启", matchType = MatchType.EQUALS, trim = true)
    public synchronized void reboot(MsgGet msg) throws Exception {
        String str = "才不会帮你重启系统呢！哼！";
        //获取当前触发者的QQ账号
        final String qqCode = msg.getAccountInfo().getAccountCode();
        //获取消息内容生成器
        MessageContentBuilder message = mCBF.getMessageContentBuilder();
        //at当前进群的成员
        MessageContent at = message.at(qqCode).build();
        //cmd命令
        final String cmd = String.format("CMD /C %s", restartScript);
        //判断当前QQ是否为最高权限者的QQ
        if (qqCode.equals(Master)) {
            if (msg instanceof GroupMsg) {
                send.groups((GroupMsg) msg, String.format("%s\n%s", at.getMsg(), str));
                Runtime.getRuntime().exec(cmd);
                System.exit(0);
            } else if (msg instanceof PrivateMsg) {
                send.privates(qqCode, str);
                Runtime.getRuntime().exec(cmd);
                System.exit(0);
            }
        } else {
            String str2 = "就凭你也想让我听你话,不可能！";
            if (msg instanceof GroupMsg) {
                send.groups((GroupMsg) msg, String.format("%s\n%s", at.getMsg(), str2));
            } else if (msg instanceof PrivateMsg) {
                send.privates(qqCode, str);
            }
        }
    }

    /**
     * 鸡你太美
     * 蔡徐坤打篮球小游戏
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "鸡你太美", matchType = MatchType.EQUALS, trim = true)
    public synchronized void chickenYouAreSoBeautiful(GroupMsg msg) {
        send.groups(msg, Master, "游戏地址\nhttps://fangkuai767.github.io/EatKun/");
    }

}
