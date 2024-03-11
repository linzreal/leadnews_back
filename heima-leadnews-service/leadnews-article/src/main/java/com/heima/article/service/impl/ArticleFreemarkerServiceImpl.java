package com.heima.article.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    @Async
    public void buildArticleToMinIO(ApArticle apArticle, String content) {
        if(content != null && StringUtils.isNotBlank(content)) {

            StringWriter out = new StringWriter();

            try {
                Template template = configuration.getTemplate("article.ftl");

                Map<String,Object> map = new HashMap<>();
                map.put("content", JSONArray.parseArray(content));
                template.process(map,out);

                //通过mino将html上传静态化
                InputStream in = new ByteArrayInputStream(out.toString().getBytes());
                String path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);

                ApArticle article = new ApArticle();
                article.setId(apArticle.getId());
                article.setStaticUrl(path);
                apArticleMapper.updateById(article);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
