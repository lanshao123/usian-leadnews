package com.usian.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.model.user.pojos.ApUserIdentity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ApUserIdentityMapper extends BaseMapper<ApUserIdentity> {
    @Update("update ap_user_identity set status=#{status} where id=#{id}")
    void updateIdentitystatus(@Param("id")Integer id,@Param("status")short status);
}
