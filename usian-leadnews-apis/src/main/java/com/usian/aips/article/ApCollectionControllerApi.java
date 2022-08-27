package com.usian.aips.article;

import com.usian.model.article.dtos.CollectionBehaviorDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApCollectionControllerApi {
    /**
     * 收藏接口
     * @param dto
     * @return
     */
    public ResponseResult collection_behavior(@RequestBody CollectionBehaviorDto dto);
}
