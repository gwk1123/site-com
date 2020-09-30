package comm.utils.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
@EnableScheduling
public class ThreadPoolExecutor {

    private final String ASYNC_POOL ="asyncExecutor";
    private final String ASYNC_POOL_NAME="async_executor";

    public static final String TASK_EXECUTOR_GDS_DEFAULT = "taskGdsExecutor";
    private static final String TASK_EXECUTOR_NAME_GDS = "task_gds_executor";

    @Bean(TASK_EXECUTOR_GDS_DEFAULT)
    public Executor getAsyncGdsExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        executor.setCorePoolSize(50);//核心池大小
        executor.setMaxPoolSize(100);//最大线程数
        executor.setQueueCapacity(1000);//队列程度
        executor.setKeepAliveSeconds(1000);//线程空闲时间
        executor.setThreadNamePrefix(TASK_EXECUTOR_NAME_GDS);//线程前缀名称
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
        return executor;
    }


    @Bean(ASYNC_POOL)
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        executor.setCorePoolSize(60);//核心池大小
        executor.setMaxPoolSize(150);//最大线程数
        executor.setQueueCapacity(2000);//队列程度
        executor.setKeepAliveSeconds(1000);//线程空闲时间
        executor.setThreadNamePrefix(ASYNC_POOL_NAME);//线程前缀名称
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
        return executor;
    }

}
