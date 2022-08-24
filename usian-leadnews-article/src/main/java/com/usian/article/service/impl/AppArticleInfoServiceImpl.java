package com.usian.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.article.mapper.ApArticleConfigMapper;
import com.usian.article.mapper.ApArticleContentMapper;
import com.usian.article.service.AppArticleInfoService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.article.dtos.ArticleInfoDto;
import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.article.pojos.ApArticleContent;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: AppArticleInfoServiceImpl
 * @author: wangheng
 * @create: 2022-08-24 18:40
 **/
@Service
@SuppressWarnings("ALL")
public class AppArticleInfoServiceImpl implements AppArticleInfoService {
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Override
    public ResponseResult loadArticleInfo(ArticleInfoDto dto) {
        Map<String, Object> resultMap = new HashMap<>();
        if (dto==null||dto.getArticleId()==null) {
            ExceptionCast.cast(1,"参数非法");
        }
        //先查询文章配置信息
        ApArticleConfig ac = apArticleConfigMapper.selectOne(Wrappers.<ApArticleConfig>lambdaQuery().eq(ApArticleConfig::getArticleId, dto.getArticleId()));
        if(ac==null){
            ExceptionCast.cast(1,"文章不存在");
        }
        //在查询文章内容
        if(!ac.getIsDelete()&&!ac.getIsDown()){
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getArticleId()));
            resultMap.put("content",apArticleContent);
        }
        resultMap.put("config",ac);
        //4.结果返回
        return ResponseResult.okResult(resultMap);
    }
}
