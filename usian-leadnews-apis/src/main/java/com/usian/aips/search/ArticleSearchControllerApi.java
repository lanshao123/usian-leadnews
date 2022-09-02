package com.usian.aips.search;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.search.dtos.UserSearchDto;

import java.io.IOException;

public interface ArticleSearchControllerApi {
    /**
     *  搜索文章
     * @param userSearchDto
     * @return
     */
    public ResponseResult search(UserSearchDto userSearchDto) throws IOException;
}
