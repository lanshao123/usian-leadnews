package com.usian.comment.service.impl;

import com.usian.comment.feign.UserFeign;
import com.usian.comment.service.CommentService;
import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: usian-leadnews
 * @description: CommentServiceImpl
 * @author: wangheng
 * @create: 2022-08-29 21:36
 **/
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserFeign userFeign;
    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {
        return null;
    }
}
