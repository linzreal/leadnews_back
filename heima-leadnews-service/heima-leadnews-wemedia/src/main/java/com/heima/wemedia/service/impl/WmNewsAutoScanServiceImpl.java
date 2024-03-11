package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.instrument.classloading.jboss.JBossLoadTimeWeaver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.server.ServerEndpoint;
import java.util.*;


@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;


    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    @Override
    @Async("myExecutor")
    public void autoScanWmNews(Integer Id) {

        //查询文章
        WmNews wmNews = wmNewsMapper.selectById(Id);
        if(wmNews == null){
            throw new RuntimeException("WmNewsAutoScanServiceImpl 文章不存在");
        }

        if(wmNews.getStatus()==1){
            //从内容中提取纯文本内容和图片
            Map<String,Object> textAndImages = handleTextAndImages(wmNews);


            //审核文本内容  默认跳过
            boolean isTextScan = handleTextScan(String.valueOf(textAndImages.get("content")),wmNews);
            if(!isTextScan){
                return;
            }

            //审核图片   默认跳过
            boolean isImageScan = handleImageScan((List<String>)textAndImages.get("images"),wmNews);
            if(!isImageScan){
                return;
            }

            //调用apArticle接口保存app端相关文章数据
            ResponseResult responseResult = saveAppArticle(wmNews);

            if(!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl 文章审核，保存app端相关文章数据失败");
            }

            //回填article_id
            wmNews.setArticleId((Long)responseResult.getData());

            wmNews.setStatus((short)9);
            wmNews.setReason("审核成功");
            wmNewsMapper.updateById(wmNews);

        }



    }

    /**
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto articleDto = new ArticleDto();

        BeanUtils.copyProperties(wmNews,articleDto);

        articleDto.setLayout(wmNews.getType());

        //频道
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel != null){
            articleDto.setChannelName(wmChannel.getName());
        }

        //作者
        articleDto.setAuthorId(Long.valueOf(wmNews.getUserId()));
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser != null){
            articleDto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if(wmNews.getArticleId() != null){
            articleDto.setId(wmNews.getArticleId());
        }

        articleDto.setCreatedTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(articleDto);

        return responseResult;
    }


    /**
     * 审核图片，默认通过
     * @param images
     * @param wmNews
     * @return
     */

    private boolean handleImageScan(List<String> images, WmNews wmNews){
        if(CollectionUtils.isEmpty(images)){
            return true;
        }

        return true;
    }

    /**
     * 审核纯文本内容 默认直接通过
     *
     * @param content
     * @param wmNews
     * @return
     */

    private boolean handleTextScan(String content, WmNews wmNews){

        if((wmNews.getTitle()+"-"+content).length()==1){
            return true;
        }
        return true;
    }


    /**
     * 1.从文章内容中提取文本和图片
     * 2.提取文章的封面图片
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {

        Map<String,Object> res = new HashMap<>();

        //存储纯文本内容
        StringBuilder stringBuilder = new StringBuilder();


        //存储图片
        List<String> images = new ArrayList<>();

        //内容中的图片和文本
        if(StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for(Map map:maps){
                if(map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }
                if(map.get("type").equals("image")){
                    images.add(String.valueOf(map.get("value")));
                }
            }
        }

        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));

        }

        res.put("content",stringBuilder.toString());
        res.put("images",images);

        return res;
    }
}
