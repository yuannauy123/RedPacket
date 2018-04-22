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
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
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

    /**
     * 乐观锁模式，抢红包需要比较版本号
    * @return
    */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int Optimistic_lock(Long userId,Long RedPacketId){
        //查询红包信息，获取库存量
        RedPacket redPacket=redPacketMapper.getRedPacket(RedPacketId);

        if(redPacket.getStock()>0)
        {
            int update=redPacketMapper.decreaseRedPacketForVersion(redPacket);
            if(update==0)//受影响记录数为0，说明数据被更新了，抢红包失败
            {
                return 0;
            }
            UserRedPacket userRedPacket=new UserRedPacket();
            userRedPacket.setRedPacketId(RedPacketId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setUserId(userId);

            return userRedPacketMapper.updateUserRedPacket(userRedPacket);
        }
        return 0;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int Reentrant_Optimistic_lock(Long userId, Long RedPacketId) {

        long start=System.currentTimeMillis();
        while(true)
        {
            long end=System.currentTimeMillis();
            if(end-start>100)
            {
                return 0;
            }
            RedPacket redPacket=redPacketMapper.getRedPacket(RedPacketId);
            if(redPacket.getStock()>0)
            {
                int update=redPacketMapper.decreaseRedPacketForVersion(redPacket);
                if(update==0)
                {
                    continue;
                }
                UserRedPacket userRedPacket=new UserRedPacket();
                userRedPacket.setRedPacketId(RedPacketId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setUserId(userId);

                return userRedPacketMapper.updateUserRedPacket(userRedPacket);

            }
            else{
                return 0;
            }
        }

    }

}
