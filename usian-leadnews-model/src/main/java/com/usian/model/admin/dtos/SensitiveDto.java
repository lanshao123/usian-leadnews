package com.usian.model.admin.dtos;

import com.usian.model.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SensitiveDto extends PageRequestDto {

    /**
     * 敏感词名称
     */
    @ApiModelProperty("敏感词名称")
    private String name;
}
