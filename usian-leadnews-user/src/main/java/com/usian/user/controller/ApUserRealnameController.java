package com.usian.user.controller;

import com.usian.aips.user.ApUserRealnameControllerApi;
import com.usian.common.contants.user.AdminConstans;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import com.usian.user.service.ApUserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.POST;

/**
 * @program: usian-leadnews
 * @description: ApUserRealnameController
 * @author: wangheng
 * @create: 2022-08-08 16:39
 **/
@RestController
@RequestMapping("/api/v1/auth")
public class ApUserRealnameController implements ApUserRealnameControllerApi {
    @Autowired
    private ApUserRealnameService apUserRealnameService;
    @Override
    @PostMapping("/list")
    public ResponseResult loadListByStatus(@RequestBody AuthDto dto) {
        return apUserRealnameService.loadListByStatus(dto);
    }

    @Override
    @PostMapping("/auto")
    public ResponseResult AutoUpdateStatus( @RequestBody AuthDto dto) {
        return apUserRealnameService.AutoUpdateStatus(dto);
    }

    @Override
    @PostMapping("/authPass")
    public ResponseResult authPass(@RequestBody AuthDto dto) {
        return apUserRealnameService.authPass(dto, AdminConstans.PASS_AUTH);
    }

    @Override
    @PostMapping("/authFail")
    public ResponseResult authFail(@RequestBody AuthDto dto) {
        return apUserRealnameService.authFail(dto, AdminConstans.FAIL_AUTH);
    }
}
