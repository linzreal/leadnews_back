package com.heima.user.service.impl;

import com.heima.common.constants.UserFollowConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.UserFollowDto;
import com.heima.user.service.UserFollowService;
import com.heima.utils.thread.UserThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class UserFollowServiceImpl implements UserFollowService {


    @Autowired
    private CacheService cacheService;


    @Override
    public ResponseResult follow(UserFollowDto dto) {

        if(dto == null || dto.getAuthorId() == null || dto.getOperation() == null ||dto.getOperation()<0 || dto.getOperation() >1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        Integer userId = UserThreadLocalUtil.getUser().getId();

        String followeeKey = UserFollowConstants.PREFIX_FOLLOWEE+"_"+userId;
        String followerKey = UserFollowConstants.PREFIX_FOLLOWER+"_"+dto.getAuthorId();

        if(dto.getOperation().equals(UserFollowConstants.FOLLOW_OPERATION)){


            if(cacheService.zScore(followeeKey,String.valueOf(dto.getAuthorId())) != null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            cacheService.zAdd(followeeKey, String.valueOf(dto.getAuthorId()),System.currentTimeMillis());
            cacheService.zAdd(followerKey,String.valueOf(userId),System.currentTimeMillis());
        } else {
            if(cacheService.zScore(followeeKey,String.valueOf(dto.getAuthorId())) == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            cacheService.zRemove(followeeKey,String.valueOf(dto.getAuthorId()));
            cacheService.zRemove(followerKey,String.valueOf(userId));
        }


        return ResponseResult.okResult(dto);

    }
}
