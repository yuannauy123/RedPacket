package cn.leonyuan.service.impl;

import cn.leonyuan.dao.RedPacketMapper;
import cn.leonyuan.dao.UserRedPacketMapper;
import cn.leonyuan.pojo.RedPacket;
import cn.leonyuan.pojo.UserRedPacket;
import cn.leonyuan.service.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: RedPacket
 * @description: 用户红包服务实现类
 * @author: LeonYuan
 * @create: 2018-04-20 15:33
 **/
@Service
public class UserRedPacketServiceImpl implements UserRedPacketService {

    @Autowired
    private UserRedPacketMapper userRedPacketMapper;

    @Autowired
    private RedPacketMapper redPacketMapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
    public int updateUserRedPacket(Long userId,Long RedPacketId) {
        //查询红包信息，获取库存量
        RedPacket redPacket=redPacketMapper.getRedPacket(RedPacketId);

        if(redPacket.getStock()>0)
        {
            redPacketMapper.decreaseRedPacket(RedPacketId);
            UserRedPacket userRedPacket=new UserRedPacket();
            userRedPacket.setRedPacketId(RedPacketId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setUserId(userId);

            return userRedPacketMapper.updateUserRedPacket(userRedPacket);
        }
        return 0;
    }
}
