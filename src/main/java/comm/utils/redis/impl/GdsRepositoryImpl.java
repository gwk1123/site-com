package comm.utils.redis.impl;

import comm.repository.entity.Gds;
import comm.repository.entity.GdsPcc;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GdsRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String GDS_KEY = "gds";

    public Gds findGds(String key){
        return (Gds) redisTemplate.opsForHash().get(GDS_KEY, key);
    }

    public Set<Gds> findAllGds(){
        return (Set<Gds>) redisTemplate.opsForHash().values(GDS_KEY);
    }


    public Gds saveOrUpdateCache(Gds gds) {
        String key = RedisCacheKeyUtil.getGdsCacheKey(gds);
        redisTemplate.opsForHash().put(GDS_KEY, key, gds);
        return gds;
    }

    public void delete(Gds gds) {
        String key = RedisCacheKeyUtil.getGdsCacheKey(gds);
        redisTemplate.opsForHash().delete(GDS_KEY, key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(GDS_KEY);
    }

}
