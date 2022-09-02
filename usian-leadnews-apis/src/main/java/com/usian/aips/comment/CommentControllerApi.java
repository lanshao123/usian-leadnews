package com.usian.aips.comment;

import com.usian.model.comment.dtos.CommentDto;
import com.usian.model.comment.dtos.CommentLikeDto;
import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.common.dtos.ResponseResult;

public interface CommentControllerApi {
    /**
     * 保存评论
     * @param dto
     * @return
     */
    public ResponseResult saveComment(CommentSaveDto dto);
    /**
     * 点赞某一条评论
     * @param dto
     * @return
     */
    public ResponseResult like(CommentLikeDto dto);
    /**
     * 查询评论
     * @param dto
     * @return
     */
    public ResponseResult findByArticleId(CommentDto dto);
}
