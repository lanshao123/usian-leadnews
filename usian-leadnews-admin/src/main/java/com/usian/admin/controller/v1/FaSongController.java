package com.usian.admin.controller.v1;

import com.usian.admin.service.FaSongService;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.dc.pr.PRError;

/**
 * @program: usian-leadnews
 * @description: FaSongController
 * @author: wangheng
 * @create: 2022-08-28 20:45
 **/
@RequestMapping("/api/v1/user")
@RestController
public class FaSongController {
    @Autowired
    private FaSongService faSongService;
    @GetMapping("/tian3")
    public ResponseResult tian3(@RequestParam("day")String day){
       return faSongService.getRedis3(day);
    }
}
