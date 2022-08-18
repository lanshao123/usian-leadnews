package com.usian.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.admin.pojos.AdSensitive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdSensitiveMapper extends BaseMapper<AdSensitive> {
    public List<String> findAllSensitive();
}
