package com.usian.aips.behavior;

import com.usian.model.behavior.pojos.ApBehaviorEntry;

/**
 * @program: usian-leadnews
 * @description: ApBehaviorEntryControllerApi
 * @author: wangheng
 * @create: 2022-08-26 14:59
 **/
public interface ApBehaviorEntryControllerApi {
    //根据user获取实体信息
    ApBehaviorEntry findApBehaviorEntryByUserId(Integer id, Integer equipmentId);
}
