package com.usian.model.comment.vo;

import com.usian.model.comment.pojos.ApComment;
import lombok.Data;

@Data
public class ApCommentVo extends ApComment {

    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}