package com.usian.behavior.controller;

import com.usian.aips.behavior.ApReadBehaviorControllerApi;
import com.usian.behavior.service.ApReadBehaviorService;
import com.usian.model.behavior.dtos.ReadBehaviorDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApReadBehaviorController
 * @author: wangheng
 * @create: 2022-08-26 14:26
 **/
@RestController
@RequestMapping("/api/v1/read_behavior")
public class ApReadBehaviorController implements ApReadBehaviorControllerApi {
    @Autowired
    private ApReadBehaviorService apReadBehaviorService;
    @Override
    @PostMapping
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDto dto) {
        return apReadBehaviorService.readBehavior(dto);
    }
}
