package com.usian.admin.controller.v1;

import com.usian.admin.service.AdChannelService;
import com.usian.aips.admin.AdChannelControllerApi;
import com.usian.common.web.admin.AdminTokenFilter;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.admin.pojos.AdUser;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.utils.threadlocal.AdminThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;

/**
 * @program: usian-leadnews
 * @description: AdChannelController
 * @author: wangheng
 * @create: 2022-08-03 16:09
 **/
@RestController
@RequestMapping("/api/v1/channel")
public class AdChannelController implements AdChannelControllerApi {
    Logger logger = LoggerFactory.getLogger(AdChannelController.class);

    @Autowired
    private AdChannelService adChannelService;
    @Override
    @PostMapping("/list")
    public ResponseResult findByNameAndPage(@RequestBody ChannelDto channelDto) {
        AdUser user = AdminThreadLocalUtils.getUser();
        logger.info("从当前线程中获取到对象:{}",user.toString());
        //获取到前端传递过来的参数 通过json 传递 对象
        return adChannelService.findByNameAndPage(channelDto);
    }

    @Override
    @GetMapping("/del/{id}")
    public ResponseResult deleteById(@PathVariable Integer id) {
        //获取到id 进行删除
        return adChannelService.deleteById(id);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdChannel adChannel) {
        //通过json格式进行传输数据

        return adChannelService.update(adChannel);
    }

    @Override
    @PostMapping("/save")
    public ResponseResult save(@RequestBody AdChannel adChannel) {
        return adChannelService.save(adChannel);
    }

    @Override
    @GetMapping("/findByIdChannel/{id}")
    public ResponseResult findByIdChannel(@PathVariable Integer id) {
        return adChannelService.findByIdChannel(id);
    }
}
