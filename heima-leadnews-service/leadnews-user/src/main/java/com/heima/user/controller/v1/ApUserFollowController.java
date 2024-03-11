package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserFollowDto;
import com.heima.user.service.UserFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class ApUserFollowController {

    @Autowired
    private UserFollowService userFollowService;
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserFollowDto dto){
        return userFollowService.follow(dto);
    }
}
