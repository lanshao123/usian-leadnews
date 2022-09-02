package com.usian.search.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.search.dtos.UserSearchDto;
import com.usian.model.search.pojos.ApUserSearch;
import com.usian.model.user.pojos.ApUser;
import com.usian.search.feign.BehaviorFeign;
import com.usian.search.mapper.ApUserSearchMapper;
import com.usian.search.service.ApUserSearchService;
import com.usian.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApUserSearchServiceImpl extends ServiceImpl<ApUserSearchMapper, ApUserSearch> implements ApUserSearchService {
    @Autowired
    private BehaviorFeign behaviorFeign;
    @Override
    public ResponseResult findUserSearch(UserSearchDto userSearchDto) {
        //1.检查数据
        if(userSearchDto.getPageSize() > 50){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.查询行为实体
        ApBehaviorEntry apBehaviorEntry = getEntry(userSearchDto);
        if(apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //3.分页查询，默认查询5条数据返回
        IPage pageParam = new Page(0, userSearchDto.getPageSize());
        IPage page = page(pageParam, Wrappers.<ApUserSearch>lambdaQuery().eq(ApUserSearch::getEntryId, apBehaviorEntry.getId())
                .eq(ApUserSearch::getStatus, 1));

        return ResponseResult.okResult(page.getRecords());
    }

    @Override
    public ResponseResult delUserSearch(UserSearchDto userSearchDto) {
        //1.检查参数
        if(userSearchDto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.更新当前搜索记录的状态  status  0
        ApBehaviorEntry apBehaviorEntry = getEntry(userSearchDto);
        if(apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        update(Wrappers.<ApUserSearch>lambdaUpdate().eq(ApUserSearch::getId,userSearchDto.getId()).eq(ApUserSearch::getEntryId,apBehaviorEntry.getId())
                .set(ApUserSearch::getStatus,0));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    @Async("taskExecutor")
    public void insert(Integer entryId, String searchWords) {
        ApUserSearch apUserSearch = getOne(Wrappers.<ApUserSearch>lambdaQuery().eq(ApUserSearch::getEntryId, entryId).eq(ApUserSearch::getKeyword, searchWords));

        //2.如果存在 更新状态
        if(apUserSearch != null && apUserSearch.getStatus() == 1){
            System.out.println("当前关键字已存在，无需再次保存");
            return;
        }else if(apUserSearch != null && apUserSearch.getStatus() == 0){
            apUserSearch.setStatus(1);
            updateById(apUserSearch);
            return;
        }

        //3.如果不存在，保存新的数据
        apUserSearch = new ApUserSearch();
        apUserSearch.setEntryId(entryId);
        apUserSearch.setStatus(1);
        apUserSearch.setKeyword(searchWords);
        apUserSearch.setCreatedTime(new Date());
        save(apUserSearch);
    }
    /**
     * 获取行为实体
     * @param userSearchDto
     * @return
     */
    private ApBehaviorEntry getEntry(UserSearchDto userSearchDto) {
        ApUser user = AppThreadLocalUtils.getUser();
        return behaviorFeign.findByUserIdOrEntryId(user.getId(),userSearchDto.getEquipmentId());
    }
}