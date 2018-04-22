package cn.leonyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: RedPacket
 * @description:
 * @author: LeonYuan
 * @create: 2018-04-20 23:10
 **/
@Controller
public class HomePageController {

    /**
     * 通过GET方法获取index
    * @return index
    */

    //@RequestMapping("/test")
    public String home() {
        return "index";
    }
}
