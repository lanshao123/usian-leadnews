package com.usian.user.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class YongYouApi {
    public static  Map conn(String url,String apicoede,Map parma){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apicode",apicoede);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8); // json
        HttpEntity<String> formEntry = new HttpEntity<>(JSONObject.toJSONString(parma), httpHeaders); // 封装请求参数
        ResponseEntity<String> response = restTemplate.postForEntity(url, formEntry, String.class);// 发送一个post请求
        String body = response.getBody();
        return JSONObject.parseObject(body,Map.class);
    }
}