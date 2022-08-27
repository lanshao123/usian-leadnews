package com.usian.behavior.controller;

import com.usian.aips.behavior.ApBehaviorEntryControllerApi;
import com.usian.behavior.service.ApBehaviorEntryService;
import com.usian.model.behavior.pojos.ApBehaviorEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: usian-leadnews
 * @description: ApBehaviorEntryController
 * @author: wangheng
 * @create: 2022-08-26 15:00
 **/
@RestController
@RequestMapping("/api/v1/behavior_entry")
public class ApBehaviorEntryController implements ApBehaviorEntryControllerApi {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @Override
    @GetMapping("/one")
    public ApBehaviorEntry findApBehaviorEntryByUserId(@RequestParam("id") Integer id,@RequestParam("equipmentId") Integer equipmentId) {
        return apBehaviorEntryService.findByUserIdOrEquipmentId(id,equipmentId);
    }
}
