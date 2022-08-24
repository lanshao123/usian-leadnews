package com.usian.admin.controller.v1;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.usian.admin.service.UserLoginService;
import com.usian.model.admin.pojos.AdUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @program: poi-excel
 * @description: AdUserController
 * @author: wangheng
 * @create: 2022-08-20 09:17
 **/
@RestController
@RequestMapping("/api/v1/user")
public class AdUserController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserLoginService adUserService;

    @GetMapping("/export")
    public void excel( HttpServletResponse response) throws Exception {
        String filename="test.csv";
        ServletOutputStream outputStream = response.getOutputStream();

        CSVWriter csvWriter=new CSVWriter(new OutputStreamWriter(outputStream,"GBK"));
        String[] title={"编号","登录用户名","登录密码","盐","昵称"
                ,"头像","手机号","状态","邮箱","最后一次登录时间","创建时间"};
        csvWriter.writeNext(title);
        response.setContentType("text/csv");
        response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        SimpleDateFormat simpleFormatter=new SimpleDateFormat("yyyy-MM-dd");
        int page=1;
        while (true){
            List<AdUser> user = adUserService.findPage(page, 10000);
            if(CollectionUtils.isEmpty(user)){
                break;
            }
            for (AdUser adUser : user) {
                csvWriter.writeNext(new String[]
                        {adUser.getId().toString(),adUser.getName(),adUser.getPassword(),adUser.getSalt(),adUser.getNickname(),
                                adUser.getImage(),adUser.getPhone(),adUser.getStatus().toString(),adUser.getEmail(),simpleFormatter.format(adUser.getLoginTime()) ,simpleFormatter.format(adUser.getCreatedTime())
                        });
            }
            page++;
            csvWriter.flush();
        }
        csvWriter.close();
    }
    @PostMapping("/csv")
    public void csv(@RequestPart("file") MultipartFile multipartFile) throws IOException, ParseException {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");

        String[] title={"编号","登录用户名","登录密码","盐","昵称"
                ,"头像","手机号","状态","邮箱","最后一次登录时间","创建时间"};
        Reader reader = new InputStreamReader(multipartFile.getInputStream(),"GBK");
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).build();
        String[] strings = csvReader.readNext();
        //比较标题信息是否一致
        //(2)	需要对模板字段进行判断，如果和下载的模板不同，需要进行提示
        boolean equals = Arrays.equals(strings, title);
        if (!equals){
            System.out.println("格式不正确");
            return;
        }
        //标题一致
        while (true){
            AdUser adUser=new AdUser();
            String[] array = csvReader.readNext();
            if(array==null){
                break;
            }
            adUser.setName(array[1]);
            adUser.setPassword(array[2]);
            adUser.setSalt(array[3]);
            adUser.setNickname(array[4]);
            adUser.setImage(array[5]);
            adUser.setPhone(array[6]);
            adUser.setStatus(Integer.parseInt(array[7]));
            adUser.setEmail(array[8]);
            adUser.setLoginTime(dateFormat.parse(array[9]));
            adUser.setCreatedTime(dateFormat.parse(array[10]));
            System.out.println(adUser);
            Long set = stringRedisTemplate.opsForSet().add("set", JSONObject.toJSONString(adUser));
            if (set==0) {
                System.out.println("Redis重复");
                //表示有重复的
                continue;
            }
            //进行姓名的判断
            LambdaQueryWrapper<AdUser> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(AdUser::getName, adUser.getName());
            AdUser one = adUserService.getOne(queryWrapper);
            if(one!=null){
                System.out.println("数据库重复");
                //有重复的
                continue;
            }
            //直接添加
            adUserService.save(adUser);
            System.out.println("添加了一条");

        }

    }
    @GetMapping("/excelModel")
    public void excelModel( HttpServletResponse response) throws Exception {
        String filename="excelModel.csv";
        ServletOutputStream outputStream = response.getOutputStream();
        CSVWriter csvWriter=new CSVWriter(new OutputStreamWriter(outputStream,"GBK"));
        String[] title={"编号","登录用户名","登录密码","盐","昵称"
                ,"头像","手机号","状态","邮箱","最后一次登录时间","创建时间"};
        csvWriter.writeNext(title);
        response.setContentType("application/octet-stream");
        response.setHeader("Connection", "close");
        response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            csvWriter.flush();
        csvWriter.close();
    }


}
