package qqrobot.listener.mapper;

import org.apache.ibatis.annotations.*;

/**
 * 操作bot数据表
 */
@Mapper
public interface ReviseBotMapping {
    /**
     * 添加好友自动进行Bot账号和对方QQ账号进行绑定
     *
     * @param friendQQ 好友QQ账号
     * @param botQQ    bot机器人账号
     */
    @Insert("INSERT INTO bot(id,friend_qq,bot_qq) VALUES(null,#{friendQQ},#{botQQ})")
    void add(String friendQQ, String botQQ);

    /**
     * 根据QQ修改Bot推送账号
     *
     * @param friendQQ 好友QQ账号
     * @param botQQ    bot机器人账号
     */
    @Update("update bot set bot_qq=#{botQQ} where friend_qq=#{friendQQ}")
    void searchAndModifyBotQQAccordingToQQ(String friendQQ, String botQQ);

    /**
     * 根据QQ进行删除
     *
     * @param friendQQ 好友QQ账号
     */
    @Delete("delete from bot where friend_qq=#{friendQQ}")
    void deleteAccordingToQQ(String friendQQ);

    /**
     * 根据操控者QQ查询该账号绑定的Bot账号
     *
     * @param friend_qq 好友QQ
     * @return String
     */
    @Select("select bot_qq from bot where friend_qq=#{friend_qq}")
    String QueryBotQQAccordingToQQ(String friend_qq);

    /**
     * 根据当前QQ查询账号是否存在
     * @param friendQQ 好友QQ
     * @return boolean
     */
    @Select("select count(friend_qq) from bot where friend_qq=#{friend_qq}")
    boolean queryQQ(String friendQQ);
}
