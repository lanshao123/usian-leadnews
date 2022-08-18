package com.usian.admin.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usian.admin.AdminApp;
import com.usian.admin.feign.ArticleFeign;
import com.usian.admin.feign.WemediaFeign;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.article.pojos.ApArticleContent;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: DemoTest
 * @author: wangheng
 * @create: 2022-08-18 20:28
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {
    @Autowired
    ArticleFeign articleFeign;
    @Autowired
    private WemediaFeign wemediaFeign;
    @Test//根据name查询文章作者
    public void selectAuthorByName(){
        ApAuthor apAuthor = articleFeign.selectAuthorByName("zhangsan");
        System.out.println(apAuthor);
    }
    @Test//保存文章配置信息
    public void saveArticleConfig(){
        ApArticleConfig apArticleConfig=new ApArticleConfig();
        apArticleConfig.setArticleId(1246808139941122147L);
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(true);
        apArticleConfig.setIsForward(true);
        ResponseResult responseResult = articleFeign.saveArticleConfig(apArticleConfig);
        System.out.println(responseResult);
    }
    @Test//保存文章
    public void saveArticle(){
        ApArticle apArticle=new ApArticle();
        apArticle.setTitle("wh11程序员");
        apArticle.setAuthorId(7L);
        apArticle.setAuthorName("zhangsan");
        apArticle.setChannelId(1);
        apArticle.setChannelName("java");
        apArticle.setLayout((short) 1);
        apArticle.setImages("http://192.168.200.130/group1/M00/00/00/wKjIgl5sw0-AYZ1KAAMOBhRxa98094.png,http://192.168.200.130/group1/M00/00/00/wKjIgl5swbGATaSAAAEPfZfx6Iw790.png,http://192.168.200.130/group1/M00/00/00/wKjIgl5sm0SAeTNuAACZDHhWRnc981.png");
        apArticle.setCreatedTime(new Date());
        apArticle.setPublishTime(new Date());
        apArticle.setSyncStatus(true);
        apArticle.setOrigin(true);
         apArticle = articleFeign.saveArticle(apArticle);
        System.out.println(apArticle);
    }
    @Test//保存文章主体内容
    public void saveArticleContent(){
        ApArticleContent apArticleContent=new ApArticleContent();
        apArticleContent.setArticleId(1246808139941122045l);
        apArticleContent.setContent("[{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl5sw0-AYZ1KAAMOBhRxa98094.png\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl5swbGATaSAAAEPfZfx6Iw790.png\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl5sm0SAeTNuAACZDHhWRnc981.png\"},{\"type\":\"image\",\"value\":\"http://192.168.200.130/group1/M00/00/00/wKjIgl5smzOABUgcAADtgkclyT8222.png\"},{\"type\":\"text\",\"value\":\"请输入文...\"}]");
        ResponseResult responseResult = articleFeign.saveArticleContent(apArticleContent);
        System.out.println(responseResult.toString());
    }

    @Test
    public void findById(){
        ResponseResult byId = wemediaFeign.findById(6181);
        System.out.println(byId);
        System.out.println(byId.getData().toString());
        //先根据id查询文章出来，
        String listTxt = JSONArray.toJSONString(byId.getData());
        WmNews wmNews = JSONObject.parseObject(listTxt, WmNews.class);
        System.out.println(wmNews);
        wmNews.setTitle("11修改成功");
        ResponseResult responseResult = wemediaFeign.updateWmNews(wmNews);
        System.out.println(responseResult);
    }
    @Test
    public void findOne(){
        WmUser wmUserById = wemediaFeign.findWmUserById(1120l);
        System.out.println(wmUserById);

    }
}
