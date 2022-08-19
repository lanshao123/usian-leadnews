package com.usian.admin.test;

import com.usian.admin.service.WemediaNewsAutoScanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: usian-leadnews
 * @description: wnNewsTest
 * @author: wangheng
 * @create: 2022-08-19 15:59
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class wnNewsTest {
    @Autowired
    WemediaNewsAutoScanService wemediaNewsAutoScanService;
    @Test
    public void newTest() throws Exception {
        wemediaNewsAutoScanService.autoScanByMediaNewsId(6181);

    }
}
