package com.heima.article.listener;

import com.alibaba.fastjson.JSON;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.BehaviorMessage;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.BehaviorMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sun.swing.StringUIClientPropertyKey;

@Component
@Slf4j
public class ArticleBehaviorListener {

    @Autowired
    private ApArticleService apArticleService;

    @KafkaListener(topics = BehaviorMessage.topic)
    public void onMessage(String message){
        if(StringUtils.isNotBlank(message)){

            BehaviorMessageVo vo = JSON.parseObject(message, BehaviorMessageVo.class);

            apArticleService.updateArticleBehaviorInfo(vo);

        }
    }
}
