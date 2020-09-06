package comm.utils.redis.impl;

import comm.utils.redis.GdsCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author gwk
 */
@Service
public class GdsCacheServiceImpl implements GdsCacheService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void deleteGDS(String redisKey,String key) {
        redisTemplate.opsForHash().delete(redisKey,key) ;
    }

}
