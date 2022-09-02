package com.usian.comment.service;

import com.usian.model.comment.dtos.CommentDto;
import com.usian.model.comment.dtos.CommentLikeDto;
import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.common.dtos.ResponseResult;

public interface CommentService {
    /**
     * 保存评论
     * @return
     */
    public ResponseResult saveComment(CommentSaveDto dto);

    /**
     * 点赞评论
     * @param dto
     * @return
     */
    public ResponseResult like(CommentLikeDto dto);
    /**
     * 根据文章id查询评论列表
     * @return
     */
    public ResponseResult findByArticleId(CommentDto dto);
}
