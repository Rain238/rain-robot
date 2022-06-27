package qqrobot.module.management;

import catcode.CatCodeUtil;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import net.mamoe.mirai.contact.PermissionDeniedException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 群管模块实现
 */
@Component
public class GroupManagement{
    /**
     * 群管模块主功能区
     * @param groupMsg 群消息
     * @param sender 发送群消息
     * @param setter 设置群参数
     */
    @OnGroup
    public void mainRibbon(GroupMsg groupMsg, Sender sender, Setter setter) {
        //获取当前QQ群号
        String groupNumber = groupMsg.getGroupInfo().getGroupCode();
        //获取猫猫码工具
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        //构建猫猫码                  类型         是否转义         参数设置                 获取发送消息者的QQ
        String at = util.toCat("at", true, "code=" + groupMsg.getAccountInfo().getAccountCode());
        //获取消息体
        String msgBody = groupMsg.getMsg();
        try {
            //获取当前消息发送者的权限信息
            String permissions = String.valueOf(groupMsg.getAccountInfo().getPermission());
            //判断当前消息发送者的权限消息是否不为普通成员 OWNER=群主 ADMINISTRATOR=管理员 MEMBER=普通成员
            if (!permissions.equals("MEMBER")) {
                //指令：禁言@xxx 6
                if (msgBody.contains("禁言") && msgBody.contains("CAT:at"))
                    //     设置群组禁令       群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号            从获取到的消息正文中截取出需要禁言的时间               将禁言时间设置为分钟
                    setter.setGroupBan(groupNumber, msgBody.split("=")[1].split("]")[0], Long.parseLong(groupMsg.getText().split("  ")[1]), TimeUnit.MINUTES);
                    //指令：解禁@xxx
                else if (msgBody.contains("解禁") && msgBody.contains("CAT:at"))
                    //     设置群组禁令       群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号  禁言时间  将禁言时间设置为分钟
                    setter.setGroupBan(groupNumber, msgBody.split("=")[1].split("]")[0], 0, TimeUnit.MINUTES);
                    //指令：踢@xxx
                else if (msgBody.contains("踢") && msgBody.contains("CAT:at"))
                    //     设置异步踢除群成员         群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号     踢除理由       添加黑名单
                    setter.setGroupMemberKick(groupNumber, msgBody.split("=")[1].split("]")[0], "", false);
                    //指令：拉黑踢@xxx
                else if (msgBody.contains("拉黑踢") && msgBody.contains("CAT:at"))
                    //     设置异步踢除群成员         群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号     踢除理由       添加黑名单
                    setter.setGroupMemberKick(groupNumber, msgBody.split("=")[1].split("]")[0], "", true);
                    //指令：添加管理@xxx
                else if (msgBody.contains("添加管理") && msgBody.contains("CAT:at"))
                    //     设置群管理         群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号            晋升管理
                    setter.setGroupAdmin(groupNumber, msgBody.split("=")[1].split("]")[0], true);
                    //指令：取消管理@xxx
                else if (msgBody.contains("取消管理") && msgBody.contains("CAT:at"))
                    //     设置群管理         群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号            晋升管理
                    setter.setGroupAdmin(groupNumber, msgBody.split("=")[1].split("]")[0], false);
                    //指令：设置头衔@xxx
                else if (msgBody.contains("设置头衔") && msgBody.contains("CAT:at"))
                    //     设置组成员特殊头衔                群号      从猫猫码中[CAT:at,code=943869478]截取出来被at的QQ号         从消息文本中截取出头衔文本内容
                    setter.setGroupMemberSpecialTitle(groupNumber, msgBody.split("=")[1].split("]")[0], groupMsg.getText().split("  ")[1]);
                    //指令：开启全员禁言
                else if (msgBody.contains("开启全员禁言"))
                    //     设置全体禁言           群号          是否开启
                    setter.setGroupWholeBan(groupNumber, true);
                    //指令：关闭全员禁言
                else if (msgBody.contains("关闭全员禁言"))
                    //     设置全体禁言           群号          是否开启
                    setter.setGroupWholeBan(groupNumber, false);
            }
        } catch (PermissionDeniedException e) {
            sender.sendGroupMsg(groupNumber, at + "权限不足");
        } catch (IllegalStateException e) {
            sender.sendGroupMsg(groupNumber, at + "达咩哟 ˃ʍ˂");
        } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendGroupMsg(groupNumber, at + "未设置禁言时间哟");
        }
    }
}
