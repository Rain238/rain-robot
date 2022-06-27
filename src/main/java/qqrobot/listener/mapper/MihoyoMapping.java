package qqrobot.listener.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作mihoyo数据表
 */
@Mapper
public interface MihoyoMapping {
    /**
     * 好友删除时删除当前QQ绑定的米游社信息
     * @param qqCode QQ账号
     */
    @Delete("delete from mihoyo where qq=#{qqCode}")
    void deleteByQQ(String qqCode);
}
