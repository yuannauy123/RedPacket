package cn.leonyuan.service;

import cn.leonyuan.pojo.RedPacket;

public interface RedPacketService {

    /**
     * 获取红包信息
     * @param id
    * @return 红包具体信息
    */
    public RedPacket getRedPacket(Long id);

    /**
     * 扣减红包库存
     * @param id
    * @return 受影响记录数
    */
    public int decreaseRedPacket(Long id);

    /**
     * 乐观锁扣减红包库存
     * @param id,version
    * @return
    */
    public int decreaseRedPacket(RedPacket redPacket);
}
