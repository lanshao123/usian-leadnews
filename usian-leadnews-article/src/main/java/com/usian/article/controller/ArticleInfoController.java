package com.usian.article.controller;

import com.usian.aips.article.ArticleInfoControllerApi;
import com.usian.article.service.ApArticleService;
import com.usian.article.service.AppArticleInfoService;
import com.usian.model.article.dtos.ArticleInfoDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ArticleInfoController
 * @author: wangheng
 * @create: 2022-08-24 19:05
 **/
@RestController
@RequestMapping("/api/v1/article")
public class ArticleInfoController implements ArticleInfoControllerApi {
    @Autowired
    private AppArticleInfoService appArticleInfoService;
    @Override
    @PostMapping("/load_article_info")
    public ResponseResult loadArticleInfo(@RequestBody ArticleInfoDto dto) {
        return appArticleInfoService.loadArticleInfo(dto);
    }
}
