package com.usian.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.search.dtos.UserSearchDto;
import com.usian.search.service.ArticleSearchService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: ArticleSearchServiceImpl
 * @author: wangheng
 * @create: 2022-09-01 19:50
 **/
@Service
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * App端文章搜索
     * @param dto
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult search(UserSearchDto dto) throws IOException {
        //检查参数
        if(dto == null || StringUtils.isBlank(dto.getSearchWords())){
            ExceptionCast.cast(1,"参数错误");
        }
        //2.从es索引库中检索数据 //构建请求对象   /app_info_article/doc/_serach
        SearchRequest searchRequest=new SearchRequest("app_info_article").types("doc");
        //条件构造器 {}
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //构建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //根据关键字进行查询
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords().trim().replace(" ", "")).field("title").field("content").defaultOperator(Operator.OR);
        boolQueryBuilder.must(queryStringQueryBuilder); //进行绑定 使用and 绑定

        if(dto.getLayout()!=null){
            TermQueryBuilder layout = QueryBuilders.termQuery("layout", dto.getLayout());
            boolQueryBuilder.must(layout);
        }

        //继续进行别的条件处理
        RangeQueryBuilder publishTime = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime());
        boolQueryBuilder.filter(publishTime);
        searchSourceBuilder.query(boolQueryBuilder);
        //进行倒叙
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);
        //分页
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(dto.getPageSize());
        //分组查询
        //分组查询 //查询分类
        TermsAggregationBuilder field = AggregationBuilders.terms("layout").field("layout");
        searchSourceBuilder.aggregation(field);
        //高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("title").preTags("<font style='color:red'>").postTags("</font>");
        highlightBuilder.field("content").preTags("<font style='color:red'>").postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //封装
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //对返回结果进行封装
        List<Map> list=new ArrayList<>();
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            Map map = JSONObject.parseObject(hit.getSourceAsString(), Map.class);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Text[] fragments = title.fragments();
            map.put("title",fragments[0].toString());//替换高亮

            HighlightField content = highlightFields.get("content");
            Text[] fragments1 = content.fragments();
            map.put("content",fragments1[0].toString());//替换高亮

            list.add(map);
        }
        //获取分组查询的数据
        Aggregations aggregations = search.getAggregations();
        Map<String, Aggregation> asMap = aggregations.getAsMap();
        Terms terms=(Terms)asMap.get("layout");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<String> layoutList=new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            layoutList.add(bucket.getKeyAsString());
        }
        Map map=new HashMap();
        map.put("layoutList",layoutList);
        list.add(map);
        return ResponseResult.okResult(list);
}
}
