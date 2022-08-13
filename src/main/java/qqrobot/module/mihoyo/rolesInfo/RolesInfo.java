package qqrobot.module.mihoyo.rolesInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
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
@Slf4j
@Component
@AllArgsConstructor
public class RolesInfo {
    /**
     * 消息内容生成器工厂
     */
    private final MessageContentBuilderFactory factory;
    /**
     * 角色信息集合
     */
    private final static ArrayList<String> rolesList = new ArrayList<>(Arrays.asList("重云", "钟离", "早柚", "云堇", "优菈", "夜兰", "岩主", "烟绯", "行秋", "辛焱", "魈", "宵宫", "香菱", "五郎", "温迪", "托马", "神里绫人", "神里绫华", "申鹤", "珊瑚宫心海", "砂糖", "琴", "七七", "诺艾尔", "凝光", "莫娜", "罗莎莉亚", "丽莎", "雷主", "雷泽", "雷电将军", "刻晴", "可莉", "凯亚", "久岐忍", "九条裟罗", "荒泷一斗", "胡桃", "甘雨", "枫原万叶", "风主", "菲谢尔", "迪卢克", "迪奥娜", "达达利亚", "北斗", "班尼特", "芭芭拉", "八重神子", "安柏", "阿贝多", "埃洛伊"));
    /**
     * 发送消息
     */
    private final Send send;

    @OnGroup//监听群聊消息
    @Filter(value = "信息", matchType = MatchType.CONTAINS, trim = true)
    public void roleInfo(MessageGet msg) {
        log.info("TriggerGr: {} - ==> TriggerQQ: {} TriggerWord: {}", ((GroupMsg) msg).getGroupInfo().getGroupCode(), msg.getAccountInfo().getAccountCode(), msg.getText());
        final String roles = msg.getText().split("信息")[0];//从消息体中截取武器名称
        for (String r : rolesList) {//构建图片并发送图片消息
            if (r.equals(roles)) send.groups((GroupMsg) msg, factory.getMessageContentBuilder().image(Objects.requireNonNull(this.getClass().getResourceAsStream(String.format("/roles-pic/%s.png", r)))).build());
        }
    }
}
