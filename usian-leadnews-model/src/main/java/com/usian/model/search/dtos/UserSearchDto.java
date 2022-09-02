package com.usian.model.search.dtos;

import com.usian.model.common.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
public class UserSearchDto {
    // 设备ID  如果用户没有登录  获取设备ID 进行搜索历史查询
    @IdEncrypt
    Integer equipmentId;
    /**
     * 搜索关键字
     */
    String searchWords;
    /**
     * 当前页
     */
    int pageNum;
    /**
     * 分页条数
     */
    int pageSize;
    /**
     * 最小时间
     */
    Date minBehotTime;

    String layout;
    /**
     * 接收搜索历史记录id
     */
    Integer id;

    public int getFromIndex(){
        if(this.pageNum<1)return 0;
        if(this.pageSize<1) this.pageSize = 10;
        return this.pageSize * (pageNum-1);
    }
}
