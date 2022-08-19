package com.usian.admin.service;

import com.usian.model.admin.dtos.NewsAuthDto;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;

public interface WemediaNewsAutoScanService {
    /**
     * 自媒体文章审核
     * @param id
     */
    public void autoScanByMediaNewsId(Integer id) throws Exception;

    ResponseResult newsAuthScan(NewsAuthDto newsAuthDto);

    PageResponseResult list(NewsAuthDto newsAuthDto);
}
