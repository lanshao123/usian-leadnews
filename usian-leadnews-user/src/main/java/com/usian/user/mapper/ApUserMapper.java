package com.usian.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: usian-leadnews
 * @description: ApUserMapper
 * @author: wangheng
 * @create: 2022-08-09 20:50
 **/
@Mapper
public interface ApUserMapper extends BaseMapper<ApUser> {
}
