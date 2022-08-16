package com.usian.wemedia.controller;

import com.usian.aips.wemedia.WmNewsControllerApi;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: WmNewsController
 * @author: wangheng
 * @create: 2022-08-16 17:01
 **/
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController implements WmNewsControllerApi {
    @Autowired
    private WmNewsService wmNewsService;

    @Override
    @PostMapping("/list")
    public ResponseResult findAll(@RequestBody WmNewsPageReqDto wmNewsPageReqDto) {
        return wmNewsService.findAll(wmNewsPageReqDto);
    }
}
