package cn.leonyuan.controller;

import cn.leonyuan.service.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @program: RedPacket
 * @description: 抢红包控制器
 * @author: LeonYuan
 * @create: 2018-04-20 15:44
 **/
@Controller

public class UserRedPacketController {

    @Autowired
    private UserRedPacketService userRedPacketService=null;


    @RequestMapping("/hh")
    public String red(){
        return "index.jsp";
    }


    @RequestMapping("/test")
    public void grabRedPacket(){

        //userRedPacketService.updateUserRedPacket(1l,1l);
        //乐观锁
        //userRedPacketService.Optimistic_lock(1l,1l);
        //可重入乐观锁
        userRedPacketService.Reentrant_Optimistic_lock(1l,1l);
    }

}
