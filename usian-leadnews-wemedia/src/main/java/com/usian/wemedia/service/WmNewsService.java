package com.usian.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.admin.dtos.NewsAuthDto;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.vos.WmNewsVo;

import java.util.List;

public interface WmNewsService extends IService<WmNews> {
    /**
     * 查询所有自媒体文章
     *
     * @return
     */
    public ResponseResult findAll(WmNewsPageReqDto wmNewsPageReqDto);

    /**
     * 自媒体文章发布
     *
     * @param wmNews
     * @param isSubmit 是否为提交 1 为提交 0为草稿
     * @return
     */
    ResponseResult saveNews(WmNewsDto wmNews, Short isSubmit);

    /**
     * 根据文章id查询文章
     *
     * @return
     */
    ResponseResult findWmNewsById(Integer id);
    /**
     * 删除文章
     * @return
     */
    ResponseResult delNews(Integer id);
    /**
     * 上下架
     * @param dto
     * @return
     */
    ResponseResult downOrUp(WmNewsDto dto);
    /**
     * 分页查询文章信息
     * @param newsAuthDto
     * @return
     */
    PageResponseResult findListAndPage(NewsAuthDto newsAuthDto);
    /**
     * 查询文章详情
     * @param id
     * @return
     */
    WmNewsVo findWmNewsVo(Integer id);
    List<Integer> findRelease();
}
