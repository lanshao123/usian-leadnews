package com.usian.search.listener;

import com.rabbitmq.client.Channel;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleContent;
import com.usian.search.config.RabbitmqConfig;
import com.usian.search.feign.ApArticleFeign;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @program: usian-leadnews
 * @description: RabbitMQlistener
 * @author: wangheng
 * @create: 2022-08-12 19:19
 **/
@Configuration
@Log4j2
public class RabbitMQListener {
    @Autowired
    private ApArticleFeign apArticleFeign;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @RabbitListener(queues = {RabbitmqConfig.ESQueue})
    public void authQueueListener(String msg, Channel channel, Message mqMsg) throws IOException {
        System.out.println("收到消息:"+msg);
        try {
            //这边需要用feign 远程调用接口，来查询文章信息，文章主题信息，然后封装到map更新到es索引库
            System.out.println("success，手动签收");
            channel.basicAck(mqMsg.getMessageProperties().getDeliveryTag(), false);
            //获取文章信息
            ApArticle one = apArticleFeign.findOne(Long.valueOf(msg));
            //获取文章主体信息
            ApArticleContent contentOne = apArticleFeign.findContentOne(Long.valueOf(msg));
            //es索引创建
            Map<String,Object> map = new HashMap();
            map.put("id",one.getId().toString());
            map.put("publishTime",one.getPublishTime());
            map.put("layout",one.getLayout());
            map.put("images",one.getImages());
            map.put("authorId",one.getAuthorId());
            map.put("title",one.getTitle());
            map.put("content",contentOne.getContent());
            //创建文档添加到索引库中
            org.elasticsearch.action.index.IndexRequest indexRequest =
                    new IndexRequest("app_info_article","doc").id(one.getId().toString()).source(map);
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(index.status().getStatus());


        } catch (Exception e) {
            System.out.println("出现异常，手动签收");
            channel.basicReject(mqMsg.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
        }

    }



}
