package com.usian.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.search.dtos.UserSearchDto;
import com.usian.model.user.pojos.ApUser;
import com.usian.search.feign.BehaviorFeign;
import com.usian.search.service.ApUserSearchService;
import com.usian.search.service.ArticleSearchService;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
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
    @Autowired
    private ApUserSearchService apUserSearchService;
    @Autowired
    private BehaviorFeign behaviorFeign;
    /**
     * App???????????????
     * @param dto
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult search(UserSearchDto dto) throws IOException {
        //????????????
        if(dto == null || StringUtils.isBlank(dto.getSearchWords())){
            ExceptionCast.cast(1,"????????????");
        }
        //??????????????????????????????????????????
        if(dto.getFromIndex() == 0){
            ApBehaviorEntry apBehaviorEntry = getEntry(dto);
            if(apBehaviorEntry == null){
               ExceptionCast.cast(1,"????????????");
            }
            apUserSearchService.insert(apBehaviorEntry.getId(),dto.getSearchWords());
        }
        //2.???es???????????????????????? //??????????????????   /app_info_article/doc/_serach
        SearchRequest searchRequest=new SearchRequest("app_info_article").types("doc");
        //??????????????? {}
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //??????????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //???????????????????????????
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords().trim().replace(" ", "")).field("title").field("content").defaultOperator(Operator.OR);
        boolQueryBuilder.must(queryStringQueryBuilder); //???????????? ??????and ??????

        if(dto.getLayout()!=null){
            TermQueryBuilder layout = QueryBuilders.termQuery("layout", dto.getLayout());
            boolQueryBuilder.must(layout);
        }

        //??????????????????????????????
        RangeQueryBuilder publishTime = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime());
        boolQueryBuilder.filter(publishTime);
        searchSourceBuilder.query(boolQueryBuilder);
        //????????????
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);
        //??????
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(dto.getPageSize());
        //????????????
        //???????????? //????????????
        TermsAggregationBuilder field = AggregationBuilders.terms("layout").field("layout");
        searchSourceBuilder.aggregation(field);
        //??????
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("title").preTags("<font style='color:red'>").postTags("</font>");
        highlightBuilder.field("content").preTags("<font style='color:red'>").postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //??????
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //???????????????????????????
        List<Map> list=new ArrayList<>();
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            Map map = JSONObject.parseObject(hit.getSourceAsString(), Map.class);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Text[] fragments = title.fragments();
            map.put("title",fragments[0].toString());//????????????

            HighlightField content = highlightFields.get("content");
            Text[] fragments1 = content.fragments();
            map.put("content",fragments1[0].toString());//????????????

            list.add(map);
        }
        //???????????????????????????
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
    /**
     * ??????????????????
     * @param userSearchDto
     * @return
     */
    private ApBehaviorEntry getEntry(UserSearchDto userSearchDto) {
        ApUser user = AppThreadLocalUtils.getUser();
        return behaviorFeign.findByUserIdOrEntryId(user.getId(),userSearchDto.getEquipmentId());
    }
}
