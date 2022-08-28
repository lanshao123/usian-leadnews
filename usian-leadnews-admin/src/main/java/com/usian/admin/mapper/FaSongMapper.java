package com.usian.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.admin.pojos.FaResult;
import com.usian.model.admin.pojos.FaSong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.Map;

@Mapper
public interface FaSongMapper extends BaseMapper<FaSong> {
    @Select("select adu.id id,adu.name name ,aa.count count from ad_user adu inner join (select adu.id id,count(1) count from fasong f left join ad_user adu on f.userid=adu.id where f.created_time BETWEEN #{date1} and #{date2} \n" +
            "and f.userid =#{id} GROUP BY id) aa on adu.id=aa.id")
    FaResult findCount(@Param("id")Integer id, @Param("date1")String date, @Param("date2")String date1);
}
