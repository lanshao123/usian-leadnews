package com.usian.behavior.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.aips.behavior.ApUnlikesBehaviorControllerApi;
import com.usian.behavior.service.ApUnlikesBehaviorService;
import com.usian.model.behavior.dtos.UnLikesBehaviorDto;
import com.usian.model.behavior.pojos.ApUnlikesBehavior;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: usian-leadnews
 * @description: ApUnlikesBehaviorController
 * @author: wangheng
 * @create: 2022-08-26 14:53
 **/
@RestController
@RequestMapping("/api/v1/unlike_behavior")
public class ApUnlikesBehaviorController implements ApUnlikesBehaviorControllerApi {
    @Autowired
    private ApUnlikesBehaviorService apUnlikesBehaviorService;

    @Override
    @PostMapping
    public ResponseResult unlike(@RequestBody UnLikesBehaviorDto dto) {
        return apUnlikesBehaviorService.unlike(dto);
    }

    @GetMapping("/one")
    @Override
    public ApUnlikesBehavior findUnLikeByArticleIdAndEntryId(@RequestParam("entryId") Integer entryId, @RequestParam("articleId") Long articleId) {
        return apUnlikesBehaviorService.getOne(Wrappers.<ApUnlikesBehavior>lambdaQuery().eq(ApUnlikesBehavior::getArticleId, articleId)
                .eq(ApUnlikesBehavior::getEntryId, entryId));
    }
}
