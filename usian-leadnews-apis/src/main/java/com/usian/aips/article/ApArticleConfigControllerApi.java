package com.usian.aips.article;

import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.common.dtos.ResponseResult;

public interface ApArticleConfigControllerApi {
    /**
     * 保存app端文章配置
     * @param apArticleConfig
     * @return
     */
    ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);
}
