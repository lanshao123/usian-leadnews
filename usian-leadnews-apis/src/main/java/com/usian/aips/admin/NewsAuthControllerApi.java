package com.usian.aips.admin;

import com.usian.model.admin.dtos.NewsAuthDto;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: usian-leadnews
 * @description: NewsAuthControllerApi
 * @author: wangheng
 * @create: 2022-08-19 16:40
 **/
@Api(value = "文章审核",tags = "newsAuth",description = "文章审核管理API")
public interface NewsAuthControllerApi {
    /**
     * 文章人工审核
     * @return
     */
    public ResponseResult auth_pass(@RequestBody NewsAuthDto newsAuthDto);
    public ResponseResult list(@RequestBody NewsAuthDto newsAuthDto);
    /**
     * 查询文章详情
     * @param id
     * @return
     */
    public ResponseResult findOne(Integer id);

}
