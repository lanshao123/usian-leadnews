package com.usian.aips.wemedia;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import io.swagger.annotations.Api;

@Api(value = "自媒体列表",tags = "userLogin",description = "自媒体列表API")
public interface WmNewsControllerApi {
    /**
     * 分页带条件查询自媒体文章列表
     * @param wmNewsPageReqDto
     * @return
     */
    public ResponseResult findAll(WmNewsPageReqDto wmNewsPageReqDto);

}
