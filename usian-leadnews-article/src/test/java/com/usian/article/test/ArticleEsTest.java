package com.usian.article.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.article.ArticleApp;
import com.usian.article.mapper.ApArticleContentMapper;
import com.usian.article.service.ApArticleService;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleContent;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: ArticleEsTest
 * @author: wangheng
 * @create: 2022-09-01 19:33
 **/
@SpringBootTest(classes = ArticleApp.class)
@RunWith(SpringRunner.class)
public class ArticleEsTest {
    @Autowired
    private ApArticleService articleService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Test
    public void testImportAll() throws IOException {

        List<ApArticle> list = articleService.list();
        for (ApArticle apArticle : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", apArticle.getId());
            map.put("publishTime", apArticle.getPublishTime());
            map.put("layout", apArticle.getLayout());
            map.put("images", apArticle.getImages());
            map.put("authorId", apArticle.getAuthorId());
            map.put("title", apArticle.getTitle());
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, apArticle.getId()));
            if(apArticleContent != null){
                map.put("content",apArticleContent.getContent());
            }
            IndexRequest indexRequest = new IndexRequest("app_info_article","doc").id(apArticle.getId().toString()).source(map);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }
}
