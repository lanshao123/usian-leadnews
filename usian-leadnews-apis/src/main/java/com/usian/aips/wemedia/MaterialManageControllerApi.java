package com.usian.aips.wemedia;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmMaterialDto;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: usian-leadnews
 * @description: MaterialManageControllerApi
 * @author: wangheng
 * @create: 2022-08-15 17:38
 **/
@Api(value = "自媒体操作管理",tags = "wmUser",description = "自媒体操作管理API")
public interface MaterialManageControllerApi {

    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);
    /**
     * 素材列表
     * @param dto
     * @return
     */
    ResponseResult findList(WmMaterialDto dto);

    /**
     * 根据图片id删除图片
     * @param id
     * @return
     */
    ResponseResult delPicture(Integer id);

    /**
     * 收藏图片
     * @param id
     * @return
     */
    ResponseResult collect(Integer id);
    /**
     * 取消收藏
     * @param id
     * @return
     */
    ResponseResult cancelCollect(Integer id);
}
