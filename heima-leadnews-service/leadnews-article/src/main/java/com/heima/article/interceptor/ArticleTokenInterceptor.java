package com.heima.article.interceptor;

import com.heima.model.article.pojos.ApArticle;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.ArticleThreadLocalUtil;
import com.heima.utils.thread.UserThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ArticleTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");

        if( userId != null){
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            ArticleThreadLocalUtil.setUser(apUser);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ArticleThreadLocalUtil.clear();
    }
}
