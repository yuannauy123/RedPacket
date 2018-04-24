package cn.leonyuan.service.impl;

import cn.leonyuan.dao.RedPacketMapper;
import cn.leonyuan.dao.UserRedPacketMapper;
import cn.leonyuan.pojo.RedPacket;
import cn.leonyuan.pojo.UserRedPacket;
import cn.leonyuan.service.RedisRedPacketService;
import cn.leonyuan.service.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

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

    @Autowired
    private RedisTemplate redisTemplate = null;

    @Autowired
    private RedisRedPacketService redisRedPacketService = null;

    // Lua脚本
    String script = "local listKey = 'red_packet_list_'..KEYS[1] \n"
            + "local redPacket = 'red_packet_'..KEYS[1] \n"
            + "local stock = tonumber(redis.call('hget', redPacket, 'stock')) \n"
            + "if stock <= 0 then return 0 end \n"
            + "stock = stock -1 \n"
            + "redis.call('hset', redPacket, 'stock', tostring(stock)) \n"
            + "redis.call('rpush', listKey, ARGV[1]) \n"
            + "if stock == 0 then return 2 end \n"
            + "return 1 \n";

    // 在缓存LUA脚本后，使用该变量保存Redis返回的32位的SHA1编码，使用它去执行缓存的LUA脚本[加入这句话]
    String sha1 = null;

    @Override
    public Long grapRedPacketbyRedis(Long redPacketId, Long userId) {
        // 当前抢红包用户和日期信息
        String args = userId + "-" + System.currentTimeMillis();
        Long result = null;
        // 获取底层Redis操作对象
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try {
            // 如果脚本没有加载过，那么进行加载，这样就会返回一个sha1编码
            if (sha1 == null) {
                sha1 = jedis.scriptLoad(script);
            }
            // 执行脚本，返回结果
            Object res = jedis.evalsha(sha1, 1, redPacketId + "", args);
            result = (Long) res;

            // 返回2时为最后一个红包，此时将抢红包信息通过异步保存到数据库中
            if (result == 0) {

                // 获取单个小红包金额
                String unitAmountStr = jedis.hget("red_packet_" + redPacketId, "unitamount");
                // 触发保存数据库操作
                Double unitAmount = Double.parseDouble(unitAmountStr);
                //System.err.println("thread_name = " + Thread.currentThread().getName());
                redisRedPacketService.saveUserRedPacketToDatabase(redPacketId, unitAmount);
                System.out.println("------------------kkkkkkkkkkkkkkkkkkk-------------------------------"+result);
            }
        } finally {
            // 确保jedis顺利关闭
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
        return result;
    }

}
