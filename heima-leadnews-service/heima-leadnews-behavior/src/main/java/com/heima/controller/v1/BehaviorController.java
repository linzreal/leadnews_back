package com.heima.controller.v1;

import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.behavior.dtos.UnlikeBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.service.BehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BehaviorController {

    @Autowired
    private BehaviorService behaviorService;

    @PostMapping("/read_behavior")
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDto dto){
        return behaviorService.readBehavior(dto);
    }

    @PostMapping("/likes_behavior")
    public ResponseResult likesBehavior(@RequestBody LikesBehaviorDto dto){
        return behaviorService.likesBehavior(dto);
    }

    @PostMapping("/un_likes_behavior")
    public ResponseResult unLikesBehavior(@RequestBody UnlikeBehaviorDto dto){
        return behaviorService.unlikedBehavior(dto);
    }




}
