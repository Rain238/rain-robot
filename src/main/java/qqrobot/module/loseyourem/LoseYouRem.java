package qqrobot.module.loseyourem;

import catcode.Neko;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.filter.MatchType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import qqrobot.simple.Send;

import java.util.List;

/**
 * 丢人小功能
 */
@Slf4j
@Component
@AllArgsConstructor
public class LoseYouRem {
    private final MessageContentBuilderFactory messageContentBuilderFactory;
    private static final String URL = "https://api.klizi.cn/API/ce/diu.php?qq=%s";
    private final Send send;//发送消息

    @OnGroup
    @Filter(value = "丢", matchType = MatchType.CONTAINS, trim = true)
    public void youRem(GroupMsg groupMsg) {
        MessageContent m = new MessageContent() {
            @NotNull
            @Override
            public String getMsg() {
                return groupMsg.getMsg().split("code=")[1].split("]")[0];
            }

            @NotNull
            @Override
            public List<Neko> getCats() {
                return null;
            }
        };
        MessageContentBuilder message = messageContentBuilderFactory.getMessageContentBuilder();
        MessageContent image = message.image(String.format(URL, m.getMsg())).build();
        send.groups(groupMsg, image);
    }
}
