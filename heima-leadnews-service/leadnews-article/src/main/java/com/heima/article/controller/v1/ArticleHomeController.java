package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dtos.ArticleBehaviorDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/article")
@Api(value = "app端文章接口",tags = "app端文章接口")
public class ArticleHomeController {


    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/load")
    @ApiOperation("普通加载")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){

        //ResponseResult res = apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
        return apArticleService.load2(dto, ArticleConstants.LOADTYPE_LOAD_MORE, true);
    }

    @PostMapping("/loadmore")
    @ApiOperation("加载更多")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE,dto);
    }


    @PostMapping("/loadnew")
    @ApiOperation("加载最新")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_NEW,dto);
    }

    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticleBehavior(@RequestBody ArticleBehaviorDto dto){
        return apArticleService.loadArticleBehavior(dto);
    }




}
