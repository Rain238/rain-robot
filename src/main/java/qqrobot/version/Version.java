package qqrobot.version;

import lombok.AllArgsConstructor;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Component;
import qqrobot.simple.Send;

/**
 * 版本信息
 * &#064;Author  RainRain
 * &#064;Data  2022/5/25 15:38
 */
@Component
@AllArgsConstructor
public class Version {
    private final Send send;

    @OnPrivate
    @Filter("版本")
    public void version(PrivateMsg privateMsg) {
        send.privates(privateMsg, vMain());
    }

    @OnGroup
    @Filter("版本")
    public void version(GroupMsg groupMsg) {
        send.groups(groupMsg, vMain());
    }

    private String vMain() {
        return "当前版本: v2.0.1\n新增进群验证功能\n新增退出机器人所在群将无法使用功能以及私聊\n退出机器人所在群/删除机器人好友将删除所有绑定数据\n" +
                "#2.0.0\n重构了日志打印\n抽离了某些需要定时修改的数据到配置文件内\n将配置文件打包在Jar包外面\n" +
                "#1.0.9\n优化自动签到\n新增将日志记录到日志文件\n增加竖屏涩图\n横屏涩图\n兽耳涩图\n星空涩图\n" +
                "#1.0.8\n新增原神角色信息查询\n新增原神武器信息查询\n" +
                "#1.0.7\n优化去水印方式\n同时支持PC和手机端抖音分享\n" +
                "#1.0.6\n新增抖音去水印\n修复logo显示错误\n优化整体结构待扩展更多功能\n" +
                "#1.0.5\n修复了一些奇奇怪怪的小问题\n新增启动Logo\n" +
                "#1.0.4\n优化代码逻辑\n重构了所有功能\n支持多Bot登陆\n支持在线修改推送Bot\n解决多Bot群内发送重复内容问题\n支持在线重启\n" +
                "#1.0.3\n优化代码逻辑\n修复Cookie失效不提示的情况\n添加Cookie失效自动解绑功能\n无需手动解绑直接重新绑定即可\n" +
                "#1.0.2\n待增加原神体力溢出提醒\n新增P站id搜索作者作品\n新增P站图片详细内容\n新增返回指定P站图片数量\n" +
                "#1.0.1\n优化代码逻辑\n去除随机涩图\n新增原神/米游社自动签到\n" +
                "#1.0.0\n支持群管\n支持随机图片\n支持Pixiv图片\n支持原神福利签到\n支持获取每日米游币\n支持米游社社区签到\n" +
                "支持频道签到:崩坏二/崩坏三/原神/未定/大墅野";
    }
}
