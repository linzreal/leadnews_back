package com.heima.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.BehaviorMessage;
import com.heima.common.constants.LikesBehaviorConstants;
import com.heima.common.constants.ReadBehaviorConstants;
import com.heima.common.constants.UnLikeBehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.vos.BehaviorMessageVo;
import com.heima.model.behavior.dtos.LikesBehaviorDto;
import com.heima.model.behavior.dtos.ReadBehaviorDto;
import com.heima.model.behavior.dtos.UnlikeBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.service.BehaviorService;
import com.heima.utils.thread.BehaviorThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BehaviorServiceImpl implements BehaviorService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult likesBehavior(LikesBehaviorDto dto) {

        if(dto == null || dto.getArticleId() == null || dto.getOperation() == null || dto.getOperation() < 0 || dto.getOperation() >1
        || dto.getType() == null || dto.getType() <0 || dto.getType() >2){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        Integer userId = BehaviorThreadLocalUtil.getUser().getId();

        String likeKey = LikesBehaviorConstants.LIKE_PREFIX+"_"+dto.getType()+"_"+userId;

        String belikedKey = LikesBehaviorConstants.CANCEL_LIKE_PREFIX+"_"+dto.getType()+"_"+dto.getArticleId();

        if(dto.getOperation().equals(LikesBehaviorConstants.OPERATION_LIKE)){

            if(cacheService.zScore(likeKey,String.valueOf(dto.getArticleId())) != null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            cacheService.zAdd(likeKey,String.valueOf(dto.getArticleId()),System.currentTimeMillis());
            cacheService.zAdd(belikedKey,String.valueOf(userId),System.currentTimeMillis());

        } else {

            if(cacheService.zScore(likeKey,String.valueOf(dto.getArticleId())) == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            cacheService.zRemove(likeKey,String.valueOf(dto.getArticleId()));
            cacheService.zRemove(belikedKey,String.valueOf(userId));
        }

        sendBehavoirMessageToKafka(dto.getArticleId(), BehaviorMessageVo.OpinionType.LIKE,dto.getOperation());

        return ResponseResult.okResult(dto);

    }

    private void sendBehavoirMessageToKafka(Long articleId, BehaviorMessageVo.OpinionType opinionType, Integer operation) {
        BehaviorMessageVo  vo = new BehaviorMessageVo();
        vo.setArticleId(articleId);
        vo.setOpinionType(opinionType);
        vo.setType(operation);

        kafkaTemplate.send(BehaviorMessage.topic, JSON.toJSONString(vo));

    }

    @Override
    public ResponseResult readBehavior(ReadBehaviorDto dto) {



        if(dto == null || dto.getArticleId() == null || dto.getCount() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        String key = ReadBehaviorConstants.READ_BEHAVIOR_PREFIX+"_"+ dto.getArticleId();

        cacheService.incrBy(key,dto.getCount());

        sendBehavoirMessageToKafka(dto.getArticleId(), BehaviorMessageVo.OpinionType.VIEW,0);

        return ResponseResult.okResult(dto);
    }

    @Override
    public ResponseResult unlikedBehavior(UnlikeBehaviorDto dto) {

        if(dto == null || dto.getArticleId() == null || dto.getType() == null || dto.getType() < 0 || dto.getType() > 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer userId = BehaviorThreadLocalUtil.getUser().getId();

        String key = UnLikeBehaviorConstants.UNLIKE_PREFIX+"_"+userId;


        if(dto.getType().equals(UnLikeBehaviorConstants.TYPE_UNLIKE) ){
            if(cacheService.zScore(key,String.valueOf(dto.getArticleId())) != null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }

            cacheService.zAdd(key,String.valueOf(dto.getArticleId()),System.currentTimeMillis());
        } else {
            if(cacheService.zScore(key,String.valueOf(dto.getArticleId())) == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            cacheService.zRemove(key,String.valueOf(dto.getArticleId()));
        }

        return ResponseResult.okResult(dto);

    }




}
