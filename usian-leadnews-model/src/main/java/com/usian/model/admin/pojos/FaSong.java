package com.usian.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: FaSong
 * @author: wangheng
 * @create: 2022-08-27 16:47
 **/
@Data
@TableName("fasong")
public class FaSong  implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道名称
     */
    @TableField("email")
    private String email;

    /**
     * 频道描述
     */
    @TableField("created_time")
    private Date createdTime;
    @TableField("userid")
    private Integer userid;


}
