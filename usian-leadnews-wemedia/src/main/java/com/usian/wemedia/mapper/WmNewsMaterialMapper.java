package com.usian.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.media.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {
    /**
     * 内容素材中间表
     * @param materials
     * @param newId
     * @param type
     */
    void saveRelationsByContent(@Param("materials") List<String> materials, @Param("newsId") Integer newId, @Param("type") int type);
}
