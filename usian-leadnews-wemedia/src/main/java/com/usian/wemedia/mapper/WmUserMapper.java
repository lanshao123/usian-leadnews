package com.usian.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.media.pojos.WmUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: usian-leadnews
 * @description: WmUserMapper
 * @author: wangheng
 * @create: 2022-08-09 16:49
 **/
@Mapper
public interface WmUserMapper extends BaseMapper<WmUser> {
}
