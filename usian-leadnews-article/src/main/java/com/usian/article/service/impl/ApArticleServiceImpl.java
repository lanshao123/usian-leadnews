package com.usian.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.article.mapper.ApArticleMapper;
import com.usian.article.service.ApArticleService;
import com.usian.model.article.pojos.ApArticle;
import org.springframework.stereotype.Service;

/**
 * @program: usian-leadnews
 * @description: ApArticleServiceImpl
 * @author: wangheng
 * @create: 2022-08-18 20:14
 **/
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {
}
