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
    /**
     * 根据文章id文章信息
     * @param id
     * @return
     */
    public ResponseResult findOne(Integer id);
    PageResponseResult list(NewsAuthDto newsAuthDto);
}
