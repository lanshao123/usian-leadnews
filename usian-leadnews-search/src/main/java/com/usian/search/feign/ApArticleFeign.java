package com.usian.search.feign;

import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("leadnews-article")
public interface ApArticleFeign {
    @GetMapping("/api/v1/article/one/{id}")
    public ApArticle findOne(@PathVariable Long id);
    @GetMapping("/api/v1/article_content/one/{id}")
    public ApArticleContent findContentOne(@PathVariable Long id);
}
