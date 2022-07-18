package qqrobot.module.mihoyo.armsinfo;

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
 * 原神武器信息
 * 例：雾切之回光信息
 * &#064;Author  Light rain
 * &#064;Date  2022/7/9 2:40
 */
@Component
@AllArgsConstructor
public class ArmsInfo {
    private final MessageContentBuilderFactory messageContentBuilderFactory;
    private final static ArrayList<String> armsList = new ArrayList<String>(Arrays.asList("宗室长剑","宗室长弓","宗室秘法录","宗室猎枪","宗室大剑","斫峰之刃","钟剑","终末嗟叹之诗","薙草之稻光","证誓之明瞳","昭心","钺矛","雨裁","渔获","幽夜华尔兹","佣兵重剑","银剑","异世界行记","以理服人","鸦羽弓","训练大剑","雪葬的星银","学徒笔记","信使","新手长枪","衔珠海皇","匣里日月","匣里灭辰","匣里龙吟","喜多院十文字","息灾","西风长枪","西风秘典","西风猎弓","西风剑","西风大剑","雾切之回光","无工之剑","无锋剑","万国诸海图谱","铁影阔剑","铁尖枪","铁蜂刺","天目影打刀","天空之翼","天空之刃","天空之卷","天空之脊","天空之傲","讨龙英杰谭","松籁响起之时","四风原典","试作斩岩","试作星镰","试作金珀","试作古华","试作澹月","神射手之誓","神乐之真意","若水","忍冬之果","千岩长枪","千岩古剑","破魔之弓","磐岩结绿","沐浴龙血的剑","魔导绪论","曚云之月","掠食者","旅行剑","落霞","龙脊长枪","流月针","流浪乐章","猎弓","历练的猎弓","黎明神剑","冷刃","狼的末路","口袋魔导书","绝弦","决斗之枪","降临之剑","甲级宝珏","祭礼剑","祭礼弓","祭礼大剑","祭礼残章","护摩之杖","黑缨枪","黑岩长剑","黑岩战弓","黑岩斩刀","黑岩绯玉","黑岩刺枪","黑剑","和璞鸢","桂木斩长正","贯虹之槊","弓藏","钢轮弓","腐殖之剑","风鹰剑","风花之颂","翡玉法球","飞天御剑","飞天大御剑","飞雷之弦振","反曲弓","恶王丸","断浪长鳍","嘟嘟可故事集","冬极白星","笛剑","弹弓","赤角石溃杵","螭骨剑","吃虎鱼刀","辰砂之纺锤","尘世之锁","苍古自由之誓","苍翠猎弓","不灭月华","波乱月白经津","白影剑","白缨枪","白铁大剑","白辰之环","暗巷闪光","暗巷猎手","暗巷的酒与诗","暗铁剑","阿莫斯之弓"));

    private final Send send;

    @OnGroup
    @Filter(value = "信息", matchType = MatchType.CONTAINS, trim = true)
    public void armsInfo(MessageGet msg){
        final String arms = msg.getText().split("信息")[0];
        for (String a : armsList) {
            if (a.equals(arms)) {
                MessageContentBuilder message = messageContentBuilderFactory.getMessageContentBuilder();
                MessageContent image = message.image(Objects.requireNonNull(this.getClass().getResourceAsStream(String.format("/arms-pic/%s.png", a)))).build();
                send.groups((GroupMsg) msg, image);
            }
        }
    }
}
