package com.usian.aips.article;

import com.usian.model.article.pojos.ApArticleContent;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;

public interface ApArticleContentControllerApi {
    /**
     * 保存app端文章内容
     * @param apArticleContent
     * @return
     */
    ResponseResult saveArticleContent(ApArticleContent apArticleContent);

    public ApArticleContent findOne(@PathVariable Long id);
}
