package com.usian.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.admin.mapper.AdSensitiveMapper;
import com.usian.admin.service.AdChannelService;
import com.usian.admin.service.AdSensitiveService;
import com.usian.common.exception.CustomException;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: AdSensitiveServiceImpl
 * @author: wangheng
 * @create: 2022-08-04 16:57
 **/
@Service
public class AdSensitiveServiceImpl  extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {

    @Override
    public ResponseResult findByNameAndPage(SensitiveDto dto) {
        if(dto==null){
            ExceptionCast.cast(1,"参数为空");
        }
        //校验参数 分页参数
        dto.checkParam();
        //开启分页
        Page<AdSensitive> page=new Page<>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<AdSensitive> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(dto.getName()!=null&&!"".equals(dto.getName()),AdSensitive::getSensitives,dto.getName());
        IPage<AdSensitive> page1 = this.page(page,queryWrapper);
        ResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page1.getTotal());
        responseResult.setData(page1.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult deleteById(Integer id) {
        if(id==null){
            ExceptionCast.cast(1,"参数为空");
        }
        if(this.getById(id)==null){
            ExceptionCast.cast(1,"对象为空");
        }
        this.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult update(AdSensitive adSensitive) {
        if(adSensitive==null){
            ExceptionCast.cast(1,"参数为空");
        }

        if(adSensitive.getId()==null){
            ExceptionCast.cast(1,"id不能为空");
        }
        if(this.getById(adSensitive.getId())==null){
            ExceptionCast.cast(1,"对象为空");
        }
        this.updateById(adSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult insert(AdSensitive adSensitive) {
        if(adSensitive==null){
            ExceptionCast.cast(1,"参数为空");
        }
        adSensitive.setCreatedTime(new Date());
        this.save(adSensitive);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


}
