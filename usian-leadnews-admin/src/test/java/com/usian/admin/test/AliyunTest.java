package com.usian.admin.test;

import com.usian.admin.AdminApp;
import com.usian.common.aliyun.GreeTextScan;
import com.usian.common.aliyun.GreenImageScan;
import com.usian.common.fastdfs.FastDFSClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: AliyunTest
 * @author: wangheng
 * @create: 2022-08-18 19:39
 **/
@SpringBootTest(classes = AdminApp.class)
@RunWith(SpringRunner.class)
public class AliyunTest {
    @Autowired
    private GreeTextScan greeTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;
    @Test
    public void textScan() throws Exception {
        List<String> list=new ArrayList<>();
        list.add("我是王恒");
        list.add("买卖冰毒");
        Map map = greeTextScan.greeTextScan(list);
        System.out.println(map);
    }
    @Test
    public void textImgaes() throws Exception {
        List<byte[]> list=new ArrayList<>();
        //需要从FastDfs读取图片，转换为二进制流数组
        byte[] group1s = fastDFSClientUtil.download("group1", "M00/00/00/wKjThGL84JqAbRfbAAAQOXk1pd0832.jpg");
        list.add(group1s);
        Map map = greenImageScan.imageScan(list);
        System.out.println(map);
    }
}
