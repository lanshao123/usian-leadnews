package com.usian.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.contants.wemedia.WemediaContans;
import com.usian.common.exception.ExceptionCast;
import com.usian.common.fastdfs.FastDFSClientUtil;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmMaterialDto;
import com.usian.model.media.dtos.WmNewsDto;
import com.usian.model.media.pojos.WmMaterial;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmNewsMaterial;
import com.usian.model.media.pojos.WmUser;
import com.usian.utils.threadlocal.WmThreadLocalUtils;
import com.usian.wemedia.config.QiNiuUtil;
import com.usian.wemedia.mapper.WmMaterialMapper;
import com.usian.wemedia.mapper.WmNewsMaterialMapper;
import com.usian.wemedia.service.WmMaterialService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: usian-leadnews
 * @description: WmMaterialServiceImpl
 * @author: wangheng
 * @create: 2022-08-15 17:40
 **/
@Service
@SuppressWarnings("ALL")
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;
    @Value("${fdfs.url}")
    private String url;
    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;
    @Override
    public ResponseResult uploadPicture(MultipartFile file,Integer type)  {
        //??????????????????
        if(file==null){
            ExceptionCast.cast(1,"???????????????");
        }
        String str=null;
        try {
            //?????????fastdfs
            str= fastDFSClientUtil.uploadFile(file);
            //str= fastDFSClientUtil.uploadFile(file);
            // String originalFilename = file.getOriginalFilename();
            // newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // switch (type){
            //     case 1:
            //         str= fastDFSClientUtil.uploadFile(file);
            //         uurl=url+str;
            //         break;
            //     case 2:
            //         break;
            //     case 3:
            //         boolean b = QiNiuUtil.uploadMultipartFile(file, newFileName, true);
            //         uurl="http://rgoty2n9l.hb-bkt.clouddn.com/"+newFileName;
            //         break;
            //     default:
            //         ExceptionCast.cast(1,"???????????????");
            //         break;
            // }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //???????????? ??????????????????
        Integer id = WmThreadLocalUtils.getUser().getId();
        WmMaterial wmMaterial=new WmMaterial();
        wmMaterial.setUserId(id);
        wmMaterial.setCreatedTime(new Date());
        wmMaterial.setIsCollection((short)0);
        wmMaterial.setType((short)0);
        wmMaterial.setUrl(str);
        this.save(wmMaterial);
        wmMaterial.setUrl(url+str);
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        if(dto==null){
            ExceptionCast.cast(1,"??????????????????");
        }
        dto.checkParam();
        Integer userid = WmThreadLocalUtils.getUser().getId();
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //???userid ????????????
        lambdaQueryWrapper.eq(WmMaterial::getUserId,userid);
        //????????????
        if(dto.getIsCollection()!=null && dto.getIsCollection().equals((short)1)){
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }
        //???????????????????????????
        lambdaQueryWrapper.orderByDesc(WmMaterial::getIsCollection,WmMaterial::getCreatedTime);
        //????????????
       IPage<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        IPage<WmMaterial> page1 = this.page(page, lambdaQueryWrapper);
        PageResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page1.getTotal());
        List<WmMaterial> records = page1.getRecords();
        records=records.stream().map(item->{
            item.setUrl(url+item.getUrl());
            return item;
        }).collect(Collectors.toList());
        responseResult.setData(records);
        return responseResult;
    }

    @Override
    public ResponseResult delPicture(Integer id) {
        //????????????
        if(id==null){
            ExceptionCast.cast(1,"????????????");
        }
        //??????id????????????????????????
        Integer userid = WmThreadLocalUtils.getUser().getId();
        LambdaQueryWrapper<WmMaterial> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(WmMaterial::getId,id);
        wrapper.eq(WmMaterial::getUserId,userid);
        WmMaterial one = this.getOne(wrapper);
        if(one==null){
            ExceptionCast.cast(1,"????????????");
        }
        //???????????????????????????
        LambdaQueryWrapper<WmNewsMaterial> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(WmNewsMaterial::getMaterialId,id);
        Integer integer = wmNewsMaterialMapper.selectCount(wrapper1);
        if(integer>0){
            ExceptionCast.cast(1,"???????????????????????????");
        }
        //??????fastdfs??????
        try {
            fastDFSClientUtil.delFile(one.getUrl());
        /*    if(one.getUrl().contains("clouddn")){
                //???????????????
                String replace = one.getUrl().replace("http://rgoty2n9l.hb-bkt.clouddn.com/", "");
                String s = QiNiuUtil.deleteUrl(replace);
                System.out.println(s);
                System.out.println("???????????????");
            }else{
                String replace = one.getUrl().replace("http://192.168.211.132:8080/", "");
                System.out.println(replace);
                fastDFSClientUtil.delFile(replace);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        //?????????????????????
        this.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult isCollection(Integer id, Short collection) {
        if(id==null){
            ExceptionCast.cast(1,"????????????");
        }
        //????????????id
        Integer userid = WmThreadLocalUtils.getUser().getId();
        //????????????????????????????????????
        LambdaQueryWrapper<WmMaterial> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(WmMaterial::getId,id);
        wrapper.eq(WmMaterial::getUserId,userid);
        WmMaterial one = this.getOne(wrapper);
        if(one==null){
            ExceptionCast.cast(1,"????????????");
        }
        //????????????
        one.setIsCollection(collection);
        this.updateById(one);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


}
