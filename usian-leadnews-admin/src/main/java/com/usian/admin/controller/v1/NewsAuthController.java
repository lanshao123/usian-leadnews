package com.usian.admin.controller.v1;

import com.usian.admin.service.WemediaNewsAutoScanService;
import com.usian.aips.admin.NewsAuthControllerApi;
import com.usian.model.admin.dtos.NewsAuthDto;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: usian-leadnews
 * @description: NewsAuthController
 * @author: wangheng
 * @create: 2022-08-19 16:42
 **/
@RestController
@RequestMapping("/api/v1/news_auth")
public class NewsAuthController implements NewsAuthControllerApi {
    @Autowired
    private WemediaNewsAutoScanService wemediaNewsAutoScanService;
    @Override
    @PostMapping("/auth_pass")
    public ResponseResult auth_pass(@RequestBody NewsAuthDto newsAuthDto) {

        return wemediaNewsAutoScanService.newsAuthScan(newsAuthDto);
    }
    @Override
    @PostMapping("/list")
    /**
     * 查询媒体列表
     */
    public PageResponseResult list(@RequestBody NewsAuthDto newsAuthDto){
        System.out.println("进入了");
        return wemediaNewsAutoScanService.list(newsAuthDto);
    }

    @Override
    @GetMapping("/one/{id}")
    public ResponseResult findOne(@PathVariable("id") Integer id) {
        return wemediaNewsAutoScanService.findOne(id);
    }
}
