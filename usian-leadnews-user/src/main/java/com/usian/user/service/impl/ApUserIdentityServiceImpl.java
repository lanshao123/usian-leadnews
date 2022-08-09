package com.usian.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.pojos.WmUser;
import com.usian.model.user.dtos.AuthDto;
import com.usian.model.user.pojos.ApUser;
import com.usian.model.user.pojos.ApUserIdentity;
import com.usian.model.user.pojos.ApUserRealname;
import com.usian.user.feign.ArticleFeign;
import com.usian.user.feign.WemediaFeign;
import com.usian.user.mapper.ApUserIdentityMapper;
import com.usian.user.mapper.ApUserMapper;
import com.usian.user.mapper.ApUserRealnameMapper;
import com.usian.user.service.ApUserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: ApUserIdentityServiceImpl
 * @author: wangheng
 * @create: 2022-08-09 19:27
 **/
@Service
@SuppressWarnings("ALL")
public class ApUserIdentityServiceImpl extends ServiceImpl<ApUserIdentityMapper,ApUserIdentity> implements ApUserIdentityService {
    @Autowired
    private ApUserIdentityMapper identityMapper;
    @Autowired
    private ApUserRealnameMapper apUserRealnameMapper;
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private WemediaFeign wemediaFeign;
    @Autowired
    private ArticleFeign articleFeign;
    /**
     * 管理员进行身份认证 接口
     * @param dto
     * @param status
     * @return
     */
    @Override
    public ResponseResult updateStatus(AuthDto dto, Short status) {
        //这是管理员手动审核的接口
        if (dto==null||dto.getId()==null) {
            ExceptionCast.cast(1,"参数错误");
        }
        //根据identity表的id查出来 身份认证的信息
        ApUserIdentity apUserIdentity = identityMapper.selectById(dto.getId());
        //判断identity是否存在数据
        if (apUserIdentity==null) {
            ExceptionCast.cast(1,"身份信息不存在");
        }
        if (!apUserIdentity.getStatus().equals((short)1)) {
            ExceptionCast.cast(1,"身份审核状态错误");
        }
        //查询是否实名
        List<ApUserRealname> apUserRealnames = apUserRealnameMapper.selectList(new LambdaQueryWrapper<ApUserRealname>().eq(ApUserRealname::getUserId, apUserIdentity.getUserId()));
        ApUserRealname userRealname=null;
        for (ApUserRealname apUserRealname : apUserRealnames) {
            userRealname=apUserRealname;
            break;
        }
        if(userRealname==null){
            ExceptionCast.cast(1,"未查到实名信息,请先进行实名");
        }
        if (!userRealname.getStatus().equals((short)9)) {
            ExceptionCast.cast(1,"实名审核未通过");
        }

        //到这一步 实名通过，以及身份信息表也有数据
        //开始进行身份认证
        //修改identity表数据
        apUserIdentity.setStatus(status);
        apUserIdentity.setUpdatedTime(new Date());
        this.updateById(apUserIdentity);
        //然后创建自媒体账号 以及作者账号
        ResponseResult wmUserAndAuthor = createWmUserAndAuthor(apUserIdentity);
        if(wmUserAndAuthor!=null){
            ExceptionCast.cast(1,"创建自媒体and作者失败");
        }
        //首先创建自媒体

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 创建自媒体
     * @param dto
     * @return
     */
    public ResponseResult createWmUserAndAuthor(ApUserIdentity dto){
        if(dto==null){
            ExceptionCast.cast(1,"参数错误");
        }
        //把user信息查询出来
        ApUser apUser = this.apUserMapper.selectById(dto.getUserId());
        if(apUser==null){
            ExceptionCast.cast(1,"user信息不存在");
        }
        //判断自媒体是否存在
        WmUser byName = wemediaFeign.findByName(apUser.getName());
        //如果不存在就新增自媒体
        if (byName==null) {
            //新增 自媒体
            byName=new WmUser();
            //登录用户名
            byName.setName(apUser.getName());
            //登录密码
            byName.setPassword(apUser.getPassword());
            //user id
            byName.setApUserId(apUser.getId());
            //手机号
            byName.setPhone(apUser.getPhone());
            //创建时间
            byName.setCreatedTime(new Date());
            //状态
            byName.setStatus(9);
            //账号类型
            byName.setType(0);
            //保存自媒体
            wemediaFeign.save(byName);

        }else{
            //不为空说明，以及存在自媒体
            ExceptionCast.cast(1,"创建自媒体失败");
        }
        //新增作者
        createAuthor(byName);
        //修改user表的信息
        apUser.setFlag((short)1);
        this.apUserMapper.updateById(apUser);
        return null;
    }

    /**
     * 创建作者
     * @param byName
     */
    private void createAuthor(WmUser byName) {
        if (byName==null) {
            ExceptionCast.cast(1,"自媒体参数错误");
        }
        Integer apUserId = byName.getApUserId();
        Integer id = byName.getId();
        //首先判断作者是否存在
        //通过userid来判断
        ApAuthor byUserId = articleFeign.findByUserId(apUserId);
        if(byUserId!=null){
            //作者存在不需要创建
            ExceptionCast.cast(1,"作者已经存在");
        }
        //保存作者
        ApAuthor apAuthor=new ApAuthor();
        apAuthor.setCreatedTime(new Date());
        apAuthor.setUserId(apUserId);
        apAuthor.setWmUserId(id);
        apAuthor.setName(byName.getName());
        apAuthor.setType(2);
        articleFeign.save(apAuthor);

    }
}
