package com.heima.article.test;

import com.heima.article.ArticleApplication;
import com.heima.common.redis.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void testList(){
        cacheService.lLeftPush("list_001","hello,redis");
    }
}
