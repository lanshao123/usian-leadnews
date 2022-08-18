package com.usian.article.controller;

import com.usian.aips.article.ApArticleContentControllerApi;
import com.usian.article.service.ApArticleContentService;
import com.usian.model.article.pojos.ApArticleContent;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApArticleContentController
 * @author: wangheng
 * @create: 2022-08-18 20:23
 **/
@RestController
@RequestMapping("/api/v1/article_content")
public class ApArticleContentController implements ApArticleContentControllerApi {
    @Autowired
    private ApArticleContentService apArticleContentService;
    @Override
    @PostMapping("/save")
    public ResponseResult saveArticleContent(@RequestBody ApArticleContent apArticleContent) {
        apArticleContentService.save(apArticleContent);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
