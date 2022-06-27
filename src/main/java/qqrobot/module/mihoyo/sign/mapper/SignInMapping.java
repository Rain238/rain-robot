package qqrobot.module.mihoyo.sign.mapper;

import org.apache.ibatis.annotations.*;
import qqrobot.module.mihoyo.sign.bean.MiHoYo;

import java.util.List;

/**
 * 米游社mihoyo表的操作
 * <p>
 * &#064;Author  Light rain
 * &#064;Date  2022/5/26 7:53
 */

@Mapper
public interface SignInMapping {
    /**
     * 查询当前QQ是否至少绑定了米游社通行证
     *
     * @param qq QQ账号
     * @return int
     */
    @Select("select count(qq) from mihoyo where qq=#{qq}")
    int queryUidByQQ(String qq);

    /**
     * 根据QQ来查询qq,uid,cookie,token字段信息
     *
     * @param qq QQ账号
     * @return MiHoYo
     */
    @Select("select uid,cookie,token from mihoyo where qq=#{qq}")
    MiHoYo queryUidAndCookieOrTokenByQQ(String qq);

    /**
     * 添加一条通行证id和Cookie/token信息
     *
     * @param qqCode QQ账号
     * @param uid    uid
     * @param cookie cookie
     * @param token  token
     * @param time   添加时间
     */
    @Insert("INSERT INTO mihoyo(id,qq,uid,cookie,token,automatic,time) VALUES(null,#{qqCode},#{uid},#{cookie},#{token},0,#{time})")
    void addUidAndCookieOrToken(String qqCode, String uid, String cookie, String token, long time);

    /**
     * 查询全部自动签到记录
     *
     * @return List<MiHoYo>
     */
    @Select("select qq,uid,cookie,token from mihoyo where automatic=1")
    List<MiHoYo> queryAllAutomatic();

    /**
     * 根据QQ账号修改米游社通行证是否为自动签到状态
     *
     * @param qqCode QQ账号
     * @param i      自动签到状态
     */
    @Update("update mihoyo set automatic=#{i} where qq=#{qqCode}")
    void upDataAutomaticCheckin(String qqCode, int i);

    /**
     * 根据qq删除指定记录
     *
     * @param qqCode qq账号
     * @return boolean
     */
    @Delete("delete from mihoyo where qq=#{qqCode}")
    boolean deleteByQQ(String qqCode);

    /**
     * 根据uid删除指定记录
     *
     * @param stuid uid
     * @return boolean
     */
    @Delete("delete from mihoyo where uid=#{stuid}")
    boolean deleteByUid(String stuid);


}
