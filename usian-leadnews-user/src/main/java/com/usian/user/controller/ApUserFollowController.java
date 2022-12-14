package com.usian.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.aips.user.ApUserFollowControllerApi;
import com.usian.model.user.pojos.ApUserFollow;
import com.usian.user.service.ApUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApUserFollowController
 * @author: wangheng
 * @create: 2022-08-26 21:30
 **/
@RestController
@RequestMapping("/api/v1/user_follow")
public class ApUserFollowController implements ApUserFollowControllerApi {

    @Autowired
    private ApUserFollowService apUserFollowService;

    @GetMapping("/one")
    @Override
    public ApUserFollow findByUserIdAndFollowId(@RequestParam("userId") Integer userId, @RequestParam("followId") Integer followId) {
        return apUserFollowService.getOne(Wrappers.<ApUserFollow>lambdaQuery()
                .eq(ApUserFollow::getUserId,userId)
                .eq(ApUserFollow::getFollowId,followId));
    }
}
