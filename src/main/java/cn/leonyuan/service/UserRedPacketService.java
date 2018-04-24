package cn.leonyuan.service;

public interface UserRedPacketService {

    /**
     *插入抢红包信息
     * @param userId,RedPacketId
    * @return 受影响记录条数
    */
    public int updateUserRedPacket(Long userId,Long RedPacketId);

    /**
     * 乐观锁模式更新红包信息
    * @return
    */
    public int Optimistic_lock(Long userId,Long RedPacketId);

    /**
     * 可重入乐观锁，解决一般乐观锁模式下大量红包剩余的情况
    * @return
    */
    public int Reentrant_Optimistic_lock(Long userId,Long RedPacketId);

    /**
     * 使用redis存储红包信息
     * return 1--成功
     * return 2--最后一个红包，触发将redis数据保存在数据库
    * @return
    */
    public Long grapRedPacketbyRedis(Long redPacketId, Long userId);
}
