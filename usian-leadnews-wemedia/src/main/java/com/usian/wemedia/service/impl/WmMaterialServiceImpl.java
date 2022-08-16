package com.usian.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.exception.ExceptionCast;
import com.usian.common.fastdfs.FastDFSClientUtil;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.dtos.WmMaterialDto;
import com.usian.model.media.pojos.WmMaterial;
import com.usian.model.media.pojos.WmNewsMaterial;
import com.usian.model.media.pojos.WmUser;
import com.usian.utils.threadlocal.WmThreadLocalUtils;
import com.usian.wemedia.config.QiNiuUtil;
import com.usian.wemedia.mapper.WmMaterialMapper;
import com.usian.wemedia.mapper.WmNewsMaterialMapper;
import com.usian.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        //上传判断参数
        if(file==null){
            ExceptionCast.cast(1,"参数不正确");
        }
        String newFileName=null;
        String str=null;
        String uurl=null;
        try {
            //str= fastDFSClientUtil.uploadFile(file);
            String originalFilename = file.getOriginalFilename();
            newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            switch (type){
                case 1:
                    str= fastDFSClientUtil.uploadFile(file);
                    uurl=url+str;
                    break;
                case 2:
                    break;
                case 3:
                    boolean b = QiNiuUtil.uploadMultipartFile(file, newFileName, true);
                    uurl="http://rgoty2n9l.hb-bkt.clouddn.com/"+newFileName;
                    break;
                default:
                    ExceptionCast.cast(1,"参数不正确");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传成功 保存到数据库
        Integer id = WmThreadLocalUtils.getUser().getId();
        WmMaterial wmMaterial=new WmMaterial();
        wmMaterial.setUserId(id);
        wmMaterial.setCreatedTime(new Date());
        wmMaterial.setIsCollection((short)0);
        wmMaterial.setType((short)0);
        // if(type.equals(1)){
        //     wmMaterial.setUrl("1="+str);
        // }else {
        //     wmMaterial.setUrl("3="+newFileName);
        // }
        if(type.equals("1")){
            wmMaterial.setUrl(str);
        }else{
            wmMaterial.setUrl(uurl);
        }

        this.save(wmMaterial);
        wmMaterial.setUrl(uurl);
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        if(dto==null){
            ExceptionCast.cast(1,"参数校验失败");
        }
        dto.checkParam();
        Integer userid = WmThreadLocalUtils.getUser().getId();
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //对userid 进行比较
        lambdaQueryWrapper.eq(WmMaterial::getUserId,userid);
        //是否收藏
        if(dto.getIsCollection()!=null && dto.getIsCollection().equals((short)1)){
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }
        //进行收藏，日期排序
        lambdaQueryWrapper.orderByDesc(WmMaterial::getIsCollection,WmMaterial::getCreatedTime);
        //开始分页
       IPage<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());
        IPage<WmMaterial> page1 = this.page(page, lambdaQueryWrapper);
        PageResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page1.getTotal());
        List<WmMaterial> records = page1.getRecords();
        responseResult.setData(records);
        return responseResult;
    }

    @Override
    public ResponseResult delPicture(Integer id) {
        //删除图片
        if(id==null){
            ExceptionCast.cast(1,"参数错误");
        }
        //判断id是否为这个用户的
        Integer userid = WmThreadLocalUtils.getUser().getId();
        LambdaQueryWrapper<WmMaterial> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(WmMaterial::getId,id);
        wrapper.eq(WmMaterial::getUserId,userid);
        WmMaterial one = this.getOne(wrapper);
        if(one==null){
            ExceptionCast.cast(1,"非法数据");
        }
        //判断图片是否被引用
        LambdaQueryWrapper<WmNewsMaterial> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(WmNewsMaterial::getMaterialId,id);
        Integer integer = wmNewsMaterialMapper.selectCount(wrapper1);
        if(integer>0){
            ExceptionCast.cast(1,"图片被引用无法删除");
        }
        //删除fastdfs图片
        try {
            if(one.getUrl().contains("clouddn")){
                //七牛云删除
                String replace = one.getUrl().replace("http://rgoty2n9l.hb-bkt.clouddn.com/", "");
                String s = QiNiuUtil.deleteUrl(replace);
                System.out.println(s);
                System.out.println("七牛云删除");
            }else{
                String replace = one.getUrl().replace("http://192.168.211.132:8080/", "");
                System.out.println(replace);
                fastDFSClientUtil.delFile(replace);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //删除数据库图片
        this.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult isCollection(Integer id, Short collection) {
        if(id==null){
            ExceptionCast.cast(1,"参数非法");
        }
        //获取用户id
        Integer userid = WmThreadLocalUtils.getUser().getId();
        //判断当前用户与图片的关系
        LambdaQueryWrapper<WmMaterial> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(WmMaterial::getId,id);
        wrapper.eq(WmMaterial::getUserId,userid);
        WmMaterial one = this.getOne(wrapper);
        if(one==null){
            ExceptionCast.cast(1,"非法数据");
        }
        //进行修改
        one.setIsCollection(collection);
        this.updateById(one);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
