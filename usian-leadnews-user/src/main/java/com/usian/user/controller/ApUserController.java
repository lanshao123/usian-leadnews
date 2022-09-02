package com.usian.user.controller;

import com.usian.aips.user.ApUserControllerApi;
import com.usian.model.user.pojos.ApUser;
import com.usian.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApUserController
 * @author: wangheng
 * @create: 2022-08-29 21:34
 **/
@RestController
@RequestMapping("/api/v1/user")
public class ApUserController implements ApUserControllerApi {
    @Autowired
    private ApUserService apUserService;

    @Override
    @GetMapping("/{id}")
    public ApUser findUserById(@PathVariable("id") Integer id) {
        return apUserService.getById(id);
    }
}
