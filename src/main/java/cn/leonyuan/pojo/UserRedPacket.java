package cn.leonyuan.pojo;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @program: RedPacketXml.xml
 * @description: 抢红包的POJO
 * @author: LeonYuan
 * @create: 2018-04-20 14:27
 **/
@Component
public class UserRedPacket {

    /** 用户抢红包表的id（主键） **/
    private Long id;

    /** 红包id **/
    private Long redPacketId;

    /** 用户id**/
    private Long userId;

    /** 抢到的红包金额 **/
    private Double amount;

    /** 抢到的时间 **/
    private Timestamp grabTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(Long redPacketId) {
        this.redPacketId = redPacketId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getGrabTime() {
        return grabTime;
    }

    public void setGrabTime(Timestamp grabTime) {
        this.grabTime = grabTime;
    }
}
