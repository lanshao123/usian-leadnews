package com.usian.search.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.search.dtos.UserSearchDto;

import java.io.IOException;

/**
 * @program: usian-leadnews
 * @description: ArticleSearchService
 * @author: wangheng
 * @create: 2022-09-01 19:49
 **/
public interface ArticleSearchService {
    /**
     ES文章分页搜索
     @return
     */
    ResponseResult search(UserSearchDto userSearchDto) throws IOException;
}
