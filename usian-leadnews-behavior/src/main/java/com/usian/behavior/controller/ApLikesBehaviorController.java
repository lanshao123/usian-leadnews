package com.usian.behavior.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.aips.behavior.ApLikesBehaviorControllerApi;
import com.usian.behavior.service.ApLikesBehaviorService;
import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.pojos.ApLikesBehavior;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: usian-leadnews
 * @description: ApLikesBehaviorController
 * @author: wangheng
 * @create: 2022-08-26 14:11
 **/
@RestController
@RequestMapping("/api/v1/likes_behavior")
public class ApLikesBehaviorController implements ApLikesBehaviorControllerApi {
    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;
    @Override
    /**
     * app端 用户点赞接口
     */
    @PostMapping
    public ResponseResult like(@RequestBody LikesBehaviorDto dto) {
        return apLikesBehaviorService.like(dto);
    }

    @GetMapping("/one")
    @Override
    public ApLikesBehavior findLikeByArticleIdAndEntryId(@RequestParam("articleId") Long articleId, @RequestParam("entryId")Integer entryId, @RequestParam("type")Short type) {
        ApLikesBehavior apLikesBehavior = apLikesBehaviorService.getOne(Wrappers.<ApLikesBehavior>lambdaQuery()
                .eq(ApLikesBehavior::getArticleId, articleId).eq(ApLikesBehavior::getEntryId, entryId)
                .eq(ApLikesBehavior::getType, type));
        return apLikesBehavior;
    }
}
