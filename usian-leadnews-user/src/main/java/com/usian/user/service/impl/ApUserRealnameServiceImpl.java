package com.usian.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.dtos.AuthDto;
import com.usian.model.user.pojos.ApUserRealname;
import com.usian.user.mapper.ApUserRealnameMapper;
import com.usian.user.service.ApUserRealnameService;
import com.usian.user.utils.YongYouApi;
import com.usian.user.utils.YongyouHttpApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: ApUserRealnameService
 * @author: wangheng
 * @create: 2022-08-08 16:25
 **/
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {

    @Autowired
    private ApUserRealnameService apUserRealnameService;

    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {
        //根据状态来查询 用户实名列表
        if (dto == null) {
            ExceptionCast.cast(2, "参数不能为空,参数非法");
        }
        //分页信息初始化
        dto.checkParam();
        //构建查询
        LambdaQueryWrapper<ApUserRealname> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dto.getStatus() != null, ApUserRealname::getStatus, dto.getStatus());
        //开启分页
        Page<ApUserRealname> page = new Page<>(dto.getPage(), dto.getSize());
        this.page(page, wrapper);
        ResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    @Override
    public ResponseResult AutoUpdateStatus(AuthDto dto) {
        boolean falg=true;
        //先根据id查询用户的信息
        if (dto == null) {
            ExceptionCast.cast(1, "参数不合法");
        }
        if (dto.getId() == null) {
            ExceptionCast.cast(1, "id不能为空");
        }
        //进行查询用户实名信息
        ApUserRealname byId = this.getById(dto.getId());
        if (byId == null) {
            ExceptionCast.cast(1, "用户不存在");
        }
        if (byId.getStatus() != 1) {//1 状态不为1  代表 不能审核
            ExceptionCast.cast(1, "审核状态错误");
        }
        //进行身份的验证
        //首先进行身份证证明ocr 识别
        //然后将识别的结果 进行身份证二要素验证 验证
        //验证成功 进入 活体检测  验证识别直接修改状态为验证识别 id为 8

        System.out.println("进行身份认证ocr");
        //进行身份证的orc识别
        //ocr提交信息
        Map map=new HashMap();
        map.put("image",byId.getFontImage());
        map.put("imageType","URL");
        map.put("ocrType","0");
        //ocr返回结果
        Map ocrMap = YongYouApi.conn(YongyouHttpApi.OCRURL, YongyouHttpApi.OCRCODE, map);
        //转换为map对象
        //String s = "{\"message\":\"成功\",\"data\":{\"tradeNo\":\"22080820005558019\",\"code\":\"0\",\"riskType\":\"normal\",\"address\":\"沈阳市东陵区文化东路24-8号1-3-6\",\"birth\":\"19510322\",\"name\":\"王东镇\",\"cardNum\":\"210103195103222113\",\"sex\":\"男\",\"nation\":\"汉\",\"issuingDate\":\"\",\"issuingAuthority\":\"\",\"expiryDate\":\"\"},\"code\":\"601200000\"}";
        System.out.println("ocr识别结果:" + ocrMap);
        //Map ocrMap = JSONObject.parseObject(s, Map.class);
        //判断ocr识别是否成功
        String code = (String) ocrMap.get("code");
        if (code.equals("601200000")){// 进入身份证二要素验证
            System.out.println("进行二要素识别");
            //获取里面的具体值
            Map data = (Map) ocrMap.get("data");
            //获取到name
            String name = (String) data.get("name");
            //获取到身份证号
            String cardNum = (String) data.get("cardNum");
            //身份证二要素提交信息
            map=new HashMap();
            map.put("idNumber",cardNum);
            map.put("userName",name);
            //调用身份证二要素验证 api
            Map mMap = YongYouApi.conn(YongyouHttpApi.MATCHIDENTITY, YongyouHttpApi.MATCHIDENTITYCODE, map);
            //String matchIdentityStr = "{\"success\":true,\"code\":400100,\"message\":\"一致\",\"data\":{\"orderNumber\":\"021659962888389491\"}}";
            System.out.println("二要素识别结果:" + mMap);
            //获取二要素的信息
            Boolean success = (Boolean) mMap.get("success");
            String mmessage = (String) mMap.get("message");
            if (success && mmessage.equals("一致")) {
                //二要素验证成功开始 活体检测
                System.out.println("开始活体检验");
                //活体检验提交信息
                map=new HashMap();
                map.put("image",byId.getLiveImage());
                map.put("imageType","URL");
                Map bMap = YongYouApi.conn(YongyouHttpApi.BIOLOGICALEXAMINATION, YongyouHttpApi.BIOLOGICALEXAMINATIONCODE, map);
                //获取活体返回结果
                //String biologicalexaminationStr = "{\"message\":\"成功\",\"data\":{\"checkStatus\":\"0\",\"score\":\"84\",\"tradeNo\":\"22080820581666222\",\"remark\":\"检测成功\",\"code\":\"0\"},\"code\":\"601200000\"}";
                System.out.println("活体检测识别结果:" + bMap);
                //对活体检测结果进行处理
                //获取返回结果
                String bcode = (String) bMap.get("code");
                //获取结果体信息
                Map bdate = (Map) bMap.get("data");
                //获取活体分值
                Integer bscore = Integer.parseInt((String) bdate.get("score"));
                String remark = (String) bdate.get("remark");
                if (bcode.equals("601200000") && bscore >= 80 && remark.equals("检测成功")) {
                    System.out.println("开始人证核验");
                    //人证核验提交信息
                    map=new HashMap();
                    map.put("idCardImage",byId.getFontImage());
                    map.put("imageType","URL");
                    map.put("liveImage",byId.getLiveImage());
                    //进行人证核验
                    Map cMap = YongYouApi.conn(YongyouHttpApi.COMPARISONSOFPERSONSANDDOCUMENTS, YongyouHttpApi.COMPARISONSOFPERSONSANDDOCUMENTSCODE, map);
                    //String cfdStr = "{\"message\":\"成功\",\"data\":{\"tradeNo\":\"22080821483274754\",\"score\":\"24.60038376\",\"remark\":\"比对成功\",\"code\":\"0\"},\"code\":\"601200000\"}";
                    //对认证核验结果进行修改
                    System.out.println("人证核验结果:" + cMap);
                    String ccode = (String) cMap.get("code");
                    Map cmap1 = (Map) cMap.get("data");
                    String score = (String) cmap1.get("score");
                    double parseDouble = Double.parseDouble(score);
                    System.out.println();
                    if (ccode.equals("601200000") && parseDouble >= 10.00) {
                        System.out.println("人证核验成功审核成功");
                    } else {
                        falg=false;
                    }

                } else {
                    falg=false;
                }
            } else {
                falg=false;
            }
        } else {
            falg=false;
        }
        if(falg){
            System.out.println("人证核验成功审核成功");
            byId.setStatus((short) 9);
            this.updateById(byId);
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }else{
            System.out.println("审核失败");
            byId.setStatus((short) 8);
            this.updateById(byId);
            ExceptionCast.cast(1, "校验失败");
        }

        return null;
    }
}
