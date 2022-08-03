package com.usian.admin.controller.v1;

import com.usian.admin.service.AdChannelService;
import com.usian.aips.admin.AdChannelControllerApi;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;

/**
 * @program: usian-leadnews
 * @description: AdChannelController
 * @author: wangheng
 * @create: 2022-08-03 16:09
 **/
@RestController
@RequestMapping("/admin/api/v1/channel")
public class AdChannelController implements AdChannelControllerApi {
    @Autowired
    private AdChannelService adChannelService;
    @Override
    @PostMapping("/list")
    public ResponseResult findByNameAndPage(@RequestBody ChannelDto channelDto) {
        //获取到前端传递过来的参数 通过json 传递 对象
        return adChannelService.findByNameAndPage(channelDto);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult deleteById(@PathVariable Integer id) {
        //获取到id 进行删除
        return adChannelService.deleteById(id);
    }

    @Override
    @PutMapping("update")
    public ResponseResult update(@RequestBody AdChannel adChannel) {
        //通过json格式进行传输数据

        return adChannelService.update(adChannel);
    }

    @Override
    @PostMapping("save")
    public ResponseResult save(@RequestBody AdChannel adChannel) {
        return adChannelService.save(adChannel);
    }
}
