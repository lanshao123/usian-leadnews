package com.usian.comment.controller;

import com.usian.aips.comment.CommentRepayControllerApi;
import com.usian.comment.service.CommentRepayService;
import com.usian.model.comment.dtos.CommentRepayDto;
import com.usian.model.comment.dtos.CommentRepayLikeDto;
import com.usian.model.comment.dtos.CommentRepaySaveDto;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: CommentRepayController
 * @author: wangheng
 * @create: 2022-08-30 19:28
 **/
@RestController
@RequestMapping("/api/v1/comment_repay")
public class CommentRepayController implements CommentRepayControllerApi {
    @Autowired

    private CommentRepayService commentRepayService;

    @Override
    @PostMapping("/load")
    public ResponseResult loadCommentRepay(@RequestBody CommentRepayDto dto) {
        dto.setMinDate(new Date());
        return commentRepayService.loadCommentRepay(dto);
    }

    @Override
    @PostMapping("/save")
    public ResponseResult saveCommentRepay(@RequestBody CommentRepaySaveDto dto) {
        return commentRepayService.saveCommentRepay(dto);
    }

    @Override
    @PostMapping("/like")
    public ResponseResult saveCommentRepayLike(@RequestBody CommentRepayLikeDto dto) {
        return commentRepayService.saveCommentRepayLike(dto);
    }
}
