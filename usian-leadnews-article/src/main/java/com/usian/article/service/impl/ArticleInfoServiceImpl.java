package com.usian.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.article.feign.BehaviorFeign;
import com.usian.article.feign.UserFeign;
import com.usian.article.mapper.ApAuthorMapper;
import com.usian.article.mapper.ApCollectionMapper;
import com.usian.article.service.ArticleInfoService;
import com.usian.model.article.dtos.ArticleInfoDto;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.article.pojos.ApCollection;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.behavior.pojos.ApLikesBehavior;
import com.usian.model.behavior.pojos.ApUnlikesBehavior;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.model.user.pojos.ApUserFollow;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: ArticleInfoServiceImpl
 * @author: wangheng
 * @create: 2022-08-26 21:35
 **/
@Service
@SuppressWarnings("ALL")
public class ArticleInfoServiceImpl implements ArticleInfoService {
    @Autowired
    private BehaviorFeign behaviorFeign;

    @Autowired
    private ApCollectionMapper apCollectionMapper;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private ApAuthorMapper authorMapper;
    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        //1.检查参数
        if(dto == null || dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.查询行为实体
        ApUser user = AppThreadLocalUtils.getUser();
        ApBehaviorEntry apBehaviorEntry = behaviorFeign.findApBehaviorEntryByUserId(user.getId(), dto.getEquipmentId());
        if(apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        boolean isUnlike=false,isLike = false,isCollection = false,isFollow = false;

        //3.查询不喜欢行为
        ApUnlikesBehavior apUnlikesBehavior = behaviorFeign.findUnLikeByArticleIdAndEntryId(apBehaviorEntry.getId(), dto.getArticleId());
        if(apUnlikesBehavior != null && apUnlikesBehavior.getType() == ApUnlikesBehavior.Type.UNLIKE.getCode()){
            isUnlike = true;
        }

        //4.查询点赞行为
        ApLikesBehavior apLikesBehavior = behaviorFeign.findLikeByArticleIdAndEntryId(apBehaviorEntry.getId(),dto.getArticleId(), ApLikesBehavior.Type.ARTICLE.getCode());
        if(apLikesBehavior != null && apLikesBehavior.getOperation() == ApLikesBehavior.Operation.LIKE.getCode()){
            isLike = true;
        }

        //5.查询收藏行为
        ApCollection apCollection = apCollectionMapper.selectOne(Wrappers.<ApCollection>lambdaQuery().eq(ApCollection::getEntryId, apBehaviorEntry.getId())
                .eq(ApCollection::getArticleId, dto.getArticleId()).eq(ApCollection::getType, ApCollection.Type.ARTICLE.getCode()));
        if(apCollection != null){
            isCollection = true;
        }

        //6.查询是否关注
        ApAuthor apAuthor = authorMapper.selectById(dto.getAuthorId());
        if(apAuthor != null){
            ApUserFollow apUserFollow = userFeign.findByUserIdAndFollowId(user.getId(), apAuthor.getUserId());
            if(apUserFollow != null){
                isFollow = true;
            }
        }


        //7.结果返回  {"isfollow":true,"islike":true,"isunlike":false,"iscollection":true}
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("isfollow",isFollow);
        resultMap.put("islike",isLike);
        resultMap.put("isunlike",isUnlike);
        resultMap.put("iscollection",isCollection);
        return ResponseResult.okResult(resultMap);
    }
}
