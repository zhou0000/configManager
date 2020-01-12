package com.server.controller;

import com.server.config.GirlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhou
 * @Date: 2020/1/11 11:30
 */
@RequestMapping("girl")
@Controller
@RefreshScope
public class GirlController {

    @Autowired
    private GirlConfig girlConfig;


    @RequestMapping("info")
    @ResponseBody
    public String info(){
        return "name "+girlConfig.getUsername()+",age "+girlConfig.getAge();
    }
}
