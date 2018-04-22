package cn.leonyuan.service.impl;

import cn.leonyuan.dao.RedPacketMapper;
import cn.leonyuan.pojo.RedPacket;
import cn.leonyuan.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: RedPacketXml.xml
 * @description: 红包服务实现类
 * @author: LeonYuan
 * @create: 2018-04-20 15:20
 **/
@Service
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private RedPacketMapper redPacketMapper;

    @Autowired
    private RedPacket redPacket;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public RedPacket getRedPacket(Long id) {
        redPacket=redPacketMapper.getRedPacket(id);
        return redPacket;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int decreaseRedPacket(Long id) {
        return redPacketMapper.decreaseRedPacket(id);
    }
}
