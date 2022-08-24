package com.usian.wemedia.controller;

import com.usian.aips.wemedia.WmNewsControllerApi;
import com.usian.model.admin.dtos.NewsAuthDto;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.vos.WmNewsVo;
import com.usian.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Override
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto wmNews) {
        if (wmNews.getStatus()== WmNews.Status.SUBMIT.getCode()) {
            return wmNewsService.saveNews(wmNews,WmNews.Status.SUBMIT.getCode());
        }else{
            return wmNewsService.saveNews(wmNews,WmNews.Status.NORMAL.getCode());
        }

    }

    @Override
    @GetMapping("/one/{id}")
    public ResponseResult findWmNewsById(@PathVariable("id") Integer id) {
        return wmNewsService.findWmNewsById(id);
    }

    @GetMapping("/del_news/{id}")
    @Override
    public ResponseResult delNews(@PathVariable("id") Integer id) {
        return wmNewsService.delNews(id);
    }

    @PostMapping("/down_or_up")
    @Override
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto) {
        return wmNewsService.downOrUp(dto);
    }

    @Override
    @GetMapping("/findOne/{id}")
    public WmNews findById(@PathVariable("id") Integer id) {
        return wmNewsService.getById(id);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult updateWmNews(@RequestBody WmNews wmNews) {
         wmNewsService.updateById(wmNews);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    @PostMapping("/findList")
    public PageResponseResult findList(@RequestBody NewsAuthDto newsAuthDto) {
        return wmNewsService.findListAndPage(newsAuthDto);
    }

    @Override
    @GetMapping("/find_news_vo/{id}")
    public WmNewsVo findWmNewsVo(@PathVariable("id") Integer id) {
        return wmNewsService.findWmNewsVo(id);
    }

    @Override
    @GetMapping("findRelease")
    public List<Integer> findRelease() {
        return wmNewsService.findRelease();
    }


}
