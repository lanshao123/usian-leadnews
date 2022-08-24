package com.usian.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.article.mapper.ApArticleMapper;
import com.usian.article.service.ApArticleService;
import com.usian.common.contants.article.ArticleConstans;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.article.dtos.ArticleHomeDto;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.common.dtos.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: ApArticleServiceImpl
 * @author: wangheng
 * @create: 2022-08-18 20:14
 **/
@Service
@SuppressWarnings("ALL")
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {
    private final static short MAX_PAGE=50;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Override
    public ResponseResult load(Short loadType, ArticleHomeDto dto) {
        if(dto==null){
            ExceptionCast.cast(1,"参数非法");
        }
        Integer size = dto.getSize();
        if(size==null|| size==0){
            size=10;
        }
        size= Math.min(size,MAX_PAGE);
        dto.setSize(size);
        //类型参数检验
        if(!loadType.equals(ArticleConstans.LOADTYPE_LOAD_MORE)&&!loadType.equals(ArticleConstans.LOADTYPE_LOAD_NEW)){
            loadType = ArticleConstans.LOADTYPE_LOAD_MORE;
        }
        //文章频道校验
        if(StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstans.DEFAULT_TAG);
        }
        //时间校验
        if(dto.getMaxBehotTime() == null) dto.setMaxBehotTime(new Date());
        if(dto.getMinBehotTime() == null) dto.setMinBehotTime(new Date());
        //2.查询数据
        List<ApArticle> apArticles = apArticleMapper.loadArticleList(dto, loadType);
        //3.结果封装
        ResponseResult responseResult = ResponseResult.okResult(apArticles);
        return responseResult;
    }
}
