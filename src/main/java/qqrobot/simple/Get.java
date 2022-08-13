package qqrobot.simple;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 封装simple获取账号等
 * &#064;Author  RainRain
 * &#064;Data  2022/7/23 14:20
 */
@Component
public class Get {
    //获取当前QQ账号
    public String prCode(@NotNull PrivateMsg privateMsg) {
        return privateMsg.getAccountInfo().getAccountCode();
    }

    //获取当前QQ账号
    public String prCode(@NotNull GroupMsg groupMsg) {
        return groupMsg.getAccountInfo().getAccountCode();
    }

    //获取当前群号
    public String grCode(@NotNull GroupMsg groupMsg) {
        return groupMsg.getGroupInfo().getGroupCode();
    }

    //获取消息体
    public String text(@NotNull PrivateMsg privateMsg) {
        return privateMsg.getText();
    }
}
