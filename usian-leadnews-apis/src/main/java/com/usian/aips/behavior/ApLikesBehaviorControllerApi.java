package com.usian.aips.behavior;

import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.pojos.ApLikesBehavior;
import com.usian.model.common.dtos.ResponseResult;

public interface ApLikesBehaviorControllerApi {

    /**
     * 保存点赞行为
     * @param dto
     * @return
     */
    ResponseResult like(LikesBehaviorDto dto);
    /**
     * 根据行为实体id和文章id查询点赞行为
     * @return
     */
    public ApLikesBehavior findLikeByArticleIdAndEntryId(Long articleId, Integer entryId, Short type);
}

