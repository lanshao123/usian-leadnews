package com.usian.article.controller;

import com.usian.aips.article.ArticleHomeControllerApi;
import com.usian.article.service.ApArticleService;
import com.usian.common.contants.article.ArticleConstans;
import com.usian.model.article.dtos.ArticleHomeDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ArticleHomeController
 * @author: wangheng
 * @create: 2022-08-24 17:32
 **/
@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController implements ArticleHomeControllerApi {
    @Autowired
    private ApArticleService apArticleService;
    @Override
    @PostMapping("/load")
    //默认首页 加载最新
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstans.LOADTYPE_LOAD_MORE,dto);
    }

    @Override
    @PostMapping("/loadmore")
    //上拉加载最新
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstans.LOADTYPE_LOAD_MORE,dto);

    }

    @Override
    @PostMapping("/loadnew")
    //下拉加载
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {

        return apArticleService.load(ArticleConstans.LOADTYPE_LOAD_NEW,dto);
    }
}
