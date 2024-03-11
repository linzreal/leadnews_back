package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     * 根据type加载文章
     * @param type 0为默认 1为加载更多 2为加载最新
     * @param dto
     * @return
     */
    public List<ApArticle> loadArticleList(@Param("type") Short type, @Param("dto")ArticleHomeDto dto);


    public List<ApArticle> findArticleListByLast5days(@Param("dayParam") Date param);
}
