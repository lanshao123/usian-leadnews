package com.usian.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import com.usian.model.user.pojos.ApUserRealname;

/**
 * @program: usian-leadnews
 * @description: ApUserRealnameService
 * @author: wangheng
 * @create: 2022-08-08 16:24
 **/
public interface ApUserRealnameService extends IService<ApUserRealname> {
    /**
     *按照状态查询用户认证列表
     * @param dto
     * @return
     */
    public ResponseResult loadListByStatus(AuthDto dto);

    /**
     * 自动审核用户实名信息
     * @param dto
     * @return
     */
    public ResponseResult AutoUpdateStatus(AuthDto dto);
}
