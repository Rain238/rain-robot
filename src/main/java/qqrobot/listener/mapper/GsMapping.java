package qqrobot.listener.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作genshin数据表
 */
@Mapper
public interface GsMapping {
    /**
     * 好友删除时删除当前QQ绑定的原神信息
     *
     * @param qqCode QQ账号
     */
    @Delete("delete from genshin where qq=#{qqCode}")
    void deleteByQQ(String qqCode);
}
