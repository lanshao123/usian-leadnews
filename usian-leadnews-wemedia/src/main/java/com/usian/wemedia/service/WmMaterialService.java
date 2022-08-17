package com.usian.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmMaterialDto;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 上传图片接口
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile,Integer type);
    /**
     * 素材列表查询
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
     * 收藏图片接口
     * @param id
     * @param collection
     * @return
     */
    ResponseResult isCollection(Integer id,Short collection);

}
