package cn.leonyuan.pojo;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @program: RedPacketXml.xml
 * @description: 红包表的POJO
 * @author: LeonYuan
 * @create: 2018-04-20 14:19
 **/
@Component
public class RedPacket {

    /**  红包id（主键） **/
    private Long id;

    /** 发红包用户的id **/
    private Long userId;

    /** 红包总金额 **/
    private Double amount;

    /** 发送时间，用时间戳表示**/
    private Timestamp sendDate;

    /** 红包总个数 **/
    private Integer total;

    /** 单个红包的金额 **/
    private Double unitAmount;

    /** 剩余红包个数 **/
    private Integer stock;

    /** 版本号，默认0，可不设置**/
    private Integer version;

    /**注释记录**/
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(Double unitAmount) {
        this.unitAmount = unitAmount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
