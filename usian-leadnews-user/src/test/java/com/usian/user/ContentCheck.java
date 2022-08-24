package com.usian.user;

import com.usian.user.utils.YongYouApi;
import com.usian.user.utils.YongyouHttpApi;
import com.usian.utils.common.AppJwtUtil;
import com.usian.utils.common.Base64Utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.kafka.common.protocol.types.Field;
import org.jcodings.util.Hash;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @program: usian-leadnews
 * @description: ContentCheck
 * @author: wangheng
 * @create: 2022-08-18 11:32
 **/
public class ContentCheck {
    public static void main(String[] args) {
        System.out.println(AppJwtUtil.getToken(1l));
    }
    @Test
    public void contentCheck(){
        //基于rest风格的 api接口形式
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        String str="ad,politics,abuse,porn,contraband,flood";
        params.put("categories", Arrays.asList(str.split(",")));
        ArrayList<String> strList=new ArrayList<String>();
        strList.add("习近平");
        strList.add("汪洋");

        ArrayList list=new ArrayList();
        for (String s : strList) {
            HashMap map=new HashMap();
            map.put("text",s);
            map.put("type","content");
            list.add(map);
        }
        System.out.println(list);
        params.put("items", list);
        Map conn = YongYouApi.conn(YongyouHttpApi.ContentCheck, YongyouHttpApi.ContentCheckCODE, params);
        System.out.println(conn);
    }
    @Test
    public void contentAudit_CompositeServices() throws IOException {
        String base64 = tt();
        System.out.println(base64);
        Map<String, Object> params1 = new HashMap<String, Object>();//请求参数
        params1.put("Politician", "1");
        params1.put("image", "data:image/png;base64,"+base64);
        params1.put("Disgust", "0");
        params1.put("Antiporn", "0");
        params1.put("Quality", "0");
        params1.put("Anti_spam", "0");
        params1.put("Watermark", "0");
        params1.put("Terror", "0");
        Map conn1 = YongYouApi.conn(YongyouHttpApi.ContentAudit_CompositeServices, YongyouHttpApi.ContentAudit_CompositeServicesCode, params1);
        System.out.println(conn1);
    }
    public  String tt() throws IOException {
        HttpGet get=new HttpGet("http://192.168.211.132:8080/group1/M00/00/00/wKjThGL84JqAbRfbAAAQOXk1pd0832.jpg");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        // 从流中获取数据
        byte[] data = new byte[(int) entity.getContentLength()];
        int offset = 0;
        int readBytes = 0;
        do {
            readBytes = inputStream.read(data, offset, 2048);
            offset += readBytes;
        } while (readBytes != -1);

        // Base64 编码
        Base64.Encoder encoder = Base64.getEncoder();
        //String result = Base64Utils.encode(data);
        //result=result.replace("\n\r","");
        String result = encoder.encodeToString(data);


        // 关闭资源
        response.close();
        httpClient.close();
        return result;
    }
}
