package com.usian.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.contants.wemedia.WemediaContans;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.dtos.WmNewsPageReqDto;
import com.usian.model.media.pojos.WmMaterial;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmNewsMaterial;
import com.usian.model.media.pojos.WmUser;
import com.usian.utils.threadlocal.WmThreadLocalUtils;
import com.usian.wemedia.mapper.WmMaterialMapper;
import com.usian.wemedia.mapper.WmNewsMapper;
import com.usian.wemedia.mapper.WmNewsMaterialMapper;
import com.usian.wemedia.service.WmNewsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: usian-leadnews
 * @description: WmNewsServiceImpl
 * @author: wangheng
 * @create: 2022-08-16 16:41
 **/
@Service
@SuppressWarnings("ALL")
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Value("${fdfs.url}")
    private String url;
    @Autowired
    private WmMaterialMapper wmMaterialMapper;
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Override
    public ResponseResult findAll(WmNewsPageReqDto dto) {
        if (dto == null) {
            ExceptionCast.cast(1, "数据非法");
        }
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            ExceptionCast.cast(1, "用户信息错误");
        }
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dto.getStatus() != null, WmNews::getStatus, dto.getStatus());
        queryWrapper.eq(dto.getChannelId() != null, WmNews::getChannelId, dto.getChannelId());
        queryWrapper.like(dto.getKeyword() != null, WmNews::getTitle, dto.getKeyword());
        queryWrapper.eq(WmNews::getUserId, user.getId());
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
            queryWrapper.between(WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());
        }
        IPage<WmNews> iPage = this.page(new Page<>(dto.getPage(), dto.getSize()), queryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) iPage.getTotal());
        responseResult.setData(iPage.getRecords());
        //responseResult.setHost(url);
        return responseResult;
    }

    @Override
    @Transactional
    public ResponseResult saveNews(WmNewsDto dto, Short isSubmit) {
        //检查参数
        if (dto == null || StringUtils.isBlank(dto.getContent())) {
            ExceptionCast.cast(1, "参数非法");
        }
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews);
        //判断图片模式
        if (dto.getType().equals(WemediaContans.WM_NEWS_TYPE_AUTO)) {
            wmNews.setType(null);
        }
        //对封面图片的处理
        List<String> images = dto.getImages();
        String url = "";
        if (images != null && images.size() > 0) {
            String s = images.toString();
            String replace = s.replace("[", "").replace("]", "").replace(" ", "");
            wmNews.setImages(replace);
        }
        //保存或修改文章
        saveWmNews(wmNews, isSubmit);
        //3.关联文章与素材的关系
        //获取文章主题
        String content = dto.getContent();
        //把文章转换为list<Map>格式
        List<Map> list = JSON.parseArray(content, Map.class);
        //获取所有的图片url 从消息主题里面
        List<String> materials = ectractUrlInfo(list);
        //3.1 关联内容中的图片与素材的关系
        if(isSubmit.equals(WmNews.Status.SUBMIT.getCode())&&materials.size()>0){
            //只有是提交，并且有图片的情况下 才会进入这里
            //参数一就是图片url，参数二是文章的id
            ResponseResult responseResult=saveRelativeInfoForContent(materials,wmNews.getId());
            if(responseResult!=null){
                return responseResult;
            }
        }

        //3.2 关联封面中的图片与素材的关系,设置wm_news的type,自动
        if(isSubmit.equals(WmNews.Status.SUBMIT.getCode())){
            ResponseResult responseResult=saveRelativeInfoForCover(dto,materials,wmNews);
            if(responseResult!=null){
                return responseResult;
            }
        }
        return null;
    }

    @Override
    public ResponseResult findWmNewsById(Integer id) {
        //1.参数检查
        if(id == null){
            ExceptionCast.cast(1,"参数非法");
        }
        //
        WmNews byId = this.getById(id);
        if(byId==null){
            ExceptionCast.cast(1,"文章不存在");
        }
        //3.结果返回
        ResponseResult responseResult = ResponseResult.okResult(byId);
        responseResult.setHost(url);
        return responseResult;
    }

    @Override
    public ResponseResult delNews(Integer id) {
        //1.参数检查
        if(id == null){
            ExceptionCast.cast(1,"参数非法");
        }
        //获取文章

        WmNews one = getOne(new LambdaQueryWrapper<WmNews>().eq(WmNews::getUserId, WmThreadLocalUtils.getUser().getId()).eq(WmNews::getId, id));
        if(one==null){
            ExceptionCast.cast(1,"文章不存在");
        }
        //判断文章状态   status==9  enable == 1
        if(one.getStatus().equals(WmNews.Status.PUBLISHED.getCode())&&one.getEnable().equals(WemediaContans.WM_NEWS_ENABLE_UP)){
            ExceptionCast.cast(1,"文章状态错误");
        }
        //删除素材与文章的关系，删除关系表数据
        wmNewsMaterialMapper.delete(new LambdaQueryWrapper<WmNewsMaterial>().eq(WmNewsMaterial::getNewsId,one.getId()));
        //删除文章
        this.removeById(one.getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
        //1.检查参数
        if(dto == null || dto.getId() == null){
          ExceptionCast.cast(1,"参数非法");
        }
        //查询文章
        WmNews wmNews = getOne(new LambdaQueryWrapper<WmNews>().eq(WmNews::getUserId, WmThreadLocalUtils.getUser().getId()).eq(WmNews::getId, dto.getId()));
        if(wmNews==null){
            ExceptionCast.cast(1,"文章不存在");
        }
        if(!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())){
            ExceptionCast.cast(1,"文章不是待发布状态，不能上下架");
        }
        ///4.修改文章状态，同步到app端（后期做）TODO
        if(dto.getEnable()!=null &&dto.getEnable()>-1 && dto.getEnable()<2){
            wmNews.setEnable(dto.getEnable());
            this.updateById(wmNews);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     *  //设置封面和素材的关系
     *  有两个模式 一个是自动，一个是我们指定 选择了几张图片
     * @param dto
     * @param materials
     * @param wmNews
     * @return
     */
    private ResponseResult saveRelativeInfoForCover(WmNewsDto dto, List<String> materials, WmNews wmNews) {
        //获取用户上传的封面图片
        List<String> images = dto.getImages();
        //如果是自动需要进行判断
        if(dto.getType().equals(WemediaContans.WM_NEWS_TYPE_AUTO)){
            if(materials.size()>0&&materials.size()<2){
                //单图
                wmNews.setType(WemediaContans.WM_NEWS_SINGLE_IMAGE);
                images=materials.stream().limit(1).collect(Collectors.toList());
            }else if(materials.size()>2){
                //多图
                wmNews.setType(WemediaContans.WM_NEWS_MANY_IMAGE);
                images=materials.stream().limit(3).collect(Collectors.toList());
            }else{
                //无图
                wmNews.setType(WemediaContans.WM_NEWS_NONE_IMAGE);
            }
        }
        //保存封面和图片的关系
        if(images!=null &&images.size()>0){
            ResponseResult responseResult = saveRelativeInfo(images, wmNews.getId(),WemediaContans.WM_COVER_REFERENCE);
            if (responseResult != null) {
                return responseResult;
            }
        }
        return null;
    }


    /**
     * 关联内容表与素材表
     * @param materials 所有素材url
     * @param id 文章id
     * @return
     */
    private ResponseResult saveRelativeInfoForContent(List<String> materials, Integer id) {
        return  saveRelativeInfo(materials,id,WemediaContans.WM_CONTENT_REFERENCE);
    }

    /**
     * 保存关系
     * @param materials 图片url
     * @param id 文章id
     * @param wmContentReference 类型 封面and文章
     * @return
     */
    private ResponseResult saveRelativeInfo(List<String> materials, Integer id, Short wmContentReference) {
        materials=materials.stream().map(item->{
            item=item.replace("http://192.168.211.132:8080/","");
            return item;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<WmMaterial> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(WmMaterial::getUrl,materials);
        queryWrapper.eq(WmMaterial::getUserId,WmThreadLocalUtils.getUser().getId());
        //获取用户下面 以及images所有对象
        List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(queryWrapper);
        List<String> list=new ArrayList<>();
        if(wmMaterials!=null&&wmMaterials.size()>0){
            //如果在素材表查询到图片
            //就转换成 map类型  url，素材表id
            //然后根据url来获取到id 添加到集合
            Map<String, Integer> collect = wmMaterials.stream().collect(Collectors.toMap(WmMaterial::getUrl, WmMaterial::getId));
            for (String material : materials) {
                String materialId=String.valueOf(collect.get(material));
                if("null".equals(material)){
                    ExceptionCast.cast(1,"图片错误");
                }
                list.add(materialId);
            }
        }
        //批量保存数据
        wmNewsMaterialMapper.saveRelationsByContent(list,id,wmContentReference);
        return null;
    }

    private List<String> ectractUrlInfo(List<Map> list) {
        List<String> images=new ArrayList<>();
        for (Map map : list) {
            if (map.get("type").equals(WemediaContans.WM_NEWS_TYPE_IMAGE)) {
               String url=(String) map.get("value");
                images.add(url);
            }
        }
        return images;
    }

    private void saveWmNews(WmNews wmNews, Short isSubmit) {
        //保存草稿 还是发布待审核
        wmNews.setStatus(isSubmit);
        wmNews.setUserId(WmThreadLocalUtils.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short)1);
        //判断是保存还是修改
        if(wmNews.getId()==null){
            //保存
            this.save(wmNews);
        }else{
            //修改
            //先删除素材跟文章关系表数据
            LambdaQueryWrapper<WmMaterial> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(WmMaterial::getId,wmNews.getId());
            wmMaterialMapper.delete(queryWrapper);
            //在进行修改
            this.updateById(wmNews);
        }
    }
}
