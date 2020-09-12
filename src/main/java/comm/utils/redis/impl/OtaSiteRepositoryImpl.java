package comm.utils.redis.impl;

import comm.repository.entity.GdsPcc;
import comm.repository.entity.OtaSite;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OtaSiteRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String OTA_SITE_KEY = "ota_site";

    public OtaSite findOtaSite(String key){
        return (OtaSite) redisTemplate.opsForHash().get(OTA_SITE_KEY, key);
    }

    public Set<OtaSite> findAllOtaSite(){
        return (Set<OtaSite>) redisTemplate.opsForHash().values(OTA_SITE_KEY);
    }


    public OtaSite saveOrUpdateCache(OtaSite gdsPcc) {
        String key = RedisCacheKeyUtil.getOtaSitecCacheKey(gdsPcc);
        redisTemplate.opsForHash().put(OTA_SITE_KEY, key, gdsPcc);
        return gdsPcc;
    }

    public void delete(OtaSite gdsPcc) {
        String key = RedisCacheKeyUtil.getOtaSitecCacheKey(gdsPcc);
        redisTemplate.opsForHash().delete(OTA_SITE_KEY, key);
    }

}
