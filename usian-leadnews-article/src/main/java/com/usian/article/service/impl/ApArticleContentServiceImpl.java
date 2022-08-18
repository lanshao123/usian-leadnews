package com.usian.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.article.mapper.ApArticleContentMapper;
import com.usian.article.service.ApArticleContentService;
import com.usian.model.article.pojos.ApArticleContent;
import org.springframework.stereotype.Service;

/**
 * @program: usian-leadnews
 * @description: ApArticleContentServiceImpl
 * @author: wangheng
 * @create: 2022-08-18 20:23
 **/
@Service
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {
}
