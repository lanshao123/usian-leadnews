package com.usian.user.controller;

import com.usian.aips.user.ApUserLoginControllerApi;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.LoginDto;
import com.usian.user.service.ApUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApUserLoginController
 * @author: wangheng
 * @create: 2022-08-24 19:59
 **/
@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController implements ApUserLoginControllerApi {
    @Autowired
    private ApUserLoginService apUserLoginService;
    @Override
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto dto) {
        return apUserLoginService.login(dto);
    }

    @Override
    @PostMapping("/register")
    public ResponseResult register(@RequestBody LoginDto dto) {
        return apUserLoginService.register(dto);
    }

    @Override
    @PostMapping("loginSms")
    public ResponseResult loginSms(@RequestBody LoginDto dto) {
        dto.setTeme_code("login");
        return apUserLoginService.sms(dto);
    }
    @Override
    @PostMapping("registerSms")
    public ResponseResult registerSms(@RequestBody LoginDto dto) {
        dto.setTeme_code("register");
        return apUserLoginService.sms(dto);
    }
}
