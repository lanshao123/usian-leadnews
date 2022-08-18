package com.usian.article.controller;

import com.usian.aips.article.ApArticleConfigControllerApi;
import com.usian.aips.article.ApArticleControllerApi;
import com.usian.article.service.ApArticleConfigService;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApArticleConfigController
 * @author: wangheng
 * @create: 2022-08-18 20:20
 **/
@RestController
@RequestMapping("/api/v1/article_config")
public class ApArticleConfigController implements ApArticleConfigControllerApi {
    @Autowired
    ApArticleConfigService apArticleConfigService;


    @Override
    @RequestMapping("/save")
    public ResponseResult saveArticleConfig(@RequestBody ApArticleConfig apArticleConfig) {
        apArticleConfigService.save(apArticleConfig);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
