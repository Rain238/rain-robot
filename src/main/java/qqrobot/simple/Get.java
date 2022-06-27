package qqrobot.simple;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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

    public String text(@NotNull PrivateMsg privateMsg) {
        return privateMsg.getText();
    }
}
