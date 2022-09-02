package com.usian.comment.service;

import com.usian.model.comment.dtos.CommentRepayDto;
import com.usian.model.comment.dtos.CommentRepayLikeDto;
import com.usian.model.comment.dtos.CommentRepaySaveDto;
import com.usian.model.common.dtos.ResponseResult;

public interface CommentRepayService {
    /**
     * 查看更多回复内容
     * @param dto
     * @return
     */
    public ResponseResult loadCommentRepay(CommentRepayDto dto);

    /**
     * 保存回复
     * @return
     */
    public ResponseResult saveCommentRepay(CommentRepaySaveDto dto);

    /**
     * 点赞回复的评论
     * @param dto
     * @return
     */
    public ResponseResult saveCommentRepayLike(CommentRepayLikeDto dto);
}
