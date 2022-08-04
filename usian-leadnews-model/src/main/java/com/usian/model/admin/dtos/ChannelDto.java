package com.usian.model.admin.dtos;

import com.usian.model.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChannelDto extends PageRequestDto {

    /**
     * 频道名称
     */
    @ApiModelProperty("频道名称")
    private String name;
    /**
     * 账号状态
     */
    @ApiModelProperty("账号状态")
    private Integer status;


}
