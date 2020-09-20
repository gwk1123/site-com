package comm.utils.redis.impl;

import comm.repository.entity.GdsRule;
import comm.repository.entity.OtaRule;
import comm.repository.entity.PolicyGlobal;
import comm.utils.constant.DirectConstants;
import comm.utils.copy.CopyUtils;
import comm.utils.exception.CustomException;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GdsRuleRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REDIS_KEY = "gds_rule";

    public GdsRule saveOrUpdate(GdsRule otaRule) {
        return this.saveOrUpdateCache(otaRule);
    }

    public GdsRule saveOrUpdateCache(GdsRule item) {
        GdsRule gdsRule = CopyUtils.deepCopy(item);
        if (gdsRule == null
                || StringUtils.isEmpty(gdsRule.getGdsCode())
                || StringUtils.isEmpty(gdsRule.getRuleType())
        ) {
            throw new CustomException("GDS规则信息不能为空");
        }

        String key = RedisCacheKeyUtil.getGdsRuleCacheKey(gdsRule);
        redisTemplate.opsForHash().put(REDIS_KEY, key, gdsRule);
        redisTemplate.opsForSet().add(REDIS_KEY+":r:"+
                gdsRule.getRuleType(),key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":g:"+gdsRule.getGdsCode()+":r:"+
                gdsRule.getRuleType(),key);
        return gdsRule;
    }


    public void delete(GdsRule gdsRule) {
        String key = RedisCacheKeyUtil.getGdsRuleCacheKey(gdsRule);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":r:"+
                gdsRule.getRuleType(),key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":g:"+gdsRule.getGdsCode()+":r:"+
                gdsRule.getRuleType(),key);
    }

    public List<GdsRule> findAll(){
        return redisTemplate.opsForHash().values(REDIS_KEY);
    }

    public List<GdsRule> findGdsRulesByRuleType(String ruleType){
       Set<GdsRule> keys=redisTemplate.opsForSet().members(REDIS_KEY + ":r:" + ruleType);
        return redisTemplate.opsForHash().multiGet(REDIS_KEY, keys);
    }

    public List<GdsRule> findGdsRulesByGdsCodeAndRuleType(String gdsCode,String ruleType){
        Set<GdsRule> keys=redisTemplate.opsForSet().members(REDIS_KEY+":g:"+gdsCode+":r:"+
                ruleType);
        return redisTemplate.opsForHash().multiGet(REDIS_KEY, keys);
    }
}
