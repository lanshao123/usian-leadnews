package com.usian.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmUser;
import com.usian.utils.threadlocal.WmThreadLocalUtils;
import com.usian.wemedia.mapper.WmNewsMapper;
import com.usian.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: usian-leadnews
 * @description: WmNewsServiceImpl
 * @author: wangheng
 * @create: 2022-08-16 16:41
 **/
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Value("${fdfs.url}")
    private String url;
    @Override
    public ResponseResult findAll(WmNewsPageReqDto dto) {
        if(dto==null){
            ExceptionCast.cast(1,"数据非法");
        }
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        if(user==null){
            ExceptionCast.cast(1,"用户信息错误");
        }
        LambdaQueryWrapper<WmNews> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getStatus()!=null,WmNews::getStatus, dto.getStatus());
        queryWrapper.eq(dto.getChannelId()!=null,WmNews::getChannelId,dto.getChannelId());
        queryWrapper.like(dto.getKeyword()!=null,WmNews::getTitle,dto.getKeyword());
        queryWrapper.eq(WmNews::getUserId,user.getId());
        if(dto.getBeginPubDate()!=null&&dto.getEndPubDate()!=null){
            queryWrapper.between(WmNews::getPublishTime,dto.getBeginPubDate(),dto.getEndPubDate());
        }
        IPage<WmNews> iPage=this.page(new Page<>(dto.getPage(),dto.getSize()),queryWrapper);
        ResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) iPage.getTotal());
        responseResult.setData(iPage.getRecords());
        responseResult.setHost(url);
        return responseResult;
    }
}
