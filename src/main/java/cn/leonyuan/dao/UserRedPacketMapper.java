package cn.leonyuan.dao;

import cn.leonyuan.pojo.UserRedPacket;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRedPacketMapper {
    /**
     *  插入抢红包信息
     * @param userRedPacket 抢红包信息
    * @return 影响记录数
    */
    public int updateUserRedPacket(UserRedPacket userRedPacket);
}
