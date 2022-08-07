package com.usian.model.admin.vos;

import com.usian.model.admin.pojos.AdUser;
import lombok.Data;

/**
 * @program: usian-leadnews
 * @description: AdUserVo
 * @author: wangheng
 * @create: 2022-08-05 16:06
 **/
@Data
public class AdUserVo  extends AdUser {
    private String token;

}
