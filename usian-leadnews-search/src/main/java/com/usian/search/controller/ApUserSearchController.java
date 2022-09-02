package com.usian.search.controller;

import com.usian.aips.search.ApUserSearchControllerApi;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.search.dtos.UserSearchDto;
import com.usian.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApUserSearchController
 * @author: wangheng
 * @create: 2022-09-02 18:32
 **/
@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController implements ApUserSearchControllerApi {
    @Autowired
    private ApUserSearchService apUserSearchService;
    @PostMapping("/load")
    @Override
    public ResponseResult findUserSearch(@RequestBody UserSearchDto userSearchDto) {
        return apUserSearchService.findUserSearch(userSearchDto);
    }

    @PostMapping("/del")
    @Override
    public ResponseResult delUserSearch(@RequestBody UserSearchDto userSearchDto) {
        return apUserSearchService.delUserSearch(userSearchDto);
    }
}
