package com.usian.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApUserIdentity
 * @author: wangheng
 * @create: 2022-08-09 16:38
 **/
@Data
@TableName("ap_user_identity")
public class ApUserIdentity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

    /**
     * 资源名称
     */
    @TableField("idno")
    private String idno;

    /**
     * 正面照片
     */
    @TableField("font_image")
    private String fontImage;

    /**
     * 背面照片
     */
    @TableField("back_image")
    private String backImage;

    /**
     * 手持照片
     */
    @TableField("hold_image")
    private String holdImage;
    /**
     * 状态
     * 0 创建中
     * 1 待审核
     * 2 审核失败
     * 9 审核通过
     */
    @TableField("status")
    private Short status;
    /**
     * 行业
     */
    @TableField("industry")
    private String industry;
    /**
     * 拒绝原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 提交时间
     */
    @TableField("submited_time")
    private Date submitedTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;
}
