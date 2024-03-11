package com.heima.service;

import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.behavior.dtos.UnlikeBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.ResponseBody;

public interface BehaviorService {
    public ResponseResult likesBehavior(LikesBehaviorDto dto);

    public ResponseResult readBehavior(ReadBehaviorDto dto);


    public ResponseResult unlikedBehavior(UnlikeBehaviorDto dto);
}
