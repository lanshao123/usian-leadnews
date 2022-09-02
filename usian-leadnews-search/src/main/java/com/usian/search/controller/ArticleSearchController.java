package com.usian.search.controller;

import com.usian.aips.search.ArticleSearchControllerApi;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.search.dtos.UserSearchDto;
import com.usian.search.service.ArticleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @program: usian-leadnews
 * @description: ArticleSearchController
 * @author: wangheng
 * @create: 2022-09-01 19:50
 **/
@RestController
@RequestMapping("/api/v1/article")
public class ArticleSearchController implements ArticleSearchControllerApi {
    @Autowired
    private ArticleSearchService articleSearchService;
    @Override
    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto userSearchDto) throws IOException {
        return articleSearchService.search(userSearchDto);
    }
}
