package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.constants.LikesBehaviorConstants;
import com.heima.common.constants.UnLikeBehaviorConstants;
import com.heima.common.constants.UserFollowConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.dtos.ArticleBehaviorDto;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.vos.BehaviorMessageVo;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.vo.SearchArticleVo;
import com.heima.utils.thread.ArticleThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {


    //单页允许加载的最多页数
    private final static Integer MAX_PAGE_SIZE = 50;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;


    @Autowired
    private ApArticleContentMapper apArticleContentMapper;


    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;



    @Autowired
    private CacheService cacheService;


    /**
     * 根据type加载文章
     * @param loadType 0为默认 1为加载更多 2为加载最新
     * @param dto
     * @return
     */
    @Override
    public ResponseResult load(Short loadType, ArticleHomeDto dto) {
        Integer size = dto.getSize();
        if(size == null|| size == 0){
            size = 10;
        }
        size = Math.min(MAX_PAGE_SIZE,size);


        dto.setSize(size);

        if(!loadType.equals(ArticleConstants.LOADTYPE_LOAD_MORE)&&!loadType.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            loadType = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        if(StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        //时间校验
        if(dto.getMaxBehotTime()==null) dto.setMaxBehotTime(new Date());
        if(dto.getMinBehotTime()==null) dto.setMinBehotTime(new Date());

        //查询
        List<ApArticle> apArticleList = apArticleMapper.loadArticleList(loadType,dto);

        return ResponseResult.okResult(apArticleList);


    }

    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if(firstPage){
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE+dto.getTag());
            if(jsonStr != null){
                List<HotArticleVo> list = JSON.parseArray(jsonStr, HotArticleVo.class);
                return ResponseResult.okResult(list);
            }
        }

        return load(type, dto);
    }


    /**
     * 保存app端相关文章
     * @param articleDto
     * @return
     */
    @Override
    public ResponseResult saveArticle(ArticleDto articleDto) {

        //1检查参数
        if(articleDto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(articleDto,apArticle);
        apArticle.setPublishTime(new Date());
        //判断是否存在id  不存在？保存 文章 文章配置 文章内容  存在？修改 文章 文章内容
        if(articleDto.getId() == null){


            //保存文章
            save(apArticle);

            //保存文章配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);

            //保存文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(articleDto.getContent());
            apArticleContentMapper.insert(apArticleContent);

        }else{

            //修改文章
            updateById(apArticle);


            //修改文章内容
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, articleDto.getId()));

            apArticleContent.setContent(articleDto.getContent());
            apArticleContentMapper.updateById(apArticleContent);

        }

        articleFreemarkerService.buildArticleToMinIO(apArticle,articleDto.getContent());

        //添加到索引当中
        createESIndex(apArticle,articleDto.getContent());

        //结果返回 返回文章id
        return ResponseResult.okResult(apArticle.getId());
    }

    @Override
    public ResponseResult loadArticleBehavior(ArticleBehaviorDto dto) {

        if(dto == null || dto.getArticleId() == null || dto.getAuthorId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        Integer userId = ArticleThreadLocalUtil.getUser().getId();

        String followeeKey = UserFollowConstants.PREFIX_FOLLOWEE+"_"+userId;

        String likeKey = LikesBehaviorConstants.LIKE_PREFIX+"_"+0+"_"+userId;

        String unlikeKey = UnLikeBehaviorConstants.UNLIKE_PREFIX+"_"+userId;


        Map<String,Boolean> map = new HashMap<>();

        if(cacheService.zScore(followeeKey,String.valueOf(dto.getAuthorId())) != null){
            map.put("isfollow",true);
        } else {
            map.put("isfollow",false);
        }

        if(cacheService.zScore(likeKey,String.valueOf(dto.getArticleId())) != null){
            map.put("islike", true);
        } else {
            map.put("islike", false);
        }

        if(cacheService.zScore(unlikeKey,String.valueOf(dto.getArticleId())) != null){
            map.put("isunlike", true);
        } else {
            map.put("isunlike", false);
        }

        map.put("iscollection", false);

        return ResponseResult.okResult(map);


    }

    @Override
    public void updateArticleBehaviorInfo(BehaviorMessageVo vo) {
        ApArticle articleDB = apArticleMapper.selectById(vo.getArticleId());

        ApArticle article = new ApArticle();
        article.setId(vo.getArticleId());

        if(vo.getOpinionType().equals(BehaviorMessageVo.OpinionType.LIKE)){
            if(articleDB.getLikes() == null){
                if(vo.getType().equals(0)) {
                    article.setLikes(1);
                } else {
                    return;
                }
            } else {
                article.setLikes(vo.getType().equals(0) ? articleDB.getLikes() + 1 : articleDB.getLikes() - 1);
            }
        } else if(vo.getOpinionType().equals(BehaviorMessageVo.OpinionType.VIEW)){
            if(articleDB.getViews() == null){
                article.setViews(1);
            } else {
                article.setViews(articleDB.getViews() + 1);
            }
        }

        apArticleMapper.updateById(article);

        log.info(vo.getArticleId()+"更新行为信息成功！！！！！！！");

    }

    private void createESIndex(ApArticle article,String content){
        SearchArticleVo vo = new SearchArticleVo();
        BeanUtils.copyProperties(article,vo);

        vo.setContent(content);
        vo.setStaticUrl("123456");

        kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, JSON.toJSONString(vo));
    }
}
