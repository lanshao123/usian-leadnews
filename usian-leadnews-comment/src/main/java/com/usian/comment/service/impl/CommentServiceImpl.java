package com.usian.comment.service.impl;

import com.usian.comment.feign.UserFeign;
import com.usian.comment.service.CommentService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.comment.dtos.CommentDto;
import com.usian.model.comment.dtos.CommentLikeDto;
import com.usian.model.comment.dtos.CommentSaveDto;
import com.usian.model.comment.pojos.ApComment;
import com.usian.model.comment.pojos.ApCommentLike;
import com.usian.model.comment.vo.ApCommentVo;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.security.cert.Certificate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: usian-leadnews
 * @description: CommentServiceImpl
 * @author: wangheng
 * @create: 2022-08-29 21:36
 **/
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserFeign userFeign;
    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {
        if(dto==null){
            ExceptionCast.cast(1,"非法参数");
        }
        if (dto.getContent() != null && dto.getContent().length() > 140) {
            ExceptionCast.cast(1,"评论内容不能超过140字");
        }
        ApUser apuser= AppThreadLocalUtils.getUser();
        if(apuser==null){
            ExceptionCast.cast(1,"用户未登录");
        }
        //todo 对评论内容做过滤
        ApUser userById = userFeign.findUserById(apuser.getId().longValue());
        if(userById==null){
            ExceptionCast.cast(1,"用户信息错误");
        }
        ApComment apComment=new ApComment();
        apComment.setAuthorId(apuser.getId());
        apComment.setAuthorName(apuser.getName());
        apComment.setContent(dto.getContent());
        apComment.setEntryId(dto.getArticleId());
        apComment.setCreatedTime(new Date());
        apComment.setUpdatedTime(new Date());
        apComment.setImage(apuser.getImage());
        apComment.setLikes(0);
        apComment.setReply(0);
        apComment.setType((short) 0);
        apComment.setFlag((short) 0);
        mongoTemplate.insert(apComment);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult like(CommentLikeDto dto) {
        if (dto.getCommentId()==null) {
            ExceptionCast.cast(1,"非法参数");
        }
        ApUser apuser= AppThreadLocalUtils.getUser();
        if(apuser==null){
            ExceptionCast.cast(1,"用户未登录");
        }
        //查询这个评论是否存在
        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        //接口校验，一个用户只能给这个评论点赞一次校验
        ApCommentLike commentId= mongoTemplate.findOne(Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(apuser.getId())), ApCommentLike.class);
        if(apComment!=null&&dto.getOperation()==0){
          //有点赞数据，不能继续点赞
            if(commentId!=null){
                ExceptionCast.cast(1,"点赞重复");
          }
            //存在并且点赞状态为0
            apComment.setLikes(apComment.getLikes()+1);
            mongoTemplate.save(apComment);
            //保存 APP评论信息点赞
            ApCommentLike apCommentLike=new ApCommentLike();
            apCommentLike.setAuthorId(apuser.getId());
            apCommentLike.setCommentId(apComment.getId());
            apCommentLike.setOperation(dto.getOperation());
            mongoTemplate.save(apCommentLike);
            //取消点赞操作
        }else if(apComment != null && dto.getOperation() == 1){
            //取消点赞，首先用户得点过赞才能取消点赞
            if(commentId==null){
                ExceptionCast.cast(1,"点赞异常");
            }
            //取消点赞，更新点赞数
            //点赞数量不能小于0
            if(apComment.getLikes()>0){
                apComment.setLikes(apComment.getLikes()-1);
                mongoTemplate.save(apComment);

            //更新 APP评论信息点赞 //删除点赞数据
            mongoTemplate.remove(Query.query(Criteria.where("authorId").is(apuser.getId()).and("commentId").is(apComment.getId())),ApCommentLike.class);
            }

        }
        //5.数据返回
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("likes",apComment.getLikes());
        return ResponseResult.okResult(resultMap);
    }

    @Override
    public ResponseResult findByArticleId(CommentDto dto) {
        if (dto.getArticleId()==null) {
            ExceptionCast.cast(1,"非法参数");
        }
        int size=10;
        Query query= Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("createdTime").lt(dto.getMinDate()));
        query.limit(size).with(Sort.by(Sort.Direction.DESC,"createdTime"));
        //按需条件查询
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);
        //获取当前用户
        ApUser apUser=AppThreadLocalUtils.getUser();
        if(apUser==null){
           return ResponseResult.okResult(list);
        }
        //如果用户登陆了，那么判断 这个用户对那些评论进行了点赞
        //获取评论所有的id
        List<String> collect = list.stream().map(item -> item.getId()).collect(Collectors.toList());
        Query query1=Query.query(Criteria.where("commentId").in(collect).and("authorId").is(apUser.getId()));
        List<ApCommentLike> apCommentLikes = mongoTemplate.find(query1, ApCommentLike.class);
        //封装的返回结果

        List<ApCommentVo> resultList = new ArrayList<>();
        if(list!=null && apCommentLikes!=null){
            list.stream().forEach(x->{
                ApCommentVo apCommentVo = new ApCommentVo();
                //把当前list 的每一个元素给 apCommentVo
                BeanUtils.copyProperties(x,apCommentVo);
                for (ApCommentLike apCommentLike : apCommentLikes) {
                    ///有点赞行为
                    if(x.getId().equals(apCommentLike.getCommentId())){
                        apCommentVo.setOperation((short)0);
                    }
                }
                resultList.add(apCommentVo);
            });
            return ResponseResult.okResult(apCommentLikes);
        }else{
            return ResponseResult.okResult(list);
        }

    }
}
