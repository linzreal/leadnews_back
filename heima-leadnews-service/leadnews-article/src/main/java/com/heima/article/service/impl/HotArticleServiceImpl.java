package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class HotArticleServiceImpl implements HotArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;


    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private CacheService cacheService;

    @Override
    public void computeHotArticle() {

        //查询前五天的文章数据
        Date dateParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5days(dateParam);

        //计算文章分值
        List<HotArticleVo> hotArticleVoList = computeHotArticleScore(apArticleList);

        //为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleVoList);


    }

    /**
     * 为每个评到缓存30条分值较高的文章
     * @param hotArticleVoList
     */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVoList) {
        ResponseResult responseResult = wemediaClient.getChannels();

        if(responseResult.getCode().equals(200)){
            String channelJSON = JSON.toJSONString(responseResult.getData());
            List<WmChannel> channelList = JSON.parseArray(channelJSON, WmChannel.class);

            if(channelList != null && channelList.size() > 0){
                for(WmChannel channel : channelList){
                    List<HotArticleVo> hotArticleVos = hotArticleVoList.stream().filter(x -> x.getChannelId().equals(channel.getId())).collect(Collectors.toList());

                    sortAndCache(hotArticleVos,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+channel.getId());
                }
            }
        }

        sortAndCache(hotArticleVoList,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);
    }


    private void sortAndCache(List<HotArticleVo> hotArticleVos, String key){

        hotArticleVos.sort(new Comparator<HotArticleVo>() {
            @Override
            public int compare(HotArticleVo o1, HotArticleVo o2) {
                return o2.getScore()-o1.getScore();
            }
        });

        if(hotArticleVos.size() > 30){
            hotArticleVos = hotArticleVos.subList(0,30);
        }

        cacheService.set(key,JSON.toJSONString(hotArticleVos));

    }

    /**
     * 计算文章分值
     * @param apArticleList
     * @return
     */
    private List<HotArticleVo> computeHotArticleScore(List<ApArticle> apArticleList) {

        List<HotArticleVo> list = new ArrayList<>();
        if(apArticleList != null && apArticleList.size() >0){
            for(ApArticle apArticle : apArticleList){
                HotArticleVo hotArticleVo = new HotArticleVo();

                BeanUtils.copyProperties(apArticle,hotArticleVo);

                Integer score = computeScore(apArticle);
                hotArticleVo.setScore(score);

                list.add(hotArticleVo);
            }
        }

        return list;

    }

    /**
     * 计算文章具体分值
     * @param apArticle
     * @return
     */
    private Integer computeScore(ApArticle apArticle) {
        Integer score = 0;

        if(apArticle.getLikes() != null){
            score += apArticle.getLikes()* ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }

        if(apArticle.getComment() != null){
            score += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }

        if(apArticle.getViews() != null){
            score += apArticle.getViews();
        }

        if(apArticle.getCollection() != null){
            score += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }

        return score;

    }
}
