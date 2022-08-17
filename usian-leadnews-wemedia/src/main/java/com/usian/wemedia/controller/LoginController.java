package com.usian.wemedia.controller;

import com.usian.aips.wemedia.UserLoginControllerApi;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmUserDto;
import com.usian.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: LoginController
 * @author: wangheng
 * @create: 2022-08-15 16:43
 **/
@RestController
@RequestMapping("/login")
public class LoginController implements UserLoginControllerApi {
    @Autowired
    private WmUserService wmUserService;
    @Override
    @RequestMapping("/in")
    public ResponseResult login(@RequestBody WmUserDto dto) {
        return wmUserService.login(dto);
    }
}
