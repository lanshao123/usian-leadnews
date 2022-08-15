package com.usian.user.controller;

import com.usian.aips.user.ApUserIdentityControllerApi;
import com.usian.common.contants.user.AdminConstans;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import com.usian.user.service.ApUserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.POST;

/**
 * @program: usian-leadnews
 * @description: ApUserIdentityController
 * @author: wangheng
 * @create: 2022-08-09 20:37
 **/
@RestController
@RequestMapping("/api/v1/auths")
@SuppressWarnings("ALL")
public class ApUserIdentityController implements ApUserIdentityControllerApi {
    @Autowired
    private ApUserIdentityService apUserIdentityService;
    @Override
    @PostMapping("/idEntityPass")
    public ResponseResult idEntityPass(@RequestBody AuthDto dto) {
        return apUserIdentityService.updateStatus(dto, AdminConstans.PASS_AUTH);
    }
}
