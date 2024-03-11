package com.heima.utils.thread;

import com.heima.model.user.pojos.ApUser;

public class ArticleThreadLocalUtil {
    private final static ThreadLocal<ApUser> ARTICLE_THREAD_LOCAL = new ThreadLocal<>();

    //存入线程中
    public static void setUser(ApUser apUser){
        ARTICLE_THREAD_LOCAL.set(apUser);
    }


    //从线程中获取
    public static ApUser getUser(){
        return ARTICLE_THREAD_LOCAL.get();
    }

    //清理
    public static void clear(){
        ARTICLE_THREAD_LOCAL.remove();
    }
}
