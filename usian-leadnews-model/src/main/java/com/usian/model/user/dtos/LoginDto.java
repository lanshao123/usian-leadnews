package com.usian.model.user.dtos;

import lombok.Data;

@Data
public class LoginDto {

    //设备id
    private Integer equipmentId;

    //手机号
    private String phone;

    //密码
    private String password;
    // login   zc
    private String teme_code;
}
