package com.usian.article.controller;

import com.usian.aips.article.ApArticleControllerApi;
import com.usian.article.service.ApArticleService;
import com.usian.model.article.pojos.ApArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApArticleController
 * @author: wangheng
 * @create: 2022-08-18 20:15
 **/
@RestController
@RequestMapping("/api/v1/article")
public class ApArticleController implements ApArticleControllerApi {
    @Autowired
    private ApArticleService apArticleService;

    @Override
    @PostMapping("save")
    public ApArticle saveArticle(@RequestBody ApArticle apArticle) {
         apArticleService.save(apArticle);
         return apArticle;
    }
}
