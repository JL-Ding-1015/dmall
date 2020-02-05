package com.djl.dmall.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean("mainThreadPoolExecutor")
    public ThreadPoolExecutor mainThreadPoolExecutor(PoolProperties poolProperties) {
        LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
        new ThreadPoolExecutor(poolProperties.getCoreSize(), poolProperties.getMaximumPoolSize(),10, TimeUnit.SECONDS, queue);
        return null;
    }

    @Bean("otherThreadPoolExecutor")
    public ThreadPoolExecutor otherThreadPoolExecutor() {



        return null;
    }
}
