package com.heima.wemedia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfiguration {
    @Bean("myExecutor")
    public Executor asynServiceExecutor(){

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(5);

        taskExecutor.setMaxPoolSize(20);

        taskExecutor.setQueueCapacity(Integer.MAX_VALUE);

        taskExecutor.setKeepAliveSeconds(60);

        taskExecutor.setThreadNamePrefix("my");

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        taskExecutor.initialize();

        return taskExecutor;
    }
}
