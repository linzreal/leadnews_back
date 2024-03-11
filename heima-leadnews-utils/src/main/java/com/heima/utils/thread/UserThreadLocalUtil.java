package com.heima.utils.thread;

import com.heima.model.user.pojos.ApUser;

public class UserThreadLocalUtil {
    private final static ThreadLocal<ApUser> USER_THREAD_LOCAL = new ThreadLocal<>();


    //存入线程中
    public static void setUser(ApUser apUser){
        USER_THREAD_LOCAL.set(apUser);
    }


    //从线程中获取
    public static ApUser getUser(){
        return USER_THREAD_LOCAL.get();
    }

    //清理
    public static void clear(){
        USER_THREAD_LOCAL.remove();
    }
}
