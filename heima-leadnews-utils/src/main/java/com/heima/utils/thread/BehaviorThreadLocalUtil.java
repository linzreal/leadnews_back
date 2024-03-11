package com.heima.utils.thread;

import com.heima.model.user.pojos.ApUser;

public class BehaviorThreadLocalUtil {
    private final static ThreadLocal<ApUser> BEHAVIOR_THREAD_LOCAL = new ThreadLocal<>();

    //存入线程中
    public static void setUser(ApUser apUser){
        BEHAVIOR_THREAD_LOCAL.set(apUser);
    }


    //从线程中获取
    public static ApUser getUser(){
        return BEHAVIOR_THREAD_LOCAL.get();
    }

    //清理
    public static void clear(){
        BEHAVIOR_THREAD_LOCAL.remove();
    }

}
