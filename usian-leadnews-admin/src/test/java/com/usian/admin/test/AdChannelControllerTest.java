package com.usian.admin.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
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
 * @description: AdChannelControllerTest
 * @author: wangheng
 * @create: 2022-08-03 16:12
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AdChannelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void list() throws Exception {
        //使用mock mvc 进行测试
        //构建请求参数
        ChannelDto channelDto=new ChannelDto();
        channelDto.setName("v");
        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/api/v1/adchannel/list");
        post.contentType(MediaType.APPLICATION_JSON_VALUE);
        post.content(objectMapper.writeValueAsBytes(channelDto));
        mockMvc.perform(post).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

    }


}
