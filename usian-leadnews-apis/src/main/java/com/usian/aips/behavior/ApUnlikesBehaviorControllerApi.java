package com.usian.aips.behavior;

import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.dtos.UnLikesBehaviorDto;
import com.usian.model.behavior.pojos.ApUnlikesBehavior;
import com.usian.model.common.dtos.ResponseResult;

public interface ApUnlikesBehaviorControllerApi {
    /**
     * 不喜欢api
     * @param dto
     * @return
     */
    ResponseResult unlike(UnLikesBehaviorDto dto);
    /**
     * 根据行为实体id和文章id查询不喜欢行为
     * @param entryId
     * @param articleId
     * @return
     */
    public ApUnlikesBehavior findUnLikeByArticleIdAndEntryId(Integer entryId, Long articleId);
}
