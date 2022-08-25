package com.usian.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.behavior.dtos.FollowBehaviorDto;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.dtos.UserRelationDto;
import com.usian.model.user.pojos.ApUser;
import com.usian.model.user.pojos.ApUserFan;
import com.usian.model.user.pojos.ApUserFollow;
import com.usian.user.feign.ArticleFeign;
import com.usian.user.mapper.ApUserFanMapper;
import com.usian.user.mapper.ApUserFollowMapper;
import com.usian.user.mapper.ApUserMapper;
import com.usian.user.service.ApUserRelationService;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: ApUserRelationServiceImpl
 * @author: wangheng
 * @create: 2022-08-25 18:55
 **/
@Service
@SuppressWarnings("ALL")
@Transactional
public class ApUserRelationServiceImpl implements ApUserRelationService {
    @Autowired
    private ApUserFanMapper apUserFanMapper;
    @Autowired
    private ApUserFollowMapper apUserFollowMapper;
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        //检查参数 不等于看 行为必须是 关注and 取关
        if (dto==null|| dto.getOperation()<0 || dto.getOperation() > 1) {
            ExceptionCast.cast(1,"参数非法");
        }
        if(dto.getArticleId()==null){
            ExceptionCast.cast(1,"作者信息不能为空");
        }
        //根据前端传递过来的作者id 查询作者信息
        ApAuthor apAuthor = articleFeign.selectById(dto.getAuthorId());
        Integer followId = null;
        if(apAuthor!=null){
            followId=apAuthor.getUserId();
        }
        if(followId==null){
            ExceptionCast.cast(1,"关注人不存在");
        }else{
            //获取用户的信息
            ApUser user = AppThreadLocalUtils.getUser();
            if(user==null ||user.getId()==0){
                ExceptionCast.cast(1,"用户未登录");
            }else{

                if(dto.getOperation() == 0){
                    //3.如果当前操作是0 创建数据（app用户关注信息和app的用户粉丝信息）
                    followByUserId(user,followId,dto.getArticleId());
                }else{
                    //4.如果当前操作是1 删除数据（app用户关注信息和app的用户粉丝信息）
                    followCancelByUserId(user,followId);
                }
            }

        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void followCancelByUserId(ApUser apUser, Integer followId) {
        //先查再删
        ApUserFollow apUserFollow = apUserFollowMapper.selectOne(Wrappers.<ApUserFollow>lambdaQuery().
                eq(ApUserFollow::getUserId, apUser.getId()).eq(ApUserFollow::getFollowId, followId));
        if (apUserFollow==null) {
            ExceptionCast.cast(1,"未关注");
        }else{
            ApUserFan apUserFan = apUserFanMapper.selectOne(Wrappers.<ApUserFan>lambdaQuery().
                    eq(ApUserFan::getUserId, followId).eq(ApUserFan::getFansId, apUser.getId()));
            //删除用户粉丝
            if(apUserFan!=null){
                apUserFanMapper.delete(Wrappers.<ApUserFan>lambdaQuery()
                        .eq(ApUserFan::getUserId, followId).eq(ApUserFan::getFansId, apUser.getId()));
            }
            //删除用户关注
            apUserFollowMapper.delete(Wrappers.<ApUserFollow>lambdaQuery().
                    eq(ApUserFollow::getUserId, apUser.getId()).eq(ApUserFollow::getFollowId, followId));
        }
    }

    /**
     * 关注操作
     * @param user
     * @param followId
     * @param articleId
     * @return
     */
    private void followByUserId(ApUser apUser, Integer followId, Long articleId) {
        ApUser apUser1 = apUserMapper.selectById(followId);
        ApUserFollow apUserFollow = apUserFollowMapper.
                selectOne(Wrappers.<ApUserFollow>lambdaQuery().
                        eq(ApUserFollow::getUserId, apUser.getId()).eq(ApUserFollow::getFollowId, followId));
        //判断用户是否关注
        if (apUserFollow==null) {
            //保存数据
            ApUserFan apUserFan = apUserFanMapper.selectOne(Wrappers.<ApUserFan>lambdaQuery()
                    .eq(ApUserFan::getUserId, followId).eq(ApUserFan::getFansId, apUser.getId()));
            if(apUserFan==null){//保存用户粉丝
                apUserFan = new ApUserFan();
                apUserFan.setUserId(followId);
                apUserFan.setFansId(apUser.getId().longValue());
                apUserFan.setFansName(apUser1.getName());
                apUserFan.setLevel((short)0);
                apUserFan.setIsDisplay(true);
                apUserFan.setIsShieldLetter(false);
                apUserFan.setIsShieldComment(false);
                apUserFan.setCreatedTime(new Date());
                apUserFanMapper.insert(apUserFan);
            }
            //保存app用户的关注信息
            apUserFollow = new ApUserFollow();
            apUserFollow.setUserId(apUser.getId());
            apUserFollow.setFollowId(followId);
            apUserFollow.setCreatedTime(new Date());
            apUserFollow.setIsNotice(true);
            apUserFollow.setLevel((short)1);
            apUserFollowMapper.insert(apUserFollow);
            //TODO 记录关注文章的行为 使用mq发送消息 进行行为记录
            //记录关注行为的实体类
            FollowBehaviorDto dto=new FollowBehaviorDto();
            dto.setFollowId(followId);
            dto.setArticleId(articleId);
            dto.setUserId(apUser.getId());
            //异步发送消息
            rabbitTemplate.convertAndSend("behaviorExchange","behavior", JSONObject.toJSONString(dto));

        }else{
           ExceptionCast.cast(1,"已关注");
        }

    }
}
