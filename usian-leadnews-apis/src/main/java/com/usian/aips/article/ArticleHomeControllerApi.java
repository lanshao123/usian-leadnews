package com.usian.aips.article;

import com.usian.model.article.dtos.ArticleHomeDto;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;

@Api(value = "文章首页",tags = "author",description = "文章首页API")
public interface ArticleHomeControllerApi {
    /**
     * 加载首页文章
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto);

    /**
     * 加载更多
     * @return
     */
    public ResponseResult loadMore(ArticleHomeDto dto);

    /**
     * 加载最新
     * @return
     */
    public ResponseResult loadNew(ArticleHomeDto dto);
}
