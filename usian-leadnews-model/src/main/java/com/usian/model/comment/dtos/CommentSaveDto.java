package com.usian.model.comment.dtos;

import com.usian.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class CommentSaveDto {

    /**
     * 文章id
     */
    @IdEncrypt
    private Long articleId;

    /**
     * 评论内容
     */
    private String content;
}