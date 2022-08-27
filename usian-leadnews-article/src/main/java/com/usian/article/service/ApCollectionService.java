package com.usian.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.article.dtos.CollectionBehaviorDto;
import com.usian.model.article.pojos.ApCollection;
import com.usian.model.common.dtos.ResponseResult;

/**
 * @program: usian-leadnews
 * @description: ApCollectionService
 * @author: wangheng
 * @create: 2022-08-26 15:06
 **/
public interface ApCollectionService extends IService<ApCollection> {
    //用户收藏文章api
    ResponseResult apCollectionSave(CollectionBehaviorDto dto);
}
