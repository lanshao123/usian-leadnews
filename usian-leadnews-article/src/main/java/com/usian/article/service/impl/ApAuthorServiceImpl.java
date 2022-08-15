package com.usian.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.article.mapper.ApAuthorMapper;
import com.usian.article.service.ApAuthorService;
import com.usian.model.article.pojos.ApAuthor;
import org.apache.commons.net.nntp.Article;
import org.springframework.stereotype.Service;

/**
 * @program: usian-leadnews
 * @description: ArticleServiceImpl
 * @author: wangheng
 * @create: 2022-08-09 18:47
 **/
@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor> implements ApAuthorService {
}
