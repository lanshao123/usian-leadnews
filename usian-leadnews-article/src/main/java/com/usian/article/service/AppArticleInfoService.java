package com.usian.article.service;

import com.usian.model.article.dtos.ArticleInfoDto;
import com.usian.model.common.dtos.ResponseResult;

public interface AppArticleInfoService {
    /**
     * 加载文章详情
     * @param dto
     * @return
     */
    public ResponseResult loadArticleInfo(ArticleInfoDto dto);
}
