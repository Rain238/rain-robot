package qqrobot.module.mihoyo.rolesInfo;

import lombok.AllArgsConstructor;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;
import qqrobot.simple.Send;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * 原神角色信息
 * 例：刻晴信息
 * &#064;Author  Light rain
 * &#064;Date  2022/7/9 1:47
 */
@Component
@AllArgsConstructor
public class RolesInfo {
    private final MessageContentBuilderFactory messageContentBuilderFactory;
    private final static ArrayList<String> rolesList = new ArrayList<String>(Arrays.asList("重云", "钟离", "早柚", "云堇", "优菈", "夜兰", "岩主", "烟绯", "行秋", "辛焱", "魈", "宵宫", "香菱", "五郎", "温迪", "托马", "神里绫人", "神里绫华", "申鹤", "珊瑚宫心海", "砂糖", "琴", "七七", "诺艾尔", "凝光", "莫娜", "罗莎莉亚", "丽莎", "雷主", "雷泽", "雷电将军", "刻晴", "可莉", "凯亚", "久岐忍", "九条裟罗", "荒泷一斗", "胡桃", "甘雨", "枫原万叶", "风主", "菲谢尔", "迪卢克", "迪奥娜", "达达利亚", "北斗", "班尼特", "芭芭拉", "八重神子", "安柏", "阿贝多", "埃洛伊"));

    private final Send send;

    @OnGroup
    @Filter(value = "信息", matchType = MatchType.CONTAINS, trim = true)
    public void roleInfo(MessageGet msg) {
        final String roles = msg.getText().split("信息")[0];
        for (String r : rolesList) {
            if (r.equals(roles)) {
                MessageContentBuilder message = messageContentBuilderFactory.getMessageContentBuilder();
                MessageContent image = message.image(Objects.requireNonNull(this.getClass().getResourceAsStream(String.format("/roles-pic/%s.png", r)))).build();
                send.groups((GroupMsg) msg, image);
            }
        }
    }
}
