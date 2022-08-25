package com.usian.behavior.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.behavior.mapper.ApFollowBehaviorMapper;
import com.usian.behavior.service.ApBehaviorEntryService;
import com.usian.behavior.service.ApFollowBehaviorService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.behavior.dtos.FollowBehaviorDto;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.behavior.pojos.ApFollowBehavior;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApFollowBehaviorServiceImpl
 * @author: wangheng
 * @create: 2022-08-25 20:23
 **/
@Service
@SuppressWarnings("ALL")
public class ApFollowBehaviorServiceImpl extends ServiceImpl<ApFollowBehaviorMapper, ApFollowBehavior> implements ApFollowBehaviorService {
   @Autowired
   private ApBehaviorEntryService apBehaviorEntryService;
    @Override
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto) {
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(dto.getUserId(),null);
        if(apBehaviorEntry==null){
            ExceptionCast.cast(1,"无效参数");
        }
        //保存关注行为
        ApFollowBehavior alb = new ApFollowBehavior();
        alb.setEntryId(apBehaviorEntry.getId());
        alb.setCreatedTime(new Date());
        alb.setArticleId(dto.getArticleId());
        alb.setFollowId(dto.getFollowId());
        return ResponseResult.okResult(save(alb));
    }
}
