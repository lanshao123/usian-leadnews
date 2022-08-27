package com.usian.article.feign;

import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.behavior.pojos.ApLikesBehavior;
import com.usian.model.behavior.pojos.ApUnlikesBehavior;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @program: usian-leadnews
 * @description: WemediaFeign
 * @author: wangheng
 * @create: 2022-08-09 18:41
 **/
@FeignClient("leadnews-behavior")
public interface BehaviorFeign {
    @GetMapping("/api/v1/behavior_entry/one")
    public ApBehaviorEntry findApBehaviorEntryByUserId(@RequestParam("id") Integer id, @RequestParam("equipmentId") Integer equipmentId);
    @GetMapping("/api/v1/unlike_behavior/one")
    ApUnlikesBehavior findUnLikeByArticleIdAndEntryId(@RequestParam("entryId") Integer entryId, @RequestParam("articleId") Long articleId);

    @GetMapping("/api/v1/likes_behavior/one")
    ApLikesBehavior findLikeByArticleIdAndEntryId(@RequestParam("entryId") Integer entryId, @RequestParam("articleId") Long articleId, @RequestParam("type") short type);
}
