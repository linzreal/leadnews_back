package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserFollowDto;

public interface UserFollowService {
    public ResponseResult follow(UserFollowDto dto);
}
