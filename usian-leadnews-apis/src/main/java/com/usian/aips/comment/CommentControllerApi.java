package com.usian.aips.comment;

import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.common.dtos.ResponseResult;

public interface CommentControllerApi {
    /**
     * 保存评论
     * @param dto
     * @return
     */
    public ResponseResult saveComment(CommentSaveDto dto);
}
