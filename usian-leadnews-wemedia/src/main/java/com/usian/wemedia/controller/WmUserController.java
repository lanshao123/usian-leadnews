package com.usian.wemedia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usian.aips.wemedia.WmUserControllerApi;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.pojos.WmUser;
import com.usian.wemedia.service.WmUserService;
import org.jcodings.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.HashMap;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: WmUserController
 * @author: wangheng
 * @create: 2022-08-09 16:50
 **/
@RestController
@RequestMapping("/api/v1/user")
public class WmUserController implements WmUserControllerApi {
    @Autowired
    private WmUserService wmUserService;
    @Override
    @PostMapping("/save")
    public ResponseResult save(@RequestBody WmUser wmUser) {
        wmUserService.save(wmUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    @GetMapping("/findByName/{name}")
    public WmUser findByName(@PathVariable("name") String name) {
        List<WmUser> list = wmUserService.list(new LambdaQueryWrapper<WmUser>().eq(WmUser::getName, name));
        for (WmUser wmUser : list) {
            return wmUser;
        }
        return null;
    }

    @Override
    @GetMapping("/findOne/{id}")
    public WmUser findById(@PathVariable("id") Integer id) {
        return wmUserService.getById(id);
    }
}
