package com.usian.behavior.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.behavior.mapper.ApUnlikesBehaviorMapper;
import com.usian.behavior.service.ApBehaviorEntryService;
import com.usian.behavior.service.ApUnlikesBehaviorService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.dtos.UnLikesBehaviorDto;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.behavior.pojos.ApReadBehavior;
import com.usian.model.behavior.pojos.ApUnlikesBehavior;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApUnlikesBehaviorServiceImpl
 * @author: wangheng
 * @create: 2022-08-26 14:40
 **/
@Service
public class ApUnlikesBehaviorServiceImpl extends ServiceImpl<ApUnlikesBehaviorMapper, ApUnlikesBehavior> implements ApUnlikesBehaviorService {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Override
    public ResponseResult unlike(UnLikesBehaviorDto dto) {
        //1.参数校验
        if(dto == null || dto.getArticleId() == null){
            ExceptionCast.cast(1,"参数无效");
        }
        //查询行为实体
        //获取当前登陆用户
        ApUser user = AppThreadLocalUtils.getUser();
        if(user==null){
            ExceptionCast.cast(1,"用户未登陆");
        }
        ApBehaviorEntry byUserIdOrEquipmentId = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if(byUserIdOrEquipmentId==null){
            ExceptionCast.cast(1,"行为实体不存在");
        }
        //记录不喜欢行为
        ApUnlikesBehavior apUnlikesBehavior = this.getOne(Wrappers.<ApUnlikesBehavior>lambdaQuery().
                eq(ApUnlikesBehavior::getArticleId, dto.getArticleId()).eq(ApUnlikesBehavior::getEntryId, byUserIdOrEquipmentId.getId()));

        if(dto.getType()==0 && apUnlikesBehavior==null){
            //则记录
            apUnlikesBehavior=new ApUnlikesBehavior();
            apUnlikesBehavior.setEntryId(byUserIdOrEquipmentId.getId());
            apUnlikesBehavior.setArticleId(dto.getArticleId());
            apUnlikesBehavior.setType(dto.getType().intValue());
            apUnlikesBehavior.setCreatedTime(new Date());
            this.save(apUnlikesBehavior);
        }else{
            //取消不喜欢
            //做个修改就可以
            apUnlikesBehavior.setType(dto.getType().intValue());
            this.updateById(apUnlikesBehavior);

        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
