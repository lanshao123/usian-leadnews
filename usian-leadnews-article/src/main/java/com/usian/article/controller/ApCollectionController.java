package com.usian.article.controller;

import com.usian.aips.article.ApCollectionControllerApi;
import com.usian.article.service.ApCollectionService;
import com.usian.model.article.dtos.CollectionBehaviorDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: usian-leadnews
 * @description: ApCollectionController
 * @author: wangheng
 * @create: 2022-08-26 15:08
 **/
@RestController
@RequestMapping("/api/v1/collection_behavior/")
public class ApCollectionController implements ApCollectionControllerApi {
    @Autowired
    private ApCollectionService apCollectionService;
    @Override
    @PostMapping
    public ResponseResult collection_behavior(@RequestBody CollectionBehaviorDto dto){
        return apCollectionService.apCollectionSave(dto);
    }
}
