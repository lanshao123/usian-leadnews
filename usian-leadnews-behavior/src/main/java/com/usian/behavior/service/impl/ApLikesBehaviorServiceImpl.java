package com.usian.behavior.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.behavior.mapper.ApLikesBehaviorMapper;
import com.usian.behavior.service.ApBehaviorEntryService;
import com.usian.behavior.service.ApLikesBehaviorService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.behavior.pojos.ApLikesBehavior;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApLikesBehaviorServiceImpl
 * @author: wangheng
 * @create: 2022-08-26 13:56
 **/
@Service
public class ApLikesBehaviorServiceImpl extends ServiceImpl<ApLikesBehaviorMapper, ApLikesBehavior> implements ApLikesBehaviorService {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        //1.检查参数
        if(dto == null || dto.getArticleId() == null || (dto.getType() < 0 && dto.getType() > 2)
                || (dto.getOperation() < 0 && dto.getOperation() > 1)){
            ExceptionCast.cast(1,"无效参数");
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
        //点赞and 取消点赞
        //查询是否点过赞
        ApLikesBehavior apLikesBehavior = this.getOne(Wrappers.<ApLikesBehavior>lambdaQuery().eq(ApLikesBehavior::getArticleId, dto.getArticleId()).eq(ApLikesBehavior::getEntryId, byUserIdOrEquipmentId.getId()));
        if(apLikesBehavior==null &&dto.getOperation()==0){
            apLikesBehavior = new ApLikesBehavior();
            apLikesBehavior.setOperation(dto.getOperation());
            apLikesBehavior.setArticleId(dto.getArticleId());
            apLikesBehavior.setEntryId(byUserIdOrEquipmentId.getId());
            apLikesBehavior.setType(dto.getType());
            apLikesBehavior.setCreatedTime(new Date());
            save(apLikesBehavior);
        }else{
            //取消点赞
            apLikesBehavior.setOperation(dto.getOperation());
            updateById(apLikesBehavior);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
