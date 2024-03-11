package com.heima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class BehaviorApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(BehaviorApplication.class,args);
    }
}
