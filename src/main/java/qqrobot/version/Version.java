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

@Component
@AllArgsConstructor
public class Version {
    private final Send send;

    @OnPrivate
    @Filter("版本")
    public void version(PrivateMsg privateMsg, Sender sender) {
        send.privates(privateMsg, Vmain());
    }

    @OnGroup
    @Filter("版本")
    public void version(GroupMsg groupMsg) {
        send.groups(groupMsg, Vmain());
    }

    private String Vmain() {
        return "当前版本: v1.0.5\n修复了一些奇奇怪怪的小问题\n新增启动Logo\n" +
                "#1.0.4\n优化代码逻辑\n重构了所有功能\n支持多Bot登陆\n支持在线修改推送Bot\n解决多Bot群内发送重复内容问题\n支持在线重启\n" +
                "#1.0.3\n优化代码逻辑\n修复Cookie失效不提示的情况\n添加Cookie失效自动解绑功能\n无需手动解绑直接重新绑定即可\n" +
                "#1.0.2\n待增加原神体力溢出提醒\n新增P站id搜索作者作品\n新增P站图片详细内容\n新增返回指定P站图片数量\n" +
                "#1.0.1\n优化代码逻辑\n去除随机涩图\n新增原神/米游社自动签到\n" +
                "#1.0.0\n支持群管\n支持随机图片\n支持Pixiv图片\n支持原神福利签到\n支持获取每日米游币\n支持米游社社区签到\n" +
                "支持频道签到:崩坏二/崩坏三/原神/未定/大墅野";
    }
}
