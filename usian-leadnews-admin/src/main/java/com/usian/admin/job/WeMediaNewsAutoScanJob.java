package com.usian.admin.job;

import com.usian.admin.feign.WemediaFeign;
import com.usian.admin.service.WemediaNewsAutoScanService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: WeMediaNewsAutoScanJob
 * @author: wangheng
 * @create: 2022-08-23 11:21
 **/
@Component
@Log4j2
public class WeMediaNewsAutoScanJob {
    @Autowired
    private WemediaFeign wemediaFeign;
    @Autowired
    private WemediaNewsAutoScanService wemediaNewsAutoScanService;
    @XxlJob("weMediaNewsAutoScanJob")
    public ReturnT<String> test(String param) throws Exception {
        List<Integer> release = wemediaFeign.findRelease();
        if(release!=null&&release.size()>0){
            for (Integer integer : release) {
                System.out.println("符合要求，开始发布");
                wemediaNewsAutoScanService.autoScanByMediaNewsId(integer);
            }
        }
        System.out.println("进行了任务"+new Date());
        return ReturnT.SUCCESS;
    }
}
