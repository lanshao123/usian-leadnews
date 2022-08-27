package com.usian.behavior.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.behavior.mapper.ApReadBehaviorMapper;
import com.usian.behavior.service.ApBehaviorEntryService;
import com.usian.behavior.service.ApReadBehaviorService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.behavior.dtos.ReadBehaviorDto;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.behavior.pojos.ApReadBehavior;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApReadBehaviorServiceImpl
 * @author: wangheng
 * @create: 2022-08-26 14:20
 **/
@Service
public class ApReadBehaviorServiceImpl extends ServiceImpl<ApReadBehaviorMapper, ApReadBehavior> implements ApReadBehaviorService {
   @Autowired
  private ApBehaviorEntryService apBehaviorEntryService;
    @Override
    public ResponseResult readBehavior(ReadBehaviorDto dto) {
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
        //3.保存或更新阅读的行为
        ApReadBehavior apReadBehavior = this.getOne(Wrappers.<ApReadBehavior>lambdaQuery().eq(ApReadBehavior::getArticleId, dto.getArticleId()).eq(ApReadBehavior::getEntryId, byUserIdOrEquipmentId.getId()));
        if(apReadBehavior==null){
            apReadBehavior = new ApReadBehavior();
            apReadBehavior.setCount(dto.getCount());
            apReadBehavior.setArticleId(dto.getArticleId());
            apReadBehavior.setPercentage(dto.getPercentage());
            apReadBehavior.setEntryId(byUserIdOrEquipmentId.getId());
            apReadBehavior.setLoadDuration(dto.getLoadDuration());
            apReadBehavior.setReadDuration(dto.getReadDuration());
            apReadBehavior.setCreatedTime(new Date());
            save(apReadBehavior);
        }else{
            apReadBehavior.setUpdatedTime(new Date());
            apReadBehavior.setCount((short)(apReadBehavior.getCount()+1));
            updateById(apReadBehavior);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
