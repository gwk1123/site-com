package com.sibecommon.utils.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class ThreadPoolExecutor {

    private final String ASYNC_POOL ="asyncExecutor";
    private final String ASYNC_POOL_NAME="async_executor";

    public static final String TASK_EXECUTOR_GDS_DEFAULT = "requestGdsExecutor";
    private static final String TASK_EXECUTOR_NAME_GDS = "request_gds_executor";

    /**
     * 时间短任务多
     */
    public static final String TASK_EXECUTOR_TIME_SHORT = "asyncTimeShortExecutor";
    public static final String TASK_EXECUTOR_TIME_SHORT_NAME = "async_time_short_executor";

    /**
     * 请求GDS
     * @return
     */
    @Bean(TASK_EXECUTOR_GDS_DEFAULT)
    public Executor getAsyncGdsExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        //核心池大小
        executor.setCorePoolSize(50);
        //最大线程数
        executor.setMaxPoolSize(100);
        //队列程度
        executor.setQueueCapacity(1000);
        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程前缀名称
        executor.setThreadNamePrefix(TASK_EXECUTOR_NAME_GDS);
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    /**
     * 默认异步
     * @return
     */
    @Bean(ASYNC_POOL)
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        //核心池大小
        executor.setCorePoolSize(60);
        //最大线程数
        executor.setMaxPoolSize(150);
        //队列程度
        executor.setQueueCapacity(2000);
        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程前缀名称
        executor.setThreadNamePrefix(ASYNC_POOL_NAME);
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    /**
     * 时间短任务多
     */
    @Bean(TASK_EXECUTOR_TIME_SHORT)
    public Executor getAsyncTimeShortExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        //核心池大小
        executor.setCorePoolSize(i*2);
        //最大线程数
        executor.setMaxPoolSize(i*2+2);
        //队列程度
        executor.setQueueCapacity(10000);
        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程前缀名称
        executor.setThreadNamePrefix(TASK_EXECUTOR_TIME_SHORT_NAME);
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
        return executor;
    }


}
