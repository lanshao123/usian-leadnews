package com.usian.admin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usian.admin.feign.ArticleFeign;
import com.usian.admin.feign.WemediaFeign;
import com.usian.admin.mapper.AdChannelMapper;
import com.usian.admin.mapper.AdSensitiveMapper;
import com.usian.admin.service.WemediaNewsAutoScanService;
import com.usian.common.aliyun.GreeTextScan;
import com.usian.common.aliyun.GreenImageScan;
import com.usian.common.exception.ExceptionCast;
import com.usian.common.fastdfs.FastDFSClientUtil;
import com.usian.model.admin.dtos.NewsAuthDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.article.pojos.ApArticle;
import com.usian.model.article.pojos.ApArticleConfig;
import com.usian.model.article.pojos.ApArticleContent;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmUser;
import com.usian.model.media.vos.WmNewsVo;
import com.usian.utils.common.SensitiveWordUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @program: usian-leadnews
 * @description: WemediaNewsAutoScanServiceImpl
 * @author: wangheng
 * @create: 2022-08-19 14:32
 **/
@Service
@SuppressWarnings("ALL")
public class WemediaNewsAutoScanServiceImpl implements WemediaNewsAutoScanService {
    @Autowired
    private WemediaFeign wemediaFeign;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private AdSensitiveMapper adSensitiveMapper;
    @Autowired
    private AdChannelMapper adChannelMapper;
    @Autowired
    private GreeTextScan greeTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 自媒体文章自动审核接口
     * @param id
     */
    @Override
    @GlobalTransactional
    public void autoScanByMediaNewsId(Integer id) throws Exception {
        if(id==null){
            ExceptionCast.cast(1,"id不能为空");
        }
        //根据id查询自媒体文章信息
        ResponseResult res = wemediaFeign.findById(id);
        if(res.getData()==null){
            ExceptionCast.cast(1,"文章不存在");
        }
        String listTxt = JSONArray.toJSONString(res.getData());
        WmNews wmNews = JSONObject.parseObject(listTxt, WmNews.class);
        if (wmNews.getStatus()==4) {
            //4 人工审核通过
            //直接保存
            saveAppArticle(wmNews);
            return;
        }
        //判断文章状态  8 ==审核通过待发布 ，发布时间小于等于现在时间表示可以直接发布文章
        if(wmNews.getPublishTime()!=null){
            if (wmNews.getStatus()==8&&wmNews.getPublishTime().getTime()<=System.currentTimeMillis()) {
                saveAppArticle(wmNews);
                return;
            }
        }

        //状态为1  提交待审核 需要自动审核
        if(wmNews.getStatus()==1){
            Map<String, Object> contentAndImagesResult =handleTextAndImages(wmNews);
            //contentAndImagesResult
            //进行文本审核
            String content=(String) contentAndImagesResult.get("content");
            boolean textScanBoolean =handleTextScan(content,wmNews);
            //文本审核失败 直接结束方法
            if(!textScanBoolean){
                return;
            }
            //开始图片审核
            List<String> list=(List) contentAndImagesResult.get("images");
            boolean imageScanBoolean =handleImagesScan(list,wmNews);
            //审核直接 直接结束方法
            if(!imageScanBoolean){
                return;
            }
            //敏感词审核
            boolean sensitiveScanBoolean = handleSensitive(content, wmNews);
            //审核直接 直接结束方法
            if(!sensitiveScanBoolean){
                return;
            }
            //最后都审核通过判断一下发布时间
            if(wmNews.getPublishTime()!=null){
                if (wmNews.getPublishTime().getTime()>System.currentTimeMillis()) {
                    //审核时间大于现在时间 ，就是审核通过待发布
                    updateWmNews(wmNews,(short) 8,"审核通过，待发布");
                    //发送延迟队列消息
                    //发布时间减去现在时间 大于0就代表待发布
                        long time =wmNews.getPublishTime().getTime() - System.currentTimeMillis();
                        System.out.println("剩余发布时间："+time);
                        if(time>0){
                            //发送消息
                            //创建消息
                            Message message = MessageBuilder
                                    .withBody(wmNews.getId().toString().getBytes())
                                    .setHeader("x-delay",time)
                                    .build();
                            // 发送消息
                            rabbitTemplate.convertAndSend("delayedAuthExchange", "auth", message);
                        }

                    return;
                }
            }
            //如果审核通过，直接发布文章
            saveAppArticle(wmNews);
        }


    }

    @Override
    /**
     * 人工手动审核
     */
    public ResponseResult newsAuthScan(NewsAuthDto newsAuthDto) {
        //判断参数
        if(newsAuthDto==null){
            ExceptionCast.cast(1,"参数错误");
        }
        //根据id查询出来文章信息，然后修改
        ResponseResult byId = wemediaFeign.findById(newsAuthDto.getId());
        if(byId.getData()==null){
            ExceptionCast.cast(1,"文章不存在");
        }
        String listTxt = JSONArray.toJSONString(byId.getData());
        WmNews wmNews = JSONObject.parseObject(listTxt, WmNews.class);
        if (newsAuthDto.getMsg()==null) {
            updateWmNews(wmNews,(short) 4,"审核通过");
            try {
                autoScanByMediaNewsId(newsAuthDto.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            updateWmNews(wmNews,(short) 2,newsAuthDto.getMsg());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult findOne(Integer id) {
        //1参数检查
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2查询数据
        WmNewsVo wmNewsVo = wemediaFeign.findWmNewsVo(id);
        //结构封装
        ResponseResult responseResult = ResponseResult.okResult(wmNewsVo);
        return responseResult;
    }

    @Override
    /**
     * 查询媒体列表
     */
    public PageResponseResult list(NewsAuthDto newsAuthDto) {
        if(newsAuthDto==null&& newsAuthDto.getTitle()!=null){
            ExceptionCast.cast(1,"参数错误");
        }
        //然后查询
        return wemediaFeign.findList(newsAuthDto);
    }

    /**
     * 敏感词审核管理
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleSensitive(String content, WmNews wmNews) {
        //获取所有敏感词 通过DFA算法进行处理
        List<String> allSensitive = adSensitiveMapper.findAllSensitive();
        SensitiveWordUtil.initMap(allSensitive);
        //文章内容自管理敏感词过滤
        Map<String, Integer> stringIntegerMap = SensitiveWordUtil.matchWords(content);
        if(stringIntegerMap.size()>0){
            updateWmNews(wmNews, (short) 2, "文章中包含了敏感词");
            return false;
        }
        return true;
    }

    /**
     * 进行图片审核
     * @param list
     * @param wmNews
     * @return
     */
    private boolean handleImagesScan(List<String> list, WmNews wmNews) throws Exception {
        if(list==null){
            return true;
        }
        List<byte[]> imageList = new ArrayList<>();
        for (String s : list) {
            //http://192.168.211.132:8080/group1/M00/00/00/wKjThGL8udCAVJzyAAABRD4S5Vk181.png
            String replace = s.replace("http://192.168.211.132:8080/", "");
            int i = replace.indexOf("/");
            String groupName=replace.substring(0,i);
            String imagePath=replace.substring(i+1);
            byte[] download = fastDFSClientUtil.download(groupName, imagePath);
            imageList.add(download);
            //把所有图片都转换成二进制流的格式
        }
        //开始审核图片
        Map map = greenImageScan.imageScan(imageList);
        //图片审核不通过
        if (!map.get("suggestion").equals("pass")) {
            //判断具体不通过类型
            if (map.get("suggestion").equals("block")) {
                updateWmNews(wmNews,(short) 2,"图片违规");
                return false;
            }
            if (map.get("suggestion").equals("review")) {
                updateWmNews(wmNews,(short) 3,"图片不确定需要人工审核");
                return false;
            }
        }
        return true;
    }

    /**
     * 文本审核
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content, WmNews wmNews) throws Exception {
        Map map = greeTextScan.greeTextScan(content);
        //审核不通过
        if(!map.get("suggestion").equals("pass")){
            //判断审核失败状态
            if(map.get("suggestion").equals("block")){
                updateWmNews(wmNews,(short)2,"文章中有敏感词汇");
                return false;
            }
            if(map.get("suggestion").equals("review")){
                updateWmNews(wmNews,(short)3,"文章需要人工审核");
                return false;
            }
        }
        return true;
    }

    /**
     * 提取文本内容和图片
     *
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        //获取到文本中的内容
        String content = wmNews.getContent();
        StringBuilder sb=new StringBuilder();
        List<String> images=new ArrayList<>();
        //把文章的转换为map对象 进行判断
        List<Map> list = JSONArray.parseArray(content, Map.class);
        for (Map map : list) {
            //如果是文本类型
            if (map.get("type").equals("text")) {
                //获取到内容追加到sb中
                String value = (String) map.get("value");
                sb.append(value);
            }
            //如果是图片类型
            if (map.get("type").equals("image")) {
                //获取到图片url加入到集合中
                String image = (String) map.get("value");
                images.add(image);
            }
        }
        //在对封面的图片进行处理 封面图片不为空，并且不是无图模式
        if(wmNews.getImages()!=null&&wmNews.getType()!=0){
            String[] split = wmNews.getImages().split(",");
            for (String s : split) {
                images.add(s);
            }
        }
        //封装 返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("content", sb.toString());
        resultMap.put("images", images);
        return resultMap;

    }

    /**
     * 保存自媒体文章
     * @param wmNews
     */
    private void saveAppArticle(WmNews wmNews) {
        //保存app文章
        ApArticle apArticle = saveArticle(wmNews);
        //保存app文章配置
        saveArticleConfig(apArticle);
        //保存app文章内容
        saveArticleContent(apArticle,wmNews);
        wmNews.setArticleId(apArticle.getId());

        //修改自媒体文章的状态为9
        updateWmNews(wmNews,(short)9,"审核通过");
        //TODO 后续需要创建es索引库

    }

    /**
     * 修改文章状态
     * @param wmNews
     * @param i
     * @param msg
     */
    private void updateWmNews(WmNews wmNews, short i, String msg) {
        if(i==3)
            rabbitTemplate.convertAndSend("EMAILExchange","email",JSONObject.toJSONString(wmNews));
        wmNews.setStatus(i);
        wmNews.setReason(msg);
        wemediaFeign.updateWmNews(wmNews);
    }

    /**
     * 保存文章内容
     * @param apArticle
     * @param wmNews
     */
    private void saveArticleContent(ApArticle apArticle, WmNews wmNews) {
        ApArticleContent apArticleContent=new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(wmNews.getContent());
        articleFeign.saveArticleContent(apArticleContent);
    }

    /**
     * 保存文章配置信息
     * @param apArticle
     */
    private void saveArticleConfig(ApArticle apArticle) {
        ApArticleConfig apArticleConfig=new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsForward(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsComment(true);
        articleFeign.saveArticleConfig(apArticleConfig);

    }

    /**
     * 保存文章到 ap_article 表中
     * @param wmNews
     * @return
     */
    private ApArticle saveArticle(WmNews wmNews) {
        ApArticle apArticle = new ApArticle();
        apArticle.setTitle(wmNews.getTitle());
        apArticle.setLayout(wmNews.getType());
        apArticle.setImages(wmNews.getImages());
        apArticle.setCreatedTime(new Date());
        //获取作者相关信息
        Integer userId = wmNews.getUserId();
        //通过userid 去查询WmUser 表中的自媒体用户信息
        WmUser wmUser = wemediaFeign.findWmUserById(userId.longValue());
        if(wmUser!=null){
            String name = wmUser.getName();
            //根据name查询作者信息
            ApAuthor apAuthor = articleFeign.selectAuthorByName(name);
            if(apAuthor!=null){
                apArticle.setAuthorId(apAuthor.getId().longValue());
                apArticle.setAuthorName(apAuthor.getName());
            }
        }
        //获取频道相关信息
        Integer channelId = wmNews.getChannelId();
        AdChannel adChannel = adChannelMapper.selectById(channelId);
        if(adChannel!=null){
            apArticle.setChannelName(adChannel.getName());
            apArticle.setChannelId(adChannel.getId());
        }
        apArticle.setPublishTime(new Date());
        //最后远程调用保存接口
        return articleFeign.saveArticle(apArticle);
    }
}
