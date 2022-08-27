package com.usian.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.article.feign.BehaviorFeign;
import com.usian.article.mapper.ApCollectionMapper;
import com.usian.article.service.ApCollectionService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.article.dtos.CollectionBehaviorDto;
import com.usian.model.article.pojos.ApCollection;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApCollectionServiceImpl
 * @author: wangheng
 * @create: 2022-08-26 15:07
 **/
@Service
public class ApCollectionServiceImpl extends ServiceImpl<ApCollectionMapper, ApCollection> implements ApCollectionService {
    @Autowired
    private BehaviorFeign behaviorFeign;

    /**
     * 保存用户收藏
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult apCollectionSave(CollectionBehaviorDto dto) {
        //1.参数校验
        if(dto == null || dto.getEntryId() == null){
            ExceptionCast.cast(1,"参数无效");
        }
        ApUser user = AppThreadLocalUtils.getUser();
        if(user==null){
            ExceptionCast.cast(1,"用户未登陆");
        }
        ApBehaviorEntry apBehaviorEntryByUserId = behaviorFeign.findApBehaviorEntryByUserId(user.getId(),null);
        if(apBehaviorEntryByUserId==null){
            ExceptionCast.cast(1,"行为实体不存在");
        }
        //记录收藏或者不收藏
        ApCollection one = this.getOne(Wrappers.<ApCollection>lambdaQuery().
                eq(ApCollection::getEntryId, apBehaviorEntryByUserId.getId()).
                eq(ApCollection::getArticleId, dto.getEntryId()));
        if(one==null&&dto.getOperation()==0){//收藏
            one=new ApCollection();
            one.setArticleId(dto.getEntryId());
            one.setEntryId(apBehaviorEntryByUserId.getId());
            one.setCollectionTime(new Date());
            one.setPublishedTime(new Date());
            one.setType(dto.getType());
            this.save(one);

        }else if (one!=null && dto.getOperation()==1){
            //取消收藏
            //直接删除收藏数据
            this.removeById(one.getId());
        }
       return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
