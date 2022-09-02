package com.usian.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usian.model.comment.dtos.*;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.common.AppJwtUtil;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @program: usian-leadnews
 * @description: LikeTest
 * @author: wangheng
 * @create: 2022-08-31 08:19
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class LikeTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void aa(){
        System.out.println(AppJwtUtil.getToken(4l));
    }

    @Test
    /**
     * 文章点赞
     */
    public void like() throws Exception {
        ApUser apUser=new ApUser();
        apUser.setId(4);
        AppThreadLocalUtils.setUser(apUser);
        CommentLikeDto commentLikeDto=new CommentLikeDto();
        commentLikeDto.setCommentId("630ed9b089bce62858094302");
        commentLikeDto.setOperation((short) 1);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/v1/comment/like");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(commentLikeDto));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    /**
     * 评论文章
     */
    public void save() throws Exception {
        ApUser apUser=new ApUser();
        apUser.setId(4);
        AppThreadLocalUtils.setUser(apUser);
        CommentSaveDto commentLikeDto=new CommentSaveDto();
        commentLikeDto.setArticleId(1562796000929751042l);
        commentLikeDto.setContent("进行了评论");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/v1/comment/save");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(commentLikeDto));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
    @Test
    /**
     * 加载文章评论
     */
    public void load() throws Exception {
        ApUser apUser=new ApUser();
        apUser.setId(4);
        AppThreadLocalUtils.setUser(apUser);
        CommentDto commentLikeDto=new CommentDto();
        commentLikeDto.setArticleId(1562796000929751042l);
        commentLikeDto.setIndex((short) 0);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/v1/comment/load");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(commentLikeDto));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    /**
     * 加载文章评论的回复
     */
    public void load_repay() throws Exception {
        ApUser apUser=new ApUser();
        apUser.setId(4);
        AppThreadLocalUtils.setUser(apUser);
        CommentRepayDto commentLikeDto=new CommentRepayDto();
     commentLikeDto.setCommentId("630ed9b089bce62858094302");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/v1/comment_repay/load");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(commentLikeDto));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    /**
     * 对文章评论的回复进行点赞
     */
    public void like_repay() throws Exception {
        ApUser apUser=new ApUser();
        apUser.setId(4);
        AppThreadLocalUtils.setUser(apUser);
        CommentRepayLikeDto commentLikeDto=new CommentRepayLikeDto();
        commentLikeDto.setCommentRepayId("630edb8689bce66654d240f6");
        commentLikeDto.setOperation((short)0);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/v1/comment_repay/like");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(commentLikeDto));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }


    @Test
    /**
     * 对文章评论的回复进行回复保存
     */
    public void save_repay() throws Exception {
        ApUser apUser=new ApUser();
        apUser.setId(4);
        AppThreadLocalUtils.setUser(apUser);
        CommentRepaySaveDto commentLikeDto=new CommentRepaySaveDto();
        commentLikeDto.setCommentId("630ed9b089bce62858094302");
        commentLikeDto.setContent("这是对你的肯定111123123");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/v1/comment_repay/save");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(commentLikeDto));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
