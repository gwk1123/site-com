package comm.utils.redis.impl;

import comm.repository.entity.Gds;
import comm.repository.entity.Ota;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OtaRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String OTA_KEY = "ota";

    public Ota findOta(String key){
        return (Ota) redisTemplate.opsForHash().get(OTA_KEY, key);
    }

    public Set<Ota> findAllOta(){
        return (Set<Ota>) redisTemplate.opsForHash().values(OTA_KEY);
    }


    public Ota saveOrUpdateCache(Ota ota) {
        String key = RedisCacheKeyUtil.getOtaCacheKey(ota);
        redisTemplate.opsForHash().put(OTA_KEY, key, ota);
        return ota;
    }

    public void delete(Ota ota) {
        String key = RedisCacheKeyUtil.getOtaCacheKey(ota);
        redisTemplate.opsForHash().delete(OTA_KEY, key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(OTA_KEY);
    }

}
