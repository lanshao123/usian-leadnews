package com.usian.comment.controller;

import com.usian.aips.comment.CommentControllerApi;
import com.usian.comment.service.CommentService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.comment.dtos.CommentDto;
import com.usian.model.comment.dtos.CommentLikeDto;
import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: CommentController
 * @author: wangheng
 * @create: 2022-08-30 13:44
 **/
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController implements CommentControllerApi {
    @Autowired
    private CommentService commentService;
    @Override
    @PostMapping("/save")
    public ResponseResult saveComment(@RequestBody CommentSaveDto dto) {
        return commentService.saveComment(dto);
    }

    @PostMapping("/like")
    @Override
    public ResponseResult like(@RequestBody CommentLikeDto dto){
        return commentService.like(dto);
    }

    @Override
    @PostMapping("/load")
    public ResponseResult findByArticleId(@RequestBody CommentDto dto){
        dto.setMinDate(new Date());
        return commentService.findByArticleId(dto);
    }
}
