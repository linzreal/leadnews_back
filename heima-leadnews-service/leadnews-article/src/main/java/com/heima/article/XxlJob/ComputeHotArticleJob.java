package com.heima.article.XxlJob;

import com.heima.article.service.HotArticleService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ComputeHotArticleJob {

    @Autowired
    private HotArticleService hotArticleService;


    @XxlJob("computeHotArticleJob")
    public void handel(){
        log.info("开始更新热点数据。。。。。");
        hotArticleService.computeHotArticle();
        log.info("更新热点数据成功。。。。。");
    }
}
