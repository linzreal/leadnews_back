package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleBehaviorDto;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.BehaviorMessageVo;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 根据type加载文章
     * @param loadType 0为默认 1为加载更多 2为加载最新
     * @param dto
     * @return
     */
    public ResponseResult load(Short loadType, ArticleHomeDto dto);

    public ResponseResult load2(ArticleHomeDto dto,Short type, boolean firstPage);
    /**
     * 保存app端相关文章
     * @param articleDto
     * @return
     */

    public ResponseResult saveArticle( ArticleDto articleDto);

    public ResponseResult loadArticleBehavior(ArticleBehaviorDto dto);

    public void updateArticleBehaviorInfo(BehaviorMessageVo vo);
}
