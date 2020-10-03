package comm.utils.redis.impl;

import comm.repository.entity.GdsPcc;
import comm.repository.entity.SiteRulesSwitch;
import comm.utils.exception.CustomException;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GdsPccRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String GDS_PCC_KEY = "gds_pcc";

    public GdsPcc findGdsPcc(String key){
        return (GdsPcc) redisTemplate.opsForHash().get(GDS_PCC_KEY, key);
    }

    public Set<GdsPcc> findAllGdsPcc(){
        return (Set<GdsPcc>) redisTemplate.opsForHash().values(GDS_PCC_KEY);
    }


    public GdsPcc saveOrUpdateCache(GdsPcc gdsPcc) {

        String key = RedisCacheKeyUtil.getGdsPccCacheKey(gdsPcc);
        redisTemplate.opsForHash().put(GDS_PCC_KEY, key, gdsPcc);
        return gdsPcc;
    }

    public void delete(GdsPcc gdsPcc) {
        String key = RedisCacheKeyUtil.getGdsPccCacheKey(gdsPcc);
        redisTemplate.opsForHash().delete(GDS_PCC_KEY, key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(GDS_PCC_KEY);
    }
}
