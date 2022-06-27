package qqrobot.module.mihoyo.genshin.mapper;

import org.apache.ibatis.annotations.*;
import qqrobot.module.mihoyo.genshin.bean.GenshinSignIn;

import java.util.List;

/**
 * 原神数据库genshin表的操作
 */
@Mapper
public interface GsSignInMapping {
    /**
     * 查询当前QQ是否至少绑定了一个原神账号
     *
     * @param qq QQ账号
     * @return int
     */
    @Select("select count(qq) from genshin where qq=#{qq}")
    int queryUidByQQ(String qq);

    /**
     * 根据QQ来查询qq,uid,cookie字段信息
     *
     * @param qq QQ账号
     * @return List<GenshinSignIn>
     */
    @Select("select qq,uid,cookie from genshin where qq=#{qq}")
    List<GenshinSignIn> queryUidAndCookieByQQ(String qq);

    /**
     * 添加一条原神Uid和Cookie信息
     *
     * @param qqCode QQ账号
     * @param uid    uid
     * @param cookie cookie
     * @param time   添加时间
     */
    @Insert("INSERT INTO genshin(id,qq,uid,cookie,automatic,time) VALUES(null,#{qqCode},#{uid},#{cookie},0,#{time})")
    void addGenshinUidAndCookie(String qqCode, String uid, String cookie, long time);

    /**
     * 根据Uid查询记录数
     *
     * @param uid uid
     * @return int
     */
    @Select("select count(uid) from genshin where uid=#{uid}")
    int queryRecordByUid(String uid);

    /**
     * 根据Uid来删除记录
     *
     * @param uid uid
     */
    @Delete("delete from genshin where uid=#{uid}")
    void deleteByUid(String uid);

    /**
     * 查询出要自动签到的全部记录
     *
     * @return List<GenshinSignIn>
     */
    @Select("select qq,uid,cookie from genshin where automatic=1")
    List<GenshinSignIn> queryAllAutomatic();

    /**
     * 根据QQ账号修改米游社通行证是否为自动签到状态
     *
     * @param qqCode QQ账号
     * @param i      自动签到状态
     */
    @Update("update genshin set automatic=#{i} where qq=#{qqCode}")
    void upDataAutomaticCheckin(String qqCode, int i);
}
