package cn.leonyuan.service.impl;

import cn.leonyuan.pojo.UserRedPacket;
import cn.leonyuan.service.RedisRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: RedPacket
 * @description:
 * @author: LeonYuan
 * @create: 2018-04-23 16:42
 **/
@Service
public class RedisRedPacketServiceImpl implements RedisRedPacketService {

    private static final int TIME_SIZE=500;
    private static final String PREFIX="red_packet_list_";

    @Autowired
    private RedisTemplate redisTemplate=null;

    @Autowired
    private DataSource dataSource=null;


    @Override
    @Async
    public void saveUserRedPacketToDatabase(Long redPacketId, Double unitAmount) {

        BoundListOperations ops=redisTemplate.boundListOps(PREFIX+redPacketId);

        Long size=ops.size();

        Long times=size%TIME_SIZE==0?size/TIME_SIZE:size/TIME_SIZE+1;

        List<UserRedPacket> userRedPacketList=new ArrayList<>(TIME_SIZE);

        for(int i=0;i<times;i++)
        {
            List userlist=null;

            if(i==0)
            {
                userlist=ops.range(0,TIME_SIZE);
            }
            else
            {
                userlist=ops.range(i*TIME_SIZE+1,(i+1)*TIME_SIZE);
            }
            userRedPacketList.clear();
            for(int j=0;j<userlist.size();j++)
            {
                String str=userlist.get(j).toString();
                String[] args=str.split("-");

                Long userId=Long.parseLong(args[0]);
                Long grapTime=Long.parseLong(args[1]);

                UserRedPacket userRedPacket=new UserRedPacket();
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(unitAmount);
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setGrabTime(new Timestamp(grapTime));

                userRedPacketList.add(userRedPacket);
            }
                int count=executeBatch(userRedPacketList);

        }

        redisTemplate.delete(PREFIX+redPacketId);
    }

    private int executeBatch(List<UserRedPacket> userRedPacketList) {
        Connection conn = null;
        Statement stmt = null;
        int[] count = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (UserRedPacket userRedPacket : userRedPacketList) {
                String sql1 = "update RED_PACKET set stock = stock-1 where id=" + userRedPacket.getRedPacketId();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sql2 = "insert into USER_RED_PACKET(red_packet_id, user_id, " + "amount, grab_time)"
                        + " values (" + userRedPacket.getRedPacketId() + ", " + userRedPacket.getUserId() + ", "
                        + userRedPacket.getAmount() + "," + "'" + df.format(userRedPacket.getGrabTime()) + "')";
                stmt.addBatch(sql1);
                stmt.addBatch(sql2);
            }
            // 执行批量
            count = stmt.executeBatch();
            // 提交事务
            conn.commit();
        } catch (SQLException e) {
            /********* 错误处理逻辑 ********/
            throw new RuntimeException("抢红包批量执行程序错误");
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 返回插入抢红包数据记录
        return count.length / 2;
    }
}
