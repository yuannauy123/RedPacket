package cn.leonyuan.dao;

import cn.leonyuan.pojo.RedPacket;
import org.springframework.stereotype.Repository;

@Repository
public interface RedPacketMapper {

    /**
     *  获取红包信息
     * @param id 红包 id
    * @return 红包具体信息
    */
    public RedPacket getRedPacket(Long id);

    /**
     * 减红包个数
     * @param id 红包 id
    * @return 更新记录的条数
    */
    public int decreaseRedPacket(Long id);

    /**
     * 乐观锁扣减红包库存
     * @param id,version
    * @return
    */
    public int decreaseRedPacketForVersion(RedPacket redPacket);
}
