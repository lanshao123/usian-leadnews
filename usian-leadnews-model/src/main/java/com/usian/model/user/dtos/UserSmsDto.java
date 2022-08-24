package com.usian.model.user.dtos;

import lombok.Data;

/**
 * @program: usian-leadnews
 * @description: UserSms
 * @author: wangheng
 * @create: 2022-08-24 21:04
 **/
@Data
public class UserSmsDto {
    private String phone;
    private String code;
    private String teme_code;

}
