package qqrobot.module.astringent.picture.entity;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory;
import love.forte.simbot.filter.MatchType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import qqrobot.module.astringent.picture.bean.Pixiv;
import qqrobot.simple.Send;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 接入Lolicon API(<a href="https://api.lolicon.app/#/setu">...</a>)
 * 随机涩图模块,图片来源于P站
 */
@Slf4j
@Component
@AllArgsConstructor

public class PixivPicture {
    //注入一个消息工厂
    private final MessageContentBuilderFactory messageContentBuilderFactory;
    //注入一个可以构建合并消息的消息工厂
    private final MiraiMessageContentBuilderFactory factory;
    //图片请求地址
    private final static String URL = "https://api.lolicon.app/setu/v2?%s";
    //每张图片的简介模板
    private final static String INTRODUCE = "画师id:%s\n画师名称:%s\n标题:%s\n标签:%s\n%s\n";

    private final Send send;//发送消息

    /**
     * 根据标签类型获取图片
     * 涩图#JK 3
     * 涩图#为前缀
     * 参数一：标签类型
     * 参数二：返回图片的数量
     * 一次性返回图片数量最多100张
     *
     * @param msgGet    消息父类
     * @param parameter 正则判断
     */
    @OnGroup
    @OnPrivate
    @Filters(value = {@Filter(value = "涩图#：*{{parameter,\\D+\\s\\d+}}", matchType = MatchType.REGEX_MATCHES, trim = true), @Filter(value = "涩图#：*{{parameter,\\D+}}", matchType = MatchType.REGEX_MATCHES, trim = true), @Filter(value = "涩图#：*{{parameter,\\w+\\s\\d+}}", matchType = MatchType.REGEX_MATCHES, trim = true), @Filter(value = "涩图#：*{{parameter,\\w+}}", matchType = MatchType.REGEX_MATCHES, trim = true)})
    public void pixivPictureMain(@NotNull MsgGet msgGet, @FilterValue("parameter") String parameter) {
        try {
            Connection connection;
            if (parameter.contains(" ")) {//标签/返回数量
                String tag = parameter.split(" ")[0];
                String num = parameter.split(" ")[1];
                connection = Jsoup.connect(String.format(URL, String.format("tag=%s&num=%s", tag, num)));
            } else {
                connection = Jsoup.connect(String.format(URL, String.format("tag=%s", parameter)));
            }
            packageRequest(connection, msgGet);
        } catch (Exception e) {
            exceptionPrompt(msgGet);
            e.printStackTrace();
        }
    }

    @OnGroup
    @OnPrivate
    @Filters(value = {@Filter(value = "涩图：*{{parameter,\\D+#\\D+}}", matchType = MatchType.REGEX_MATCHES, trim = true), @Filter(value = "涩图：*{{parameter,\\D+#\\D+\\s\\d+}}", matchType = MatchType.REGEX_MATCHES, trim = true),})
    public void s(@NotNull MsgGet msgGet, @FilterValue("parameter") String parameter) {
        try {
            String[] tag = parameter.split("#");
            Connection connection;
            if (Objects.requireNonNull(msgGet.getText()).contains(" ")) {
                String num = Objects.requireNonNull(msgGet.getText()).split(" ")[1];
                connection = Jsoup.connect(String.format(URL, "tag=" + tag[0] + "%7C" + tag[1].split(" ")[0] + "&num=" + num));
            } else {
                connection = Jsoup.connect(String.format(URL, "tag=" + tag[0] + "%7C" + tag[1]));
            }
            packageRequest(connection, msgGet);
        } catch (Exception e) {
            exceptionPrompt(msgGet);
            e.printStackTrace();
        }
    }

    /**
     * 根据id查找指定作者的P站作品
     * 例：pid#3036679 20
     * pid#为前缀
     * 参数一：作者id
     * 参数二：返回图片的数量
     * 指定id一次性返回图片数量最多20张
     *
     * @param msgGet    消息父类
     * @param parameter 正则判断
     */
    @OnGroup
    @OnPrivate
    @Filter(value = "pid#：*{{parameter,\\d+\\s\\d+}}", matchType = MatchType.REGEX_MATCHES, trim = true)
    public void findAuthorById(@NotNull MsgGet msgGet, @FilterValue("parameter") String parameter) {
        try {
            String uid = parameter.split(" ")[0];
            String num = parameter.split(" ")[1];
            //使用Jsoup爬取URL链接的Json数据并封装成Bean对象
            Connection connection = Jsoup.connect(String.format(URL, String.format("uid=%s&num=%s", uid, num)));
            packageRequest(connection, msgGet);
        } catch (Exception e) {
            exceptionPrompt(msgGet);
        }
    }

    /**
     * 请求封装方法
     * 拿到url后执行请求操作
     * 获取json数据封装成Pixiv对象
     * 遍历Pixiv对象获取图片链接
     * 根据图片链接获取到图片
     * 如果是群聊则执行构建合并消息内容
     * 如为私聊消息则不构建合并消息内容
     *
     * @param connection Connection
     * @param msgGet     消息父类
     * @throws IOException 读取失败异常
     */
    private void packageRequest(@NotNull Connection connection, MsgGet msgGet) throws IOException {
        //获取网页的Document对象并设置超时时间和忽略内容类型get请求后使用标签选择器来获取body标签体的内容
        Elements json = connection.timeout(10000 * 1000).ignoreContentType(true).get().select("body");
        //通过fromObject将json字符串翻译成JSON对象(JSONObject)
        JSONObject jsonObject = JSONObject.fromObject(json.text());
        //上面的jsonObject类型就是json数组如：data:[{"tags":[],"urls":{}}]
        //返回的是非单一的json对象
        JSONArray dataJsonArray = jsonObject.getJSONArray("data");
        ArrayList<Pixiv> arrayPixiv = new ArrayList<>();
        for (Object o : dataJsonArray) {
            //将获取的单个json字符串翻译成JSONObject
            JSONObject jsonParts = JSONObject.fromObject(o.toString());
            //将json对象翻译成Pixiv对象
            arrayPixiv.add((Pixiv) JSONObject.toBean(jsonParts, Pixiv.class));
        }
        if (msgGet instanceof GroupMsg) {
            //获取一个Mirai消息内容生成器
            MiraiMessageContentBuilder builder = factory.getMessageContentBuilder();
            //构建合并消息内容
            builder.forwardMessage(forwardBuilder -> {
                //获取Pixiv对象里面的数据
                for (Pixiv pixiv : arrayPixiv) {
                    //先清除一次再构建
                    builder.clear();
                    //新url链接
                    String newUrl = newUrl(pixiv.getUrls().get("original"));
                    //获取秒为单位的时间戳
                    String timestamp = String.valueOf(new Date().getTime());
                    int length = timestamp.length();
                    int integer = Integer.parseInt(timestamp.substring(0, length - 3));
                    forwardBuilder.add(msgGet, integer, builder.text(introduce(pixiv)).image(newUrl).build());
                }
            });
            send.groupMsgAsync((GroupMsg) msgGet, builder.build());
        } else if (msgGet instanceof PrivateMsg) {
            for (Pixiv pixiv : arrayPixiv) {
                //获取消息工厂
                MessageContentBuilder message = messageContentBuilderFactory.getMessageContentBuilder();
                //新url链接
                String newUrl = newUrl(pixiv.getUrls().get("original"));
                send.privateMsgAsync((PrivateMsg) msgGet, message.text(introduce(pixiv)).image(newUrl).build());
            }
        }
    }

    /**
     * 异常提示信息
     *
     * @param msgGet 消息父类
     */
    private void exceptionPrompt(MsgGet msgGet) {
        if (msgGet instanceof GroupMsg) {
            send.groups(((GroupMsg) msgGet), "网络波动异常请重试...");
        } else if (msgGet instanceof PrivateMsg) {
            send.privates((PrivateMsg) msgGet, "网络波动异常请重试...");
        }
    }

    /**
     * 封装图片介绍信息
     *
     * @param pixiv Pixiv
     * @return String
     */
    private String introduce(Pixiv pixiv) {
        return String.format(INTRODUCE, pixiv.getUid(), pixiv.getAuthor(), pixiv.getTitle(), Arrays.toString(pixiv.getTags()), newUrl(pixiv.getUrls().get("original")));
    }

    /**
     * 替换图片链接头部
     *
     * @param urls 链接
     * @return String
     */
    private String newUrl(String urls) {
        return String.format("https://i.pixiv.re%s", urls.split("https://i.pixiv.cat")[1]);
    }
}
