package com.usian.admin.feign;

import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.article.pojos.ApArticleContent;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("leadnews-article")
public interface ArticleFeign {
    /**
     * 保存文章配置信息
     * @param apArticleConfig
     * @return
     */
    @PostMapping("/api/v1/article_config/save")
    ResponseResult saveArticleConfig(ApArticleConfig apArticleConfig);

    /**
     * 根据name查询文章作者
     * @param name
     * @return
     */
    @GetMapping("/api/v1/author/findByName/{name}")
    ApAuthor selectAuthorByName(@PathVariable("name") String name);

    /**
     * 保存文章
     * @param apArticle
     * @return
     */
    @PostMapping("/api/v1/article/save")
    ApArticle saveArticle(ApArticle apArticle);

    /**
     * 保存文章主体内容
     * @param apArticleContent
     * @return
     */
    @PostMapping("/api/v1/article_content/save")
    ResponseResult saveArticleContent(ApArticleContent apArticleContent);
}
