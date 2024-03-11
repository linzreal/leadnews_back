package com.heima.article.test;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.ArticleApplication;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Test
    public void createStaticUrlTest() throws Exception{
        //获取文章内容


        LambdaQueryWrapper<ApArticleContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        List<ApArticleContent> apArticleContents = apArticleContentMapper.selectList(lambdaQueryWrapper);
        for(ApArticleContent apArticleContent : apArticleContents){
            if(apArticleContent!=null && StringUtils.isNotBlank(apArticleContent.getContent())){
                //通过freemaker生成html
                StringWriter out = new StringWriter();
                Template template = configuration.getTemplate("article.ftl");

                //数据模型
                Map<String,Object> map = new HashMap<>();
                map.put("content", JSONArray.parseArray(apArticleContent.getContent()));



                //合成
                template.process(map,out);

                //通过mino将html上传静态化
                InputStream in = new ByteArrayInputStream(out.toString().getBytes());
                String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", in);



                //修改ap_article表，保存static_url字段。
                ApArticle apArticle = new ApArticle();
                apArticle.setId(apArticleContent.getArticleId());
                apArticle.setStaticUrl(path);
                apArticleMapper.updateById(apArticle);
            }
        }


    }
}
