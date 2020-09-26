package comm.utils.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolExecutor {

    private final String REQUEST_GDS_POOL="requestGdsPool";
    private final String ASYNC_POOL ="asyncPool";

    @Bean(REQUEST_GDS_POOL)
    public ExecutorService requestGdsPool() {
        return Executors.newFixedThreadPool(50);
    }


    @Bean(ASYNC_POOL)
    public ExecutorService requestGdsPool() {
        return Executors.newFixedThreadPool(30);
    }

}
