package com.usian.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
public class JavaDemo {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
     
    public static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
     
    //配置您申请的KEY
    public static final String APICODE ="766be96f47804bc2bfe6772b3b2b72dc";
 

 
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map<String,Object> params, Map<String,Object> headerParams,String method, String paramFormat) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            String contentType = null;
            if(headerParams.containsKey("Content-Type"))
                contentType = headerParams.get("Content-Type").toString();
                 
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
             
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
             
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            for (String i : headerParams.keySet()) {
                conn.setRequestProperty(i, headerParams.get(i).toString());
            }
            if("form".equals(paramFormat) && !"application/x-www-form-urlencoded".equals(contentType) && !"application/xml".equals(contentType)) {
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            }
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    OutputStream out = conn.getOutputStream();
                    if("form".equals(paramFormat)) {
                        if("application/x-www-form-urlencoded".equals(contentType))
                            out.write(urlencode(params).getBytes("utf-8"));
                        else if("application/xml".equals(contentType))
                            out.write(xmlencode(params).getBytes("utf-8"));
                        else
                            out.write(jsonencode(params).getBytes("utf-8"));
                    } else
                        out.write(params.toString().getBytes("utf-8"));
                     
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
     
    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                if(("").equals(i.getKey())) {
                    sb.append(URLEncoder.encode(i.getValue()+"","UTF-8"));
                } else {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
     
     //将map型转为请求参数型
    public static String jsonencode(Map<String,Object>data) throws JSONException {
        JSONObject jparam = new JSONObject();
        for (Map.Entry i : data.entrySet())
            jparam.put((String) i.getKey(), i.getValue());
          
        return jparam.toString();
    }
     
    //将map型转为请求参数型
    public static String xmlencode(Map<String,Object>data) {
        StringBuffer xmlData = new StringBuffer(); 
        xmlData.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        for (Map.Entry i : data.entrySet())
             xmlData.append("<" + i.getKey() + ">" + i.getValue() + "</" + i.getKey() + ">");
         
        return xmlData.toString();
    }
     
        static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
 
        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }
 
        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }
 
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
 
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
 
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    //1.身份证OCR
    public static  String IdcardOCR (){
        String result =null;
        String url ="https://api.yonyoucloud.com/apis/dst/IdcardOCR/IdcardOCR";//请求接口地址
        String method = "POST";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        params.put("image", "https://hmtt122.oss-cn-shanghai.aliyuncs.com/demo_idcard.png");
        params.put("ocrType", "0");
        params.put("imageType", "URL");

        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("apicode", APICODE);//APICODE
        headerParams.put("Content-Type", "application/json");

        try {
            result = net(url, params, headerParams, method, paramFormat);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //2.身份证二要素验证
    public static String matchIdentity (String idNumber,String userName){
        String result =null;
        String url ="https://api.yonyoucloud.com/apis/dst/matchIdentity/matchIdentity";//请求接口地址
        String method = "POST";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        params.put("idNumber", idNumber);
        params.put("userName",userName);

        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("apicode", "8596ad1a3021478fa3e4ec9d012ea7bd");//APICODE
        headerParams.put("Content-Type", "application/json");

        try {
            result = net(url, params, headerParams, method, paramFormat);
           return  result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        //这个是调用了身份证OCR 识别身份证信息
        //String request = IdcardOCR ();
        //System.out.println(request);
        //然后取到ocr的识别结果 进行解析 取到身份证号和姓名 进行身份证二要素验证
        //讲字符串序列化为map对象
        String s="{\"message\":\"成功\",\"data\":{\"tradeNo\":\"22080820005558019\",\"code\":\"0\",\"riskType\":\"normal\",\"address\":\"沈阳市东陵区文化东路24-8号1-3-6\",\"birth\":\"19510322\",\"name\":\"王东镇\",\"cardNum\":\"210103195103222113\",\"sex\":\"男\",\"nation\":\"汉\",\"issuingDate\":\"\",\"issuingAuthority\":\"\",\"expiryDate\":\"\"},\"code\":\"601200000\"}";
       //转换为map对象
        Map map = JSONObject.parseObject(s, Map.class);
        //获取里面的具体值
        Map data = (Map) map.get("data");
        //获取到name
        String name =(String) data.get("name");
        //获取到身份证号
        String cardNum =(String) data.get("cardNum");
        //然后调用身份证二要素验证 api
        String matchIdentityResponse = matchIdentity(cardNum, name);
        System.out.println(matchIdentityResponse);
        // {"success":true,"code":400100,"message":"一致","data":{"orderNumber":"021659961166368022"}}
        // 身份证二要素验证

    }
}