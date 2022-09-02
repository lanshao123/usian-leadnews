package com.usian.aips.article;

import com.usian.model.article.pojos.ApArticle;

public interface ApArticleControllerApi {
    /**
     * 保存app文章
     * @param apArticle
     * @return
     */
    ApArticle saveArticle(ApArticle apArticle);
    //根据文章id查询文章
    ApArticle findOne(Long id);
}
