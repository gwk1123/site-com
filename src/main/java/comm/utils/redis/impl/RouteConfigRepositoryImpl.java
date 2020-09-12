package comm.utils.redis.impl;

import comm.repository.entity.Gds;
import comm.repository.entity.RouteConfig;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RouteConfigRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String ROUTE_CONFIG_KEY = "route_config";

    public RouteConfig findRouteConfig(String key){
        return (RouteConfig) redisTemplate.opsForHash().get(ROUTE_CONFIG_KEY, key);
    }

    public Set<RouteConfig> findAllRouteConfig(){
        return (Set<RouteConfig>) redisTemplate.opsForHash().values(ROUTE_CONFIG_KEY);
    }


    public RouteConfig saveOrUpdateCache(RouteConfig gds) {
        String key = RedisCacheKeyUtil.getRouteConfigCacheKey(gds);
        redisTemplate.opsForHash().put(ROUTE_CONFIG_KEY, key, gds);
        return gds;
    }

    public void delete(RouteConfig gds) {
        String key = RedisCacheKeyUtil.getRouteConfigCacheKey(gds);
        redisTemplate.opsForHash().delete(ROUTE_CONFIG_KEY, key);
    }

}
