package com.usian.comment.feign;

import com.usian.model.user.pojos.ApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("leadnews-user")
public interface UserFeign {
    @GetMapping("/api/v1/user/{id}")
    ApUser findUserById(@PathVariable("id") Long id);
}
