package cn.leonyuan.service;

public interface UserRedPacketService {

    /**
     *插入抢红包信息
     * @param userId,RedPacketId
    * @return 受影响记录条数
    */
    public int updateUserRedPacket(Long userId,Long RedPacketId);
}
