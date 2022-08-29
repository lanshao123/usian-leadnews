package com.usian.comment.service;

import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.common.dtos.ResponseResult;

public interface CommentService {
    /**
     * 保存评论
     * @return
     */
    public ResponseResult saveComment(CommentSaveDto dto);

}
